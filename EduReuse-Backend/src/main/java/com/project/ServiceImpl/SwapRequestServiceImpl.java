package com.project.ServiceImpl;

import com.project.Entity.*;
import com.project.Exception.*;
import com.project.Dao.*;
import com.project.Service.SwapRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwapRequestServiceImpl implements SwapRequestService {

    @Autowired
    private SwapRequestRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookSetRepository setRepo;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public SwapRequest createSwapRequest(Long requesterId, Long requestedSetId, Long offeredSetId) {
        User requester = userRepo.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + requesterId));
        if (requester.isDeleted()) throw new InvalidActionException("Requester does not exist");

        BookSet requested = setRepo.findById(requestedSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Requested set not found: " + requestedSetId));
        if (requested.isDeleted()) throw new InvalidActionException("Requested set does not exist");

        BookSet offered = setRepo.findById(offeredSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Offered set not found: " + offeredSetId));
        if (offered.isDeleted()) throw new InvalidActionException("Offered set does not exist");

        User recipient = requested.getSeller();
        if (recipient == null || recipient.isDeleted()) {
            throw new NoSuchUserExist("Recipient not found or deleted");
        }

        if (!requested.getListingType().equals(ListingType.SWAP))
            throw new InvalidActionException("Requested set not available for SWAP");

        if (!offered.getListingType().equals(ListingType.SWAP) ||
                !offered.getClassLevel().equals(requested.getDesiredClassLevel()) ||
                !offered.getBoard().equalsIgnoreCase(requested.getDesiredBoard()) ||
                !requested.getDesiredCompleteSet().equals(offered.getCompleteSet()))
            throw new InvalidActionException("Offered set doesn't match desired criteria");

        if (!requester.getBookSets().contains(offered))
            throw new InvalidSwapRequestException("You do not have any bookset with id: " + offeredSetId);

        if (repo.existsByOfferedBookSetAndRequestedBookSet(offered, requested)) {
            for (SwapRequest sr : repo.findByRequester(requester)) {
                if ((sr.getSwapStatus() == SwapStatus.PENDING)||(sr.getSwapStatus() == SwapStatus.ACCEPTED))
                    throw new InvalidSwapRequestException("You already sent a swap request for this bookset");
            }
        }

        if (repo.existsByOfferedBookSetAndRequestedBookSet(requested, offered)) {
            for (SwapRequest sr : repo.findByRequester(recipient)) {
                if ((sr.getSwapStatus() == SwapStatus.PENDING)||(sr.getSwapStatus() == SwapStatus.ACCEPTED))
                    throw new InvalidSwapRequestException("You already sent a swap request for this bookset");
            }
        }

        SwapRequest sr = new SwapRequest();
        sr.setRequester(requester);
        sr.setRequestedBookSet(requested);
        sr.setOfferedBookSet(offered);
        sr.setSwapStatus(SwapStatus.PENDING);
        sr.setCreatedAt(Timestamp.from(Instant.now()));

        return repo.save(sr);
    }

    @Override
    public List<SwapRequest> getRequestsMade(Long userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (u.isDeleted()) throw new InvalidActionException("User does not exist");
        return repo.findByRequester(u);
    }

    public List<BookSet> findMatchingBookSets(Long requestedId, Long requesterId) {
        BookSet requested = setRepo.findById(requestedId)
                .orElseThrow(() -> new RuntimeException("Requested BookSet not found"));

        List<BookSet> allUserSets = userRepo.findById(requesterId).get().getBookSets();

        return allUserSets.stream()
                .filter(bs -> bs.getListingType() == ListingType.SWAP)
                .filter(bs -> bs.getClassLevel().equals(requested.getDesiredClassLevel()))
                .filter(bs -> bs.getBoard().equals(requested.getDesiredBoard()))
                .filter(bs -> bs.getCompleteSet().equals(requested.getDesiredCompleteSet()))
                .collect(Collectors.toList());
    }


    @Override
    public List<SwapRequest> getRequestsReceived(Long userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (u.isDeleted()) throw new InvalidActionException("User does not exist");
        return repo.findByRequestedBookSetSeller(u);
    }

    @Override
    @Transactional
    public SwapRequest rejectRequest(Long userId, Long requestId) {
        SwapRequest sr = repo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found: " + requestId));

        if (!sr.getSwapStatus().equals(SwapStatus.PENDING))
            throw new InvalidActionException("Request is not pending");

        if (sr.getRequestedBookSet() == null || sr.getRequestedBookSet().getSeller() == null || sr.getRequestedBookSet().getSeller().isDeleted())
            throw new UnauthorizedException("Invalid request or deleted seller");

        if (!sr.getRequestedBookSet().getSeller().getUserId().equals(userId))
            throw new UnauthorizedException("Cannot reject someone else's request");

        sr.setSwapStatus(SwapStatus.REJECTED);
        return repo.save(sr);
    }

    @Override
    @Transactional
    public SwapRequest acceptRequest(Long userId, Long requestId) {
        SwapRequest sr = repo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found: " + requestId));

        BookSet requested = sr.getRequestedBookSet();
        BookSet offered = sr.getOfferedBookSet();

        if (requested == null || requested.isDeleted() ||
                requested.getSeller() == null || requested.getSeller().isDeleted() ||
                !requested.getSeller().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized or invalid request");
        }

        if (!sr.getSwapStatus().equals(SwapStatus.PENDING))
            throw new InvalidActionException("Request is not pending");

        if (offered.isDeleted() || requested.isDeleted())
            throw new InvalidActionException("Cannot accept request involving deleted book sets");

        offered.setDeleted(true);
        requested.setDeleted(true);

        setRepo.save(offered);
        setRepo.save(requested);

        sr.setSwapStatus(SwapStatus.ACCEPTED);
        return repo.save(sr);
    }

    @Override
    @Transactional
    public void withdrawRequest(Long userId, Long requestId) {
        SwapRequest sr = repo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found: " + requestId));

        if (sr.getRequester() == null || sr.getRequester().isDeleted())
            throw new UnauthorizedException("Invalid requester");

        if (!sr.getRequester().getUserId().equals(userId))
            throw new UnauthorizedException("Cannot withdraw someone else's request");

        if (!sr.getSwapStatus().equals(SwapStatus.PENDING))
            throw new InvalidActionException("Request is no more pending");

        sr.setSwapStatus(SwapStatus.WITHDRAWN);
        repo.save(sr);
    }

    @Override
    public List<SwapRequest> getSentRequestsByUser(Long userId) {
        return repo.findByRequester_UserId(userId);
    }

}

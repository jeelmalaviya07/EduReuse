package com.project.Controller;

import com.project.Dto.SwapRequestDTO;
import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;
import com.project.Service.SwapRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swaps")
public class SwapRequestController {

    @Autowired
    private SwapRequestService swapRequestService;

    @PostMapping("/{requesterId}/create/{requestedSetId}/{offeredSetId}")
    public ResponseEntity<SwapRequest> createSwapRequest(
            @PathVariable Long requesterId,
            @PathVariable Long requestedSetId,
            @PathVariable Long offeredSetId
    ) {
        SwapRequest request = swapRequestService.createSwapRequest(requesterId, requestedSetId, offeredSetId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/made/{userId}")
    public ResponseEntity<List<SwapRequest>> getRequestsMadeByUser(@PathVariable Long userId) {
        List<SwapRequest> madeRequests = swapRequestService.getRequestsMade(userId);
        return ResponseEntity.ok(madeRequests);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<SwapRequest>> getRequestsReceivedByUser(@PathVariable Long userId) {
        List<SwapRequest> receivedRequests = swapRequestService.getRequestsReceived(userId);
        return ResponseEntity.ok(receivedRequests);
    }

    @GetMapping("/match/{requestedId}")
    public ResponseEntity<List<BookSet>> getMatchingBookSets(
            @PathVariable Long requestedId,
            @RequestParam Long requesterId
    ) {
        List<BookSet> matchingSets = swapRequestService.findMatchingBookSets(requestedId, requesterId);
        return ResponseEntity.ok(matchingSets);
    }

    @PostMapping("/{requestId}/accept/{userId}")
    public ResponseEntity<SwapRequest> acceptSwapRequest(
            @PathVariable Long requestId,
            @PathVariable Long userId
    ) {
        SwapRequest acceptedRequest = swapRequestService.acceptRequest(userId, requestId);
        return ResponseEntity.ok(acceptedRequest);
    }

    @PostMapping("/{requestId}/reject/{userId}")
    public ResponseEntity<SwapRequest> rejectSwapRequest(
            @PathVariable Long requestId,
            @PathVariable Long userId
    ) {
        SwapRequest rejectedRequest = swapRequestService.rejectRequest(userId, requestId);
        return ResponseEntity.ok(rejectedRequest);
    }

    @PostMapping("/{requestId}/withdraw/{userId}")
    public ResponseEntity<Void> withdrawSwapRequest(
            @PathVariable Long requestId,
            @PathVariable Long userId
    ) {
        swapRequestService.withdrawRequest(userId, requestId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{userId}/sent")
    public List<SwapRequestDTO> getSent(@PathVariable Long userId) {
        List<SwapRequest> original = swapRequestService.getRequestsMade(userId);
        return original.stream()
                .map(req -> new SwapRequestDTO(
                        req.getId(),
                        req.getOfferedBookSet(),
                        req.getRequestedBookSet(),
                        req.getSwapStatus(),
                        req.getCreatedAt()
                ))
                .toList();
    }


}

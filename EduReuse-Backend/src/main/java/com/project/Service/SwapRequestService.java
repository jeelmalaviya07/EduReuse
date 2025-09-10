package com.project.Service;

import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;

import java.util.List;

public interface SwapRequestService {
    SwapRequest createSwapRequest(Long requesterId, Long requestedBookSetId, Long offeredBookSetId);
    List<SwapRequest> getRequestsMade(Long userId);
    List<SwapRequest> getRequestsReceived(Long userId);
    SwapRequest acceptRequest(Long userId, Long requestId);
    SwapRequest rejectRequest(Long userId, Long requestId);
    void withdrawRequest(Long userId, Long requestId);
    public List<BookSet> findMatchingBookSets(Long requestedId, Long requesterId);
    List<SwapRequest> getSentRequestsByUser(Long userId);

}
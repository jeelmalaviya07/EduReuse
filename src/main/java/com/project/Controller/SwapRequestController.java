package com.project.Controller;

import com.project.Entity.SwapRequest;
import com.project.ServiceImpl.SwapRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swaps")
public class SwapRequestController {

    @Autowired
    private SwapRequestServiceImpl srv;

    @PostMapping("/{requesterId}/create/{requestedSetId}/{offeredSetId}")
    public SwapRequest create(@PathVariable Long requesterId,
                              @PathVariable Long requestedSetId,
                              @PathVariable Long offeredSetId) {
        return srv.createSwapRequest(requesterId, requestedSetId, offeredSetId);
    }

    @GetMapping("/made/{userId}")
    public List<SwapRequest> getMade(@PathVariable Long userId) {
        return srv.getRequestsMade(userId);
    }

    @GetMapping("/received/{userId}")
    public List<SwapRequest> getReceived(@PathVariable Long userId) {
        return srv.getRequestsReceived(userId);
    }

    @PostMapping("/{requestId}/accept/{userId}")
    public SwapRequest accept(@PathVariable Long userId, @PathVariable Long requestId) {
        return srv.acceptRequest(userId, requestId);
    }

    @PostMapping("/{requestId}/reject/{userId}")
    public SwapRequest reject(@PathVariable Long userId, @PathVariable Long requestId) {
        return srv.rejectRequest(userId, requestId);
    }

    @PostMapping("/{requestId}/withdraw/{userId}")
    public void withdraw(@PathVariable Long userId, @PathVariable Long requestId) {
        srv.withdrawRequest(userId, requestId);
    }
}

package com.project.DtoConverter;

import com.project.Dto.SwapRequestDTO;
import com.project.Entity.Book;
import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;
import com.project.Entity.User;

public class SwapDtoConverter {

    public static SwapRequestDTO toSwapDTO(SwapRequest swap) {
        SwapRequestDTO dto = new SwapRequestDTO();

        dto.setRequesterId(swap.getRequester().getUserId());
        dto.setRequestedBookSetId(swap.getRequestedBookSet().getSetId());
        dto.setOfferedBookSetId(swap.getOfferedBookSet().getSetId());
        dto.setSwapStatus(swap.getSwapStatus());
        dto.setCreatedAt(swap.getCreatedAt());
        
        return dto;
    }

    public static SwapRequest toSwapRequestEntity(SwapRequestDTO dto, User requester, BookSet requestedBookSet, BookSet offeredBookSet) {
        SwapRequest swapRequest = new SwapRequest();
        
        swapRequest.setRequester(requester);
        swapRequest.setRequestedBookSet(requestedBookSet);
        swapRequest.setOfferedBookSet(offeredBookSet);
        swapRequest.setSwapStatus(dto.getSwapStatus());
        swapRequest.setCreatedAt(dto.getCreatedAt());
        
        return swapRequest;
    }
}

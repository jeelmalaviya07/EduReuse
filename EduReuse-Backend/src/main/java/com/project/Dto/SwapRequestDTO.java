package com.project.Dto;

import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;
import com.project.Entity.SwapStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SwapRequestDTO {
    private Long id;
    private Long requesterId;
    private BookSet requestedBookSetId;
    private BookSet offeredBookSetId;
    private SwapStatus swapStatus;
    private Timestamp createdAt;

    public SwapRequestDTO(){}

    public SwapRequestDTO(Long id, BookSet requestedBookSetId, BookSet offeredBookSetId, SwapStatus swapStatus, Timestamp createdAt) {
        this.id = id;
        this.requestedBookSetId = requestedBookSetId;
        this.offeredBookSetId = offeredBookSetId;
        this.swapStatus = swapStatus;
        this.createdAt = createdAt;
    }
}
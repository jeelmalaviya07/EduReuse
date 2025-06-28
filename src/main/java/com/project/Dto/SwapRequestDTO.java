package com.project.Dto;

import com.project.Entity.SwapRequest;
import com.project.Entity.SwapStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SwapRequestDTO {
    private Long requesterId;
    private Long requestedBookSetId;
    private Long offeredBookSetId;
    private SwapStatus swapStatus;
    private Timestamp createdAt;
}
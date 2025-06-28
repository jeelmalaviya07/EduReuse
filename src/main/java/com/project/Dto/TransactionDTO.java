package com.project.Dto;


import com.project.Entity.TransactionStatus;
import com.project.Entity.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class TransactionDTO {
    private Long buyerId;
    private Long sellerId;
    private Long bookId;
    private TransactionType transactionType;
    private TransactionStatus status;
    private Timestamp createdAt;
}

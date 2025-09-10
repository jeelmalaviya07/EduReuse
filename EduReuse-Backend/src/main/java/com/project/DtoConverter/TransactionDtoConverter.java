package com.project.DtoConverter;

import com.project.Dto.TransactionDTO;
import com.project.Entity.Book;
import com.project.Entity.BookSet;
import com.project.Entity.Transaction;
import com.project.Entity.User;

public class TransactionDtoConverter {

    public static TransactionDTO toTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();

        dto.setBuyerId(transaction.getBuyer().getUserId());
        dto.setSellerId(transaction.getSeller().getUserId() );
        dto.setBookId( transaction.getBookSet().getSetId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        
        return dto;
    }

    public static Transaction toTransactionEntity(TransactionDTO transactionDTO, User buyer, User seller, BookSet book) {
        Transaction transaction = new Transaction();

        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setBookSet(book);
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setCreatedAt(transactionDTO.getCreatedAt());

        return transaction;
    }
}

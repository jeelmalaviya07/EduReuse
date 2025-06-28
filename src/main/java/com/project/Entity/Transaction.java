package com.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @JsonBackReference
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    private User seller;

    //one to many
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookset_id")
    @JsonBackReference
    private BookSet bookSet;


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // SALE or DONATION only

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private BigDecimal price=BigDecimal.ZERO;

    private Timestamp createdAt;
}
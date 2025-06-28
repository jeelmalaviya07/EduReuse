package com.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class SwapRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @JsonBackReference
    private User requester;

    @ManyToOne
    @JoinColumn(name = "requested_bookset_id")
    @JsonBackReference
    private BookSet requestedBookSet;

    @ManyToOne
    @JoinColumn(name = "offered_bookset_id")
    @JsonBackReference
    private BookSet offeredBookSet;

    @Enumerated(EnumType.STRING)
    private SwapStatus swapStatus;

    private Timestamp createdAt;
}
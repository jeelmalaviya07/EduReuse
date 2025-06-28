package com.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "book_set",
        indexes = {
                @Index(name = "idx_deleted_bookset", columnList = "deleted")
        }
)
@SQLDelete(sql = "UPDATE book_set SET deleted = true WHERE set_id = ?")
@Where(clause = "deleted = false")
public class BookSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    private User seller;

    @OneToMany(mappedBy = "bookSet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Book> books;

    private String title;
    private Integer classLevel;
    private String board;

    @Enumerated(EnumType.STRING)
    private ListingType listingType; // SALE, DONATION, SWAP

    private BigDecimal price;
    private String bookConditionDescription;
    private String imageUrl;
    private Boolean completeSet;
    private Timestamp createdAt;

    // Only for Swap
    private Integer desiredClassLevel;
    private String desiredBoard;
    private Boolean desiredCompleteSet;

    @Column(nullable = false)
    private boolean deleted = false;
}

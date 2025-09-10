package com.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String author;
    private String subject;
    private String book_condition;

    private BigDecimal price;
    private BigDecimal quantity;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "book_set_id")
    @JsonBackReference
    private BookSet bookSet;

}

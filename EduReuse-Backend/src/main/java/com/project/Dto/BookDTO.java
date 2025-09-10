package com.project.Dto;

import com.project.Entity.BookSet;
import com.project.Entity.ListingType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class BookDTO {
    private String title;
    private String author;
    private String subject;
    private String book_condition;
    private BigDecimal price;
    private BigDecimal quantity;
    private String imageUrl;
}

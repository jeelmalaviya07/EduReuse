package com.project.Dto;

import com.project.Entity.ListingType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
public class BookSetDTO {
    private String title;
    private Integer classLevel;
    private String board;
    private ListingType listingType;
    private String bookCondition;
    private String imageUrl;
    private Timestamp createdAt;
    private Long sellerId;
    private Boolean completeSet;
    private List<BookDTO> books;
    private Integer desiredClassLevel;
    private String desiredBoard;
    private Boolean desiredCompleteSet;

}
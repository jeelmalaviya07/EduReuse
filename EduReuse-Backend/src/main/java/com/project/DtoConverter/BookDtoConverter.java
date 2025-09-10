package com.project.DtoConverter;

import com.project.Dto.BookDTO;
import com.project.Entity.Book;

import java.math.BigDecimal;

public class BookDtoConverter {

    public static Book toBookEntity(BookDTO bookDto) {
        Book book = new Book();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setSubject(bookDto.getSubject());
        book.setBook_condition(bookDto.getBook_condition());
        book.setPrice(bookDto.getPrice());
        book.setImageUrl(bookDto.getImageUrl());
        book.setQuantity(bookDto.getQuantity());

        return book;
    }

    public static BookDTO toBookDTO(Book book) {
        BookDTO bookDto = new BookDTO();

        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setSubject(book.getSubject());
        bookDto.setBook_condition(book.getBook_condition());
        bookDto.setPrice(book.getPrice());
        bookDto.setImageUrl(book.getImageUrl());
        bookDto.setQuantity(book.getQuantity());

        return bookDto;
    }

}

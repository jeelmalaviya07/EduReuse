package com.project.DtoConverter;

import com.project.Dao.UserRepository;
import com.project.Dto.BookDTO;
import com.project.Dto.BookSetDTO;
import com.project.Entity.Book;
import com.project.Entity.BookSet;
import com.project.Entity.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.project.Entity.ListingType.DONATION;

public class BookSetDtoConverter {

    @Autowired
    static UserRepository userRepository;

    public static BookSetDTO toBookSetDTO(BookSet set) {
        BookSetDTO bookSetDto = new BookSetDTO();

        bookSetDto.setTitle(set.getTitle());
        bookSetDto.setClassLevel(set.getClassLevel());
        bookSetDto.setBoard(set.getBoard());
        bookSetDto.setListingType(set.getListingType());
        bookSetDto.setBookCondition(set.getBookConditionDescription());
        bookSetDto.setImageUrl(set.getImageUrl());
        bookSetDto.setCreatedAt(set.getCreatedAt());
        bookSetDto.setCompleteSet(set.getCompleteSet());
        bookSetDto.setSellerId(set.getSeller().getUserId());
        bookSetDto.setDesiredCompleteSet(set.getDesiredCompleteSet());
        bookSetDto.setDesiredBoard(set.getDesiredBoard());
        bookSetDto.setDesiredClassLevel(set.getDesiredClassLevel());

        return bookSetDto;
    }

    public static Pair<BookSet,Long> toBookSetEntity(BookSetDTO dto) {
        BookSet bookSet = new BookSet();

        bookSet.setTitle(dto.getTitle());
        bookSet.setClassLevel(dto.getClassLevel());
        bookSet.setBoard(dto.getBoard());
        bookSet.setImageUrl(dto.getImageUrl());
        bookSet.setCreatedAt(dto.getCreatedAt());
        bookSet.setBookConditionDescription(dto.getBookCondition());
        bookSet.setCompleteSet(dto.getCompleteSet());
        bookSet.setListingType(dto.getListingType());
        bookSet.setDesiredBoard(dto.getDesiredBoard());
        bookSet.setDesiredCompleteSet(dto.getDesiredCompleteSet());
        bookSet.setDesiredClassLevel(dto.getDesiredClassLevel());

        BigDecimal bookSetPrice = BigDecimal.ZERO;
        List<Book> books = new ArrayList<>();

        if (dto.getBooks() != null) {
            for (BookDTO bookDto : dto.getBooks()) {
                Book book = new Book(); // new book for each iteration
                book.setTitle(bookDto.getTitle());
                book.setAuthor(bookDto.getAuthor());
                book.setSubject(bookDto.getSubject());
                book.setBook_condition(bookDto.getBook_condition());
                book.setPrice(bookDto.getPrice());
                book.setQuantity(bookDto.getQuantity());
                book.setImageUrl(bookDto.getImageUrl());

                //Setting the owning side of the relationship
                book.setBookSet(bookSet);

                if(dto.getListingType().equals(DONATION)) {
                    book.setPrice(BigDecimal.ZERO);
                }
                else if (bookDto.getPrice() != null) {
                    bookSetPrice = bookSetPrice.add(bookDto.getPrice().multiply(bookDto.getQuantity()));
                }
                books.add(book);

            }
        }

        if(dto.getListingType().equals(DONATION))
        {
            bookSetPrice = BigDecimal.ZERO;
        }

        bookSet.setPrice(bookSetPrice);
        bookSet.setBooks(books);
        return new Pair<>(bookSet,dto.getSellerId());
    }
}

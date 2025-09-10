package com.project.Controller;

import com.project.Dto.BookSetDTO;
import com.project.DtoConverter.BookSetDtoConverter;
import com.project.Entity.BookSet;
import com.project.Entity.User;
import com.project.Exception.NoSuchBookSetExistException;
import com.project.ServiceImpl.BookSetServiceImpl;

import com.project.ServiceImpl.UserServiceImpl;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users/booksets")
public class BookSetController
{
    @Autowired
    BookSetServiceImpl bookSetService;

    @PostMapping("")
    public ResponseEntity<BookSet> createBookSet(@RequestBody BookSetDTO bookSetDTO) {
        Pair<BookSet,Long> bookSetAndSellerId= BookSetDtoConverter.toBookSetEntity(bookSetDTO);
        bookSetService.createBookSet(bookSetAndSellerId);
        BookSet bookSet=bookSetAndSellerId.a;
        return new ResponseEntity<>(bookSet,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookSet> getBookSetById(@PathVariable("id") Long id) {
        BookSet bookSet = bookSetService.getBookSetById(id);
        return ResponseEntity.ok(bookSet);
    }

    @GetMapping("")
    public ResponseEntity<List<BookSet>> getAllBookSets()
    {
        return ResponseEntity.ok(bookSetService.getAllBookSets());
    }

    @GetMapping("/withSellerId")
    public ResponseEntity<List<Pair<BookSet,Long>>> getAllBookSetWithSeller()
    {
        List<BookSet> allBookSets = bookSetService.getAllBookSets();
        List<Pair<BookSet, Long>> res = new ArrayList<>();
        for (BookSet bookSet : allBookSets) {
            res.add(new Pair<>(bookSet,bookSet.getSeller().getUserId()));
        }
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookSet(@PathVariable("id") Long id) {
        bookSetService.deleteBookSet(id);
        return ResponseEntity.ok("BookSet with id " + id + " deleted successfully.");
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookSet> updateBookSet(@PathVariable("id") Long id, @RequestBody BookSetDTO bookSetDTO) {
        Pair<BookSet,Long> updatedSetWithSellerId = BookSetDtoConverter.toBookSetEntity(bookSetDTO);
        updatedSetWithSellerId.a.setSetId(id);
        BookSet saved = bookSetService.updateBookSet(updatedSetWithSellerId);
        return ResponseEntity.ok(saved);
    }

}

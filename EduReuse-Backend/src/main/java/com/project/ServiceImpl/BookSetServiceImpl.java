package com.project.ServiceImpl;

import com.project.Dao.BookSetRepository;
import com.project.Dao.SwapRequestRepository;
import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;
import com.project.Entity.SwapStatus;
import com.project.Entity.User;
import com.project.Exception.NoSuchBookSetExistException;
import com.project.Exception.NoSuchUserExist;
import com.project.Service.BookSetService;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookSetServiceImpl implements BookSetService {

    @Autowired
    BookSetRepository bookSetRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    SwapRequestRepository swapRequestRepository;

    @Transactional
    public BookSet createBookSet(Pair<BookSet, Long> bookSetAndSellerId) {
        BookSet bookSet = bookSetAndSellerId.a;
        Long sellerId = bookSetAndSellerId.b;

        User seller = userService.getUser(sellerId);
        if (seller.isDeleted()) {
            throw new NoSuchUserExist("Cannot create BookSet. Seller is deleted with id: " + sellerId);
        }

        bookSet.setSeller(seller);
        seller.getBookSets().add(bookSet);

        return bookSetRepository.save(bookSet);
    }

    public BookSet getBookSetById(Long id) {
        BookSet bookSet = bookSetRepository.findById(id)
                .orElseThrow(() -> new NoSuchBookSetExistException("No BookSet found with id: " + id));

        if (bookSet.isDeleted()) {
            throw new NoSuchBookSetExistException("BookSet is deleted with id: " + id);
        }

        return bookSet;
    }

    public List<BookSet> getAllBookSets() {
        return bookSetRepository.findAll();
    }

    @Transactional
    public void deleteBookSet(Long id) {
        BookSet bookSet = bookSetRepository.findById(id)
                .orElseThrow(() -> new NoSuchBookSetExistException("Cannot delete. No BookSet exists with id: " + id));

        if (bookSet.isDeleted()) {
            throw new NoSuchBookSetExistException("BookSet already deleted with id: " + id);
        }

        User seller = bookSet.getSeller();

        if (seller == null) {
            throw new NoSuchUserExist("No such user exists for BookSet with id: " + id);
        }

        List<SwapRequest> requestList=swapRequestRepository.findByRequestedBookSet_SetId(id);
        List<SwapRequest> receiptList=swapRequestRepository.findByOfferedBookSet_SetId(id);

        for(SwapRequest swapRequest:requestList) {
            swapRequest.setSwapStatus(SwapStatus.ABORTED);
        }

        for(SwapRequest swapRequest:receiptList){
            swapRequest.setSwapStatus(SwapStatus.ABORTED);
        }

        bookSet.setDeleted(true);

        bookSetRepository.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(Pair<BookSet, Long> updatedSetWithSellerId) {
        Long bookSetId = updatedSetWithSellerId.a.getSetId();
        BookSet updatedBookSet = updatedSetWithSellerId.a;
        Long sellerId = updatedSetWithSellerId.b;

        BookSet existingBookSet = bookSetRepository.findById(bookSetId)
                .orElseThrow(() -> new NoSuchBookSetExistException("No such BookSet exists with id: " + bookSetId));

        if (existingBookSet.isDeleted()) {
            throw new NoSuchBookSetExistException("Cannot update. BookSet is already deleted with id: " + bookSetId);
        }

        User seller = userService.getUser(sellerId);
        if (seller.isDeleted()) {
            throw new NoSuchUserExist("Cannot assign to deleted User with id: " + sellerId);
        }

        updatedBookSet.setSetId(existingBookSet.getSetId());
        updatedBookSet.setDeleted(false);
        updatedBookSet.setSeller(seller);

        return bookSetRepository.save(updatedBookSet);
    }
}

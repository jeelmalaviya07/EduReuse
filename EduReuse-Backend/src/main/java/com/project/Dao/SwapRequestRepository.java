package com.project.Dao;

import com.project.Entity.Book;
import com.project.Entity.BookSet;
import com.project.Entity.SwapRequest;
import com.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    Boolean existsByOfferedBookSetAndRequestedBookSet(BookSet offered, BookSet requested);

    List<SwapRequest> findByRequestedBookSetSeller(User seller);

    List<SwapRequest> findByRequester(User requester);

    List<SwapRequest> findByRequestedBookSet_SetId(Long setId);

    List<SwapRequest> findByRequesterUserId(Long requesterId);

    List<SwapRequest> findByOfferedBookSet_SetId(Long setId);

    List<SwapRequest> findByRequester_UserId(Long userId);
}

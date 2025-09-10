package com.project.Dao;

import com.project.Entity.BookSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSetRepository extends JpaRepository<BookSet,Long> {
    List<BookSet> findBySeller_UserId(Long userId);
    Page<BookSet> findBySeller_UserIdNot(Long sellerId, Pageable pageable);
}
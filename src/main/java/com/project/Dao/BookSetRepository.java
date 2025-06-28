package com.project.Dao;

import com.project.Entity.BookSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSetRepository extends JpaRepository<BookSet,Long> {

}
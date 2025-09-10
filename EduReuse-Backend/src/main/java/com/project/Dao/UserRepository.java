package com.project.Dao;

import com.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByMobile(String mobile);
    List<User> findAllByDeletedFalse();
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User findAnyByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE mobile = :mobile", nativeQuery = true)
    User findAnyByMobile(@Param("mobile") String mobile);
}


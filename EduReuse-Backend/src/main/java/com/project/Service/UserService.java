package com.project.Service;

import com.project.Entity.BookSet;
import com.project.Entity.User;

import java.util.List;


public interface UserService {

    User createUser(User user);
    User getUser(Long userId);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
    List<BookSet> getMyBookSets(Long userId);
}

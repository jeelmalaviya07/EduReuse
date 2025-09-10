package com.project.ServiceImpl;

import com.project.Dao.SwapRequestRepository;
import com.project.Dao.UserRepository;
import com.project.Entity.*;
import com.project.Exception.NoSuchUserExist;
import com.project.Exception.UserAlreadyExistException;
import com.project.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        String email = user.getEmail();
        String mobile = user.getMobile();

        User existingUserWithEmail = userRepository.findAnyByEmail(email);
        User existingUserWithMobile = userRepository.findAnyByMobile(mobile);

        if (existingUserWithEmail != null) {
            throw new UserAlreadyExistException("User already exists with email: " + email);
        }
        if (existingUserWithMobile != null) {
            throw new UserAlreadyExistException("User already exists with mobile: " + mobile);
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExist("No such user exists with id: " + id));

        if (user.isDeleted()) {
            throw new NoSuchUserExist("No such user exists with id: " + id);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByDeletedFalse();
    }

    public List<BookSet> getMyBookSets(Long userId)
    {
        User user=userRepository.findById(userId).get();
        List<BookSet> myBookSets=user.getBookSets();
        return myBookSets;
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExist("User already deleted or does not exist with id: " + id));

        if (user.isDeleted()) {
            throw new NoSuchUserExist("User already deleted or does not exist with id: " + id);
        }

        user.setDeleted(true);

        for (BookSet bookSet : user.getBookSets()) {
            bookSet.setDeleted(true);
        }

        List<SwapRequest> requestList = swapRequestRepository.findByRequester(user);
        List<SwapRequest> receiptList = swapRequestRepository.findByRequestedBookSetSeller(user);

        for (SwapRequest swapRequest : requestList) {
            swapRequest.setSwapStatus(SwapStatus.ABORTED);
        }

        for (SwapRequest swapRequest : receiptList) {
            swapRequest.setSwapStatus(SwapStatus.ABORTED);
        }

        userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExist("Cannot update. User does not exist with id: " + id));

        if (existingUser.isDeleted()) {
            throw new NoSuchUserExist("Cannot update. User is deleted with id: " + id);
        }

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMobile(updatedUser.getMobile());
        existingUser.setCity(updatedUser.getCity());
        existingUser.setState(updatedUser.getState());
        existingUser.setRole(updatedUser.getRole());

        if (!passwordEncoder.matches(updatedUser.getPassword(), existingUser.getPassword())) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }
}
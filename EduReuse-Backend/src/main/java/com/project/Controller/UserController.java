package com.project.Controller;

import com.project.Dto.UserDTO;
import com.project.DtoConverter.UserDtoConverter;
import com.project.Entity.BookSet;
import com.project.Entity.User;
import com.project.ServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId){
        User user=userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getAllUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElse(null); // or throw exception if not found

        return ResponseEntity.ok(user);
    }

    @GetMapping("myBookSets/{id}")
    public ResponseEntity<List<BookSet>> getMyBookSets(@PathVariable("id") Long id)
    {
        List<BookSet> bookSetList=userService.getMyBookSets(id);
        return ResponseEntity.ok(bookSetList);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUsers=userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User user= UserDtoConverter.toUserEntity(userDTO);
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return  ResponseEntity.ok("User with id: "+userId+" deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id")Long userId,@RequestBody User updatedUser) {
        userService.updateUser(userId,updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

}

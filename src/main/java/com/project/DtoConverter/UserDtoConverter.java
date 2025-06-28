package com.project.DtoConverter;

import com.project.Dto.UserDTO;
import com.project.Entity.User;

public class UserDtoConverter {

    public static UserDTO toUserDto(User user) {
        UserDTO userDto = new UserDTO();
        
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setMobile(user.getMobile());
        userDto.setCity(user.getCity());
        userDto.setState(user.getState());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        
        return userDto;
    }

    public static User toUserEntity(UserDTO userDto) {
        User user = new User();

        user.setFullName(userDto.getFullName());
        user.setPassword(user.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setMobile(userDto.getMobile());
        user.setCity(userDto.getCity());
        user.setState(userDto.getState());
        user.setRole(userDto.getRole());
        user.setCreatedAt(userDto.getCreatedAt());
        
        return user;
    }
}

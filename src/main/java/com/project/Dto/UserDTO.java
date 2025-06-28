package com.project.Dto;

import com.project.Entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Data
public class UserDTO {
    private String fullName;
    private String email;
    private String password;
    private String mobile;
    private Role role; // USER, ADMIN
    private String city;
    private String state;
    private Timestamp createdAt;
}

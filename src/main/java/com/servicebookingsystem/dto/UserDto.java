package com.servicebookingsystem.dto;

import com.servicebookingsystem.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private long id;

    private String email;

    private String password;

    private  String name;

    private String lastname;

    private String phone;

    private UserRole role;
}

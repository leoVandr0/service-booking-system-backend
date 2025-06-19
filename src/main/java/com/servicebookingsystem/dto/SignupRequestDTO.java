package com.servicebookingsystem.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignupRequestDTO {

    private long id;

    private String email;

    private String password;

    private  String name;

    private String lastname;

    private String phone;
}

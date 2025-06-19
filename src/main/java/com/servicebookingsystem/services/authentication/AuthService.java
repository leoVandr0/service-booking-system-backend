package com.servicebookingsystem.services.authentication;

import com.servicebookingsystem.dto.SignupRequestDTO;
import com.servicebookingsystem.dto.UserDto;

public interface AuthService {

    public UserDto signupClient(SignupRequestDTO signupRequestDTO);

    public Boolean presentByEmail(String email);

    public UserDto signupCompany(SignupRequestDTO signupRequestDTO);
}

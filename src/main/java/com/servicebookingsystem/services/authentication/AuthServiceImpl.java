package com.servicebookingsystem.services.authentication;

import com.servicebookingsystem.dto.AdDTO;
import com.servicebookingsystem.dto.SignupRequestDTO;
import com.servicebookingsystem.dto.UserDto;
import com.servicebookingsystem.entity.User;
import com.servicebookingsystem.enums.UserRole;
import com.servicebookingsystem.repository.AdRepository;
import com.servicebookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;


    public UserDto signupClient(SignupRequestDTO signupRequestDTO){
        User user = new User();

        user.setName(signupRequestDTO.getName());
        user.setLastname(signupRequestDTO.getLastname());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.CLIENT);

        return userRepository.save(user).getDto(); // converting this user into userDto
    }

    public Boolean presentByEmail(String email){
        return userRepository.findFirstByEmail(email) != null;
    }

    public UserDto signupCompany(SignupRequestDTO signupRequestDTO){
        User user = new User();

        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.COMPANY);

        return userRepository.save(user).getDto(); // converting this user into userDto
    }


}

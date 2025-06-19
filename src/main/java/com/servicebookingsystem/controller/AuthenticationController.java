package com.servicebookingsystem.controller;

import com.servicebookingsystem.dto.AuthenticationRequest;
import com.servicebookingsystem.dto.SignupRequestDTO;
import com.servicebookingsystem.dto.UserDto;
import com.servicebookingsystem.entity.User;
import com.servicebookingsystem.repository.UserRepository;
import com.servicebookingsystem.services.authentication.AuthService;
import com.servicebookingsystem.services.jwt.UserDetailsServiceImpl;
import com.servicebookingsystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;



    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER_STRING = "Authorization";



    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){
        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Client already exists with this email!", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto createdUser = authService.signupClient(signupRequestDTO);

        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }


    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){
        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Company already exists with this email!", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto createdUser = authService.signupCompany(signupRequestDTO);

        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }



    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

        JSONObject responseBody = new JSONObject();
        try {
            responseBody.put("token", jwt);
            responseBody.put("userId", user.getId());
            responseBody.put("role", user.getRole());
        } catch (JSONException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON error");
        }

        return ResponseEntity.ok(responseBody.toString());
    }


//
//    @PostMapping("/authenticate")
//    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
//                                          HttpServletResponse response) throws IOException, JSONException, java.io.IOException {
//
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
//            )
//            );
//        } catch (BadCredentialsException e){
//            throw new BadCredentialsException("Incorrect username or password", e);
//
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//
//        final  String jwt = jwtUtil.generateToken(userDetails.getUsername());
//        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());
//
//        response.getWriter().write(new JSONObject()
//                .put("userId", user.getId())
//                .put("role", user.getRole())
//                .toString()
//        );
//
//        response.addHeader("Access-Control-Expose-Headers", "Authorization");
//        response.addHeader("Access-Control-Allow-Headers", "Authorization" +
//                "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
//
//        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
//
//    }

}
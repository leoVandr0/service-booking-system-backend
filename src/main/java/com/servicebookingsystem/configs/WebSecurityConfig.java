package com.servicebookingsystem.configs;

import com.servicebookingsystem.services.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter requestFilter;


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                          .requestMatchers(
//                                "/api/v1/authenticate",
//                                "/api/v1/company/sign-up",
//                                "/api/v1/client/sign-up",
//                                "/api/v1/ads",
//                                "/api/v1/search/**",
//                                "/error",
//                                "/favicon.ico"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/authenticate",
                                "/api/v1/company/sign-up",
                                "/api/v1/client/sign-up",
                                "/api/v1/ads",
                                "/api/v1/search/**",
                                "/error",                  // ✅ Allow access to error page
                                "/favicon.ico",            // (optional)
                                "/actuator/**"            // (optional for actuator monitoring)

                        ).permitAll()
                        .requestMatchers("/api/v1/company/ad/**", "/api/v1/company/ads/**").permitAll() //hasAuthority("ROLE_COMPANY")

                        .requestMatchers("/api/**").authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

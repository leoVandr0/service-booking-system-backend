package com.servicebookingsystem.controller;

import com.servicebookingsystem.dto.AdDTO;
import com.servicebookingsystem.services.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/company")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    @PostMapping(value = "/ad/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postAd(@PathVariable Long userId, @ModelAttribute AdDTO adDTO) {
        try {
            boolean success = companyService.postAd(userId, adDTO);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                // Validation failed or user not found
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ad data or user not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAd(@PathVariable Long adId) {
        try {
            AdDTO ad = companyService.getAdById(adId);
            if (ad == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ad not found");
            }
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/ads/{userId}")
    public ResponseEntity<?> getAllAdsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(companyService.getAllAds(userId));
    }
}

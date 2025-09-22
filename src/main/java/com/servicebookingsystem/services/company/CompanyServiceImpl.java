package com.servicebookingsystem.services.company;

import com.servicebookingsystem.dto.AdDTO;
import com.servicebookingsystem.entity.Ad;
import com.servicebookingsystem.entity.User;
import com.servicebookingsystem.repository.AdRepository;
import com.servicebookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;


    public boolean postAd(Long userId, AdDTO adDTO) throws IOException {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        if (adDTO == null) {
            throw new IllegalArgumentException("Ad data cannot be null");
        }
        
        if (adDTO.getServiceName() == null || adDTO.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name is required");
        }
        
        if (adDTO.getDescription() == null || adDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        
        if (adDTO.getPrice() == null || adDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Valid price is required");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
        
        Ad ad = new Ad();
        ad.setServiceName(adDTO.getServiceName().trim());
        ad.setDescription(adDTO.getDescription().trim());
        ad.setPrice(adDTO.getPrice());
        ad.setUser(optionalUser.get());
        
        if (adDTO.getImg() != null) {
            ad.setImg(adDTO.getImg().getBytes());
        } else {
            // If image is required, throw exception instead of returning false
            throw new IllegalArgumentException("Image is required for ad creation");
        }

        try {
            adRepository.save(ad);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save ad: " + e.getMessage(), e);
        }
    }


    public List<AdDTO> getAllAds(Long userId){
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        return adRepository.findAllByUserId(userId).stream().map(Ad::getAdDto).collect(Collectors.toList());
    }

    @Override
    public AdDTO getAdById(Long adId) {
        if (adId == null) {
            throw new IllegalArgumentException("Ad ID cannot be null");
        }
        
        return adRepository.findById(adId)
                .map(Ad::getAdDto)
                .orElseThrow(() -> new EntityNotFoundException("Ad with ID " + adId + " not found"));
    }
}

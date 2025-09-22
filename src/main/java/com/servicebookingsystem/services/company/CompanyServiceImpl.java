package com.servicebookingsystem.services.company;

import com.servicebookingsystem.dto.AdDTO;
import com.servicebookingsystem.entity.Ad;
import com.servicebookingsystem.entity.User;
import com.servicebookingsystem.repository.AdRepository;
import com.servicebookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Ad ad = new Ad();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            if (adDTO.getImg() != null) {
                ad.setImg(adDTO.getImg().getBytes());
            } else {
                // If image is required, return false to signal validation failure to controller
                return false;
            }
            ad.setPrice(adDTO.getPrice());
            ad.setUser(optionalUser.get());


            adRepository.save(ad);
            return true;
        }
        return false;
    }


    public List<AdDTO> getAllAds(Long userId){
        return adRepository.findAllByUserId(userId).stream().map(Ad::getAdDto).collect(Collectors.toList());
    }

    @Override
    public AdDTO getAdById(Long adId) {
        return adRepository.findById(adId)
                .map(Ad::getAdDto)
                .orElse(null);
    }
}

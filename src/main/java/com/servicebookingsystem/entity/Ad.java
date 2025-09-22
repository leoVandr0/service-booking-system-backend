package com.servicebookingsystem.entity;

import com.servicebookingsystem.dto.AdDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ads")
@Data
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceName;

    private String description;

    private Double price;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] img;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore  // Prevent circular reference during JSON serialization
    private User user;


    public AdDTO getAdDto(){
        AdDTO adDTO = new AdDTO();

        adDTO.setId(id);
        adDTO.setServiceName(serviceName);
        adDTO.setDescription(description);
        adDTO.setPrice(price);
        
        // Handle potential null user to prevent NPE
        if (user != null) {
            adDTO.setCompanyName(user.getName());
        } else {
            adDTO.setCompanyName("Unknown Company");
        }
        
        adDTO.setReturnedImg(img);

        return adDTO;
    }


}

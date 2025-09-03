package com.furniture.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfo {
    
    @Column(name = "street_address")
    private String streetAddress;
    
    @Column(name = "unit")
    private String unit;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    @Column(name = "country")
    private String country;
    
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress);
        
        if (unit != null && !unit.trim().isEmpty()) {
            sb.append(" ").append(unit);
        }
        
        sb.append(", ").append(city)
          .append(", ").append(state)
          .append(" ").append(zipCode);
        
        if (country != null && !country.trim().isEmpty()) {
            sb.append(", ").append(country);
        }
        
        return sb.toString();
    }
}
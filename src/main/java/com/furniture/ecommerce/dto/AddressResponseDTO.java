package com.furniture.ecommerce.dto;

public class AddressResponseDTO {
    
    private String streetAddress;
    private String unit;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    // Constructors
    public AddressResponseDTO() {
    }
    
    public AddressResponseDTO(String streetAddress, String unit, String city, 
                            String state, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    // Factory method for creating from AddressInfo entity
    public static AddressResponseDTO fromAddressInfo(com.furniture.ecommerce.model.AddressInfo addressInfo) {
        if (addressInfo == null) {
            return null;
        }
        
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setStreetAddress(addressInfo.getStreetAddress());
        dto.setUnit(addressInfo.getUnit());
        dto.setCity(addressInfo.getCity());
        dto.setState(addressInfo.getState());
        dto.setZipCode(addressInfo.getZipCode());
        dto.setCountry(addressInfo.getCountry());
        
        return dto;
    }
    
    // Helper method to get formatted address
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
    
    // Getters and Setters
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}
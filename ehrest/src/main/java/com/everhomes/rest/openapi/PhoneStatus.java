package com.everhomes.rest.openapi;

public class PhoneStatus {
    private String phone;
    private String status;
    
    public PhoneStatus() {
        
    }
    public PhoneStatus(String phone, String status) {
        this.phone = phone;
        this.status = status;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

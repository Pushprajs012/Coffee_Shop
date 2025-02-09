package com.ps.coffeeshop.Data;

public class User {
    private String fullName;
    private String mobile;
    private String email;
    private String address;
    private String pinCode;

    public User() {

    }

    public User(String fullName, String mobile, String email, String address, String pinCode) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.pinCode = pinCode;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }
}

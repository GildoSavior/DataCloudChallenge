package com.datacloudchallenge.AdminCliente.domain.dtos.auth.login;


public class LoginRequest {
    private String phoneNumber;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

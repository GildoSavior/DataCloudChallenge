package com.datacloudchallenge.AdminCliente.domain.dtos.auth;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;


public class AuthResponse {
    private String jwtToken;
    private String phoneNumber;
    private AccessLevel accessLevel;

    public AuthResponse(String phoneNumber, AccessLevel accessLevel, String jwtToken) {
        this.jwtToken = jwtToken;
        this.phoneNumber = phoneNumber;
        this.accessLevel = accessLevel;
    }

    public AuthResponse() {
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}

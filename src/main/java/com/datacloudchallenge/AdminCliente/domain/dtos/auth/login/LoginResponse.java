package com.datacloudchallenge.AdminCliente.domain.dtos.auth.login;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

public class LoginResponse {
    private String jwtToken;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    public LoginResponse(String jwtToken, String phoneNumber, AccessLevel accessLevel) {
        this.jwtToken = jwtToken;
        this.phoneNumber = phoneNumber;
        this.accessLevel = accessLevel;
    }

    public LoginResponse() {
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

package com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private String password;

    public SignUpRequest(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public SignUpRequest() {
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    //    TODO.GS.14.02.2024 - Validar se há necessidade de manter este atributo nesta class
//    @Enumerated(EnumType.STRING)
//    private AccessLevel accessLevel;
}

package com.datacloudchallenge.AdminCliente.domain.dtos.auth.login;


import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}

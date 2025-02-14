package com.datacloudchallenge.AdminCliente.domain.dtos.auth.login;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

}

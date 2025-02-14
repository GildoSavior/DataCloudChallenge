package com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup;

import com.datacloudchallenge.AdminCliente.data.models.User;
import lombok.Data;

@Data
public class SignUpResponse {
    private User user;
    private String jwtToken;
}

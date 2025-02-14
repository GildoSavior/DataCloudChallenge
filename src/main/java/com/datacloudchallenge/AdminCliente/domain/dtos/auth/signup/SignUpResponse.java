package com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup;

import com.datacloudchallenge.AdminCliente.data.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SignUpResponse {
    private User user;
    private String jwtToken;

    public SignUpResponse() {
    }

    public SignUpResponse(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

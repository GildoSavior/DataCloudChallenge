package com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;


public class SignUpResponse {
    private UserModel user;
    private String jwtToken;

    public SignUpResponse() {
    }

    public SignUpResponse(UserModel user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public UserModel getUser() {
        return user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

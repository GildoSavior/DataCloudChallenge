package com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
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

    public static UserModel signupRequestToUser(SignUpRequest request) {
        UserModel user = new UserModel();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAccessLevel(AccessLevel.ROLE_ADMIN);
        return user;
    }
}

package com.datacloudchallenge.AdminCliente.data.mappers;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;


import java.util.Date;

public class UserMapper {
    public static UserModel toUser(SignUpRequest request) {
        UserModel user = new UserModel();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAccessLevel(AccessLevel.CLIENT);
        user.setLastLogin(new Date()); // Define a data de último login como agora
        return user;
    }
}

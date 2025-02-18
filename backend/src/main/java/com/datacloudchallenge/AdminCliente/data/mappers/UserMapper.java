package com.datacloudchallenge.AdminCliente.data.mappers;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import org.apache.catalina.User;

public class UserMapper {


    public static UserModel userDtoToUser(UserDto request) {
        UserModel user = new UserModel();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAccessLevel(request.getAccessLevel());
        user.setImageUrl(request.getImageUrl());
        return user;
    }


}

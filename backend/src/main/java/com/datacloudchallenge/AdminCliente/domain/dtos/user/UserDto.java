package com.datacloudchallenge.AdminCliente.domain.dtos.user;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;

import java.time.LocalDateTime;

public class UserDto {

    public static final String DEFAULT_PASSWORD = "12345";

    private String name;

    private String imageUrl;

    private String phoneNumber;

    private String email;

    private String password = DEFAULT_PASSWORD;

    private AccessLevel accessLevel;
    private LocalDateTime lastLogin;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public String getPassword() {return password;}

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserDto() {
    }


    public UserDto(String name, String imageUrl, String phoneNumber, String email, String password, AccessLevel accessLevel, LocalDateTime lastLogin) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
        this.lastLogin = lastLogin;
    }

    public static UserDto userToUserDto(UserModel userModel) {
        UserDto userDto = new UserDto();
        userDto.setName(userModel.getName());
        userDto.setPhoneNumber(userModel.getPhoneNumber());
        userDto.setEmail(userModel.getEmail());
        userDto.setPassword(userModel.getPassword());
        userDto.setAccessLevel(userModel.getAccessLevel());
        userDto.setImageUrl(userModel.getImageUrl());
        userDto.setLastLogin(userModel.getLastLogin());
        return userDto;
    }
}

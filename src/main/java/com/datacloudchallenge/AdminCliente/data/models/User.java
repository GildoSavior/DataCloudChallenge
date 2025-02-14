package com.datacloudchallenge.AdminCliente.data.models;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 6)
    private String name;

    private String imageUrl;

    private Date data;

    @NotNull
    @Size(min = 9, max = 9)
    private String phoneNumber;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccessLevel accessLevel;

    private Date lastLogin;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getData() {
        return data;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setData(Date data) {
        this.data = data;
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

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public User() {
    }

    public User(UUID id, String name, String imageUrl, Date data, String phoneNumber, String email, String password, AccessLevel accessLevel, Date lastLogin) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.data = data;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
        this.lastLogin = lastLogin;
    }
}

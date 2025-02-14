package com.datacloudchallenge.AdminCliente.data.models;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Date data;

    @Column(name = "phone_number", length = 9, nullable = false)
    private String phoneNumber;

    private String email;


    private String password;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    private Date lastLogin;

    private boolean enabled;

    public Long getId() {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setId(Long id) {
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserModel() {
    }



    public UserModel(Long id, String name, String imageUrl, Date data, String phoneNumber, String email, String password, AccessLevel accessLevel, Date lastLogin, boolean enabled) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.data = data;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
        this.lastLogin = lastLogin;
        this.enabled = enabled;
    }
}

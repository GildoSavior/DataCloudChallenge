package com.datacloudchallenge.AdminCliente.data.models;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String imageUrl;
    private Date data;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
}

package com.datacloudchallenge.AdminCliente.data.models;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String imageUrl;
    private Date data;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
    private Date lastLogin;
}

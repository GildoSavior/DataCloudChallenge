package com.datacloudchallenge.AdminCliente.data.repository;

import com.datacloudchallenge.AdminCliente.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    void deleteByEmail(String email);
}

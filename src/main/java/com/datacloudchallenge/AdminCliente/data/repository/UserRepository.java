package com.datacloudchallenge.AdminCliente.data.repository;

import com.datacloudchallenge.AdminCliente.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

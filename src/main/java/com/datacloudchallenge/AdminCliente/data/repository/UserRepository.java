package com.datacloudchallenge.AdminCliente.data.repository;

import com.datacloudchallenge.AdminCliente.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

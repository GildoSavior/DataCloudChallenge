package com.datacloudchallenge.AdminCliente.data.repository;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByPhoneNumber(String phoneNumber);

    void deleteByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    void deleteByPhoneNumber(String phoneNumber);
}

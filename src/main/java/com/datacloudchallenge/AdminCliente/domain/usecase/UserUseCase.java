package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.data.models.User;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    Result<List<User>> findAll();
    Result<User> findUserById(UUID id);
    Result<User> findUserByEmail(String email);
    Result<User> createUser(User user);
    Result<User> updateUser(User user);
    Result<String> deleteUser(String email);
}

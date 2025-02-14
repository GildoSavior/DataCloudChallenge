package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;

import java.util.List;

public interface UserUseCase {
    Result<List<UserModel>> findAll();
    Result<UserModel> findUserById(Long id);
    Result<UserModel> findUserByEmail(String email);
    Result<UserModel> createUser(UserModel user);
    Result<UserModel> updateUser(UserModel user);
    Result<String> deleteUser(String email);
}

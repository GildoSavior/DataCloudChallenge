package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UpdateUserClientResquest;

import java.util.List;

public interface UserUseCase {
    Result<List<UserDto>> findAll();
    Result<UserDto> findUserById(Long id);
    Result<UserDto> findUserByEmail(String email);
    Result<UserDto> createUser(UserDto user);
    Result<UserDto> updateUser(String phoneNumber, UpdateUserClientResquest user );
    Result<String> deleteUser(String phoneNumber);

    Result<UserModel> findUserByPhoneNumber(String username);
}

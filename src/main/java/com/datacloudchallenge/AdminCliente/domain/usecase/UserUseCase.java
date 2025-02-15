package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;

import java.util.List;

public interface UserUseCase {
    Result<List<UserDto>> findAll();
    Result<UserDto> findUserById(Long id);
    Result<UserDto> findUserByEmail(String email);
    Result<UserDto> createUser(UserDto user);
    Result<UserDto> updateUser(String userToUpdatePhoneNumber, UserDto user );
    Result<String> deleteUserByPhoneNumber(String phoneNumber);
    Result<UserDto> findUserByPhoneNumber(String phoneNumber);
}

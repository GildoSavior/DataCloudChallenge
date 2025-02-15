package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.AuthResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;

public interface AuthUseCase {
    Result<AuthResponse> createUser(SignUpRequest request);
    Result<AuthResponse> login(LoginRequest request);
    Result<String> logout();
}

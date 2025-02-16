package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.HttpResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.AuthResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.usecase.AuthUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUseCase authUseCase;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {

        Result<AuthResponse> result = authUseCase.createUser(request);

        HttpResponse<AuthResponse> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<?> signUp(@RequestBody LoginRequest request) {
        Result<AuthResponse> result = authUseCase.login(request);
        HttpResponse<AuthResponse> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

}


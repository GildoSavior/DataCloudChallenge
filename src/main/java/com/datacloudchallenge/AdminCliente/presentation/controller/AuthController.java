package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.AuthResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.usecase.AuthUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUseCase authUseCase;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {

        Result<AuthResponse> result = authUseCase.createUser(request);
        if(result.isOk())
            return ResponseEntity.ok(result.getData());

        return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + result.getMessage());

    }

    @PostMapping("/login")
    public ResponseEntity<?> signUp(@RequestBody LoginRequest request) {

        Result<AuthResponse> result = authUseCase.login(request);
        if(result.isOk())
            return ResponseEntity.ok(result.getData());

        return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + result.getMessage());

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        Result<String> result = authUseCase.logout();
        if(result.isOk())
            return ResponseEntity.ok(result.getData());

        return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + result.getMessage());

    }
}


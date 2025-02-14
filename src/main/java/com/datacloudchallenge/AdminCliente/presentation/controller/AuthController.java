package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpResponse;
import com.datacloudchallenge.AdminCliente.domain.services.AuthService;
import com.datacloudchallenge.AdminCliente.domain.usecase.AuthUseCase;
import jakarta.annotation.PostConstruct;
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
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {

        Result<SignUpResponse> result = authService.signUp(request);
        if(result.isOk())
            return ResponseEntity.ok(result.getData());

        return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + result.getMessage());

    }
}


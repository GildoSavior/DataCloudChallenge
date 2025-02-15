package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.AuthResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<?> findAll() {

        Result<List<UserDto>> result = userUseCase.findAll();
        if(result.isOk())
            return ResponseEntity.ok(result.getData());

        return ResponseEntity.badRequest().body("Erro ao processar a solicitação: " + result.getMessage());

    }
}

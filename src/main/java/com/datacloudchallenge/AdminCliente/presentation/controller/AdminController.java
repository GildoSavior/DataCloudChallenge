package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/users")
    public ResponseEntity<?> findAllClients() {
        Result<List<UserDto>> result = userUseCase.findAll();

        return result.isOk() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @PostMapping("/clients")
    public ResponseEntity<?> createClient(@RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.createUser(user);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }


    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestParam String userToUpdatePhoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(userToUpdatePhoneNumber, user);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@RequestParam String phoneNumber) {

            Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
            return result.isOk() ? ResponseEntity.ok("Cliente removido com sucesso") : ResponseEntity.badRequest().body(result.getMessage());

    }

    public ResponseEntity<?> getInfo(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        return result.isOk() ? ResponseEntity.ok(result.getMessage()) : ResponseEntity.badRequest().body(result.getMessage());
    }


}


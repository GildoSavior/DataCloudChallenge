package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() {
        Result<List<UserDto>> result = userUseCase.findAll();
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @GetMapping("/users/user")
    public ResponseEntity<?> getInfo(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.createUser(user);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestParam String phoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(phoneNumber, user);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam String phoneNumber) {
        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        return result.isOk() ? ResponseEntity.ok(result.getMessage()) : ResponseEntity.badRequest().body(result.getMessage());
    }
}

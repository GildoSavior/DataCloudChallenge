package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.HttpResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() {
        Result<List<UserDto>> result = userUseCase.findAll();
        HttpResponse<List<UserDto>> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getInfo(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.createUser(user);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestParam String phoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(phoneNumber, user);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam String phoneNumber) {
        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        HttpResponse<String> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}

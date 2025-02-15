package com.datacloudchallenge.AdminCliente.presentation.controller;

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
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/users")
    public ResponseEntity<?> findAllClients() {
        Result<List<UserDto>> result = userUseCase.findAll();
        HttpResponse<List<UserDto>> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> createClient(@RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.createUser(user);
        return result.isOk() ? ResponseEntity.ok(new HttpResponse<>(result.getMessage(), result.getData())) :
                ResponseEntity.badRequest().body(new HttpResponse<>(result.getMessage(), result.getData()));
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestParam String phoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(phoneNumber, user);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }


    @DeleteMapping("/users")
    public ResponseEntity<?> delete(@RequestParam String phoneNumber) {
        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        HttpResponse<String> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getInfo(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }


}


package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.HttpResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/user")
    public ResponseEntity<?> getProfile(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateProfile(@RequestParam String phoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(phoneNumber, user);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteProfile(@RequestParam String phoneNumber) {
        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        HttpResponse<String> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

}

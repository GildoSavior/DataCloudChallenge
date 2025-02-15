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

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String phoneNumber) {
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(phoneNumber);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam String userToUpdatePhoneNumber, @RequestBody UserDto user) {
        Result<UserDto> result = userUseCase.updateUser(userToUpdatePhoneNumber, user);
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(Authentication authentication) {
        String phoneNumber = authentication.getName();
        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        HttpResponse<String> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

}

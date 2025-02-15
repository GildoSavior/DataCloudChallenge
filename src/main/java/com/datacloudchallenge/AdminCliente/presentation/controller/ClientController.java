package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        Result<UserDto> result = userUseCase.findUserByPhoneNumber(username);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam String userToUpdatePhoneNumber, @RequestBody UserDto user) {

        Result<UserDto> result = userUseCase.updateUser(userToUpdatePhoneNumber, user);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication) {
        String phoneNumber = authentication.getName();

        Result<String> result = userUseCase.deleteUserByPhoneNumber(phoneNumber);
        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
    }


}

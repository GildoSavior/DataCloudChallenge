package com.datacloudchallenge.AdminCliente.presentation.controller;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
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

//
//    @PutMapping("/clients/{id}")
//    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody UserModel user) {
//        if (!"ROLE_CLIENT".equals(user.getRole())) {
//            return ResponseEntity.status(403).body("Admins só podem editar clientes");
//        }
//        Result<UserModel> result = userUseCase.updateUser(id, user);
//        return result.isOk() ? ResponseEntity.ok(result.getData()) : ResponseEntity.badRequest().body(result.getMessage());
//    }
//
//    @DeleteMapping("/clients/{id}")
//    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
//        Result<UserModel> userResult = userUseCase.findById(id);
//        if (userResult.isOk() && "ROLE_CLIENT".equals(userResult.getData().getRole())) {
//            Result<Boolean> result = userUseCase.deleteUser(id);
//            return result.isOk() ? ResponseEntity.ok("Cliente removido com sucesso") : ResponseEntity.badRequest().body(result.getMessage());
//        }
//        return ResponseEntity.status(403).body("Admins só podem excluir clientes");
//    }
}


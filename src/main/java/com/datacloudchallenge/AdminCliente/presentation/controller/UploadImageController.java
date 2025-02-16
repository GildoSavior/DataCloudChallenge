package com.datacloudchallenge.AdminCliente.presentation.controller;


import com.datacloudchallenge.AdminCliente.domain.dtos.HttpResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UploadImageUseCase;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadImageController {

    private final UploadImageUseCase uploadImageUseCase;
    private final UserUseCase userUseCase;

    public UploadImageController(UploadImageUseCase uploadImageUseCase, UserUseCase userUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userUseCase = userUseCase;
    }

    @PostMapping("/{phoneNumber}")
    public ResponseEntity<?> uploadUserImage(@PathVariable String phoneNumber,
                                             @RequestParam("file") MultipartFile file) {
        try {

            String imageUrl = uploadImageUseCase.uploadImage(file);

            Result<UserDto> result = userUseCase.updateUserImage(phoneNumber, imageUrl);
            HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
            return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao fazer upload da imagem");
        }
    }
}

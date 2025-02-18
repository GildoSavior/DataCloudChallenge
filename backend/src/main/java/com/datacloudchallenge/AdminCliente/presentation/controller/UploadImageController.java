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

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
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

        Result<String> uploadImageResult = uploadImageUseCase.uploadImage(file);
        if(!uploadImageResult.isOk()) return ResponseEntity.badRequest().body(uploadImageResult.getMessage());

        Result<UserDto> result = userUseCase.updateUserImage(phoneNumber, uploadImageResult.getData());
        HttpResponse<UserDto> response = new HttpResponse<>(result.getMessage(), result.getData());
        return result.isOk() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);

    }
}

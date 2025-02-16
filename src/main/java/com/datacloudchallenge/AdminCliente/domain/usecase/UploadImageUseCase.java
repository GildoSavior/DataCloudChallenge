package com.datacloudchallenge.AdminCliente.domain.usecase;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImageUseCase {
    String uploadImage(MultipartFile file) throws IOException;
}

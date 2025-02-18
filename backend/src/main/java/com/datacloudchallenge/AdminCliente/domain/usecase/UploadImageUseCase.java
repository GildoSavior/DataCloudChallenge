package com.datacloudchallenge.AdminCliente.domain.usecase;

import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImageUseCase {
    Result<String> uploadImage(MultipartFile file) ;
}

package com.datacloudchallenge.AdminCliente.domain.services;
import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.usecase.UploadImageUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadImageService implements UploadImageUseCase {

    private final Cloudinary cloudinary;

    public UploadImageService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public Result<String> uploadImage(MultipartFile file)  {
        try {
            System.out.println("Tamanho do arquivo: " + file.getSize());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return Result.success( uploadResult.get("url").toString(), "");
        } catch (Exception e) {
            return Result.failure("Falha ao fazer upload da image: " + e.getMessage());
        }
    }
}

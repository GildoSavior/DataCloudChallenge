package com.datacloudchallenge.AdminCliente.domain.dtos;

import lombok.Data;

@Data
public class HttpResponse<T> {
    private String message;
    private T data;

    public HttpResponse(String message,  T data) {
        this.message = message;
        this.data = data;
    }
}
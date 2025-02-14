package com.datacloudchallenge.AdminCliente.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Result<T> {

    private String message;
    private boolean ok;
    private T data;

    public Result(String message, boolean ok, T data) {
        this.message = message;
        this.ok = ok;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOk() {
        return ok;
    }

    public T getData() {
        return data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("Successo", true, data);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(message, false, null);
    }
}

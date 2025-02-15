package com.datacloudchallenge.AdminCliente.domain.utils;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static String validateUserCreate(UserModel user) {
        StringBuilder errors = new StringBuilder();

        if (user.getName() == null || user.getName().isEmpty()) {
            errors.append("O nome deve estar preenchido.\n");
        } else if (user.getName().length() < 8) {
            errors.append("O nome deve ter no mínimo 8 caracteres.\n");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.append("O email deve estar preenchido.\n");
        }

        if(!isValidEmail(user.getEmail())){
            errors.append("O email é invalido.\n");
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            errors.append("O número de telefone deve estar preenchido.\n");
        } else if (user.getPhoneNumber().length() != 9) {
            errors.append("O número de telefone deve ter 9 caracteres.\n");
        }


        return errors.toString().trim();
    }

    public static String validateUserUpdate(UserDto user) {
        StringBuilder errors = new StringBuilder();

        if (user.getName() == null || user.getName().isEmpty()) {
            errors.append("O nome deve estar preenchido.\n");
        } else if (user.getName().length() < 8) {
            errors.append("O nome do utilizador deve ter no mínimo 8 caracteres.\n");
        }

        return errors.toString().trim();
    }



    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
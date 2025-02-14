package com.datacloudchallenge.AdminCliente.domain.utils;

import com.datacloudchallenge.AdminCliente.data.models.User;

public class Validator {
    public static String validateUser(User user) {
        StringBuilder errors = new StringBuilder();

        if (user.getName() == null || user.getName().isEmpty()) {
            errors.append("O nome deve estar preenchido.\n");
        } else if (user.getName().length() < 8) {
            errors.append("O nome do utilizador deve ter no mínimo 8 caracteres.\n");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.append("O email deve estar preenchido.\n");
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            errors.append("O número de telefone deve estar preenchido.\n");
        } else if (user.getPhoneNumber().length() != 9) {
            errors.append("O número de telefone deve ter 9 caracteres.\n");
        }

        return errors.toString().trim();
    }
}
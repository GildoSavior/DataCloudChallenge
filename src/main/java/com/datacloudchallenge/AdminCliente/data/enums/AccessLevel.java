package com.datacloudchallenge.AdminCliente.data.enums;

import lombok.Getter;


public enum AccessLevel {
    SUPER_ADMIN("Super", "Tem acesso total ao sistema"),
    ADMIN("Admin", "Tem acesso limitados aos clientes "),
    CLIENT("Client", "Tem acesso limitidados ao seu perfil");

    private final String displayName;
    private final String description;

    AccessLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
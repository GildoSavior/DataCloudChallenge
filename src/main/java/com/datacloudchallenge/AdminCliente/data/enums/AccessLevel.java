package com.datacloudchallenge.AdminCliente.data.enums;


public enum AccessLevel {
    ROLE_SUPER_ADMIN("Super_Admin", "Tem acesso total ao sistema"),
    ROLE_ADMIN("Admin", "Tem acesso limitados aos clientes "),
    ROLE_CLIENT("Client", "Tem acesso limitidados ao seu perfil");

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
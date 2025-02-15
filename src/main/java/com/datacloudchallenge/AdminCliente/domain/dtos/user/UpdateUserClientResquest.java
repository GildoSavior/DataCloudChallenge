package com.datacloudchallenge.AdminCliente.domain.dtos.user;

public class UpdateUserClientResquest {
    private String name;
    private String imageUrl;

    public UpdateUserClientResquest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

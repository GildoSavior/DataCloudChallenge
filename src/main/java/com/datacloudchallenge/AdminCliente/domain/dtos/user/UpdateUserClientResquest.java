package com.datacloudchallenge.AdminCliente.domain.dtos.user;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;

public class UpdateUserClientResquest {
    private String name;
    private String imageUrl;
    private AccessLevel accessLevel;

    public UpdateUserClientResquest(String name, String imageUrl, AccessLevel accessLevel) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.accessLevel = accessLevel;
    }



    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}

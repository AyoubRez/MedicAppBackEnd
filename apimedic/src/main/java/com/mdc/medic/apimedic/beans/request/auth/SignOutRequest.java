package com.mdc.medic.apimedic.beans.request.auth;

import javax.validation.constraints.NotBlank;

public class SignOutRequest {
    @NotBlank
    private String toExpireToken;
    @NotBlank
    private String refreshToken;


    public String getToExpireToken() {
        return toExpireToken;
    }

    public void setToExpireToken(String toExpireToken) {
        this.toExpireToken = toExpireToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

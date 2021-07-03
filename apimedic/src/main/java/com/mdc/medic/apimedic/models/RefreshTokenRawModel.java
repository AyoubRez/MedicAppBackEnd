package com.mdc.medic.apimedic.models;

public class RefreshTokenRawModel {
    private RefreshToken refreshToken;
    private String decryptedRefreshToken;

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDecryptedRefreshToken() {
        return decryptedRefreshToken;
    }

    public void setDecryptedRefreshToken(String decryptedRefreshToken) {
        this.decryptedRefreshToken = decryptedRefreshToken;
    }
}

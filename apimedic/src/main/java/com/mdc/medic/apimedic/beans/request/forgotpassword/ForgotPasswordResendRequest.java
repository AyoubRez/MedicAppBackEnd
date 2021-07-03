package com.mdc.medic.apimedic.beans.request.forgotpassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ForgotPasswordResendRequest {
    @NotNull(message = "Please enter id")
    private Long otpId;
    @NotBlank
    private String username;

    public Long getOtpId() {
        return otpId;
    }

    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

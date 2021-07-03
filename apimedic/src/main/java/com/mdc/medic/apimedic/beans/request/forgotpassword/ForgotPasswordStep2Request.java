package com.mdc.medic.apimedic.beans.request.forgotpassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ForgotPasswordStep2Request {

    @NotBlank
    private String newPassword;

    @NotBlank
    private String otpValue;

    @NotNull(message = "Please enter otp id")
    private Long otpId;


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public Long getOtpId() {
        return otpId;
    }

    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }
}

package com.mdc.medic.apimedic.beans.request.forgotpassword;

import javax.validation.constraints.NotBlank;

public class ForgotPasswordStep1Request {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

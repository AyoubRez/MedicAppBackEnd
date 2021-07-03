package com.mdc.medic.apimedic.beans.reponse.forgotpassword;

public class ForgotPasswordStep1Response {
    private String phoneNumber;

    private Long activityId;

    private Long otpId;


    public ForgotPasswordStep1Response(String phoneNumber, Long activityId, Long otpId) {
        this.phoneNumber = phoneNumber;
        this.activityId = activityId;
        this.otpId = otpId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getOtpId() {
        return otpId;
    }

    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }
}

package com.mdc.medic.apimedic.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "CHANGE_PASSWORD_OTP_REQUEST")
public class ChangePasswordOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OTP_ID",nullable = false)
    private Long otpId;

    @ManyToOne
    @JoinColumn(name = "CHANGE_PASSWORD_REQUEST_ID")
    private ChangePassword changePassword;

    @Column(name = "OTP_VALUE",nullable = false)
    private String otpValue;

    @Column(name = "IS_USED",nullable = false)
    private Boolean isUsed;

    @Column(name = "ADD_TIME", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    @Column(name = "EXPIRE_TIME", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    public Long getOtpId() {
        return otpId;
    }

    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }

    public ChangePassword getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(ChangePassword changePassword) {
        this.changePassword = changePassword;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}

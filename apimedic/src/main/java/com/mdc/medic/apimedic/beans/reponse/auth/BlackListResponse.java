package com.mdc.medic.apimedic.beans.reponse.auth;

import java.util.Date;

public class BlackListResponse {
    private Long id;
    private String username;
    private String token;
    private Date logOutDate;

    public BlackListResponse() {
    }

    public BlackListResponse(Long id, String username, String token, Date logOutDate) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.logOutDate = logOutDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLogOutDate() {
        return logOutDate;
    }

    public void setLogOutDate(Date logOutDate) {
        this.logOutDate = logOutDate;
    }
}

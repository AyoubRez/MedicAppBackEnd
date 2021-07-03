package com.mdc.medic.apimedic.models;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;


@RedisHash(value = "blackListToken", timeToLive = 1000)
@Data
public class TokenBlackList implements Serializable {


    private Long id;
    private User user;
    private String refreshToken;
    @Indexed
    private String token;
    private Date logOutDate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

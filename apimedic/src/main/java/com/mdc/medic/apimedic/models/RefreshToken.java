package com.mdc.medic.apimedic.models;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "REFRESH_TOKEN")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "TOKEN" ,nullable = false, unique = true)
    private String token;

    @Column(name="EXPIRY_DATE",nullable = false) @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Column(name="IS_USED",nullable = false)
    private Boolean isUsed;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}

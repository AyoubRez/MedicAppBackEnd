package com.mdc.medic.apimedic.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "CHANGE_PASSWORD_REQUEST")
@Table(name="CHANGE_PASSWORD_REQUEST")
public class ChangePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;

    @Column(name = "EXECUTION_TIME",nullable = false)
    private Date executionTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ADD_TIME",nullable = false)
    private Date addTime;

    @Column(name = "EDIT_TIME",nullable = true)
    private Date editTime;

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

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}

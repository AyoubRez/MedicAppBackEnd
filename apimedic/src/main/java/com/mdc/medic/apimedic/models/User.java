package com.mdc.medic.apimedic.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(	name = "`USER`",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "USERNAME"),
                @UniqueConstraint(columnNames = "EMAIL")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Size(max=20)
    @Column(name = "USERNAME")
    private String username;

    @NotBlank
    @Size(max=50)
    @Email
    @Column(name = "EMAIL")
    private String email;

    @NotBlank
    @Size(max=120)
    @Column(name = "PASSWORD")
    private String password;

    @ManyToOne
    @JoinColumn(name = "SECTOR_ID",nullable = true)
    private Sector sector;

    @Size(max=50)
    @Column(name = "NAME")
    private String name;

    @Size(max=20)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADD_TIME")
    private Date addTime;

    @Column(name = "EDIT_TIME")
    private Date editTime;

    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    @Column(name = "LAST_ACTION_TIME")
    private Date lastActionTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
    joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))

    private Set<Role> roles = new HashSet<>();

    @Column(name = "IS_ENABLED")
    private Boolean isEnabled;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "IS_EDITABLE")
    private Boolean isEditable;

    public User() {
    }

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, @Size(max = 50) String name, @Size(max = 20) String phoneNumber, Sector sector, Date addTime, Date editTime, Boolean isEnabled, Boolean isDeleted, Boolean isEditable) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addTime=addTime;
        this.editTime=editTime;
        this.isEnabled=isEnabled;
        this.isDeleted=isDeleted;
        this.isEditable=isEditable;
        this.sector=sector;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Date lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }
}

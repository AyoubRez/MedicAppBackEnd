package com.mdc.medic.apimedic.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="SECTOR")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;

    @Column(name = "SECTOR_NAME")
    private String sectorName;

    @Column(name = "SECTOR_CODE")
    private String sectorCode;

    @Column(name = "IS_ENABLED")
    private Boolean isEnabled;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

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

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
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

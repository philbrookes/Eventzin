/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(name="mobilekeys",catalog="eventlas_events")
public class MobileKeys implements Serializable{

    private Integer id;
    private String key;
    private String uid;
    private String deviceToken;
    private String devicetype;

    

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name="\"key\"",unique=true,length=32)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Column(name="uid", unique=true, length=40)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @Column(nullable=true,length=128)
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
    @Column(nullable=false,name="devicetype")
    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    





}

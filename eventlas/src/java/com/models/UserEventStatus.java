/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(catalog="eventlas_events", name="usereventstatus")
public class UserEventStatus implements Serializable{

    Integer id = new Integer(-1);
    Events event;
    Users user;
    String status;
    String hash;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventid")
    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid")
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }
    @Column(name="statushash")
    public String getStatusHash() {
        return hash;
    }

    public void setStatusHash(String hash) {
        this.hash = hash;
    }

    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEventStatus other = (UserEventStatus) obj;
        if (this.event != other.event && (this.event == null || !this.event.equals(other.event))) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.event != null ? this.event.hashCode() : 0);
        hash = 79 * hash + (this.user != null ? this.user.hashCode() : 0);
        return hash;
    }



}

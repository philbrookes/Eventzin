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
@Table(name="tempinvitekey", schema="eventlas_events")
public class TempInvite implements Serializable{

    private Integer id;
    private String invitekey;
    private String eventkeyhash;
    private Events eventid;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventid", nullable=false)
    public Events getEventid() {
        return eventid;
    }

    public void setEventid(Events eventid) {
        this.eventid = eventid;
    }
    @Column(name="eventkeyhash",length=32,nullable=false)
    public String getEventkeyhash() {
        return eventkeyhash;
    }

    public void setEventkeyhash(String eventkeyhash) {
        this.eventkeyhash = eventkeyhash;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name="invitekey",length=32, nullable=false)
    public String getInvitekey() {
        return invitekey;
    }

    public void setInvitekey(String invitekey) {
        this.invitekey = invitekey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TempInvite other = (TempInvite) obj;
        if ((this.eventkeyhash == null) ? (other.eventkeyhash != null) : !this.eventkeyhash.equals(other.eventkeyhash)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.eventkeyhash != null ? this.eventkeyhash.hashCode() : 0);
        return hash;
    }

    


    

}

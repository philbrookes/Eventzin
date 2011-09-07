package com.models;
// Generated Oct 24, 2010 10:46:46 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Invites generated by hbm2java
 */
@Entity
@Table(name="invites"
    ,catalog="eventlas_events"
)
public class Invites  implements java.io.Serializable {


     private Integer id = new Integer(-1);
     private Users users;
     private Users inviterid;
     private String invitehash;
     private String status;
     private Events events;
     private Date dateadded;
     private Integer invitersid;

    public static final String ACCEPT_INVITE = "attending";
    public static final String IGNORE_INVITE = "ignored";
    public static final String DECLINE_INVITE = "declined";
    public static final String PENDING_INVITE = "pending";

    public Invites() {
    }

    public Invites(int id, Users users, int eventid, Users inviterid) {
       this.id = id;
       this.users = users;
      
       this.inviterid = inviterid;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid", nullable=false)
    public Users getUsers() {
        return this.users;
    }
    
    public void setUsers(Users users) {
        this.users = users;
    }

    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inviterid",nullable=false)
    public Users getInviterid() {
        return this.inviterid;
    }
    
    public void setInviterid(Users inviterid) {
        this.inviterid = inviterid;
    }

    @Column(name="inviterid", nullable=true, insertable=false, updatable=false)
    public Integer getInvitersid() {
        return invitersid;
    }

    
    public void setInvitersid(Integer invitersid) {
        this.invitersid = invitersid;
    }
    
    
    
    @Column(name="invitehash", nullable=false)
    public String getInvitehash(){
        return this.invitehash;
    }

    public void setInvitehash(String invitehash){
        this.invitehash = invitehash;
    }
    @Column(name="status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventid")
    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }
    @Temporal(TemporalType.DATE)
    @Column(insertable=false,name="dateadded",updatable=false)
    public Date getDateadded() {
        return dateadded;
    }

    public void setDateadded(Date dateadded) {
        this.dateadded = dateadded;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Invites other = (Invites) obj;
        if ((this.invitehash == null) ? (other.invitehash != null) : !this.invitehash.equals(other.invitehash)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.invitehash != null ? this.invitehash.hashCode() : 0);
        return hash;
    }

    







}



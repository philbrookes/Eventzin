/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(catalog="eventlas_events",name="eventcomments")
public class Eventcomments implements Serializable, Comment {
    private static final long serialVersionUID = 1L;


    private Integer id;
    private Users users;
    private Events events;
    private String comment;
    private String commenthash;
    private int rating;
    private Date dateAdded;

    @Column(name="commenthash")
    public String getCommenthash() {
        return commenthash;
    }

    public void setCommenthash(String commenthash) {
        this.commenthash = commenthash;
    }

      @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="comment", nullable=false, length=32)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventid")
    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid")
    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }
    @Column(name="rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Eventcomments)) {
            return false;
        }
        Eventcomments other = (Eventcomments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.models.Eventcomments[id=" + id + "]";
    }

}

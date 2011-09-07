/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.Date;
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
@Table(catalog="eventlas_events", name="venuecomments")
public class Venuecomments implements Serializable, Comment {
    private static final long serialVersionUID = 1L;

    private Venues venue;
    private Users user;
    private String commenthash;
    private String comment;
    private int rating;
    private Date dateAdded;

    @Column(name="comment", length=256)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
     @Column(name="commenthash",length=32)
    public String getCommenthash() {
        return commenthash;
    }
   
    public void setCommenthash(String commenthash) {
        this.commenthash = commenthash;
    }
    @Column(name="rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid")
    public Users getUser() {
        return user;
    }

    public void setUser(Users userid) {
        this.user = userid;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="venueid")
    public Venues getVenueid() {
        return venue;
    }

    public void setVenueid(Venues venueid) {
        this.venue = venueid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Override
    public Date getDateAdded(){
        return this.dateAdded;
    }

    public void setDateAdded(Date date){
        this.dateAdded = date;
    }


    
    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venuecomments)) {
            return false;
        }
        Venuecomments other = (Venuecomments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.models.Venuecomments[id=" + id + "]";
    }

}

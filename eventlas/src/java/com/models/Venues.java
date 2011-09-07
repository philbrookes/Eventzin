package com.models;
// Generated Oct 24, 2010 10:46:46 PM by Hibernate Tools 3.2.1.GA


import api.helpers.ParameterHelper;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Venues generated by hbm2java
 */
@Entity
@Table(name="venues"
    ,catalog="eventlas_events"
)
public class Venues  implements java.io.Serializable {


     private Integer id;
     private Icons icons;
     private Location location;
     private String name;
     private String address;
     private String summary;
     private Set<Events> eventses = new HashSet<Events>(0);
     private Set<Venuecomments>venuecomments = new HashSet<Venuecomments>(0);
     private Integer rating;
     private String googleid;
     

    public Venues() {
    }

	
    public Venues(Icons icons, Location location, String name, String address, String summary) {
        this.icons = icons;
        this.location = location;
        this.name = name;
        this.address = address;
        this.summary = summary;
    }
    public Venues(Icons icons, Location location, String name, String address, String summary, Set<Events> eventses) {
       this.icons = icons;
       this.location = location;
       this.name = name;
       this.address = address;
       this.summary = summary;
       this.eventses = eventses;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="iconid", nullable=false)
    public Icons getIcons() {
        return this.icons;
    }
    
    public void setIcons(Icons icons) {
        this.icons = icons;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="locid", nullable=false)
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Column(name="name", nullable=false, length=256)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="address", nullable=false, length=65535)
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Column(name="summary", nullable=false, length=140)
    public String getSummary() {
        return this.summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
   
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="venues")
    public Set<Events> getEventses() {
        return this.eventses;
    }
    
    public void setEventses(Set<Events> eventses) {
        this.eventses = eventses;
    }
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="venue")
    public Set<Venuecomments> getVenuecomments() {
        return venuecomments;
    }

    public void setVenuecomments(Set<Venuecomments> venuecomments) {
        this.venuecomments = venuecomments;
    }
    @Column(name="rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    @Column(length=512)
    public String getGoogleid() {
        return googleid;
    }

    public void setGoogleid(String googleid) {
        this.googleid = googleid;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Venues other = (Venues) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }


    
    




}



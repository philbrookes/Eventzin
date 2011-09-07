/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


/**
 *
 * @author craigbrookes
 */
@Entity
public class EventFullDetail  implements Serializable{

    private Integer id;
    private String title;
    private String summary;
    private Long eventdate;
    private Float longitude;
    private Float latitude;
    private String venuename;
    private String venueaddress;
    private Integer locid;
    private Long eventend;
    private Users user;
    private EventCategorys category;
    private Visibility visibility;
    private Set<UserEventStatus> statuses;
    private Set<EventQuestions> questions;
    private Integer venueid;
    private Set<Invites>invites;
    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    
    public Long getEventdate() {
        return eventdate;
    }

    public void setEventdate(Long eventdate) {
        this.eventdate = eventdate;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="visibilityid", nullable=false)
    public Visibility getVisibility() {
        return this.visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="categoryid", nullable=false)
    public EventCategorys getCategory(){
        return category;
    }

    public void setCategory(EventCategorys category){
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
    
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getVenueaddress() {
        return venueaddress;
    }

    public void setVenueaddress(String venueaddress) {
        this.venueaddress = venueaddress;
    }

    public String getVenuename() {
        return venuename;
    }

    public void setVenuename(String venuename) {
        this.venuename = venuename;
    }

    public Integer getLocid() {
        return locid;
    }

    public void setLocid(Integer locid) {
        this.locid = locid;
    }
   
    @Column(name="enddate")
    public Long getEventend() {
        return eventend;
    }

    public void setEventend(Long eventend) {
        this.eventend = eventend;
    }
    @ManyToOne
    @JoinColumn(name="userid")
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    @OneToMany
    @JoinColumn(name="eventid")
    public Set<UserEventStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<UserEventStatus> statuses) {
        this.statuses = statuses;
    }

    public Integer getVenueid() {
        return venueid;
    }

    public void setVenueid(Integer venueid) {
        this.venueid = venueid;
    }
    @OneToMany
    @JoinColumn(name="eventid")
    public Set<EventQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<EventQuestions> questions) {
        this.questions = questions;
    }
    @OneToMany
    @JoinColumn(name="eventid")
    public Set<Invites> getInvites() {
        return invites;
    }

    public void setInvites(Set<Invites> invites) {
        this.invites = invites;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventFullDetail other = (EventFullDetail) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

  

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    

    
    


    
    


    


    

    

}

package com.models;
// Generated Oct 24, 2010 10:46:46 PM by Hibernate Tools 3.2.1.GA



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
 * Events generated by hbm2java
 */
@Entity
@Table(name="events"
    ,catalog="eventlas_events"
)
public class Events  implements java.io.Serializable {


     private Integer id;
     private Icons icons;
     private Visibility visibility;
     private Venues venues;
     private Users users;
     private Location location;
     private String title;
     private String summary;
     private Long eventdate;
     private Long eventend;
     private EventCategorys category;
     private Set<Eventcomments>comments = new HashSet<Eventcomments>(0);
     private Set<EventQuestions>questions = new HashSet<EventQuestions>(0);
     private Set<Invites>invites = new HashSet<Invites>(0);
     private Set<UserEventStatus> statuses = new HashSet<UserEventStatus>(0);
     public Boolean is_current = false;

     public static final int DEFAULT_NO_VENUE = 4;
    
    public Events() {
    }

    public Events(Icons icons, Visibility visibility, Venues venues, Users users, Location location, String title, String summary, Long eventdate) {
       this.icons = icons;
       this.visibility = visibility;
       this.venues = venues;
       this.users = users;
       this.location = location;
       this.title = title;
       this.summary = summary;
       this.eventdate = eventdate;
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
    @JoinColumn(name="visibilityid", nullable=false)
    public Visibility getVisibility() {
        return this.visibility;
    }
    
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="venueid", nullable=false)
    public Venues getVenues() {
        return this.venues;
    }
    
    public void setVenues(Venues venues) {
        this.venues = venues;
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
    @JoinColumn(name="locid", nullable=false)
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Column(name="title", nullable=false, length=128)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Column(name="summary", nullable=false, length=140)
    public String getSummary() {
        return this.summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
   
    @Column(name="eventdate", nullable=false, length=19)
    public Long getEventdate() {
        return this.eventdate;
    }
    
    public void setEventdate(Long eventdate) {
        this.eventdate = eventdate;
    }
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, mappedBy="events")
    public Set<Eventcomments> getComments() {
        return comments;
    }

    public void setComments(Set<Eventcomments> comments) {
        this.comments = comments;
    }
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="event")
    public Set<UserEventStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<UserEventStatus> statuses) {
        this.statuses = statuses;
    }


    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="events")
    public Set<Invites> getInvites() {
        return invites;
    }

    public void setInvites(Set<Invites> invites) {
        this.invites = invites;
    }
    
    @Column(name="enddate")
    public Long getEventend() {
        return eventend;
    }

    public void setEventend(Long eventend) {
      
        
        this.eventend = eventend;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="categoryid",nullable=false)
    public EventCategorys getCategory() {
        return category;
    }

    public void setCategory(EventCategorys category) {
        this.category = category;
    }
    
    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="event")
    public Set<EventQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<EventQuestions> questions) {
        this.questions = questions;
    }
    
    













}


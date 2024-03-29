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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Users generated by hbm2java
 */
@Entity
@Table(name="users"
    ,catalog="eventlas_events"
    , uniqueConstraints = @UniqueConstraint(columnNames="username") 
)
public class Users  implements java.io.Serializable {


     private Integer id;
     private String username;
     private String password;
     private Set<Events> eventses = new HashSet<Events>(0);
     private Set<Invites> inviteses = new HashSet<Invites>(0);
     private Set<Usersgroups> usersgroupsesForUserid = new HashSet<Usersgroups>(0);
     private Set<Groups> groupses = new HashSet<Groups>(0);
     private Set<Usersgroups> usersgroupsesForGroupmemberid = new HashSet<Usersgroups>(0);
     private Set<ApiKeys> keyses = new HashSet<ApiKeys>(0);
     private String email;
     private String mobileKey;
     private String phone;
     private String ageRange;
     private String sex;
     private String city;
     private String countryCode;
     private Profile profile;

     private Set<UserFriends> usersfriends = new HashSet<UserFriends>(0);



     @Column(name="agerange",nullable=true)
    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }
    @Column(name="phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name="sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Users() {
    }

	
    public Users(String username) {
        this.username = username;
    }
    public Users(String username, String password, Set<Events> eventses, Set<Invites> inviteses, Set<Usersgroups> usersgroupsesForUserid, Set<Groups> groupses, Set<Usersgroups> usersgroupsesForGroupmemberid, Set<ApiKeys> keyses) {
       this.username = username;
       this.password = password;
       this.eventses = eventses;
       this.inviteses = inviteses;
       this.usersgroupsesForUserid = usersgroupsesForUserid;
       this.groupses = groupses;
       this.usersgroupsesForGroupmemberid = usersgroupsesForGroupmemberid;
       this.keyses = keyses;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="username", unique=true, nullable=false, length=45)
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Column(name="password", length=45)
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="users")
    public Set<Events> getEventses() {
        return this.eventses;
    }
    
    public void setEventses(Set<Events> eventses) {
        this.eventses = eventses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="users")
    public Set<Invites> getInviteses() {
        return this.inviteses;
    }
    
    public void setInviteses(Set<Invites> inviteses) {
        this.inviteses = inviteses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usersByUserid")
    public Set<Usersgroups> getUsersgroupsesForUserid() {
        return this.usersgroupsesForUserid;
    }
    
    public void setUsersgroupsesForUserid(Set<Usersgroups> usersgroupsesForUserid) {
        this.usersgroupsesForUserid = usersgroupsesForUserid;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="users")
    public Set<Groups> getGroupses() {
        return this.groupses;
    }
    
    public void setGroupses(Set<Groups> groupses) {
        this.groupses = groupses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usersByGroupmemberid")
    public Set<Usersgroups> getUsersgroupsesForGroupmemberid() {
        return this.usersgroupsesForGroupmemberid;
    }
    
    public void setUsersgroupsesForGroupmemberid(Set<Usersgroups> usersgroupsesForGroupmemberid) {
        this.usersgroupsesForGroupmemberid = usersgroupsesForGroupmemberid;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="users")
    public Set<ApiKeys> getKeyses() {
        return this.keyses;
    }
    
    public void setKeyses(Set<ApiKeys> keyses) {
        this.keyses = keyses;
    }
    @Column(length=256,name="email",nullable=true,unique=true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name="mobilekey", length=32)
    public String getMobileKey() {
        return mobileKey;
    }

    public void setMobileKey(String mobileKey) {
        this.mobileKey = mobileKey;
    }
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="user",targetEntity=UserFriends.class)
    public Set<UserFriends> getUsersfriends() {
        return usersfriends;
    }

    public void setUsersfriends(Set<UserFriends> usersfriends) {
        this.usersfriends = usersfriends;
    }

    @Column(name="city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Column(name="countrycode")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    @OneToOne(fetch= FetchType.LAZY)
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        
        return true;
    }

   

    
    
   

    







}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(name="userfriends", schema="eventlas_events")
public class UserFriends implements Serializable {

    private Integer id;
    private Users friend;
    private Users user;
    private Groups group;
    private String uqhash;
    private Integer friendid;
    private Set<Usersgroups>groups;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="friendid")
    public Users getFriend() {
        return friend;
    }

    public void setFriend(Users friend) {
        this.friend = friend;
    }
    @Column(updatable=false,insertable=false,name="friendid")
    public Integer getFriendid() {
        return friendid;
    }

    public void setFriendid(Integer friendid) {
        this.friendid = friendid;
    }
    
    

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="groupid")
    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid", nullable=false)
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    @Column(name="uqhash",length=32,unique=true)
    public String getUqhash() {
        return uqhash;
        
    }

    public void setUqhash(String hash) {
        this.uqhash = hash;
    }
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="usersByGroupmemberid")
    public Set<Usersgroups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Usersgroups> groups) {
        this.groups = groups;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserFriends other = (UserFriends) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.friend != null ? this.friend.hashCode() : 0);
        hash = 79 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 79 * hash + (this.group != null ? this.group.hashCode() : 0);
        return hash;
    }



    

}

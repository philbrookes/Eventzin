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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Groups generated by hbm2java
 */
@Entity
@Table(name="groups")
public class Groups  implements java.io.Serializable {


     private Integer id;
     private String group;
     private Set<Usersgroups> usersgroupses = new HashSet<Usersgroups>(0);

    public Groups() {
    }

	
    public Groups(String group) {
        
        this.group = group;
    }
    public Groups( String group, Set<Usersgroups> usersgroupses) {
       
       this.group = group;
       this.usersgroupses = usersgroupses;
    }
   
    @Id @GeneratedValue(strategy=IDENTITY) 
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="groupname", nullable=false, length=128)
    public String getGroup() {
        return this.group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="groups")
    public Set<Usersgroups> getUsersgroupses() {
        return this.usersgroupses;
    }
    
    public void setUsersgroupses(Set<Usersgroups> usersgroupses) {
        this.usersgroupses = usersgroupses;
    }




}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(name="methods", catalog="eventlas_events")
@NamedNativeQuery(name="getMethodByName",query="SELECT * FROM methods m WHERE m.name=:name", resultClass=Methods.class)
public class Methods implements Serializable {

    protected Integer id;
    protected String name;
    protected String notes;
    protected String example;
    protected Integer auth;
    protected String response;

    @Column(name="example")
    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name="name",unique=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="notes")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    @Column(name="auth")
    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }
    @Column(name="response")
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    
    






}

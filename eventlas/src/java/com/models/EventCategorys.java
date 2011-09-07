/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table(name="eventcategorys",schema="eventlas_events")
public class EventCategorys implements Serializable{


    private Integer id;
    private String category;
    private Icons iconid;
    @Column(name="category",length=128,nullable=false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="iconid",nullable=false)
    public Icons getIconid() {
        return iconid;
    }

    public void setIconid(Icons iconid) {
        this.iconid = iconid;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventCategorys other = (EventCategorys) obj;
        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        if (this.iconid != other.iconid && (this.iconid == null || !this.iconid.equals(other.iconid))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 79 * hash + (this.iconid != null ? this.iconid.hashCode() : 0);
        return hash;
    }


    




}

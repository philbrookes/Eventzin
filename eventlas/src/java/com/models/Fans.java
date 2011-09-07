/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
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
@Table(name="fans",catalog="eventlas_events")
public class Fans implements Serializable {

    private Integer id;
    private Users fanid;
    private Users userid;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fanid")
    public Users getFanid() {
        return fanid;
    }

    public void setFanid(Users fanid) {
        this.fanid = fanid;
    }
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid")
    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }




}

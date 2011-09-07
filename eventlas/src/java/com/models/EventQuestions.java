/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.Table;

/**
 *
 * @author craigbrookes
 */
@Entity
@Table( name="eventquestions",schema="eventlas_events")
public class EventQuestions implements Serializable{

    Integer id;
    String question;
    Users user;
    Events event;
    Integer privacy;
    
    Set<EventQuestionAnswers>answers = new HashSet<EventQuestionAnswers>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventid")
    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="askerid")
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    @OneToMany(fetch=FetchType.LAZY,mappedBy="question")
    public Set<EventQuestionAnswers> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<EventQuestionAnswers> answers) {
        this.answers = answers;
    }
    @Column(name="privacy" , nullable=true)
    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    
    


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventQuestions other = (EventQuestions) obj;
        if ((this.question == null) ? (other.question != null) : !this.question.equals(other.question)) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.event != other.event && (this.event == null || !this.event.equals(other.event))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.question != null ? this.question.hashCode() : 0);
        hash = 59 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 59 * hash + (this.event != null ? this.event.hashCode() : 0);
        return hash;
    }

    

}

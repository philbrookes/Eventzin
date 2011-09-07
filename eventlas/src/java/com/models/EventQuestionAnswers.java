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
@Table(name="eventquestionanswers",schema="eventlas_events")
public class EventQuestionAnswers implements Serializable{

    private Integer id;
    private String answer;
    private EventQuestions question;

    @Column(name="answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="questionid")
    public EventQuestions getQuestion() {
        return question;
    }

    public void setQuestion(EventQuestions question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventQuestionAnswers other = (EventQuestionAnswers) obj;
        if ((this.answer == null) ? (other.answer != null) : !this.answer.equals(other.answer)) {
            return false;
        }
        if (this.question != other.question && (this.question == null || !this.question.equals(other.question))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.answer != null ? this.answer.hashCode() : 0);
        hash = 53 * hash + (this.question != null ? this.question.hashCode() : 0);
        return hash;
    }



}
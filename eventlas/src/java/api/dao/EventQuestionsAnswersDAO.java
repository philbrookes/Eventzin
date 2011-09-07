/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventQuestionAnswers;
import com.models.EventQuestions;


/**
 *
 * @author craigbrookes
 */
public interface EventQuestionsAnswersDAO extends GenericDAO<EventQuestionAnswers, Integer>{
    
    public EventQuestionAnswers getAnswerByQuestion(EventQuestions q);

}

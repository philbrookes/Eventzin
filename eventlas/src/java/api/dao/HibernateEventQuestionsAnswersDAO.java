/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventQuestionAnswers;
import com.models.EventQuestions;
import java.util.List;
import org.hibernate.Query;


/**
 *
 * @author craigbrookes
 */
public class HibernateEventQuestionsAnswersDAO extends HibernateDAO<EventQuestionAnswers, Integer> implements EventQuestionsAnswersDAO{

    public HibernateEventQuestionsAnswersDAO() {
        super(EventQuestionAnswers.class);
    }
    
    public EventQuestionAnswers getAnswerByQuestion(EventQuestions q){
        Query query = HibernateUtil.getSession().createQuery("from EventQuestionAnswers eq WHERE eq.question=:ques");
        query.setEntity("ques", q);
        return (EventQuestionAnswers) query.uniqueResult();
    }


}

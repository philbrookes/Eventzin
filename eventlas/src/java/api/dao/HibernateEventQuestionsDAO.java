/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventQuestions;
import com.models.Events;
import java.util.List;
import org.hibernate.Query;


/**
 *
 * @author craigbrookes
 */
public class HibernateEventQuestionsDAO extends HibernateDAO<EventQuestions, Integer> implements EventQuestionsDAO{

    public HibernateEventQuestionsDAO(){
        super(EventQuestions.class);
    }

    
    @Override
    public List<EventQuestions>findByEventId(Events event){
        Query q = HibernateUtil.getSession().createQuery("from EventQuestions eq WHERE eq.event=:event");
        q.setEntity("event", event);
        return q.list();
    }
   




}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventQuestions;
import com.models.Events;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface EventQuestionsDAO extends GenericDAO<EventQuestions, Integer>{

    public List<EventQuestions>findByEventId(Events eventid);
}

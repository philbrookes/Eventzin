/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventFullDetail;
import com.models.Eventcomments;
import com.models.Events;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface EventscommentDAO extends GenericDAO<Eventcomments, Integer> {

   public List<Eventcomments> getEventCommentsByEventId(int id);
   public Integer countCommentsForEvent(EventFullDetail ev);

}

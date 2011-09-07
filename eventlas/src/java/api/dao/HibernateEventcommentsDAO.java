/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventFullDetail;
import com.models.Eventcomments;
import com.models.Events;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;


/**
 *
 * @author craigbrookes
 */
public class HibernateEventcommentsDAO extends HibernateDAO<Eventcomments, Integer> implements EventscommentDAO {

    public HibernateEventcommentsDAO() {
        super(Eventcomments.class);
    }

    @Override
    public List<Eventcomments> getEventCommentsByEventId(int id) {
        Query q = HibernateUtil.getSession().createQuery("from Eventcomments ec WHERE ec.events.id = :eventid ORDER BY ec.dateAdded DESC ");
        q.setInteger("eventid", id);
        return q.list();

    }

    @Override
    public Integer countCommentsForEvent(EventFullDetail ev) {
        Session session = HibernateUtil.getSession();
        Query q = session.createSQLQuery("SELECT count(*) FROM eventcomments ec WHERE ec.eventid = :evid");
        q.setInteger("evid",ev.getId() );
        Number c = (Number) q.uniqueResult();
        Integer ci = c.intValue();
        return ci;
    }

    


}

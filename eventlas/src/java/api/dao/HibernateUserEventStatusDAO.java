/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Events;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateUserEventStatusDAO extends HibernateDAO<UserEventStatus, Integer> implements UserEventStatusDAO{

    public HibernateUserEventStatusDAO() {
        super(UserEventStatus.class);
    }

    @Override
    public UserEventStatus getUserEventStatusByEventAndUser(Events event, Users user){
        Criteria crit = HibernateUtil.getSession().createCriteria(UserEventStatus.class);
        HashMap<String,Object>propsvals = new HashMap<String, Object>(2);
        propsvals.put("event", event);
        propsvals.put("user", user);
        crit.add(Restrictions.allEq(propsvals));
        return (UserEventStatus) crit.uniqueResult();
    }

    @Override
    public List<UserEventStatus> getStatusesByUser(Users u){
        Query q = HibernateUtil.getSession().createQuery("from UserEventStatus ue WHERE ue.user=:user");
        q.setEntity("user", u);
        return q.list();
    }



}

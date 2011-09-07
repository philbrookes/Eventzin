/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.TwitterEventStatus;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateTwitterEventStatusDAO extends HibernateDAO<TwitterEventStatus, Integer> implements TwitterEventStatusDAO {

    public HibernateTwitterEventStatusDAO() {
        super(TwitterEventStatus.class);
    }
    
    @Override
    public TwitterEventStatus findByUsernameAndEventid(String username, Integer evid){
        Query q = HibernateUtil.getSession().createSQLQuery("SELECT * FROM `twittereventstatus` WHERE `twittername`=:us AND eventid=:evid");
        q.setString("us", username);
        q.setInteger("evid", evid);
        return (TwitterEventStatus)q.uniqueResult();
    }
    
    @Override
    public List<TwitterEventStatus> findByEventId(Integer evid){
        Query q = HibernateUtil.getSession().createSQLQuery("SELECT * FROM `twittereventstatus` WHERE `eventid`=:evid").addEntity(TwitterEventStatus.class);
        q.setInteger("evid", evid);
        List stats =  q.list();
        
        return stats;
    }
    
}

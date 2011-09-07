/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.Twitter;
import java.io.Serializable;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateTwitterDAO extends HibernateDAO<Twitter, Integer> implements TwitterDAO {

    public HibernateTwitterDAO() {
        super(Twitter.class);
    }

    @Override
    public Twitter fetchByTokenAndUserName(String twitterhandle) {
       Query q = HibernateUtil.getSession().createQuery("from Twitter t WHERE t.twitterHandle=:handle ");
       q.setString("handle", twitterhandle);
       
       return (Twitter) q.uniqueResult();
    }
    
    
 
    
}

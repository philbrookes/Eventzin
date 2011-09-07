/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.AppInvites;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateAppInvitesDAO extends HibernateDAO<AppInvites, Integer> implements AppInvitesDAO {

    public HibernateAppInvitesDAO() {
        super(AppInvites.class);
    }

    @Override
    public AppInvites findByCode(String code) {
        Query q = HibernateUtil.getSession().createQuery("from AppInvites appinv WHERE appinv.md5hash=:code");
        q.setString("code", code);
        return (AppInvites)q.uniqueResult();
        
    }
    
    
    
}

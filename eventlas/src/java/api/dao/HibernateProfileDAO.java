/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Profile;
import com.models.Users;
import java.io.Serializable;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateProfileDAO extends HibernateDAO<Profile, Integer> implements ProfileDAO {

    public HibernateProfileDAO() {
        super(Profile.class);
    }

    @Override
    public Profile fingByUser(Users user) {
        Query q = HibernateUtil.getSession().createQuery("from Profile p WHERE p.user =:user");
        q.setEntity("user", user);
        return (Profile) q.uniqueResult();
    }





}

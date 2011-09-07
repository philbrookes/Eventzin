/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateUsersDAO extends HibernateDAO<Users, Integer> implements UsersDAO {

    public HibernateUsersDAO() {
        super(Users.class);
    }

    public Users findUserByUserName(String username) {
        Query q = HibernateUtil.getSession().createQuery("from Users us WHERE us.username=:user");
        q.setString("user", username);
        Users u = (Users)q.uniqueResult();
        return u;
    }

    @Override
    public List<Users> findUsersByEmailAddresses(ArrayList<String> emails) {
       Criteria c = HibernateUtil.getSession().createCriteria(Users.class);
       HashMap<String,String>toFind = new HashMap<String, String>();

       c.add(Restrictions.in("email", emails));

       return c.list();
    }

    @Override
    public List<Users> findUsersByPhoneNumbers(ArrayList<String> phoneNumbers) {
         Criteria c = HibernateUtil.getSession().createCriteria(Users.class);
       HashMap<String,String>toFind = new HashMap<String, String>();

       c.add(Restrictions.in("phone", phoneNumbers));

       return c.list();
    }

    @Override
    public List<Users> searchForUsersLike(String searchtext) {

        Query q = HibernateUtil.getSession().createQuery("from Users u WHERE u.username LIKE :uname OR u.phone LIKE :phone OR u.email LIKE :email ");
        q.setString("uname", searchtext+"%");
        q.setString("phone", searchtext+"%");
        q.setString("email", searchtext+"%");
        return q.list();
    }

    @Override
    public Users getUserByEmail(String email) {
        Query q = HibernateUtil.getSession().createQuery("from Users u WHERE u.email=:email");
        q.setString("email", email);
        return (Users) q.uniqueResult();
    }

    
    




    



}

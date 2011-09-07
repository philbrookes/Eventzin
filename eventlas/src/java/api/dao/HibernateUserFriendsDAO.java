/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.UserFriends;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateUserFriendsDAO extends HibernateDAO<UserFriends, Integer> implements UserFriendsDAO {

    public HibernateUserFriendsDAO() {
        super(UserFriends.class);
    }

    public List<UserFriends> findFriendsByUsersId(Integer id){
        Criteria crit = HibernateUtil.getSession().createCriteria(UserFriends.class);
        crit.add(Restrictions.eq("user.id", id));
        return crit.list();
    }

    @Override
    public UserFriends findByFriendId(int id) {
       Query q = HibernateUtil.getSession().createQuery("from UserFriends uf WHERE uf.friend.id=:id");
       q.setInteger("id", id);
       return (UserFriends)q.uniqueResult();
    }

    @Override
    public UserFriends findByFriendIdAndUserId(int friendid, int userid){
        Query q = HibernateUtil.getSession().createQuery("from UserFriends uf WHERE uf.friend.id=:fid AND uf.user.id=:uid");
        q.setInteger("fid", friendid);
        q.setInteger("uid", userid);
        return (UserFriends) q.uniqueResult(); 
    }

     
}

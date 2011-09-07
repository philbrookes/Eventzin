/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Groups;
import com.models.Users;
import com.models.Usersgroups;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateUsersGroupsDAO extends HibernateDAO<Usersgroups, Integer> implements UsersGroupsDAO{

    public HibernateUsersGroupsDAO() {
        super(Usersgroups.class);
    }

    @Override
    public List<Usersgroups> findByUser(Users u) {
        Query q = HibernateUtil.getSession().createQuery("from Usersgroups ug WHERE ug.usersByUserid=:user");
        q.setEntity("user", u);
        return q.list();
    }

    @Override
    public List<Usersgroups> findByFriend(Users u) {
         Query q = HibernateUtil.getSession().createQuery("from Usersgroups ug WHERE ug.usersByGroupmemberid=:user");
        q.setEntity("user", u);
        return q.list();
    }

    @Override
    public List<Usersgroups> findByGroupAndUser(Groups group, Users us) {
       Query q = HibernateUtil.getSession().createQuery("from Usersgroups ug WHERE ug.groups = :group AND ug.usersByUserid=:user");
       q.setEntity("group", group);
       q.setEntity("user", us);
       return q.list();
    }
    
    public Usersgroups findGroupMemberByGroupIdAndMemberId(Integer gid, Integer mid){
        Query q = HibernateUtil.getSession().createQuery("from Usersgroups ug WHERE ug.groups.id = :gid AND ug.usersByGroupmemberid.id=:mid");
        q.setInteger("gid", gid).setInteger("mid", mid);
        return (Usersgroups) q.uniqueResult();
    }






}

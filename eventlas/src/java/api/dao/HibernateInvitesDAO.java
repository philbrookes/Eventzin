/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Events;
import com.models.Invites;
import com.models.Users;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateInvitesDAO extends HibernateDAO<Invites, Integer> implements InvitesDAO{

    public HibernateInvitesDAO() {
        super(Invites.class);
    }

    @Override
    public List<Invites> getInvitesByUser(Users user) {
       Criteria crit = HibernateUtil.getSession().createCriteria(Invites.class);
       crit.add(Restrictions.eq("users", user));
       return crit.list();
    }

    @Override
    public List<Invites>getCurrentInvitesSentByUser(Users user){
      Query q = HibernateUtil.getSession().createQuery("from Invites inv WHERE inv.inviterid=:iid ");
      q.setInteger("iid", user.getId());
      List<Invites>invs = q.list();
      return invs;
    }
    
    @Override
    public void updateInviteStatus(int inviteid, String status){
        Query q = HibernateUtil.getSession().createSQLQuery("UPDATE invites SET status=:status WHERE id=:id");
        q.setString("status", status);
        q.setInteger("id", inviteid);
        q.executeUpdate();
    }

    @Override
    public List<Invites>getInvitesByEventId(Integer eventid)
    {
        Query q = HibernateUtil.getSession().createQuery("from Invites inv WHERE inv.events.id=:eventid");
        q.setInteger("eventid", eventid);
        return q.list();
    }

    @Override
    public Invites getInviteByUserAndEvent(Users u, Integer eventid) {
      Query q = HibernateUtil.getSession().createQuery("from Invites inv WHERE inv.events.id=:event AND inv.users=:user");
      q.setInteger("event", eventid);
      q.setEntity("user", u);
      q.setMaxResults(1);
      return (Invites) q.uniqueResult();
    }

    @Override
    public List<Integer> getInvitedUserIdsByEventId(Integer id) {
        Query q = HibernateUtil.getSession().createSQLQuery("SELECT userid FROM invites WHERE eventid=:evid");
        q.setInteger("evid", id);
        return q.list();
    }


    



}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Events;
import com.models.Invites;
import com.models.Users;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface InvitesDAO extends GenericDAO<Invites, Integer>{
    public List<Invites>getInvitesByUser(Users user);

     public List<Invites>getCurrentInvitesSentByUser(Users user);
     public void updateInviteStatus(int inviteid, String status);
     public List<Invites>getInvitesByEventId(Integer eventid);
     public Invites getInviteByUserAndEvent(Users u, Integer eventid );
     public List<Integer>getInvitedUserIdsByEventId(Integer id);
}

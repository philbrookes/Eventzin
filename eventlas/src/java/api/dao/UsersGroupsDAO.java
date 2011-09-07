/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Groups;
import com.models.Users;
import com.models.Usersgroups;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public interface UsersGroupsDAO extends GenericDAO<Usersgroups, Integer> {
    public List<Usersgroups>findByUser(Users u);
    public List<Usersgroups>findByFriend(Users u);
    public List<Usersgroups>findByGroupAndUser(Groups group, Users us);
    public Usersgroups findGroupMemberByGroupIdAndMemberId(Integer gid, Integer mid);
    
}

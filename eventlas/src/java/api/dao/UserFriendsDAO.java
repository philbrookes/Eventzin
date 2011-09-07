/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.UserFriends;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface UserFriendsDAO extends GenericDAO<UserFriends, Integer> {

     public List<UserFriends> findFriendsByUsersId(Integer id);
     public UserFriends findByFriendId(int id);
     public UserFriends findByFriendIdAndUserId(int friendid, int userid);
     

}

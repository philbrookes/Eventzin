/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Events;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public interface UserEventStatusDAO extends GenericDAO<UserEventStatus, Integer> {

     public UserEventStatus getUserEventStatusByEventAndUser(Events event, Users user);
     public List<UserEventStatus> getStatusesByUser(Users u);
}

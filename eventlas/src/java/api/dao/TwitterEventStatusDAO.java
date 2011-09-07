/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.TwitterEventStatus;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface TwitterEventStatusDAO extends GenericDAO<TwitterEventStatus, Integer> {
    public TwitterEventStatus findByUsernameAndEventid(String username, Integer evid);
    public List<TwitterEventStatus>findByEventId(Integer evid);
}

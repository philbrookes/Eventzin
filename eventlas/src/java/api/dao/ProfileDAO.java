/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Profile;
import com.models.Users;

/**
 *
 * @author craigbrookes
 */
public interface ProfileDAO extends GenericDAO<Profile, Integer> {

    public Profile fingByUser(Users user);

}

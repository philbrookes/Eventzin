/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Users;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface UsersDAO extends GenericDAO<Users, Integer> {

    public Users findUserByUserName(String username);

    public List<Users> findUsersByEmailAddresses(ArrayList<String>emails);

    public List<Users> findUsersByPhoneNumbers(ArrayList<String>phoneNumbers);

    public List<Users> searchForUsersLike(String searchtext);
    
     public Users getUserByEmail(String email);
}

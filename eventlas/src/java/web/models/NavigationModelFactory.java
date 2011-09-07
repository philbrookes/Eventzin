/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.models;

import com.models.Users;
import java.util.ArrayList;

/**
 *
 * @author craigbrookes
 */
public class NavigationModelFactory {



    public static ArrayList<NavigationItem>getUserNavigationItems(Users user){
        ArrayList<NavigationItem>navitems =new ArrayList<NavigationItem>();
            String username = user.getUsername();
            NavigationItem item = new NavigationItem("/"+username+"/events", "events");
            navitems.add(item);
        return navitems;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;


import api.helpers.PushNotificationsHelper;
import com.models.ApiKeys;
import com.models.Users;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author craigbrookes
 */
abstract public class AuthenticatedRequest<T> extends ApiAbController implements Authenticated{

    
    protected String apiKey;

    protected final String SALT = "4Jh1*d>}49KjNQ'/?365";

    protected PushNotificationsHelper pushHelper;


    public AuthenticatedRequest(HttpServletRequest req) {
        super(req);
        required.add("apikey");
        setApiKey(this.request.getParameter("apikey"));
        pushHelper = new PushNotificationsHelper();
    }

    @Override
    public final boolean authenticateUser(ApiKeys key) {
        
      if(key == null)
          return false;
     /**
      * @todo fix to check ip address;
      */
      if(key.getUsers() != null){
        
         return true;
      }
      
   
      return false;
    }

    public final Users getUserFromKey(ApiKeys key){
	return key.getUsers();
    }

    protected final void setApiKey(String key){
        this.apiKey = key;
    }

    protected final String getApiKey(){
        return this.apiKey;
    }


    

    


}

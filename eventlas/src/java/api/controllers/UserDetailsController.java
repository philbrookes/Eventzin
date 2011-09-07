package api.controllers;

import api.dao.HibernateUtil;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.models.ApiKeys;
import api.dao.ApiKeysDAO;
import api.dao.UsersDAO;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Users;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class UserDetailsController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public UserDetailsController(HttpServletRequest req) {
        super(req);
        required.add("apikey");
        required.add("email");
    }
    
    public LinkedHashMap<String, Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        
        try {
            
            ApiKeysDAO kdao = factory.getKeysDAO();
            
            String email = request.getParameter("email");
            
            UsersDAO udao = factory.getUserDAO();
            
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true || key.getPermission() < 2) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                Users u = udao.getUserByEmail(email);
                if(u != null){
                    System.out.println(u.getUsername()+" USERNAME");
                    Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                    message.setSubject("your details for eventzin");
                    message.setContentFromTemplate("userdetails");
                    HashMap<String, String> replace = new HashMap<String, String>();
                    replace.put("#Username# ", u.getUsername());
                    replace.put("#Password#", u.getPassword());
                    
                    message.replaceTagsInContent(replace);
                    message.setTo(u.getEmail());
                    message.send();
                }
                
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse();
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        }
    }
}

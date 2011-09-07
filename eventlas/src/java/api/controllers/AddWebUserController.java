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
import api.dao.ProfileDAO;
import api.dao.UsersDAO;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Profile;
import com.models.Users;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class AddWebUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public AddWebUserController(HttpServletRequest req) {
        super(req);
        required.add("username");
        required.add("password");
        required.add("email");
        optional.add("permoverride");
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
            UsersDAO udao = factory.getUserDAO();
            ProfileDAO pdao = factory.getProfileDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
             Users u = new Users();
            if (key.getUsers().getUsername() == "site") {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
             String username = request.getParameter("username");
             String password = request.getParameter("password");
             String email = request.getParameter("email");
             Boolean perm = (request.getParameter("permoverride")!=null)?true:false;
             
             if(username != null && password != null && email !=null){
               
                u.setUsername(username);
                u.setPassword(password);
                u.setEmail(email);
                String mkey =  ParameterHelper.createMobileKey(new Date().getTime(), username);
                u.setMobileKey(mkey);
                udao.save(u);
                ApiKeys apiKey = new ApiKeys();
                apiKey.setApikey(mkey);
                if(perm){
                    apiKey.setPermission(new Integer(1));
                }else{
                    apiKey.setPermission(new Integer(0));
                }
                apiKey.setUsers(u);
                kdao.save(apiKey);
                Profile pro = new Profile();
                pro.setProfilepic("http://eventzin.com/images/profile/blankprofile.jpg");
                pro.setUser(u);
                pdao.save(pro);
                //send mail
                 Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                 message.setSubject("welcome to eventzin "+username);
                 message.setContentFromTemplate("newuser");
                 HashMap<String, String> replace = new HashMap<String, String>();
                 String clink = "<a href='http://eventzin.com/register/confirmation?id="+u.getId()+"&c="+mkey+"'>confirmation</a>";
                 replace.put("#confirmation#", clink);
                 replace.put("#username#", username);
                 message.replaceTagsInContent(replace);
                 message.setTo(email);
                 message.send();
                
             }
             
                /**
                LOGIC CODE HERE
                 **/
                
                
            }
            kdao.commitTransaction();
            response.put("id", u.getId());
            response.put("sessionkey", u.getMobileKey());
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "The user name is taken");
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

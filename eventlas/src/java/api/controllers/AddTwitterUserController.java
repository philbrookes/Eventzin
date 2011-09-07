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
import api.dao.TwitterDAO;
import api.dao.UsersDAO;
import com.models.Profile;
import com.models.Twitter;
import com.models.Users;
import java.util.Date;

/**
 *
 * @author craigbrookes
 */
public class AddTwitterUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public AddTwitterUserController(HttpServletRequest req) {
        super(req);
        required.add("twitterhandle");
        required.add("password");
        required.add("username");
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
            TwitterDAO tdao = factory.getTwitterDAO();
            ProfileDAO pdao = factory.getProfileDAO();
            
            /**
            Add more Dao as needed
             **/
            
            String twitterHandle = request.getParameter("twitterhandle");
            String password = request.getParameter("password");
            String username = request.getParameter("username");
            
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                
                Users u = udao.findUserByUserName(username);
                if(u == null){
                
                
                 u = new Users();
                 u.setUsername(username);
                 u.setPassword(password);
                
                 String mkey = ParameterHelper.createMobileKey(new Date().getTime(),twitterHandle);
                 u.setMobileKey(mkey);
                 udao.save(u);
                
                 ApiKeys ukey = new ApiKeys();
                 ukey.setApikey(mkey);
                 ukey.setPermission(new Integer(1));
                 ukey.setUsers(u);
                 kdao.save(ukey);
                 
                  Profile pro = new Profile();
                pro.setProfilepic("http://eventzin.com/images/profile/blankprofile.jpg");
                pro.setTwitterUsername("@"+twitterHandle);
                pro.setUser(u);
                pdao.save(pro);
                
                }else if(u.getPassword().equals(password)!=true){
                    HibernateUtil.closeSession();
                    return ApiResponse.getNoPermissionsResponse();
                }
                
                
                Twitter tw = new Twitter();
                tw.setTwitterHandle(twitterHandle);
                tw.setUser(u);
                
               
                
                tdao.save(tw); 
                
                
                response.put("id", u.getId());
                response.put("sessionkey", u.getMobileKey());
                        
                       
                /**
                LOGIC CODE HERE
                 **/
                
                //create a user insert then add twitter account
            }
            kdao.commitTransaction();
            
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            
            
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        }
    }
}

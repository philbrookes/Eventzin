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
import api.dao.UserFriendsDAO;
import com.models.UserFriends;
import com.models.Users;

/**
 *
 * @author craigbrookes
 */
public class RemoveFriendController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public RemoveFriendController(HttpServletRequest req) {
        super(req);
        required.add("friendid");
        
    }
    
    @Override
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
            Integer friendid = new Integer(request.getParameter("friendid"));
            ApiKeysDAO kdao = factory.getKeysDAO();
            UserFriendsDAO ufdao = factory.getUserFriendsDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                Users u = key.getUsers(); 
                UserFriends friend = ufdao.findByFriendIdAndUserId(friendid,u.getId() );
                if(friend != null){
                    ufdao.delete(friend);
                }else{
                    HibernateUtil.closeSession();
                    response.put(ApiResponse.ERROR_INFO_KEY, "no valid friend found");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }
                
                /**
                LOGIC CODE HERE
                 **/
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

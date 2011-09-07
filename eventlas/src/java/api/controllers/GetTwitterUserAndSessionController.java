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
import api.dao.TwitterDAO;
import com.models.Twitter;
import com.models.UserFriends;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author craigbrookes
 */
public class GetTwitterUserAndSessionController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public GetTwitterUserAndSessionController(HttpServletRequest req) {
        super(req);
        required.add("twitterhandle");
        
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
            TwitterDAO tdao = factory.getTwitterDAO();
            
            /**
            Add more Dao as needed
             **/
            String username = request.getParameter("twitterhandle");
            String token = request.getParameter("token");
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                Twitter t = tdao.fetchByTokenAndUserName(username);
                if(t != null){
                        Users user = t.getUser();
                        String phone = (user.getPhone() == null)?"":user.getPhone();
                        String sex = (user.getSex()== null)?"":user.getPhone();
                        String city = (user.getCity() == null)?"":user.getCity();
                        String country = (user.getCountryCode() == null)?"":user.getCountryCode();
                        String agerange = (user.getAgeRange() == null)?"":user.getAgeRange();
                        
                        HashMap<String,Object>userMap = new HashMap<String, Object>();
                        userMap.put("username", user.getUsername());
                        userMap.put("email", user.getEmail());
                        userMap.put("sex", sex);
                        userMap.put("city", city);
                        userMap.put("countrycode", country);
                        userMap.put("agerange", agerange);
                        userMap.put("id",user.getId());
                        Set<UserFriends> friends = user.getUsersfriends();
                        ArrayList<Integer>friendIds = new ArrayList<Integer>(0);
                        for(UserFriends f : friends){
                            friendIds.add(f.getFriend().getId());
                        }
                        userMap.put("friend_ids", friendIds);
                        response.put("user", userMap);
                        response.put("sessionkey", user.getMobileKey());
                }else{
                    HibernateUtil.closeSession();
                    return ApiResponse.getNoPermissionsResponse();
                }
                
                /**
                LOGIC CODE HERE
                 **/
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            
            
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

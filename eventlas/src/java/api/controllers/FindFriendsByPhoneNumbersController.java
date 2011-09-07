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
import com.models.Users;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author craigbrookes
 */
public class FindFriendsByPhoneNumbersController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public FindFriendsByPhoneNumbersController(HttpServletRequest req) {
        super(req);
        required.add("phones[]");
        
    }

    public LinkedHashMap<String,Object>process(){
        ArrayList<HashMap<String,Object>>responseList = new ArrayList<HashMap<String, Object>>();
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        try{

                ApiKeysDAO kdao = factory.getKeysDAO();
                UsersDAO udao = factory.getUserDAO();
                ArrayList<String>phoneNumbers = new ArrayList<String>(Arrays.asList(request.getParameterValues("phones[]")));
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{

                    List<Users> friendUsers = udao.findUsersByPhoneNumbers(phoneNumbers);
                     for(Users usr : friendUsers){
                           HashMap<String,Object>userMap = new HashMap<String, Object>();
                           userMap.put("username", usr.getUsername());
                           userMap.put("id", usr.getId());
                           responseList.add(userMap);

                       }


                }
                kdao.commitTransaction();
                 return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(responseList));


        }catch(ObjectNotFoundException e){
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }catch(ConstraintViolationException e){
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"A problem occurred "+ e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"A problem occurred  "+ e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }catch(Exception e){
                 HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"A problem occurred "+ e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
    }

}
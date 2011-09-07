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
import api.logger.Logger;
import com.models.Users;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author craigbrookes
 */
public class FindFriendsByEmailAddressesController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public FindFriendsByEmailAddressesController(HttpServletRequest req) {
        super(req);
        required.add("emails[]");
    }

    public LinkedHashMap<String,Object>process(){
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        try{

                ApiKeysDAO kdao = factory.getKeysDAO();
                UsersDAO udao = factory.getUserDAO();
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                List<Users>retrievedUsers = null;
                ArrayList<HashMap<String,Object>> responseList = new ArrayList<HashMap<String, Object>>();
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{

                    ArrayList<String>emailAddresses = new ArrayList<String>(Arrays.asList(request.getParameterValues("emails[]")));
                    if(emailAddresses.isEmpty() != true){

                       for(String email : emailAddresses){
                            Logger.debug(FindFriendsByEmailAddressesController.class.getName(), "the email is equal to "+email+"\n\r");
                           if(email.equals("") || email.contains("@")){
                             
                           }
                       }

                       retrievedUsers = udao.findUsersByEmailAddresses(emailAddresses);
                       for(Users usr : retrievedUsers){
                           HashMap<String,Object>userMap = new HashMap<String, Object>();
                           userMap.put("username", usr.getUsername());
                           userMap.put("id", usr.getId());
                           responseList.add(userMap);

                       }


                    }
                    else{
                        HibernateUtil.closeSession();
                        response.clear();
                        response.put(ApiResponse.ERROR_INFO_KEY, "no email addresses found");
                        return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

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
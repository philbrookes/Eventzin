package api.controllers;
import api.dao.ApiKeysDAO;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.UserEventStatusDAO;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.Events;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
/**
 *
 * @author craigbrookes
 */
public class GetUserEventStatusController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public GetUserEventStatusController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
        
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
                EventsDAO edao = factory.getEventDAO();
                UserEventStatusDAO uesd = factory.getUserEventStatusDAO();
                Integer eventid = new Integer(this.request.getParameter("eventid"));
                HashMap<String,Object>  statusMap = new  HashMap<String, Object>();

                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{
                      Events event = edao.findByPrimaryKey(eventid);
                      Users user = key.getUsers();
                      UserEventStatus status = uesd.getUserEventStatusByEventAndUser(event, user);
                      statusMap.put("userid", user.getId());
                      statusMap.put("username", user.getUsername());
                      statusMap.put("eventid", eventid);
                      if(status != null){
                         statusMap.put("status", status.getStatus());
                         
                     }else{
                          statusMap.put("status", "none");
                     }

                      
                }
                kdao.commitTransaction();
                 return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(statusMap));
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

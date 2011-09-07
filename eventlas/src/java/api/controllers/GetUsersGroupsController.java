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
import api.dao.GroupDAO;
import api.dao.UsersGroupsDAO;
import com.models.Groups;
import com.models.Users;
import com.models.Usersgroups;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author craigbrookes
 */
public class GetUsersGroupsController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public GetUsersGroupsController(HttpServletRequest req) {
        super(req);
        
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
                UsersGroupsDAO ugdao = factory.getUsersGroupsDAO();
                GroupDAO gdao =factory.getGroupDAO();
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                Users user = key.getUsers();
                ArrayList<HashMap<String,Object>>grouplist = new ArrayList<HashMap<String, Object>>();
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{

                   
                    List<Groups> groups = gdao.findAll(0, 10000);

                    for(Groups g : groups){
                        List<Usersgroups>userG = ugdao.findByGroupAndUser(g, user);

                        HashMap<String,Object>groupMap = new HashMap<String, Object>();
                        ArrayList userids = new ArrayList();
                        for(Usersgroups ugs : userG){
                            
                            groupMap.put("name", g.getGroup());
                            groupMap.put("id",g.getId());
                            userids.add(ugs.getUsersByGroupmemberid().getId());
                        }
                        groupMap.put("members", userids);
                        grouplist.add(groupMap);
                    }

            
                }
                kdao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(grouplist));


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
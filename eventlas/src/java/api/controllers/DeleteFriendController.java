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
import api.dao.UsersGroupsDAO;
import com.models.UserFriends;
import com.models.Usersgroups;
import java.util.List;
import java.util.Set;
/**
 *
 * @author craigbrookes
 */
public class DeleteFriendController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public DeleteFriendController(HttpServletRequest req) {
        super(req);
        required.add("friendid");
        optional.add("deletefromgroups");
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
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                UserFriendsDAO ufdao = factory.getUserFriendsDAO();
                UsersGroupsDAO ugdao = factory.getUsersGroupsDAO();
                Integer friendid = new Integer(request.getParameter("friendid"));
                //Integer deletefromgroups = new Integer(request.getParameter("deletefromgroups"));
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{

                    UserFriends usfriend = ufdao.findByFriendIdAndUserId(friendid, key.getUsers().getId());
                    List<Usersgroups>ugroups = ugdao.findByFriend(usfriend.getFriend());
                    for(Usersgroups group : ugroups){
                        System.out.println("group founf with id = "+ group.getId() + " And "+group.getUsersByGroupmemberid().getEmail());
                       
                            if(group.getGroups().getId() == 6)
                                ugdao.delete(group);
                        
                    }
                    ufdao.delete(usfriend);

                }
                kdao.commitTransaction();

                return ApiResponse.getSuccessResponse();


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
                e.printStackTrace();
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
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
import api.dao.UserEventStatusDAO;
import api.dao.UsersDAO;
import com.models.Events;
import com.models.Profile;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/**
 *
 * @author craigbrookes
 */
public class GetProfileController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public GetProfileController(HttpServletRequest req) {
        super(req);
        optional.add("userid");
        optional.add("username");
        
    }

    @Override
    public LinkedHashMap<String,Object>process(){
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        try{

                ApiKeysDAO kdao = factory.getKeysDAO();
                ProfileDAO pdao = factory.getProfileDAO();
                UserEventStatusDAO uesDAO =factory.getUserEventStatusDAO();
                UsersDAO udao = factory.getUserDAO();
                Users u = null;

               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                ArrayList<HashMap<String,Object>>profileList = new ArrayList<HashMap<String, Object>>(1);
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{
                  if(request.getParameter("userid") !=null){
                    Integer userid = new Integer(request.getParameter("userid"));
                    u = udao.findByPrimaryKey(userid);
                   }else if(request.getParameter("username") !=null){
                       String username = request.getParameter("username");
                       u = udao.findUserByUserName(username);
                   }
                  else{
                      u = key.getUsers();
                   }
                   Profile p = pdao.fingByUser(u);
                   String website = (p.getWebsite()!=null)?p.getWebsite():"";
                   String twitterUsername = (p.getTwitterUsername()!=null)?p.getTwitterUsername():"";
                   String facebookpage = (p.getFacebookpage()!=null)?p.getFacebookpage():"";
                   String bio = (p.getBio() != null)?p.getBio():"";
                   String profilePic = (p.getProfilepic()!=null)?p.getProfilepic():"";
                   HashMap<String,Object>profileMap = new HashMap<String, Object>();
                   profileMap.put("website", website);
                   profileMap.put("twitterusername", twitterUsername);
                   profileMap.put("facebookpage", facebookpage);
                   profileMap.put("bio", bio);
                   profileMap.put("profilepic", profilePic);
                   profileMap.put("fans", new ArrayList());
                   profileMap.put("id", p.getId());
                   profileMap.put("userid", p.getUser().getId());
                   profileMap.put("username",u.getUsername());
                   profileList.add(profileMap);
                   Set<Events>events = u.getEventses();
                   ArrayList<Integer>usersEventIds = new ArrayList<Integer>(events.size());
                   for(Events e : events)
                   {
                       usersEventIds.add(e.getId());
                   }
                   profileMap.put("events", usersEventIds);
                   List<UserEventStatus>statuses = uesDAO.getStatusesByUser(u);
                   ArrayList<Integer>attended = new ArrayList<Integer>();
                   for(UserEventStatus ues : statuses)
                   {
                       if(ues.getStatus().equals("attending")){
                           attended.add(ues.getEvent().getId());
                       }
                   }
                   profileMap.put("attended", attended);


                }
                kdao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(profileList));


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
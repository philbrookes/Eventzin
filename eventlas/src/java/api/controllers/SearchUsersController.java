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
import com.models.Profile;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author craigbrookes
 */
public class SearchUsersController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public SearchUsersController(HttpServletRequest req) {
        super(req);
        required.add("searchtext");
        
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
                UsersDAO udao = factory.getUserDAO();
                ArrayList<HashMap<String,Object>>responseList = new ArrayList<HashMap<String, Object>>(0);
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                //test
                List<Users> users = udao.searchForUsersLike(request.getParameter("searchtext"));
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{
                    for(Users u : users){
                        HashMap<String,Object>userMap = new HashMap<String, Object>();
                        userMap.put("username",u.getUsername() );
                       userMap.put("id", u.getId());
                       Profile profile = u.getProfile();
                       HashMap<String,Object>profMap = new HashMap<String, Object>(5);
                       String bio = (profile.getBio()!=null)?profile.getBio():"";
                       String fbp = (profile.getFacebookpage()!=null)?profile.getFacebookpage():"";
                       String pic = (profile.getProfilepic()!=null)?profile.getProfilepic():"";
                       String twitter = (profile.getTwitterUsername()!=null)?profile.getTwitterUsername():"";
                       String website = (profile.getWebsite()!=null)?profile.getWebsite():"";
                       profMap.put("bio", bio);
                       profMap.put("facebookpage", fbp);
                       profMap.put("picture",pic);
                       profMap.put("twitter", twitter);
                       profMap.put("website",website);
                       userMap.put("profile", profMap);
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
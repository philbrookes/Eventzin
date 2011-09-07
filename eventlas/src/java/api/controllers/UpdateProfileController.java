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
import com.models.Profile;
import com.models.Users;
/**
 *
 * @author craigbrookes
 */
public class UpdateProfileController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public UpdateProfileController(HttpServletRequest req) {
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
                ProfileDAO pdao = factory.getProfileDAO();

                String website = (request.getParameter("website")!=null)?request.getParameter("website"):"";
                String facebookpage =  (request.getParameter("facebookpage")!=null)?request.getParameter("facebookpage"):"";
                String twitterusername =(request.getParameter("twitterusername")!=null)?request.getParameter("twitterusername"):"";
                String bio = (request.getParameter("bio")!=null)?request.getParameter("bio"):"";

               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{

                   Users u = key.getUsers();
                   Profile p = pdao.fingByUser(u);
                   if(website.equals("")!=true){
                       p.setWebsite(website);
                   }
                   if(facebookpage.equals("")!= true){
                       p.setFacebookpage(facebookpage);
                   }
                   if(twitterusername.equals("")!=true){
                       p.setTwitterUsername(twitterusername);
                   }
                   if(bio.equals("")!=true){
                       p.setBio(bio);
                   }

                   pdao.save(p);

                    /**
                        LOGIC CODE HERE
                    **/
                   response.put("id", p.getId());

                }
                kdao.commitTransaction();


                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));


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
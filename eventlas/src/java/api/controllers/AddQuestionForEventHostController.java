package api.controllers;
import api.dao.ApiKeysDAO;
import api.dao.EventQuestionsDAO;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.EventQuestions;
import com.models.Events;
import com.models.Invites;
import com.models.Users;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
/**
 *
 * @author craigbrookes
 */
public class AddQuestionForEventHostController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    Boolean is_invited = false;

    public AddQuestionForEventHostController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
        required.add("question");
        optional.add("privacy");
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
                EventQuestionsDAO eqd = factory.getEventQuestionsDAO();
                EventsDAO evdao = factory.getEventDAO();
                UsersDAO udao = factory.getUserDAO();
                Integer eventid = new Integer(request.getParameter("eventid"));
                String question = request.getParameter("question");
                String privacy = request.getParameter("privacy"); 
                        
                
               /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{
                    EventQuestions eventQuestion = new EventQuestions();
                    Events event = evdao.findByPrimaryKey(eventid);
                    Users user = key.getUsers();
                    if(event.getVisibility().getId() > 1){
                        Set<Invites> invs = event.getInvites();
                        Iterator<Invites>inIt = invs.iterator();
                        while(inIt.hasNext()){
                            Invites inv = inIt.next();
                            if(inv.getUsers().equals(user)){
                                this.is_invited = true;
                                break;
                            }

                        }

                        if(this.is_invited == false){
                            response.clear();
                            response.put(ApiResponse.ERROR_INFO_KEY, "this is a private event that you have not been invited to");
                            return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                        }

                    }
                    
                    eventQuestion.setEvent(event);
                    eventQuestion.setUser(user);
                    eventQuestion.setQuestion(question);
                    if(privacy != null){
                        eventQuestion.setPrivacy(new Integer(1));
                    }

                    eqd.save(eventQuestion);
                    /**
                        LOGIC CODE HERE
                    **/
                    response.clear();
                    response.put("questionid", eventQuestion.getId());


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
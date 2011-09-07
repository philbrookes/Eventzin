package api.controllers;
import api.dao.ApiKeysDAO;
import api.dao.EventQuestionsAnswersDAO;
import api.dao.EventQuestionsDAO;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.EventQuestionAnswers;
import com.models.EventQuestions;
import com.models.Events;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
/**
 *
 * @author craigbrookes
 */
public class GetQuestionsForEventController extends AuthenticatedRequest<LinkedHashMap<String,Object>>{

    public GetQuestionsForEventController(HttpServletRequest req) {
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
                EventQuestionsDAO eqd = factory.getEventQuestionsDAO();
                EventsDAO edao =factory.getEventDAO();
                EventQuestionsAnswersDAO eqaDAO = factory.getEventQuestionsAnswersDAO();
                Integer eventid = new Integer(request.getParameter("eventid"));
                UsersDAO udao = factory.getUserDAO();
                ArrayList<HashMap<String,Object>> listResponse = new ArrayList<HashMap<String, Object>>();
                        /**
               Add more Dao as needed
               **/
                kdao.beginTransaction();
                ApiKeys key = kdao.findKeyByApiKey(apiKey);
                
                if(this.authenticateUser(key) !=true)
                    return ApiResponse.getNoPermissionsResponse();
                else{
                    /**
                        LOGIC CODE HERE
                    **/
                    Users user = key.getUsers();
                    Events event = edao.findByPrimaryKey(eventid);
                    
                       
                        List<EventQuestions>questions = eqd.findByEventId(event);                                
                        Iterator<EventQuestions> lIt = questions.iterator();

                        while(lIt.hasNext()){
                            EventQuestions equest = lIt.next();
                            HashMap<String,Object> eqMap = new HashMap<String, Object>();
                            eqMap.put("id", equest.getId());
                            eqMap.put("question", equest.getQuestion());
                            eqMap.put("eventid", equest.getEvent().getId());
                            Users asker = equest.getUser();
                            eqMap.put("askerid",asker.getId());
                            eqMap.put("username", asker.getUsername());
                            
                            EventQuestionAnswers answer = eqaDAO.getAnswerByQuestion(equest);
                            
                            if(answer !=null){
                                HashMap<String,Object>answerMap = new HashMap<String, Object>();
                                answerMap.put("answer", answer.getAnswer());
                                answerMap.put("id", answer.getId());
                                eqMap.put("answer", answerMap);
                            }
                                    
                            listResponse.add(eqMap);
                            
                        }


                   
                    
                    

                }
                kdao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(listResponse));


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
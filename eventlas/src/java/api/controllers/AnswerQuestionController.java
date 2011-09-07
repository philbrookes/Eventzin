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
import api.dao.EventQuestionsAnswersDAO;
import api.dao.EventQuestionsDAO;
import com.models.EventQuestionAnswers;
import com.models.EventQuestions;
import com.models.Users;

/**
 *
 * @author craigbrookes
 */
public class AnswerQuestionController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public AnswerQuestionController(HttpServletRequest req) {
        super(req);
        required.add("questionid");
        required.add("answer");
    }
    
    @Override
    public LinkedHashMap<String, Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        
        try {
            
            ApiKeysDAO kdao = factory.getKeysDAO();
            EventQuestionsDAO eqdao = factory.getEventQuestionsDAO();
            EventQuestionsAnswersDAO eqadao = factory.getEventQuestionsAnswersDAO();
            
            Integer questionid = new Integer(request.getParameter("questionid"));
            String answer = request.getParameter("answer"); 
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            Users keyUser = key.getUsers();
            System.out.println("the user is "+keyUser.getId());
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                EventQuestions quest = eqdao.findByPrimaryKey(questionid);
                Users eventOwner = quest.getEvent().getUsers();
                System.out.println("the user is "+eventOwner.getId());
                if(keyUser.getId().equals(eventOwner.getId())){
                    EventQuestionAnswers questionAnswer = new EventQuestionAnswers();
                    questionAnswer.setAnswer(answer);
                    questionAnswer.setQuestion(quest);
                    eqadao.save(questionAnswer);
                }else{
                    HibernateUtil.closeSession();
                    return ApiResponse.getNoPermissionsResponse();
                }
                    
               
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse();
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        }
    }
}

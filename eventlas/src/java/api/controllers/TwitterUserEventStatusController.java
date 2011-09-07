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
import api.dao.EventsDAO;
import api.dao.TwitterDAO;
import api.dao.TwitterEventStatusDAO;
import api.dao.UserEventStatusDAO;
import com.models.Events;
import com.models.Twitter;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author craigbrookes
 */
public class TwitterUserEventStatusController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public static ArrayList<String> statuses = new ArrayList<String>(Arrays.asList("ingnored", "attending", "unattended", "declined", "maybe"));

    public TwitterUserEventStatusController(HttpServletRequest req) {
        super(req);
        required.add("username");
        required.add("status");
        required.add("eventid");
    }

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
            TwitterEventStatusDAO twEvDao = factory.getTwitterEventStatusDAO();
            EventsDAO evdao = factory.getEventDAO();
            TwitterDAO twDao = factory.getTwitterDAO();
            String status = request.getParameter("status");
            Integer eventid = new Integer(request.getParameter("eventid"));
            String twitterHandle = request.getParameter("username");
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            }
            if (statuses.contains(status) != true) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "the status is not a reconginesed status it must be ignored, unattended, attending or declined");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } else {
                Events event = evdao.findByPrimaryKey(eventid);
                if (event == null) {
                    HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "no matching event");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }
                //check it twitter handle is associated with real user
                Twitter twitterUser = twDao.fetchByTokenAndUserName(twitterHandle);
                if (twitterUser != null) {
                    UserEventStatusDAO uesd = factory.getUserEventStatusDAO();
                    Users u = twitterUser.getUser();
                    UserEventStatus usStatus = uesd. getUserEventStatusByEventAndUser(event, u);
                    if(usStatus == null){
                        usStatus = new UserEventStatus();
                    }
                    
                    usStatus.setEvent(event);
                    usStatus.setUser(u);
                    usStatus.setStatus(status);
                    usStatus.setStatusHash(ParameterHelper.md5(event.getId().toString()+u.getId().toString()));
                    
                    uesd.save(usStatus); 
                    //update UserEventStatus
                    
                } else {


                    TwitterEventStatus eventStatus = twEvDao.findByUsernameAndEventid(twitterHandle, eventid);
                    if (eventStatus == null) {
                        eventStatus = new TwitterEventStatus();
                    }
                    eventStatus.setEvent(event);
                    eventStatus.setUsername(twitterHandle);
                    eventStatus.setStatus(status);
                    twEvDao.save(eventStatus);
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

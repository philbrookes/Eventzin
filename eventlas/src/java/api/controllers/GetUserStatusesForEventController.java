package api.controllers;

import api.dao.ApiKeysDAO;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.TwitterEventStatusDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.Events;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import controllers.AbController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class GetUserStatusesForEventController extends ApiAbController {

    public GetUserStatusesForEventController(HttpServletRequest req) {
        super(req);
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


            EventsDAO evdao = factory.getEventDAO();
            TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            Integer eventid = new Integer(request.getParameter("eventid"));
            ArrayList<HashMap<String, Object>> statusList = new ArrayList<HashMap<String, Object>>();
            /**
            Add more Dao as needed
             **/
            evdao.beginTransaction();


            Events event = evdao.findByPrimaryKey(eventid);
            Set<UserEventStatus> statuses = event.getStatuses();
            Iterator<UserEventStatus> sit = statuses.iterator();

            while (sit.hasNext()) {
                UserEventStatus ues = sit.next();
                HashMap<String, Object> statusMap = new HashMap<String, Object>();
                statusMap.put("username", ues.getUser().getUsername());
                statusMap.put("status", ues.getStatus());
                statusList.add(statusMap);

            }
            System.out.println("About to get the twitter Statuses");
            List<TwitterEventStatus>twitterStatuses = tevsDao.findByEventId(eventid);
            System.out.println("Statuses= "+twitterStatuses);
            if(twitterStatuses !=null && twitterStatuses.isEmpty() !=true){
                for(TwitterEventStatus tst : twitterStatuses){
                     HashMap<String, Object> statusMap = new HashMap<String, Object>();
                    statusMap.put("username", "@"+tst.getUsername());
                    statusMap.put("status", tst.getStatus());
                    statusList.add(statusMap);
                }
            }


            /**
            LOGIC CODE HERE
             **/
            evdao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(statusList));


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
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
    }
}
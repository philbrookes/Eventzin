package api.controllers;

import api.dao.ApiKeysDAO;
import api.dao.EventsDAO;
import api.dao.EventscommentDAO;
import api.dao.HibernateUtil;
import api.dao.TwitterEventStatusDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.EventQuestions;
import com.models.Invites;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Visibility;
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
public class GetUsersEventsController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetUsersEventsController(HttpServletRequest req) {
        super(req);
       

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
            EventsDAO edao = factory.getEventDAO();
            EventscommentDAO   commen = factory.getEventsCommentDAO();
            TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                /**
                LOGIC CODE HERE
                 **/
                /**
                 * @ToDo make this call the getEvents a native sql statement. As is could lead to extra load on server
                 */
                Users eventuser = key.getUsers();
                List<EventFullDetail> userEvents = edao.getCurrentEventsForUser(eventuser);
                ArrayList<HashMap<String, Object>> eventMaps = new ArrayList<HashMap<String, Object>>(userEvents.size());
                Iterator<EventFullDetail> eIterator = userEvents.iterator();
                while (eIterator.hasNext()) {
                    EventFullDetail event = eIterator.next();

                    HashMap<String, Object> eventMap = new HashMap<String, Object>();
                    ArrayList<HashMap<String, Object>> qMap = new ArrayList<HashMap<String, Object>>();
                    Set<EventQuestions> qs = event.getQuestions();
                    for (EventQuestions eq : qs) {
                        HashMap<String, Object> question = new HashMap<String, Object>(3);
                        question.put("question", eq.getQuestion());
                        question.put("id", eq.getId());
                        question.put("userid", eq.getUser().getId());
                        qMap.add(question);
                    }
                    eventMap.put("questions", qMap);
                    Set<UserEventStatus> statuses = event.getStatuses();
                    int attending = 0;

                    ArrayList<HashMap<String, Object>> statusList = new ArrayList<HashMap<String, Object>>(statuses.size());
                    for (UserEventStatus s : statuses) {
                        HashMap<String, Object> tempMap = new HashMap<String, Object>();
                        if (s.getStatus().equals("attending")) {
                            attending++;
                        }


                        Users su = s.getUser();
                        Integer statusUserId = su.getId();
                        tempMap.put("id", statusUserId);
                        
                        tempMap.put("username", su.getUsername());
                        tempMap.put("status", s.getStatus());
                        statusList.add(tempMap);

                    }
                    
                    List<TwitterEventStatus>twitterStatuses = tevsDao.findByEventId(event.getId());
                    
                    if(twitterStatuses !=null && twitterStatuses.isEmpty() !=true){
                        for(TwitterEventStatus tst : twitterStatuses){
                             HashMap<String, Object> statusMap = new HashMap<String, Object>();
                            statusMap.put("username", "@"+tst.getUsername());
                            statusMap.put("status", tst.getStatus());
                            if(tst.getStatus().equals("attending")){
                                attending++;
                            }
                            statusList.add(statusMap);
                        }
                    }
                    Integer count = commen.countCommentsForEvent(event);
                    eventMap.put("comments", count);
                    eventMap.put("statuses", statusList);
                    eventMap.put("attending", attending);
                    eventMap.put("title", event.getTitle());
                    eventMap.put("overview", event.getSummary());
                    eventMap.put("id", event.getId());
                    eventMap.put("eventdate", event.getEventdate());
                    eventMap.put("enddate", event.getEventend());
                    HashMap<String, Object> locMap = new HashMap<String, Object>(2);
                    locMap.put("latitude", event.getLatitude());
                    locMap.put("longitude", event.getLongitude());
                    locMap.put("id", event.getLocid());
                    locMap.put("link", "http://eventlas.com/api/GetLocationById?locid="+event.getLocid());
                    eventMap.put("location", locMap);
                    HashMap<String, Object> venueMap = new HashMap<String, Object>(2);

                    venueMap.put("name", event.getVenuename());
                    venueMap.put("address", event.getVenueaddress());
                    venueMap.put("id",event.getVenueid());
                    eventMap.put("venue", venueMap);
                    HashMap<String,Object>categoryMap = new HashMap<String, Object>(2);
                    EventCategorys category = event.getCategory();
                    categoryMap.put("name", category.getCategory());
                    categoryMap.put("id", category.getId());
                    categoryMap.put("icon", category.getIconid().getIcon());
                    eventMap.put("category", categoryMap);
                    Users u = event.getUser();
                    eventMap.put("username", u.getUsername());
                    eventMap.put("userid", u.getId());
                    HashMap<String,Object> visMap = new HashMap<String, Object>();
                    Visibility vis = event.getVisibility();
                    visMap.put("id", vis.getId());
                    visMap.put("visibility", vis.getPrivacylevel());
                    eventMap.put("visibility", visMap);
                    eventMaps.add(eventMap);


                }

                kdao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(eventMaps));
            }



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
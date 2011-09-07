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
import api.dao.TwitterEventStatusDAO;
import api.dao.UsersDAO;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author craigbrookes
 */
public class GetEventByUsernameAndEventNameController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetEventByUsernameAndEventNameController(HttpServletRequest req) {
        super(req);
        required.add("username");
        required.add("eventname");
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
            HashMap<String, Object> eventMap = new HashMap<String, Object>();
            String username = request.getParameter("username");
            String eventname = request.getParameter("eventname");

            if (eventname != null) {
              eventname=  eventname.replaceAll("-", " ");
            }


            ApiKeysDAO kdao = factory.getKeysDAO();
            EventsDAO edao = factory.getEventDAO();
            UsersDAO udao = factory.getUserDAO();
            TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                Users user = udao.findUserByUserName(username);
                user.toString();
                EventFullDetail event = edao.getEventByEventNameAndUser(eventname, user);

                if (event != null) {
                      Set<UserEventStatus>statuses = event.getStatuses();
                    int attending = 0;
                   
                    ArrayList<HashMap<String,Object>>statusList = new ArrayList<HashMap<String, Object>>(statuses.size());
                    for(UserEventStatus s : statuses){
                        HashMap<String,Object>tempMap = new HashMap<String, Object>();
                        if(s.getStatus().equals("attending")){
                            attending++;
                        }


                        Users su = s.getUser();
                        tempMap.put("id", su.getId());
                        tempMap.put("username", su.getUsername());
                        tempMap.put("status", s.getStatus());
                        statusList.add(tempMap);

                    }
                    List<TwitterEventStatus>twitterStatuses = tevsDao.findByEventId(event.getId());
                    if(twitterStatuses !=null && twitterStatuses.isEmpty() !=true){
                        for(TwitterEventStatus tst : twitterStatuses){
                             HashMap<String, Object> statusMap = new HashMap<String, Object>();
                            statusMap.put("username", "@"+tst.getUsername());
                            if(tst.getStatus().equals("attending")){
                                attending++;
                            }
                            statusMap.put("status", tst.getStatus());
                            statusList.add(statusMap);
                        }
                    }
                   
                    if(event.getEventend() > new Date().getTime() / 1000){
                        eventMap.put("expired", "no");
                    }else{
                        eventMap.put("expired", "yes");
                    }
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
                    locMap.put("locid", event.getLocid());
                    locMap.put("link", "http://eventlas.com/api/GetLocationById?locid=" + event.getLocid());
                    eventMap.put("location", locMap);
                    HashMap<String, Object> venueMap = new HashMap<String, Object>(2);

                    venueMap.put("name", event.getVenuename());
                    venueMap.put("address", event.getVenueaddress());
                    venueMap.put("id", event.getVenueid());

                    eventMap.put("venue", venueMap);
                    HashMap<String, Object> categoryMap = new HashMap<String, Object>(2);
                    EventCategorys category = event.getCategory();
                    categoryMap.put("name", category.getCategory());
                    categoryMap.put("id", category.getId());
                    categoryMap.put("icon", category.getIconid().getIcon());
                    eventMap.put("category", categoryMap);
                    Users u = event.getUser();
                    eventMap.put("username", u.getUsername());
                    eventMap.put("userid", u.getId());
                    HashMap<String, Object> visMap = new HashMap<String, Object>();
                    Visibility vis = event.getVisibility();
                    visMap.put("id", vis.getId());
                    visMap.put("visibility", vis.getPrivacylevel());
                    eventMap.put("visibility", visMap);

                }
            }
            kdao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(eventMap));


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
        }
    }
}

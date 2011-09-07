package api.controllers;

import api.dao.ApiKeysDAO;
import api.models.operation.Operation;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.InvitesDAO;
import api.dao.TwitterEventStatusDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Events;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author philip
 */
public class GetEventByIdController extends ApiAbController {

    public GetEventByIdController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
    }

    @Override
    public LinkedHashMap<String, Object> process() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>();

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        try {
            

            Integer eventId = new Integer(this.request.getParameter("eventid"));

            EventsDAO edao = factory.getEventDAO();
            ApiKeysDAO kdao = factory.getKeysDAO();
            edao.beginTransaction();
            EventFullDetail event = edao.getFullEventDetail(eventId);
            TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            HashMap<String,Object>eventMap = new HashMap<String, Object>();
            if(event != null){
             
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
                            statusMap.put("status", tst.getStatus());
                            if(tst.getStatus().equals("attending")){
                                attending++;
                            }
                            statusList.add(statusMap);
                        }
                    }
                    eventMap.put("statuses", statusList);
                    eventMap.put("attending", attending);
                    eventMap.put("title", event.getTitle());
                    if(event.getEventend() > new Date().getTime() / 1000){
                        eventMap.put("expired", "no");
                    }else{
                        eventMap.put("expired", "yes");
                    }
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

            if(event.getVisibility().getId() > 1){
                //private event
                String apikey = request.getParameter("apikey");
                if(apikey != null){
                    ApiKeys key = kdao.findKeyByApiKey(apikey);
                    Users keyUser = key.getUsers();
                    InvitesDAO invd = factory.getInvitesDAO();
                    List<Integer> invids = invd.getInvitedUserIdsByEventId(eventId);
                    Users eventUser = event.getUser();
                    if(invids.contains(keyUser.getId())||keyUser.equals(eventUser) ){
                    
                    
                        return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(eventMap));
                    }
                }else{
                    response.clear();
                    HibernateUtil.closeSession();
                    response.put(ApiResponse.ERROR_INFO_KEY,"to view a private event an apikey is required");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                }

            }


            response.clear();
            edao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(eventMap));
          }else{
              response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with the eventid it does not relate to an active event");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
          }
        } catch (NumberFormatException e) {
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with the eventid it could not be parsed into an int");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }


    }
}

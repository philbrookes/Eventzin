/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.LocationDAO;
import api.dao.TwitterEventStatusDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import api.responses.GetEventsByLocationResponse;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class GetEventsByLocationController extends ApiAbController {

    public GetEventsByLocationController(HttpServletRequest req) {

        super(req);
        required.add("longitude");
        required.add("latitude");

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
            Float longitude = new Float(this.request.getParameter("longitude"));
            Float latitude = new Float(this.request.getParameter("latitude"));
            String distanceValue = this.request.getParameter("distance");
            Integer distance;
            if(distanceValue == null)
                distance  = new Integer(10);
            else
                distance = new Integer(distanceValue);

            EventsDAO edao = this.factory.getEventDAO();
            LocationDAO ldao = this.factory.getLocationDAO();
            TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            edao.beginTransaction();

            List<EventFullDetail> eventList = edao.getEventsByLongAndLat(longitude, latitude, distance);
            //check to see if any events were returned
            if (eventList.isEmpty()) {
                return GetEventsByLocationResponse.getNoResultsResponse();
            } else {
                ArrayList responseList = new ArrayList();


                for (EventFullDetail event : eventList) {
                    HashMap<String, Object> eventMap = new HashMap<String, Object>();

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
                    responseList.add(eventMap);
                }

                //now have a list of events


                
                list = GetEventsByLocationResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(responseList));
                list.put("count", eventList.size());
                edao.commitTransaction();
                return list;
                
            }

        }catch(ObjectNotFoundException e){
             HibernateUtil.closeSession();
             response.clear();
             response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
             return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        }
        catch (HibernateException e) {
            HibernateUtil.closeSession();
            return GetEventsByLocationResponse.getFatalResponse();
        }



    }
}

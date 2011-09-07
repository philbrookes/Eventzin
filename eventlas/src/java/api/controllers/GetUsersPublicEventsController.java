/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.TwitterEventStatusDAO;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
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
public class GetUsersPublicEventsController extends ApiAbController {

    public GetUsersPublicEventsController(HttpServletRequest req) {
        super(req);
        this.optional.add("username");
        this.optional.add("userid");
        this.optional.add("startpos");
        this.optional.add("endpos");
    }

    @Override
    public LinkedHashMap<String, Object> process() {

        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        String username = request.getParameter("username");
        String uid = request.getParameter("userid");
        String startpos = request.getParameter("startpos");
        String endpos = request.getParameter("endpos");
        Integer userid = null;
        Integer startint = 0;
        Integer endint = 20;
        if (uid != null) {
            userid = new Integer(uid);
        }

        if (startpos != null && endpos != null) {
            startint = new Integer(startpos);
            endint = new Integer(endpos);
        }

        if (userid != null || username != null) {
            try {

                EventsDAO edao = factory.getEventDAO();
                UsersDAO udao = factory.getUserDAO();
                TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
                edao.beginTransaction();
                Users u = null;
                if (username != null) {
                    u = udao.findUserByUserName(username);
                } else {
                    u = udao.findByPrimaryKey(userid);
                }

                List<EventFullDetail> events = edao.getEventsByUser(u, startint, endint);
                ArrayList<HashMap<String, Object>> eventsRespose = new ArrayList(events.size());
                for (EventFullDetail event : events) {
                    Visibility vis = event.getVisibility();
                    if (vis.getId() < 2) {
                        HashMap<String, Object> eventMap = new HashMap<String, Object>();
                        int attending = 0;

                        ArrayList<HashMap<String, Object>> statusList = new ArrayList<HashMap<String, Object>>(10000);

                        Set<UserEventStatus> statuses = event.getStatuses();


                        for (UserEventStatus s : statuses) {
                            HashMap<String, Object> tempMap = new HashMap<String, Object>();
                            if (s.getStatus().equals("attending")) {
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
                        eventMap.put("username", u.getUsername());
                        eventMap.put("userid", u.getId());
                        HashMap<String, Object> visMap = new HashMap<String, Object>();

                        visMap.put("id", vis.getId());
                        visMap.put("visibility", vis.getPrivacylevel());
                        eventMap.put("visibility", visMap);
                        eventsRespose.add(eventMap);
                    }
                }



                edao.commitTransaction();

                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(eventsRespose));

            } catch (ObjectNotFoundException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
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
        } else {
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "you must set either userid or username to a valid value");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

    }
}

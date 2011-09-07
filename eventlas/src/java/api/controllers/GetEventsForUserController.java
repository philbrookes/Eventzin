package api.controllers;

import api.dao.HibernateUtil;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.models.ApiKeys;
import api.dao.ApiKeysDAO;
import api.dao.EventsDAO;
import api.dao.EventscommentDAO;
import api.dao.InvitesDAO;
import api.dao.TwitterEventStatusDAO;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Invites;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.UserFriends;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author craigbrookes
 */
public class GetEventsForUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetEventsForUserController(HttpServletRequest req) {
        super(req);
        optional.add("longitude");
        optional.add("latitude");
        optional.add("startpos");

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
            InvitesDAO invDAO = factory.getInvitesDAO();
           EventscommentDAO commen = factory.getEventsCommentDAO();
           TwitterEventStatusDAO tevsDao = factory.getTwitterEventStatusDAO();
            /**
            Add more Dao as needed
             **/
            ArrayList responseList = new ArrayList();
            String longi = request.getParameter("longitude");
            String lati = request.getParameter("latitude");
            Float longitude = null;
            Float latitude = null;
            if (longi != null && lati != null) {
                longitude = new Float(longi);
                latitude = new Float(lati);
            }

            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {

                Users user = key.getUsers();
                EventsDAO edao = factory.getEventDAO();
                Set<UserFriends> friends = user.getUsersfriends();
                Set<Invites> invitesToUser = user.getInviteses();

                
                ArrayList<Integer>ingnored = new ArrayList<Integer>();
                ingnored.add(user.getId());
                ArrayList<Integer> friendsIds = new ArrayList<Integer>();
                ArrayList<Integer> inviteIds = new ArrayList<Integer>();
                if(friends != null){
                 for (UserFriends f : friends) {
                        Integer fid = f.getFriendid();
                       friendsIds.add(fid);
                    //System.out.println("user id for friend = " + fid);
                  }
                }
                if(invitesToUser != null){
                    for (Invites invite : invitesToUser) {
                        inviteIds.add(invite.getId());
                    }
                }

                

                // need to get events the user has been invited to

                // also need to get events the users friends have added at public level
                // if long and lat set need to get by location also


                HashSet<EventFullDetail> personalEvents = new HashSet<EventFullDetail>();

                if(inviteIds.isEmpty() != true){
                    List<EventFullDetail> invitedEvents = edao.getEventsByInviteIds(inviteIds);
                    if (invitedEvents != null) {
                        personalEvents.addAll(invitedEvents);
                    }
                }

              

                List<EventFullDetail> friendsEvents = null;

                if (friendsIds.isEmpty() != true) {
                    if (longitude != null && latitude != null) {

                        friendsEvents = edao.getEventsByUserIdsAndLocation(friendsIds, longitude, latitude, 35, ingnored);

                    } else {
                        friendsEvents = edao.getPublicEventsByUserIds(friendsIds);
                    }

                    if (friendsEvents != null) {
                        for (EventFullDetail ev : friendsEvents) {
                            if (personalEvents.contains(ev) != true) {
                                personalEvents.add(ev);
                            }
                        }
                    }

                }

                for (EventFullDetail event : personalEvents) {
                    HashMap<String, Object> eventMap = new HashMap<String, Object>();
                    int attending = 0;
                    Integer count = commen.countCommentsForEvent(event);
                    eventMap.put("comments", count);
                    Set<Invites>eventinvs = event.getInvites();
                    for(Invites inv : eventinvs){
                        if(inv.getUsers().equals(user)){
                            eventMap.put("invited", "yes");
                            break;
                        }
                    }
                    
                    if(eventMap.containsKey("invited")!=true){
                        eventMap.put("invited", "no");
                    }



                    Set<UserEventStatus> statuses = event.getStatuses();

                    ArrayList<HashMap<String, Object>> statusList = new ArrayList<HashMap<String, Object>>(statuses.size());


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
                    Users u = event.getUser();
                    eventMap.put("username", u.getUsername());
                    eventMap.put("userid", u.getId());
                    HashMap<String, Object> visMap = new HashMap<String, Object>();
                    Visibility vis = event.getVisibility();
                    visMap.put("id", vis.getId());
                    visMap.put("visibility", vis.getPrivacylevel());
                    eventMap.put("visibility", visMap);
                    responseList.add(eventMap);
                }




            }
            kdao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(responseList));


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
        }
    }
}

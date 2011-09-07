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
import api.dao.InvitesDAO;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Invites;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class GetInvitesSentByUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetInvitesSentByUserController(HttpServletRequest req) {
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
            /**
            Add more Dao as needed
             **/
            InvitesDAO idao = factory.getInvitesDAO();
            ArrayList<HashMap<String, Object>> invitesList = new ArrayList<HashMap<String, Object>>();
            idao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            EventsDAO eventDao = factory.getEventDAO();


            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                Users user = key.getUsers();
                
                List<Invites> invites = idao.getCurrentInvitesSentByUser(user);
                System.out.println("CALLED GET CURRENT INVITES ");
                if (invites.isEmpty() != true) {
                    Iterator<Invites> iit = invites.iterator();
                    while (iit.hasNext()) {
                        Invites in = iit.next();
                        HashMap<String, Object> inviteMap = new HashMap<String, Object>();
                        EventFullDetail event = eventDao.getEventsByInvite(in);

                        System.out.println("event " + event.getTitle() + " and eventid= " + event.getId() + " date = " + event.getEventend() + " = currenst stamp = " + new Date().getTime() / 1000);

                        if (event != null && event.getEventend() > new Date().getTime() / 1000) {
                            System.out.println("event is current");
                            Users invitedUser = in.getUsers();
                            inviteMap.put("id", in.getId());
                            inviteMap.put("inviterid", in.getInviterid().getId());
                            inviteMap.put("status", in.getStatus());
                            HashMap<String, Object> eventMap = new HashMap<String, Object>();
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

                            inviteMap.put("event", eventMap);

                            inviteMap.put("inviter", user.getUsername());
                            inviteMap.put("invited", invitedUser.getUsername());
                            invitesList.add(inviteMap);
                        } 
                    }


                }

            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(invitesList));


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

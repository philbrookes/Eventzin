/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.HibernateUtil;
import api.dao.InvitesDAO;
import api.dao.ApiKeysDAO;
import api.dao.EventsDAO;
import api.dao.UserEventStatusDAO;
import api.helpers.ParameterHelper;
import api.helpers.Responses;
import api.responses.ApiResponse;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class GetInvitesForUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetInvitesForUserController(HttpServletRequest req) {
        super(req);
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
            InvitesDAO invDao = factory.getInvitesDAO();
            ApiKeysDAO kdap = factory.getKeysDAO();
            UserEventStatusDAO uesdao = factory.getUserEventStatusDAO();
            EventsDAO eventDao = factory.getEventDAO();
            invDao.beginTransaction();
            ApiKeys key = kdap.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) == true) {
                Users user = this.getUserFromKey(key);

                List<Invites> invites = invDao.getInvitesByUser(user);
                ArrayList<HashMap<String, Object>> inviteList = new ArrayList<HashMap<String, Object>>();

                Iterator<Invites> invIt = invites.iterator();
                while (invIt.hasNext()) {
                    Invites in = invIt.next();
                    HashMap<String, Object> inviteMap = new HashMap<String, Object>();
                    EventFullDetail event = eventDao.getEventsByInvite(in);

                    //System.out.println("event " + event.getTitle() + " and eventid= " + event.getId() + " date = " + event.getEventend() + " = currenst stamp = " + new Date().getTime() / 1000);

                    if (event != null && event.getEventend() > new Date().getTime() / 1000) {
                        System.out.println("event is current");
                        Users invitedUser = in.getUsers();
                        Users inviter = in.getInviterid();
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

                        inviteMap.put("inviter", inviter.getUsername());
                        inviteMap.put("invited", invitedUser.getUsername());
                        inviteList.add(inviteMap);
                    }
                }
                invDao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(inviteList));


            } else {
                return ApiResponse.getUnauthorizedResponse();
            }


        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.put("message", "a problem occurred and the invites could not be retrieved" + e.toString());
            response.put("code", new Integer(Responses.RES_FATAL).toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
    }
}

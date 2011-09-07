/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventCategorysDAO;
import api.helpers.ParameterHelper;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.IconsDAO;
import api.dao.ApiKeysDAO;
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import api.dao.VisibiltyDAO;
import api.responses.ApiResponse;
import com.models.EventCategorys;
import com.models.Events;
import com.models.Icons;
import com.models.ApiKeys;
import com.models.Location;
import com.models.Users;
import com.models.Venues;
import com.models.Visibility;
import java.text.ParseException;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author Craig and Debbie
 */
public class AddEventToLocationController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddEventToLocationController(HttpServletRequest req) {

        super(req);

        required.add("title");
        required.add("overview");
        required.add("longitude");
        required.add("latitude");
        required.add("eventstarts");
        required.add("eventends");
        required.add("visibility");
        required.add("categoryid");

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

        EventsDAO eventdao = factory.getEventDAO();
        Events event = new Events();
        eventdao.beginTransaction();

        //auth
        ApiKeysDAO kdao = factory.getKeysDAO();
        ApiKeys apikey = kdao.findKeyByApiKey(this.request.getParameter("apikey"));

        if (this.authenticateUser(apikey) == true) {

            try {
                VenuesDAO vdao = factory.getVenuesDAO();
                Venues venue = vdao.findByPrimaryKey(new Integer("4"));
                venue.toString();
                event.setVenues(venue);
                Users users = apikey.getUsers();
                users.toString();
                event.setUsers(users);
                EventCategorysDAO evcdao = factory.getEventCategoryDAO();
                EventCategorys eventCategory = evcdao.findByPrimaryKey(new Integer(this.request.getParameter("categoryid")));
                eventCategory.toString();
                Icons icon = eventCategory.getIconid();
                event.setIcons(icon);
                event.setCategory(eventCategory);
                LocationDAO ldao = factory.getLocationDAO();
                Location loc = ldao.createLocation(new Float(this.request.getParameter("longitude")), new Float(this.request.getParameter("latitude")));
                loc.toString();
                event.setLocation(loc);
                event.setEventdate(new Long(this.request.getParameter("eventstarts")));
                event.setEventend(new Long(this.request.getParameter("eventends")));
                VisibiltyDAO visdao = factory.getVisibilityDAO();
                Visibility vis = visdao.findByPrimaryKey(new Integer(this.request.getParameter("visibility")));
                vis.toString();
                event.setVisibility(vis);
                event.setSummary(this.request.getParameter("overview"));
                event.setTitle(this.request.getParameter("title"));
                eventdao.save(event);
                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "event added");
                response.put("eventid", event.getId());
                eventdao.commitTransaction();

                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            } catch (ObjectNotFoundException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"the event could not be created "+e.getEntityName() );
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }  catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "the event could not be saved ");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }


        } else {
            response.clear();
            return ApiResponse.getUnauthorizedResponse();
        }



    }
}

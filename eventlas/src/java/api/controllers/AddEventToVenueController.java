/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.helpers.ParameterHelper;
import api.dao.DAOFactory;
import api.dao.EventCategorysDAO;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.dao.UserEventStatusDAO;
import api.dao.VenuesDAO;
import api.dao.VisibiltyDAO;
import api.responses.ApiResponse;
import com.models.EventCategorys;
import com.models.Events;
import com.models.Icons;
import com.models.ApiKeys;
import com.models.Location;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Venues;
import com.models.Visibility;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class AddEventToVenueController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddEventToVenueController(HttpServletRequest req) {
        super(req);

        factory = DAOFactory.getFactory();
        required.add("title");
        required.add("venueid");
        required.add("eventstarts");
        optional.add("eventends");
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

        EventsDAO edao = factory.getEventDAO();
        edao.beginTransaction();

        Events event = new Events();

        ApiKeysDAO kdao = factory.getKeysDAO();
        UserEventStatusDAO uesdao = factory.getUserEventStatusDAO();
        ApiKeys apikey = kdao.findKeyByApiKey(this.request.getParameter("apikey"));

        if (this.authenticateUser(apikey) == true) {

            try {
                Users user = apikey.getUsers();
                user.toString();
                event.setUsers(user);
                VenuesDAO vdao = factory.getVenuesDAO();
                Venues venue = vdao.findByPrimaryKey(new Integer(this.request.getParameter("venueid")));
                venue.toString();
                event.setVenues(venue);
                VisibiltyDAO viddao = factory.getVisibilityDAO();
                Visibility visibility = viddao.findByPrimaryKey(new Integer(this.request.getParameter("visibility")));
                visibility.toString();
                event.setVisibility(visibility);
                EventCategorysDAO evcdao = factory.getEventCategoryDAO();
                EventCategorys eventCategory = evcdao.findByPrimaryKey(new Integer(this.request.getParameter("categoryid")));
                eventCategory.toString();
                Icons icon = eventCategory.getIconid();
                event.setIcons(icon);
                event.setCategory(eventCategory);
                
                event.setEventdate(new Long(this.request.getParameter("eventstarts")));
                Long eventends;
                if(request.getParameter("eventends")!=null){
                   eventends = new Long(request.getParameter("eventends"));
                }else{
                    eventends = new Long(request.getParameter("eventstarts")) + 10800; //3 hrs
                }
                if(eventends !=null){
                 event.setEventend(eventends);
                }else{
                    System.out.println("no event ends");
                }
                Location loc = venue.getLocation();
                loc.toString();
                event.setLocation(loc);
                 String eventOverview = (request.getParameter("overview")!=null)?request.getParameter("overview"):"";
                if (eventOverview != null && eventOverview.length() > 512) {
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "the overview must be no more than 500 characters");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }
               
                event.setSummary(eventOverview);
                event.setTitle(this.request.getParameter("title"));
                edao.save(event);
                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "event added to venue");
                response.put("venueid", event.getVenues().getId().toString());
                response.put("id", event.getId().toString());
                UserEventStatus status = new UserEventStatus();
                status.setEvent(event);
                status.setStatus("attending");
                status.setUser(user);
                uesdao.save(status);
                edao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));

            } catch (ObjectNotFoundException e) {
                api.logger.Logger.error(AddEventToLocationController.class.getName(), e.getMessage());
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }  catch (HibernateException e) {
                api.logger.Logger.error(AddEventToLocationController.class.getName(), e.getMessage());
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "problem with adding the event");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }
            catch(Exception e){
                 HibernateUtil.closeSession();
                 
                e.printStackTrace();
                 response.put(ApiResponse.ERROR_INFO_KEY, e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }

        } else {
            return ApiResponse.getUnauthorizedResponse();
        }


    }
}

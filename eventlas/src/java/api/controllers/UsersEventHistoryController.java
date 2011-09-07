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
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Users;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class UsersEventHistoryController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public UsersEventHistoryController(HttpServletRequest req) {
        super(req);
        required.add("from");
        optional.add("to");
        
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
            ArrayList<HashMap<String,Object>>responseList = null;
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                Long from = new Long(request.getParameter("from"));
                Date dfrom = new Date(from * 1000);//millisecs
                Date todate = null;
                if(request.getParameter("to") != null){
                    Long to = new Long(request.getParameter("to"));
                    todate = new Date(to * 1000); 
                }else{
                    todate = new Date();
                }
                
                
                List<EventFullDetail>events = edao.getEventsByDateRangeAndUser(dfrom, todate, key.getUsers()); 
                responseList = new ArrayList<HashMap<String, Object>>(events.size());
                for(EventFullDetail event : events){
                     HashMap<String, Object> eventMap = new HashMap<String, Object>();
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

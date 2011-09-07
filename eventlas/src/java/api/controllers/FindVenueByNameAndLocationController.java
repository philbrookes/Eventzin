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
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import com.models.Icons;
import com.models.Location;
import com.models.Venues;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class FindVenueByNameAndLocationController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public FindVenueByNameAndLocationController(HttpServletRequest req) {
        super(req);
        required.add("venue");
        required.add("longitude");
        required.add("latitude");
        optional.add("distance");
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
            String venuename = request.getParameter("venue");
            Float longitude = new Float(request.getParameter("longitude"));
            Float latitude = new Float(request.getParameter("latitude"));
            ApiKeysDAO kdao = factory.getKeysDAO();
            LocationDAO locdao = factory.getLocationDAO();
            VenuesDAO vendao = factory.getVenuesDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                int distance = 75;
                if(request.getParameter("distance")!=null){
                     distance = new Integer(request.getParameter("distance")).intValue();
                }
                List<Location> locs = locdao.findNearbyLocations(longitude, latitude, distance);
                if (locs.isEmpty()) {
                    HibernateUtil.closeSession();
                    return ApiResponse.getSuccessResponse();
                } else {
                    List<Venues> vens = vendao.getVenuesWithLocIdsAndNameLike(locs, venuename);
                    if (vens.isEmpty() != true) {
                        for (Venues avit : vens) {
                            HashMap<String, Object> venMap = new HashMap<String, Object>(12);
                            venMap.put("name", avit.getName());
                            HashMap<String, Object> locMap = new HashMap<String, Object>(3);
                            Location loc = avit.getLocation();
                            locMap.put("latitude", loc.getLatitude());
                            locMap.put("longitude", loc.getLongitude());
                            locMap.put("id", loc.getId());
                            venMap.put("address", avit.getAddress());
                            venMap.put("location", locMap);
                            venMap.put("summary", avit.getSummary());
                            HashMap<String, Object> iconMap = new HashMap<String, Object>();
                            Icons icon = avit.getIcons();
                            iconMap.put("icon", icon.getIcon());
                            iconMap.put("id", icon.getId());
                            venMap.put("icon", iconMap);
                            venMap.put("id", avit.getId());
                            venMap.put("rating", avit.getRating());
                            apiResponse.add(venMap);
                        }
                    }

                }
                /**
                LOGIC CODE HERE
                 **/
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(apiResponse));


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

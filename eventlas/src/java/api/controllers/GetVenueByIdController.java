/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.HibernateUtil;
import api.dao.VenuesDAO;
import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.responses.ApiResponse;
import com.models.Icons;
import com.models.Location;
import com.models.Venues;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author phil
 */
public class GetVenueByIdController extends ApiAbController {

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
            Integer venueId = new Integer(this.request.getParameter("venueid"));

            VenuesDAO vdao = factory.getVenuesDAO();
            vdao.beginTransaction();
            Venues venue = vdao.findByPrimaryKey(venueId);
            venue.toString();
            HashMap<String,Object>venueMap = new HashMap<String, Object>();

                venueMap.put("name", venue.getName());
                HashMap<String, Object> locMap = new HashMap<String, Object>(3);
                Location loc = venue.getLocation();
                locMap.put("latitude", loc.getLatitude());
                locMap.put("longitude", loc.getLongitude());
                locMap.put("id", loc.getId());
                venueMap.put("address", venue.getAddress());
                venueMap.put("location", locMap);
                venueMap.put("summary", venue.getSummary());
                HashMap<String,Object>iconMap = new HashMap<String, Object>();
                Icons icon = venue.getIcons();
                iconMap.put("icon", icon.getIcon());
                iconMap.put("id", icon.getId());
                venueMap.put("icon", iconMap);
                venueMap.put("id", venue.getId());
                venueMap.put("rating", venue.getRating());
            vdao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(venueMap));

        } catch (NumberFormatException e) {
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with the eventid it could not be parsed into an int");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (org.hibernate.ObjectNotFoundException e) {
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

    public GetVenueByIdController(HttpServletRequest req) {
        super(req);
        required.add("venueid");
    }
}

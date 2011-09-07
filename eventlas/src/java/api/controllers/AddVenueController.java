/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.HibernateUtil;
import api.helpers.ParameterHelper;
import api.dao.IconsDAO;
import api.dao.ApiKeysDAO;
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import api.responses.ApiResponse;
import com.models.Icons;
import com.models.ApiKeys;
import com.models.Location;
import com.models.Venues;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class AddVenueController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddVenueController(HttpServletRequest req) {
        super(req);
        required.add("name");
        required.add("address");
        required.add("overview");
        required.add("iconid");
        required.add("longitude");
        required.add("latitude");

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



        VenuesDAO vdao = this.getFactory().getVenuesDAO();
        vdao.beginTransaction();
        ApiKeysDAO kdao = getFactory().getKeysDAO();
        ApiKeys key = kdao.findKeyByApiKey(apiKey);
       if (authenticateUser(key) == true) {

            try {
                Venues venue = new Venues();
                LocationDAO ldao = getFactory().getLocationDAO();
                Location loc = ldao.createLocation(new Float(this.request.getParameter("longitude")), new Float(this.request.getParameter("latitude")));
                loc.toString();
                venue.setLocation(loc);
                IconsDAO icdao = getFactory().getIconsDAO();
                Icons icon = icdao.findByPrimaryKey(new Integer(this.request.getParameter("iconid")));
                icon.toString();
                venue.setIcons(icon);
                venue.setAddress(this.request.getParameter("address"));
                venue.setName(this.request.getParameter("name"));
                venue.setRating(new Integer(0));
                if (this.request.getParameter("overview").length() > 256) {
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "the overview was too long pls keep it to 256 characters or less");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }
                venue.setSummary(this.request.getParameter("overview"));
                vdao.save(venue);
                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "venue added successfully");
                response.put("venueid", venue.getId().toString());
                vdao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
               
            } catch (ObjectNotFoundException e) {
                 HibernateUtil.closeSession();
                 response.clear();
                 response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
                 return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"there was a problem saving the venue "+ e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }

        } else {
          return ApiResponse.getUnauthorizedResponse();
        }




    }
}

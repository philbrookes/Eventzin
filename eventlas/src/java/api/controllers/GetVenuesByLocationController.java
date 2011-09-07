/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.HibernateUtil;
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import api.responses.GetVenuesByLocationResponse;
import com.apis.external.googleplaces.GooglePlaces;
import com.models.Icons;
import com.models.Location;
import com.models.Venues;
import java.util.ArrayList;
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
public class GetVenuesByLocationController extends ApiAbController{

    public GetVenuesByLocationController(HttpServletRequest req) {
        super(req);
        required.add("longitude");
        required.add("latitude");
        optional.add("distance");
    }


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
        try{

            LocationDAO ldao = factory.getLocationDAO();
            Float longitude = new Float(request.getParameter("longitude"));
            Float latitude = new Float(request.getParameter("latitude"));
            String distance = request.getParameter("distance");
            Integer dist = null;
            List<Venues> venues = null;
            /*
             * @todo need to stop it hitting google every time
             */
            GooglePlaces gp = new GooglePlaces(latitude, longitude, new Integer(300));
            gp.getPlacesConvertToVenuesAndSave();
            if(distance !=null){
                dist = new Integer(distance);
            }else{
                dist = new Integer(15);
            }
            ldao.beginTransaction();
            List<Location> locations = ldao.findNearbyLocations(longitude, latitude,dist);
            VenuesDAO vdao = factory.getVenuesDAO();
            venues = vdao.getVenuesWithLocIds(locations);
            
            Iterator<Venues>vit = venues.iterator();

            if(venues.isEmpty()){
                HibernateUtil.closeSession();
                return GetVenuesByLocationResponse.getNoVenuesResponse();
            }
            ArrayList responseList = new  ArrayList();

            while(vit.hasNext()){
                Venues avit = vit.next();
                HashMap<String,Object>venueMap = new HashMap<String, Object>();

                venueMap.put("name", avit.getName());
                HashMap<String, Object> locMap = new HashMap<String, Object>(3);
                Location loc = avit.getLocation();
                locMap.put("latitude", loc.getLatitude());
                locMap.put("longitude", loc.getLongitude());
                locMap.put("id", loc.getId());
                venueMap.put("address", avit.getAddress());
                venueMap.put("location", locMap);
                venueMap.put("summary", avit.getSummary());
                HashMap<String,Object>iconMap = new HashMap<String, Object>();
                Icons icon = avit.getIcons();
                iconMap.put("icon", icon.getIcon());
                iconMap.put("id", icon.getId());
                venueMap.put("icon", iconMap);
                venueMap.put("id", avit.getId());
                venueMap.put("rating", avit.getRating());
              //venueMap.put("venuecommentsid", avit.)

                responseList.add(venueMap);
            }

            ldao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(responseList));

        }catch(ObjectNotFoundException e){
            e.printStackTrace();
             HibernateUtil.closeSession();
             response.clear();
             response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
             return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
        catch (HibernateException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            return ApiResponse.getFatalResponse();
        }
      }
}

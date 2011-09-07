/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.HibernateUtil;
import api.dao.LocationDAO;
import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.responses.ApiResponse;
import com.models.Location;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class GetLocationByIdController extends ApiAbController {

    public GetLocationByIdController(HttpServletRequest req) {
        super(req);
        required.add("locid");
    }



    @Override
    public LinkedHashMap<String,Object> process() {

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() != true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));


        LocationDAO ldao = factory.getLocationDAO();
        ldao.beginTransaction();
        Integer locid = new Integer(this.request.getParameter("locid"));
        try{
            Location location = ldao.findByPrimaryKey(locid);
           HashMap<String,Object>locationMap = new HashMap<String, Object>(3);
           locationMap.put("longitude", location.getLongitude());
           locationMap.put("latitude", location.getLatitude());
           locationMap.put("id", location.getId());
           ldao.commitTransaction();
           return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(locationMap));
        }catch(ObjectNotFoundException e){
            return ApiResponse.getFatalResponse();
        }catch(HibernateException e){
            HibernateUtil.closeSession();
            return ApiResponse.getFatalResponse();
        }




    }



}

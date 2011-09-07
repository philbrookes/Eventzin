/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.dao.HibernateUtil;
import api.dao.LocationDAO;
import api.responses.ApiResponse;
import com.models.Location;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class AddLocationController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddLocationController(HttpServletRequest req) {
        super(req);
        this.required.add("latitude");
        this.required.add("longitude");


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
        ////////////////

        //everything ok so try to make an entry
        try {
            
            LocationDAO locDao = factory.getLocationDAO();
            locDao.beginTransaction();
            Location location = locDao.createLocation(new Float(this.request.getParameter("longitude")), new Float(this.request.getParameter("latitude")));


            if (location != null) {
                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "location controller");
                response.put("locid", location.getId());
                locDao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            } else {
                HibernateUtil.closeSession();
                return ApiResponse.getFatalResponse();
            }

        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            return ApiResponse.getFatalResponse();
        }
    }
}

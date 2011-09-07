/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.models.operation.Operation;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.helpers.EventResponses;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Events;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class GetEventsByVenueController extends ApiAbController {

    public GetEventsByVenueController(HttpServletRequest req) {
        super(req);
        required.add("venueid");
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

              EventsDAO retevents = factory.getEventDAO();
            retevents.beginTransaction();
        try {
          
            ArrayList<Events> eventslist = (ArrayList) retevents.getEventsByVenueId(new Integer(this.request.getParameter("venueid")));
            

           list = ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(EventResponses.makeEventsHashmap(new HashSet<Events>(eventslist))));

           retevents.commitTransaction();

           return list;

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
}

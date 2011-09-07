/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.models.operation.Operation;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.helpers.EventResponses;
import api.responses.ApiResponse;
import com.models.Events;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class GetEventsByRatingController extends ApiAbController {

    public GetEventsByRatingController(HttpServletRequest req) {
        super(req);

    }

    @Override
    public LinkedHashMap<String, Object> process() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>();



        ArrayList ret = new ArrayList();
        Integer lower, higher;
        if (this.request.getParameter("lower") == null) {
            lower = 0;
        } else {
            lower = new Integer(this.request.getParameter("lower"));
        }
        if (this.request.getParameter("higher") == null) {
            higher = 5;
        } else {
            higher = new Integer(this.request.getParameter("higher"));
        }
        try {

            EventsDAO retevents = factory.getEventDAO();
            retevents.beginTransaction();
            ArrayList<Events> eventslist = (ArrayList) retevents.getEventsByRating(lower, higher);
            retevents.commitTransaction();

            response.clear();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(EventResponses.makeEventsHashmap(new HashSet<Events>(eventslist))));
        } catch (NumberFormatException e) {
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with the argument it could not be parsed into an int");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (ObjectNotFoundException e) {
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

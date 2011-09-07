/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.models.operation.Operation;
import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Events;
import com.models.Users;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author philip
 */
public class GetUserController extends ApiAbController {

    public GetUserController(HttpServletRequest req) {
        super(req);
        required.add("userid");
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
        try {

            UsersDAO udao = factory.getUserDAO();
            udao.beginTransaction();
            Integer userid = new Integer(this.request.getParameter("userid"));
            Users u = udao.findByPrimaryKey(userid);
            u.toString();
            response.clear();
            response.put("username", u.getUsername());
            response.put("id", u.getId());
            Set<Events> userevents = u.getEventses();
            response.put("eventsadded",userevents.size());
            ArrayList<Integer> eventids = new ArrayList<Integer>(userevents.size());
            Iterator<Events> evit = userevents.iterator();
            while(evit.hasNext()){
                eventids.add(evit.next().getId());
            }
            response.put("eventids", eventids);
            //TODO change fans and attending to work properly
            response.put("fans", "10");
            response.put("attending", "3");
            udao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
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

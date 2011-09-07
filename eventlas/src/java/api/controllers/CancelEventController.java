/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.models.operation.Operation;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.dao.InvitesDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Events;
import com.models.ApiKeys;
import com.models.Invites;
import com.models.Users;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class CancelEventController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public CancelEventController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
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
        ApiKeysDAO keyd = factory.getKeysDAO();
        EventsDAO edao = factory.getEventDAO();
        InvitesDAO invDao = factory.getInvitesDAO();
        edao.beginTransaction();
        ApiKeys key = keyd.findKeyByApiKey(this.request.getParameter("apikey"));
        if (this.authenticateUser(key) == true) {
            try {
                Events event = edao.findByPrimaryKey(new Integer(this.request.getParameter("eventid")));
                Users user = event.getUsers();
                Users requestingUser = key.getUsers();
                if (user.equals(requestingUser)) {
                    //TODO deal with invites when deleting event

                    /**
                     * HERE WILL BE A CALL TO event.getInvitees and the list
                     * could be sent to a messaging class to send out a message to them to let them know
                     */

                    Set<Invites> invs = event.getInvites();
                    edao.delete(event);

                    for(Invites in : invs){
                        invDao.delete(in);
                    }

                    response.clear();
                    response.put(ApiResponse.GENERAL_INFO_KEY, "event has been cancelled and removed invitees will be notified");

                    edao.commitTransaction();
                    return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
                } else {


                    return ApiResponse.getNoPermissionsResponse();
                }

            } catch (NumberFormatException e) {
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "an argument that is required to be an integer could not be parsed");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

            } catch (ObjectNotFoundException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

            } catch (HibernateException e) {
                HibernateUtil.rollbackTransaction();
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred and the the event has not been cancelled");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

            }

        } else {
            return ApiResponse.getUnauthorizedResponse();
        }


    }
}

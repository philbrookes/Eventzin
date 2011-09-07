/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.InvitesDAO;
import api.dao.ApiKeysDAO;
import api.dao.UserEventStatusDAO;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Events;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.Users;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class AddInviteForUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddInviteForUserController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
        required.add("userid");
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


        try {

            ApiKeysDAO kdao = factory.getKeysDAO();
            InvitesDAO idao = factory.getInvitesDAO();
            EventsDAO edao = factory.getEventDAO();
            UsersDAO udao = factory.getUserDAO();
            

            Integer eventid = new Integer(request.getParameter("eventid"));
            Integer userid = new Integer(request.getParameter("userid"));

            Invites invite = new Invites();

            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(this.apiKey);
            Users invited = udao.findByPrimaryKey(userid);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getUnauthorizedResponse();
            } else {
                Users apiUser = key.getUsers();
                Events event = edao.findByPrimaryKey(eventid);
                invite.setEvents(event);
                invite.setUsers(invited);
                invite.setInviterid(apiUser);
                invite.setInvitehash(eventid.toString() + userid.toString());
                invite.setStatus(Invites.PENDING_INVITE);

                if (event.getVisibility().getId() > 1) {
                    if (apiUser.equals(event.getUsers())) {

                        idao.save(invite);


                    } else {
                        response.clear();
                        response.put(new Integer(ApiResponse.NO_PERMISSIONS).toString(), ApiResponse.NO_PERMISIONS_MESSAGE);
                        return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                    }

                } else {
                    idao.save(invite);

                }

                idao.commitTransaction();
                response.clear();
                response.put("inviteid", invite.getId());
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            }





        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "an invite for this event has already been sent to this user by you");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "The ivite was not added. A problem occurred");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

    }
}

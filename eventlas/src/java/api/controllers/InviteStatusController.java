/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.InvitesDAO;
import api.dao.ApiKeysDAO;
import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.responses.ApiResponse;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Events;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.Users;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class InviteStatusController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    ArrayList<String> statuses = new ArrayList<String>(Arrays.asList("accept", "decline", "ignore"));

    public InviteStatusController(HttpServletRequest req) {
        super(req);
        required.add("status");
        required.add("inviteid");
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

        InvitesDAO idao = factory.getInvitesDAO();
        idao.beginTransaction();
        ApiKeysDAO kdao = factory.getKeysDAO();
        ApiKeys key = kdao.findKeyByApiKey(this.request.getParameter("apikey"));


        if (authenticateUser(key) == true) {
            try {
                //load up the invite and check the user is actually invited
                Invites invite = idao.findByPrimaryKey(new Integer(this.request.getParameter("inviteid")));
                Users invitedUser = invite.getUsers();
                Users inviter = key.getUsers();
                if (invitedUser.getId() == inviter.getId()) {
                    String email = inviter.getEmail();
                    Integer eventid = invite.getEvents().getId();
                    String username = invitedUser.getUsername();
                    EventsDAO edao = factory.getEventDAO();
                    Events event = edao.findByPrimaryKey(eventid);
                    event.toString();
                    //tcorrect user

                    if (statuses.contains(this.request.getParameter("status"))) {
                        invite.setStatus(this.request.getParameter("status"));
                        idao.save(invite);

                        //send message to inviter letting them know their invite has been accepted


                        Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                        message.setSubject("RSVP From "+ invitedUser.getUsername()+ "From Eventzin");
                        message.setContentFromTemplate("invite");
                        HashMap<String, String> replace = new HashMap<String, String>();
                        replace.put("#user#", invitedUser.getUsername());
                        replace.put("#eventname#", event.getTitle());
                        replace.put("#eventdate#", event.getEventdate().toString());
                        replace.put("#location#", event.getVenues().getName());
                        replace.put("#eventtype#", "decided to " + this.request.getParameter("status") + " your invite ");
                        message.replaceTagsInContent(replace);
                        message.setTo(email);

                        idao.commitTransaction();

                        message.send();
                        message = null;



                        response.clear();
                        response.put(ApiResponse.GENERAL_INFO_KEY, "invite status updated");
                        return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
                    } else {
                        HibernateUtil.closeSession();
                        response.clear();
                        response.put(ApiResponse.ERROR_INFO_KEY, "status must be one of: pending, accept, ignore, decline ");
                        return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                    }
                } else {
                    HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "you do not appear to have an invite to this event");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }

            } catch (MessagingException e) {
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "you do not appear to have an invite to this event");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (ObjectNotFoundException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "problem with adding the event");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }




        } else {
            return ApiResponse.getUnauthorizedResponse();
        }
    }
}

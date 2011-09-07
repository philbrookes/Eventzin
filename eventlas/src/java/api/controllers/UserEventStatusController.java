/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.dao.InvitesDAO;
import api.dao.MobilekeysDAO;
import api.dao.UserEventStatusDAO;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Events;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.MobileKeys;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class UserEventStatusController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public static ArrayList<String> statuses = new ArrayList<String>(Arrays.asList("ingnored", "attending", "unattended", "declined", "maybe"));

    public UserEventStatusController(HttpServletRequest req) {
        super(req);
       
        required.add("eventid");
        required.add("status");
    }

    @Override
    public LinkedHashMap<String, Object> process() {

        Boolean save = true;

        response = ParameterHelper.checkForNeededValues(requestParams, required);

        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        try {
            EventsDAO edao = factory.getEventDAO();
            UsersDAO udao = factory.getUserDAO();
            UserEventStatusDAO uesdao = factory.getUserEventStatusDAO();
            ApiKeysDAO kdao = factory.getKeysDAO();
            InvitesDAO invDao = factory.getInvitesDAO();
            MobilekeysDAO mkeyDAO = factory.getTempMobileKeysDAO();
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            Events event = null;
            Users user = null;
            Users eventUser = null;
            Users inviter = null;
            UserEventStatus eventStatus = null;
            String eventid = request.getParameter("eventid");
            String userid = request.getParameter("userid");
            String status = request.getParameter("status");
            if (statuses.contains(status) != true) {
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "the status is not a reconginesed status it must be ignored, unattended, attending or declined");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }
            if (this.authenticateUser(key) == true) {
                System.out.println("user authed");
                event = edao.findByPrimaryKey(new Integer(eventid));
                user = key.getUsers();
                eventStatus = uesdao.getUserEventStatusByEventAndUser(event, user);

                if (eventStatus != null) {
                    save = false;
                } else {
                    eventStatus = new UserEventStatus();
                    save = true;
                }
                System.out.println("current timestamp = "+ParameterHelper.getUnixTimeStamp()+" event end time = "+ event.getEventend()+" title="+event.getTitle());
                if (event.getEventend() < ParameterHelper.getUnixTimeStamp()) {
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "the event is in the past");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }

                if (event.getVisibility().getId() > 1) {
                    eventUser = event.getUsers();
                    //if it happens to be the event owner
                    if (eventUser.equals(user)) {

                        eventStatus.setEvent(event);
                        eventStatus.setUser(user);
                        eventStatus.setStatus(status);
                        

                    } else {
                        //check the event invites for an invite beloning to the current user
                        Set<Invites> invites = event.getInvites();
                        Iterator<Invites> inviterate = invites.iterator();
                        ArrayList<String> eventInviters = new ArrayList<String>();

                        while (inviterate.hasNext()) {
                            Invites inv = inviterate.next();
                            if (inv.getUsers().equals(user)) {
                               
                                inviter = inv.getInviterid();
                                System.out.println("will send email to "+ inviter.getEmail());
                                eventInviters.add(inviter.getEmail());
                                eventStatus.setEvent(event);
                                eventStatus.setUser(user);
                                eventStatus.setStatus(status);
                                //update invites status as well
                                inv.setStatus(status);
                                invDao.save(inv);
                                // send out a push notification to the inviter
                                MobileKeys m = mkeyDAO.getByMobileKey(inviter.getMobileKey());
                                if(m !=null && m.getDevicetype().equals("ios") && m.getDeviceToken() !=null){
                                    pushHelper.sendPushNotification(m.getDeviceToken(), user.getUsername() +" Has "+status+" your invite for "+ event.getTitle());
                                }
                            }
                        }

                        //send mail to the inviter letting them know that their invite has been
                        //responded to
                        if (status.equals("attending") && eventInviters.isEmpty() != true || status.equals("declined") && eventInviters.isEmpty() != true) {
                            Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                            message.setSubject("RSVP From " + user.getUsername()+ " For "+event.getTitle() );
                            message.setContentFromTemplate("rsvp");
                            HashMap<String, String> replace = new HashMap<String, String>();
                            replace.put("#user#", user.getUsername());
                            replace.put("#eventname#", event.getTitle());
                            if (status.equals("rejected")) {
                                replace.put("#status#", "Not Attending");
                            } else {
                                replace.put("#status#", status);
                            }
                            message.replaceTagsInContent(replace);
                            message.setMultipleRecipients(eventInviters);
                            message.send();
                        }

                    }

                } else {
                    //Todo tidy and make better UserEventStatusController



                    eventStatus.setEvent(event);
                    eventStatus.setUser(user);
                    eventStatus.setStatus(status);
                    Set<Invites> invites = event.getInvites();
                    Iterator<Invites> inviteratr = invites.iterator();
                    ArrayList<String> emailAddresses = new ArrayList<String>();
                    while (inviteratr.hasNext()) {
                        
                        Invites anInvite = inviteratr.next();
                        if (anInvite.getUsers().equals(user)) {
                         System.out.println("an invite for the user has been found");   
                        Users anInviteUser = anInvite.getInviterid();
                        emailAddresses.add(anInviteUser.getEmail());
                        anInvite.setStatus(status);
                        String notMessage = "";
                        if(status.equals("attending")){
                            notMessage = user.getUsername()+" is attending "+ event.getTitle();
                        }else if(status.equals("declined")){
                            notMessage = user.getUsername()+" has declined your invite for "+ event.getTitle();
                        }
                        invDao.save(anInvite);
                         MobileKeys m = mkeyDAO.getByMobileKey(anInviteUser.getMobileKey());
                                if(m !=null && m.getDevicetype().equals("ios") && m.getDeviceToken() !=null){
                                    pushHelper.sendPushNotification(m.getDeviceToken(), notMessage);
                                }
                        }
                    }

                    if (status.equals("attending")  && emailAddresses.isEmpty() != true || status.equals("declined") && emailAddresses.isEmpty() != true) {

                        Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                        message.setSubject("RSVP From " + user.getUsername() + " For "+ event.getTitle());
                        message.setContentFromTemplate("rsvp");
                        HashMap<String, String> replace = new HashMap<String, String>();
                        replace.put("#user#", user.getUsername());
                        replace.put("#eventname#", event.getTitle());
                        if (status.equals("rejected")) {
                            replace.put("#status#", "Not Attending");
                        } else {
                            replace.put("#status#", status);
                        }

                        message.replaceTagsInContent(replace);


                        message.setMultipleRecipients(emailAddresses);
                        message.send();
                    }

                }

            } else {
                return ApiResponse.getUnauthorizedResponse();
            }
            if (eventStatus == null) {
                return ApiResponse.getNoPermissionsResponse();
            }

            if (save == true) {
                uesdao.save(eventStatus);
            } else {
                uesdao.update(eventStatus);
            }

            
            response.clear();
            response.put("status", eventStatus.getStatus());
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));

        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem adding the users event status " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem saving adding the users status " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
    }
}

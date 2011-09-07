/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.helpers.ParameterHelper;
import api.helpers.Responses;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.InvitesDAO;
import api.dao.ApiKeysDAO;
import api.dao.MobilekeysDAO;
import api.dao.TempInvitesDAO;
import api.dao.UserEventStatusDAO;
import api.dao.UsersDAO;
import api.helpers.SiteHelper;
import api.logger.Logger;
import api.responses.ApiResponse;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Events;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.Location;
import com.models.MobileKeys;
import com.models.TempInvite;
import com.models.UserEventStatus;
import com.models.Users;
import com.models.Venues;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class AddInvitesController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddInvitesController(HttpServletRequest req) {
        super(req);
        //add required parameteres
        required.add("eventid");




    }
    private ArrayList<String> userids = null;
    private ArrayList<String> groupids = null;
    private ArrayList<String> emails = null;
    private ArrayList<String> phones = null;
    private ArrayList<String> devicesToNotify = new ArrayList<String>();
    private Integer eventid = null;
    private ArrayList<String> sentTo = new ArrayList<String>();
    private ArrayList<HashMap<String,Object>> toSend = new ArrayList<HashMap<String,Object>>();
    private Boolean is_public = true;

    @Override
    public LinkedHashMap<String, Object> process() {
        /**
         * check all required arguments are present
         */
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
        /**
         * set up the class Instance vars
         */
        if (this.request.getParameter("userids[]") != null) {
            userids = new ArrayList<String>(Arrays.asList(this.request.getParameterValues("userids[]")));
        }
        if (this.request.getParameter("groupids[]") != null) {
            groupids = new ArrayList<String>(Arrays.asList(this.request.getParameterValues("groupids[]")));
        }
        if (this.request.getParameter("emails[]") != null) {
            emails = new ArrayList<String>(Arrays.asList(this.request.getParameterValues("emails[]")));
        }
        if (this.request.getParameter("phones[]") != null) {
            phones = new ArrayList<String>(Arrays.asList(this.request.getParameterValues("phones[]")));
        }
        if(this.request.getParameter("idslist")!=null){
            ArrayList<String> ids = new ArrayList<String>(Arrays.asList(this.request.getParameter("idslist").split(",")));
            if(userids !=null){
                userids.addAll(ids);
            }
            else{
                userids = ids;
            }
            
        }


        this.eventid = new Integer(this.request.getParameter("eventid"));
        this.apiKey = this.request.getParameter("apikey");
       
        try {
            /**
             * start db transcation and get the keys object that
             * is the api key
             */
            System.out.println("the query string = "+ request.getQueryString());
            
            UsersDAO udao = factory.getUserDAO();
            ApiKeysDAO kd = factory.getKeysDAO();
            EventsDAO edao = factory.getEventDAO();
            UserEventStatusDAO uesDAO = factory.getUserEventStatusDAO();
            InvitesDAO idao = factory.getInvitesDAO();
            idao.beginTransaction();

            ApiKeys key = kd.findKeyByApiKey(this.apiKey);

            if (this.authenticateUser(key) == true) {

                Users invitee = key.getUsers();
                Events event = edao.findByPrimaryKey(this.eventid);
                Venues venue = event.getVenues();
                Location loca = event.getLocation();
                Users eventOwner = null;
                Visibility vis = event.getVisibility();
                if (event.getEventend() < new Date().getTime()/1000) {
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY, "the event is not a current event and so an invite can not be added");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                }
                if (vis.getId() > 1) {
                    this.is_public = false;

                    eventOwner = event.getUsers();
                    
                    if (vis.getId() == 3) {
                        if (eventOwner.getId().equals(invitee.getId()) != true) {
                            response.clear();
                            response.put(ApiResponse.ERROR_INFO_KEY, "this is not a public event. Invites can only come from the event owner");
                            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
                        }
                    } else if (vis.getId() == 2 && eventOwner.equals(invitee) != true) {
                        boolean is_invited = false;
                        Set<Invites> eventInvites = event.getInvites();
                        if (eventInvites.isEmpty() != true) {
                            System.out.println("events invites isn't empty ");
                            for (Invites inv : eventInvites) {
                                if (inv.getUsers().equals(invitee)) {
                                    is_invited = true;
                                    break;
                                }
                            }
                        }

                        if (is_invited == false) {
                            response.clear();
                            response.put(ApiResponse.ERROR_INFO_KEY, "This is not a public event, Invites can only come from invited users");
                            return ApiResponse.getNoPermissionsResponse(null);
                        }
                    }

                }


                if (userids != null) {
                    Set<Invites> invitesSent = event.getInvites();
                    System.out.println("userids = "+userids);
                    for(String invitedid : userids) {
                       
                        if (invitedid.equals("") != true) {
                            Users invited = udao.findByPrimaryKey(new Integer(invitedid));

                            String invitedEmail = invited.getEmail();
                            //create and save the invite
                            Invites invite = new Invites();
                            invite.setEvents(event);
                            invite.setUsers(invited);
                            if (this.is_public == false && vis.getId() == 3) {
                                invitee = eventOwner;

                            }
                            invite.setInviterid(invitee);

                            invite.setInvitehash(ParameterHelper.md5(event.getId().toString() + invited.getId().toString()));
                            invite.setStatus(Invites.PENDING_INVITE);
                            if (invitesSent.contains(invite) != true) {
                                invite = idao.save(invite);
                                String eventLink;
                                if (this.is_public == false) {
                                    eventLink = "<a href=\"http://" + SiteHelper.getSiteName() + "/event/" + event.getTitle().replaceAll(" ", "-") + "/" + event.getId() + "/?viewkey=" + invite.getInvitehash() + "\">" + event.getTitle() + "</a>";
                                } else {
                                    eventLink = "<a href=\"http://" + SiteHelper.getSiteName() + "/event/" + event.getTitle().replaceAll(" ", "-") + "/" + event.getId() + "/ \">" + event.getTitle() + "</a>";
                                }
                                //send mail
                               Boolean sent = this.sendOutMessage(event, invitee, venue, loca, invitedEmail, eventLink);
                                //get any device tokens to send push nots
                                String m = invited.getMobileKey();
                                MobilekeysDAO mkDao = factory.getTempMobileKeysDAO();
                                MobileKeys mkey = mkDao.getByMobileKey(m);
                                System.out.println("MOBILE KEY IS EQUAL TO " + m);
                                if (mkey != null) {
                                    System.out.println("WILL NOTIFTY MOBILE " + mkey.getDeviceToken());
                                    this.devicesToNotify.add(mkey.getDeviceToken());
                                }
                            }

                            UserEventStatus eventStatus = uesDAO.getUserEventStatusByEventAndUser(event, invited);
                            if(eventStatus == null){
                                 System.out.println("in IF for eventstatus");
                                eventStatus= new UserEventStatus();
                                eventStatus.setEvent(event);
                                eventStatus.setStatus("invited");
                                eventStatus.setUser(invited);
                                eventStatus.setStatusHash(ParameterHelper.md5(event.getId().toString()+invited.getId().toString()));
                                System.out.println("about to save event status");
                                uesDAO.save(eventStatus);
                                System.out.println("saved event status");
                            }else{
                                System.out.println("in ELSE for event status");
                            }
                             

                        }

                    }
                }

                if (groupids != null) {
                    Iterator<String> grupit = groupids.iterator();
                    while (grupit.hasNext()) {
                        String groupsid = grupit.next();
                        if (groupsid.equals("") != true) {
                        }
                    }
                    //TODO add group invites
                }
                if (emails != null) {
                    Iterator<String> emit = emails.iterator();
                    TempInvitesDAO tempdao = factory.getTempInvitesDAO();
                    while (emit.hasNext()) {
                        String em = emit.next();
                        if (em.equals("") != true) {
                            //create temp viewing key
                            TempInvite temp = new TempInvite();
                            temp.setEventid(event);
                            temp.setInvitekey(ParameterHelper.md5(em));
                            temp.setEventkeyhash(ParameterHelper.md5(event.getId().toString() + em));
                            tempdao.save(temp);
                            String eventLink = "<a href=\"http://" + SiteHelper.getSiteName() + "/event/" + event.getTitle().replaceAll(" ", "-") + "/" + event.getId() + "/?tempkey=" + temp.getInvitekey() + "\">" + event.getTitle() + "</a>";
                            Boolean sent = this.sendOutMessage(event, invitee, venue, loca, em, eventLink);
                        }

                    }
                }
                if (phones != null) {
                    Iterator<String> phoneit = phones.iterator();
                    while (phoneit.hasNext()) {
                        String phonenumber = phoneit.next();
                        if (phonenumber.equals("") != true) {
                        }
                    }
                    //TODO send text message
                }



                /**
                 * end and commit
                 */
               

                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "invite added succesfully");

                if (this.devicesToNotify != null && devicesToNotify.isEmpty() != true) {
                    for (String token : devicesToNotify) {
                        System.out.println("sending notification");
                        this.pushHelper.sendPushNotification(token, "New Invite Recieved From " + invitee.getUsername());
                    }
                }
                
                
                idao.commitTransaction();
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
                

            } else {
                return ApiResponse.getUnauthorizedResponse();
            }

        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (MessagingException e) {
            e.printStackTrace();
            Logger.debug(AddInvitesController.class.getName(), e.toString());
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, ApiResponse.MESSAGE_TO_USER_FAIL_MESSAGE);
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "an invite for this event has already been sent to this user by you");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        } catch (HibernateException e) {
            e.printStackTrace();
            HibernateUtil.closeSession();
            response.put("message", "the invite was not saved or added " + e.toString());
            response.put("code", new Integer(Responses.RES_FATAL).toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        

    }

    private Boolean sendOutMessage(Events event, Users invitee, Venues venue, Location loca, String email, String eventLink) throws MessagingException {

        if (this.sentTo.contains(email)) {
            return false;
        }

        Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
        message.setSubject("new invite from eventlas");
        message.setContentFromTemplate("invite");
        HashMap<String, String> replace = new HashMap<String, String>();
        replace.put("#user#", invitee.getUsername());
        replace.put("#eventname#", event.getTitle());
        replace.put("#eventdate#", new Date(event.getEventdate() * 1000).toString());
        replace.put("#eventtype#", "Invited You");
        if (venue.getId() == Events.DEFAULT_NO_VENUE) {

            replace.put("#location#", "no venue <a href=\"http://" + SiteHelper.getSiteName() + "/map/location/?" + loca.getHashid() + "\">view location on map </a>");
        } else {
            String link = "<a href=\"http://" + SiteHelper.getSiteName() + "/venues/" + venue.getId() + "/" + venue.getName() + "/\">" + venue.getName() + "</a>";
            replace.put("#location#", link);
        }


        replace.put("#eventlink#", eventLink);
        message.replaceTagsInContent(replace);
        message.setTo(email);
        message.send();
        this.sentTo.add(email);
        return true;

    }

    private void log(String message) {
        Logger.debug(AddInvitesController.class.getName(), message);
    }
}

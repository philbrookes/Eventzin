/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import api.responses.ApiResponse;
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.Events;
import com.models.Icons;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.Location;
import com.models.Users;
import com.models.Venues;
import com.models.Visibility;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class UpdateEventController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public UpdateEventController(HttpServletRequest req) {
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
        EventsDAO evdao = factory.getEventDAO();
        evdao.beginTransaction();
        ApiKeys key = keyd.findKeyByApiKey(getApiKey());
        if (authenticateUser(key) == true) {
            try {
                Integer eventid = new Integer(this.request.getParameter("eventid"));
                Events event = evdao.findByPrimaryKey(eventid);
                Users requestingUser = key.getUsers();
                Users eventUser = event.getUsers();
                if (requestingUser.getId() == eventUser.getId()) {
                    //the user is authenticated and has the same id as the event creator so go ahead and change the event
                    String title = this.request.getParameter("title");
                    String overview = this.request.getParameter("overview");
                    String iconid = this.request.getParameter("iconid");
                    String time = this.request.getParameter("eventstarts");
                    Long endtime = null;
                    if(time !=null){
                         endtime = (this.request.getParameter("eventends")!=null)?new Long(request.getParameter("eventends")):new Long(time)+9000;
                    }
                    String venueid = this.request.getParameter("venueid");
                    String longitude = this.request.getParameter("longitude");
                    String latitude = this.request.getParameter("latitude");
                    String visibility = this.request.getParameter("visibility");

                    HashMap<String,String>changeSet = new HashMap<String, String>();

                    //deal with the location first
                    if (venueid != null && longitude == null && latitude == null) {
                        //update

                        VenuesDAO vdao = factory.getVenuesDAO();
                        Venues venue = vdao.findByPrimaryKey(new Integer(venueid));
                        venue.toString();
                        event.setVenues(venue);

                        event.setLocation(venue.getLocation());

                        changeSet.put("venue", venue.getName());

                    } else if (venueid == null && longitude != null && latitude != null) {
                        //update
                        LocationDAO locd = factory.getLocationDAO();
                        Location location = locd.createLocation(new Float(longitude), new Float(latitude));
                        event.setLocation(location);
                        event.setVenues(factory.getVenuesDAO().findByPrimaryKey(4));

                        changeSet.put("location", "lat:"+latitude+ "long: "+longitude);

                    } else if (venueid == null && latitude == null && longitude == null) {
                        //do nothing
                    } else {
                        //error
                        HibernateUtil.closeSession();
                        response.clear();
                        response.put(ApiResponse.ERROR_INFO_KEY, "you cannot have a venuid and a longitude and latitude an event is either at a location or a venue");
                        return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

                    }


                    if (visibility != null) {


                        Visibility visi = factory.getVisibilityDAO().findByPrimaryKey(new Integer(visibility));
                        visi.toString();
                        event.setVisibility(visi);

                    }
                    if (iconid != null) {

                        Icons icon = factory.getIconsDAO().findByPrimaryKey(new Integer(iconid));
                        icon.toString();
                        event.setIcons(icon);



                    }
                    if (time != null) {

                        Long eventTime = new Long(time);
                        event.setEventdate(eventTime);
                        event.setEventend(endtime);
                        changeSet.put("time", eventTime.toString());

                    }
                    if (title != null) {
                        event.setTitle(title);
                        changeSet.put("title",title );
                    }
                    if (overview != null) {
                        event.setSummary(overview);
                        changeSet.put("details", overview);
                    }



                    evdao.update(event);

                    if (this.request.getParameter("updateinvites") != null) {
                        /**
                         * @todo need to add a message template for updated invite and add code to send it
                         */
                        //the user wants the invited people to be informed of the change
                        //get list of invities event.getInviteses();
                        //send to messaging and set the message
                        /**
                         * send email to invited user
                         */

                        Messenger message = new InviteMessenger(Messenger.FROM_INVITES);
                         Set<Invites> invites = event.getInvites();
                        if(invites.isEmpty()!=true){
                        Iterator<Invites> it = invites.iterator();
                        ArrayList<String> mailaddresses = new ArrayList<String>();
                        while (it.hasNext()) {
                            mailaddresses.add(it.next().getUsers().getEmail());
                        }
                        message.setMultipleRecipients(mailaddresses);
                        message.setSubject("updated invite from eventlas");
                        message.setContentFromTemplate("updatedinvite");
                        HashMap<String, String> replace = new HashMap<String, String>();
                        replace.put("#user#", event.getUsers().getUsername());
                        replace.put("#eventname#", event.getTitle());
                        replace.put("#eventdate#", event.getEventdate().toString());
                        replace.put("#eventtype#", "Invited You");
                        if(event.getSummary().equals("")!=true){
                                replace.put("#furtherdetails#", "<h3 style=\"color: #ffffff; margin: 0px; text-shadow: #999999 -1px -1px 0px; font-size: 36px; font-family: Georgia, 'Times New Roman', Times, serif;\">Further Details</h3>"+event.getSummary());
                            }
                        else{
                            replace.put("#furtherdetails#", "");
                        }

                        Venues venue = event.getVenues();
                        Location loca = event.getLocation();
                        if (venue.getId() == Events.DEFAULT_NO_VENUE) {
                            replace.put("#location#", "no venue <a href=\"http://eventlas.com/eventmap/?id=" + loca.getHashid() + "\">view location on map </a>");
                        } else {
                            replace.put("#location#", venue.getName());
                        }
                         String changedVals = "";
                        if(changeSet.isEmpty() != true){
                            Set<String> keys = changeSet.keySet();

                            Iterator<String> kit = keys.iterator();

                            while(kit.hasNext()){
                                String nextVal = kit.next();
                                changedVals+=" "+nextVal + " : "+changeSet.get(nextVal)+" <br/>";
                            }

                        }

                         replace.put("#changed#", changedVals);

                        message.replaceTagsInContent(replace);
                       
                        message.send();
                        response.clear();
                        response.put(ApiResponse.GENERAL_INFO_KEY, "the event was updated and those invited have been notified of the change");
                        }else{
                              response.clear();
                              response.put("id",event.getId() );
                              response.put(ApiResponse.GENERAL_INFO_KEY, "the event was updated but no ivites were found");
                        }
                    } else {
                        response.clear();
                        response.put(ApiResponse.GENERAL_INFO_KEY, "the event was updated");
                        response.put("id",event.getId() );
                    }

                    evdao.commitTransaction();

                    return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));


                } else {
                    return ApiResponse.getNoPermissionsResponse();
                }
            } catch (ObjectNotFoundException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (MessagingException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "no updates to the invited users"+ e.getMessage());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "problem with adding the event");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }


        } else {
            HibernateUtil.closeSession();
            return ApiResponse.getUnauthorizedResponse();
        }





    }
}

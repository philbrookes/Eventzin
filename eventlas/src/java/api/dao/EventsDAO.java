/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventFullDetail;
import com.models.Events;
import com.models.Invites;
import com.models.Location;
import com.models.Users;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;





/**
 *
 * @author craigbrookes
 */
public interface EventsDAO extends GenericDAO<Events, Integer>{
    public List<Events> getEventsByVenueId(Integer venueId);
    public List<Events> getEventsByRating(Integer lowerLimit, Integer upperLimit);
    public Events getEventById(Integer eventId);
    public List<EventFullDetail>getEventsByLocationIds(List<Location>locations) ;
    public EventFullDetail getFullEventDetail(Integer eventid);
    public List<EventFullDetail>getEventsByLongAndLat(Float currentlongitude, Float currentlatitude, int dist);
    public List<EventFullDetail>getEventsByUserEventStatus(Integer userid, String status);
    public List<EventFullDetail>getEventsByUser(Users user, int startPos, int endPos);
    public EventFullDetail getEventByEventNameAndUser(String eventName, Users user);
    public List<EventFullDetail>getEventsByUserIdsAndLocation(ArrayList<Integer> userids, Float longitude, Float latitude, int distance, ArrayList<Integer>ignore);
    public List<EventFullDetail>getCurrentEventsForUser(Users user);
    public EventFullDetail getEventsByInvite(Invites inv);
    public List<EventFullDetail> getEventsByFriendsAndInvites(ArrayList<Integer> userids, ArrayList<Integer>eventids);
    public List<EventFullDetail>getEventsByInviteIds(ArrayList<Integer>inviteids);
    public List<EventFullDetail>getPublicEventsByUserIds(ArrayList<Integer>userids);
     public List<EventFullDetail> getEventsByDateRangeAndUser(Date from, Date to, Users u);
            
   
}

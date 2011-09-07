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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author philbrookes
 */
public class HibernateEventsDAO extends HibernateDAO<Events, Integer> implements EventsDAO {

    public HibernateEventsDAO() {
        super(Events.class);
    }

    @Override
    public List<Events> getEventsByVenueId(Integer venueId){
        Criteria crit = HibernateUtil.getSession().createCriteria(Events.class);

        crit.add(Restrictions.eq("venues.id", venueId));
        crit.add(Restrictions.gt("eventdate", new Date()));
        return crit.list();
    }

    @Override
    public List<Events> getEventsByRating(Integer lowerLimit, Integer upperLimit){
        Criteria crit = HibernateUtil.getSession().createCriteria(Events.class);
        crit.add(Restrictions.gt("rating", lowerLimit));
        crit.add(Restrictions.lt("rating", upperLimit));
        return crit.list();
    }
    @Override
    public Events getEventById(Integer eventId){
        Criteria crit = HibernateUtil.getSession().createCriteria(Events.class);
        crit.add(Restrictions.eq("id", eventId));
        return (Events) crit.uniqueResult();
    }
@Override
    public List<EventFullDetail>getEventsByLocationIds(List<Location>locations){
      
        Iterator<Location> it = locations.iterator();
        Set<Integer>ids = new  HashSet<Integer>();
        if(locations.isEmpty())
        {
            List<EventFullDetail> e = new ArrayList<EventFullDetail>();
            return e;
        }
        
        while(it.hasNext()){
           ids.add(it.next().getId());
        }

        String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress FROM events "
                + "INNER JOIN location on events.locid=location.id "
                + "INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE";
        for(int i = 0; i<ids.size(); i++){
            if(i == 0)
                sql +=" events.locid=?";
            else
                sql += " OR events.locid=?";
       
        }
        sql +=" AND events.eventdate > UNIX_TIMESTAMP()";
         org.hibernate.Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
         Object[] iids = ids.toArray();
         for(int i = 0; i<ids.size(); i++){

             q.setInteger(i, (Integer)iids[i]);
         }

        List<EventFullDetail>e = q.list();
        return e;
    }


    @Override
    public List<EventFullDetail>getEventsByLongAndLat(Float currentlongitude, Float currentlatitude, int dist){
       String sql = "SELECT location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, events.* ,  ( 3959 * acos( cos( radians("+currentlatitude+") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("+currentlongitude+") ) + sin( radians("+currentlatitude+") ) * sin( radians( latitude ) ) ) ) AS distance FROM location "
                + " INNER JOIN events on events.locid=location.id"
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE events.enddate > UNIX_TIMESTAMP() AND visibilityid=1 HAVING distance < "+dist
                + " ORDER By distance";
        org.hibernate.Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
        List<EventFullDetail>events = q.list();
        return events;

    }

    @Override
    public EventFullDetail getFullEventDetail(Integer eventid) {
        String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " events.id=?";
        org.hibernate.Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
        q.setInteger(0, eventid);

        return (EventFullDetail)q.uniqueResult();
    }
    @Override
    public List<EventFullDetail>getEventsByUserEventStatus(Integer userid, String status){
        String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id"
                + " INNER JOIN usereventstatus on events.id=usereventstatus.eventid"
                + " WHERE usereventstatus.status =:status AND usereventstatus.userid=:userid AND events.enddate > UNIX_TIMESTAMP() LIMIT 50 ";
                Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                q.setString("status", status);
                q.setInteger("userid", userid);
                List<EventFullDetail>events = q.list();
                return events;

    }

    @Override
    public List<EventFullDetail>getEventsByUser(Users user, int startPos, int endPos){
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " events.userid=:userid AND events.enddate > UNIX_TIMESTAMP() LIMIT :start, :end";
                Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                q.setInteger("userid", user.getId());
                q.setInteger("start", startPos);
                q.setInteger("end", endPos);
                List<EventFullDetail>events = q.list();
                return events;
    }
    
    
    @Override
    public List<EventFullDetail>getEventsByUserIdsAndLocation(ArrayList<Integer> userids, Float longitude, Float latitude, int distance, ArrayList<Integer>ignore){
        
          String sql = "SELECT location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, events.* ,  ( 3959 * acos( cos( radians(52.25) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(-7.43) ) + sin( radians(52.25) ) * sin( radians( latitude ) ) ) ) AS distance FROM location"
          +" INNER JOIN events on events.locid=location.id"
          +" INNER JOIN venues on events.venueid=venues.id"
          +" INNER JOIN visibility on events.visibilityid=visibility.id WHERE events.enddate > UNIX_TIMESTAMP() AND events.visibilityid=1 AND events.userid NOT in(:ignore) HAVING distance < 25"
          +" OR events.enddate > UNIX_TIMESTAMP()  AND events.visibilityid='1' AND events.userid in(:list) AND events.userid NOT in(:ignore)"
          +" ORDER By distance";
            Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
            userids.add(0);
            q.setParameterList("list", userids);
            ignore.add(0);
            q.setParameterList("ignore", ignore);
            List<EventFullDetail>eList = q.list();
            return eList;
                  
        
    }
    
    

    @Override
    public EventFullDetail getEventByEventNameAndUser(String eventName, Users user){
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE "
                + " CONCAT(:eventname,:userid) = CONCAT(events.title,events.userid) ORDER BY events.enddate DESC LIMIT 1";
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                q.setInteger("userid", user.getId());
                q.setString("eventname", eventName);
                return (EventFullDetail) q.uniqueResult();

    }
    
    @Override
    public List<EventFullDetail>getCurrentEventsForUser(Users user){
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " events.userid=:userid AND events.enddate > UNIX_TIMESTAMP()";
         
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setInteger("userid", user.getId());
                 return q.list();
    }


    @Override
    public EventFullDetail getEventsByInvite(Invites inv){
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id"
                + " INNER JOIN invites on invites.eventid=events.id"
                + " WHERE invites.id=:id";
          Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setInteger("id", inv.getId());
                 return (EventFullDetail) q.uniqueResult();

    }

    @Override
    public List<EventFullDetail> getEventsByFriendsAndInvites(ArrayList<Integer> userids, ArrayList<Integer>eventids) {
        String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " events.userid in(:userids) OR events.id in(:eventids) AND events.enddate > UNIX_TIMESTAMP()";
         
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setParameter("userids", userids);
                 q.setParameterList("eventids", eventids);
                 return q.list();
    }
    
    @Override
    public List<EventFullDetail>getEventsByInviteIds(ArrayList<Integer>inviteids){
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN invites on invites.eventid=events.id" 
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " invites.id in(:inviteids) AND events.enddate > UNIX_TIMESTAMP()";
         
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setParameterList("inviteids", inviteids);
                 return q.list();
    }
    
    
    @Override
    public List<EventFullDetail>getPublicEventsByUserIds(ArrayList<Integer>userids){
        
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id WHERE"
                + " events.userid in(:userids) AND events.visibilityid='1' AND events.enddate > UNIX_TIMESTAMP()";
         
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setParameterList("userids", userids);
                 return q.list();
        
    }

    @Override
    public List<EventFullDetail> getEventsByDateRangeAndUser(Date from, Date to, Users u) {
         String sql = "SELECT events.*, location.longitude as longitude, location.latitude as latitude, location.id as locid , venues.name as venuename, venues.address as venueaddress, venues.id as venueid FROM events"
                + " INNER JOIN location on events.locid=location.id "
                + " INNER JOIN venues on events.venueid=venues.id "
                + " INNER JOIN visibility on events.visibilityid=visibility.id "
                + " INNER JOIN usereventstatus ues on ues.eventid=events.id "
                +"  WHERE ues.userid=:user AND ues.status='attending' AND events.eventdate > :from AND events.enddate < :to "
                + " AND events.status='on' ORDER BY eventdate DESC";
         
                 Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(EventFullDetail.class);
                 q.setLong("from", from.getTime() / 1000);
                 q.setLong("to", to.getTime() / 1000);
                 q.setInteger("user", u.getId());
                 return q.list();
    }

    
    
    
    
    
    
    
    


}

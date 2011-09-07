/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Location;
import com.models.Venues;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateVenuesDAO extends HibernateDAO<Venues, Integer> implements VenuesDAO {

    public HibernateVenuesDAO() {
        super(Venues.class);
    }

    @Override
    public List<Venues> getVenuesWithinLongLatRad(Float lat, Float Long) {
        throw new  UnsupportedOperationException();
    }
    @Override
    public List<Venues>getVenuesWithLocIds(List<Location>locations){
         Iterator<Location> it = locations.iterator();
        Set<Integer>ids = new  HashSet<Integer>();
     
        while(it.hasNext()){
           ids.add(it.next().getId());
        }

        String sql = "SELECT venues.*, location.longitude as longitude, location.latitude as latitude, location.id as locid  FROM venues "
                + "INNER JOIN location on venues.locid=location.id "
                + "WHERE venues.locid in(:locids)";
       

         org.hibernate.Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(Venues.class);
         q.setParameterList("locids", ids);
         return q.list();
    }

    public List<Venues>getVenuesWithLocIdsAndNameLike(List<Location>locations,String name){
         Iterator<Location> it = locations.iterator();
        Set<Integer>ids = new  HashSet<Integer>();
     
        while(it.hasNext()){
           ids.add(it.next().getId());
        }

        String sql = "SELECT venues.*, location.longitude as longitude, location.latitude as latitude, location.id as locid  FROM venues "
                + "INNER JOIN location on venues.locid=location.id "
                + "WHERE venues.locid in(:locids) AND venues.name LIKE :name";
       

         org.hibernate.Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(Venues.class);
         q.setParameterList("locids", ids);
         q.setString("name", name+"%");
         return q.list();
    }

    @Override
    public Venues getByGoogleId(String id) {
        Query q = HibernateUtil.getSession().createQuery("from Venues v WHERE v.googleid=:id");
        q.setString("id", id);
        return (Venues)q.uniqueResult();
    }
    
    
    


}

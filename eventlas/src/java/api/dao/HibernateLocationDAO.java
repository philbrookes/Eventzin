/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import api.helpers.ParameterHelper;
import com.models.Location;
import java.util.List;

import org.hibernate.Criteria;

import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.criterion.Example;


/**
 *
 * @author craigbrookes
 */
public class HibernateLocationDAO extends HibernateDAO<Location, Integer> implements LocationDAO {

    public HibernateLocationDAO() {
        super(Location.class);
    }

    public Location createLocation(Float longitude, Float latitude) {

        Location loc = null;
        String toHash = new Float(latitude + longitude).toString();
        String hashword = ParameterHelper.md5(toHash);
        if (hashword != null) {
            loc = new Location();
            loc.setHashid(hashword);
            Query crit = HibernateUtil.getSession().createQuery("from Location loc WHERE loc.hashid=:hash");
            crit.setString("hash", hashword);
            Location l = (Location) crit.uniqueResult();
            
            if (l != null) {
                this.save(l);
                return l;
            } else {
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                loc.setHashid(hashword);
                this.save(loc);
            }
        }
        return loc;
    }

    public Location loadLocationByHashid(String hashid) {
        Location loc = new Location();
        loc.setHashid(hashid);
        Criteria crit = HibernateUtil.getSession().createCriteria(Location.class);
        Example exam = Example.create(loc);
        return this.save((Location) crit.uniqueResult());

    }

    public List<Location> findNearbyLocations(Float longitude, Float latitude){
        Session sess = HibernateUtil.getSession();
        Query q =  sess.getNamedQuery("findNearbyLocations");
        q.setFloat("longitudeval", longitude);
        q.setFloat("latitudeval", latitude);
        q.setInteger("distance", 20);
        List<Location> l = q.list();
        return l;
    }
    
     public List<Location> findNearbyLocations(Float longitude, Float latitude, int distance){
        Session sess = HibernateUtil.getSession();
        Query q =  sess.getNamedQuery("findNearbyLocations");
        q.setFloat("longitudeval", longitude);
        q.setFloat("latitudeval", latitude);
        q.setInteger("distance", distance);
        List<Location> l = q.list();
        return l;
    }
}

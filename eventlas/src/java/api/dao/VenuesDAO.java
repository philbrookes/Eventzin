/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Location;
import com.models.Venues;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface VenuesDAO extends GenericDAO<Venues, Integer>{

    public List<Venues>getVenuesWithinLongLatRad(Float lat, Float Long);

    public List<Venues>getVenuesWithLocIds(List<Location>locations);

    public List<Venues>getVenuesWithLocIdsAndNameLike(List<Location>locations,String name);
    
    public Venues getByGoogleId(String id);
}

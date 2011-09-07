/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Location;
import java.util.List;


/**
 *
 * @author craigbrookes
 */
public interface LocationDAO extends GenericDAO<Location, Integer>{

     public Location createLocation(Float longitude, Float latitude);
     public Location loadLocationByHashid(String hashid);
     public List<Location> findNearbyLocations(Float longitude, Float latitude);
      public List<Location> findNearbyLocations(Float longitude, Float latitude, int distance);
}

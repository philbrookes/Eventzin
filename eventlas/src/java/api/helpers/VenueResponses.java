/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;

import com.models.Venues;
import java.util.HashMap;

/**
 *
 * @author phil
 */
public class VenueResponses extends Responses {
    public static HashMap makeVenueHashmap(Venues venue){
        HashMap<String, String> returns = new HashMap<String, String>();
        returns.put("name", venue.getName());
        returns.put("adress", venue.getAddress());
        returns.put("summary", venue.getSummary());
    /*    Location loc = event.getLocation();
        returns.put("latitude", new Float(loc.getLatitude()).toString());
        returns.put("longitude", new Float(loc.getLongitude()).toString());*/
        return returns;
    }
}

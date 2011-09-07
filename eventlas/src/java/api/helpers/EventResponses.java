/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;

import com.models.Events;
import com.models.Visibility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author phil
 */
public class EventResponses extends Responses{
    public static HashMap makeEventHashmap(Events event){
        HashMap<String, Object> returns = new HashMap<String, Object>();
        returns.put("title", event.getTitle());
        returns.put("overview", event.getSummary());
        returns.put("eventdate", event.getEventdate());
        returns.put("eventend", event.getEventend());
        returns.put("id", event.getId());
        
        /**
         * @todo: something up with locations? has it changed somehow?
         */
    /*    Location loc = event.getLocation();
        returns.put("latitude", new Float(loc.getLatitude()).toString());
        returns.put("longitude", new Float(loc.getLongitude()).toString());*/
        Visibility vis = event.getVisibility();
        HashMap<String,Object>visMap = new   HashMap<String, Object>();
        visMap.put("visibility", vis.getPrivacylevel());
        visMap.put("id", vis.getId() );
        returns.put("visibility", visMap);
        return returns;
    }
    public static ArrayList<HashMap<String,Object>> makeEventsHashmap(Set<Events> events){
        ArrayList<HashMap<String,Object>> returns = new ArrayList<HashMap<String, Object>>();

        for(Events event: events){
            returns.add(makeEventHashmap(event));
        }

        return returns;
    }
}

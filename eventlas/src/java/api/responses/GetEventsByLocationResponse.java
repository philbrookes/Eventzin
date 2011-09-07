/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.responses;

import java.util.LinkedHashMap;

/**
 *
 * @author craigbrookes
 */
public class GetEventsByLocationResponse extends ApiResponse {

    public static final String NO_EVENTS_FOUND_MESSAGE = "No events were found for your location";


    public static LinkedHashMap<String,Object>getNoEventsResponse(){
        return createResponse(NO_EVENTS_FOUND_MESSAGE, OK);
    }


}

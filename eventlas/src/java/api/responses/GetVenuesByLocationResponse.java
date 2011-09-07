/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author craigbrookes
 */
public class GetVenuesByLocationResponse extends ApiResponse {

     public static final String NO_VENUES_FOUND_MESSAGE = "No venues were found for your location";


    public static LinkedHashMap<String,Object>getNoVenuesResponse(){
        ArrayList<HashMap<String,Object>> empty  = new ArrayList<HashMap<String,Object>>();

        return createResponse(NO_VENUES_FOUND_MESSAGE, OK , new ApiResponse().setArrayListResponse(empty));
    }

}

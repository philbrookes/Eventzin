/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.api;

import com.apis.external.googlemaps.GoogleGeoCoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class AddVenuesTest extends ApiTest {

    public static void main(String[] args){

        

        ArrayList arr = new ArrayList();

        HashMap<String,String> downs = new HashMap<String, String>();
        downs.put("name", "Downes");
        downs.put("address", "10 Thomas Hill Waterford, Co. Waterford");
        downs.put("iconid", "1");
        downs.put("overview", "Henry Downes Established in 1759, and in the same (eccentric) family for six generations, John de Bromhead's unusual pub is one of the few remaining houses to bottle its own whiskey.");
        try{
       // String jsonaddress = stringOfUrl("http://maps.google.com/maps/api/geocode/json?sensor=false&address=" + URLEncoder.encode(downs.get("address"),"UTF-8"));

        
        HashMap<String,Float>location = GoogleGeoCoder.getLongitudeLatitude(downs.get("address"));
        Float lat = location.get("latitude");
        Float lon = location.get("longitude");

        downs.put("longitude", lon.toString());
        downs.put("latitude", lat.toString());
        //from parent class
        String added = apiCall("AddVenue",downs);

        System.out.println(added);

        
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    


   


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apis.external.googlemaps;


import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;



import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.HashMap;



/**
 *
 * @author craigbrookes
 */
public class GoogleGeoCoder implements GeoCoder{

    private String address;
    
    private String rawResult;
    
    public GoogleGeoCoder(String address) {
        this.address = address;
    }
    
    
   private void geoCode(){
       
       
   }
   
   public static HashMap<String,Float> getLongitudeLatitude(String Address) throws IOException{
        String url = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=" + URLEncoder.encode(Address,"UTF-8");
        String json = stringOfUrl(url);
        HashMap<String,Float>response = new HashMap<String, Float>(2);
        MapData map = new Gson().fromJson(json, MapData.class);
        //response.put("latitude", map.getResults().get(0).getGeometry().getLocation().get("lat"));
       // response.put("longitude", map.getResults().get(0).getGeometry().getLocation().get("lng"));
        return response;

   }

    public String getRawResultString() {
        return rawResult;
    }

     public static String stringOfUrl(String addr) throws IOException {
        URL url = new URL(addr);
        URLConnection conn = url.openConnection();
        DataInputStream in = new DataInputStream ( conn.getInputStream (  )  ) ;
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        String contents ="";
            while(d.ready())
            {
                contents += d.readLine();
            }
        return contents;
    }
   
    
    

}

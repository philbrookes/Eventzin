/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.api;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class AddEventsTest extends ApiTest {

    public static void main(String[] args){

    
        HashMap<String,String>params = new HashMap<String, String>();

        params.put("title", "OctoberFest 2011");
        params.put("visibility", "1");
        params.put("venueid", "9");
        params.put("iconid", "7");
        params.put("overview", "The infamous Octoberfest is happening at Downes");
        params.put("time", "Oct 16, 2011 7:00 PM");

        try{
            apiCall("AddEventToVenue", params);
        }catch(IOException e){
            e.toString();
        }
     


    }




}

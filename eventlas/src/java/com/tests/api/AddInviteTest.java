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
public class AddInviteTest extends ApiTest{


    public static void main(String[] args){

        HashMap<String,String>params = new HashMap<String, String>();

        params.put("eventid", "9");
        params.put("userid", "2");

        try{
            apiCall("AddInvite", params);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author craigbrookes
 */
public abstract class ApiTest {

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


    public static String apiCall(String methodname, HashMap<String,String>params) throws IOException{
        String response = "";
        String url = "http://eventlas.com/api/"+methodname+"?apikey=d4f373af7bff17de6360d1e9c442d5ee";
        Set<String> keys = params.keySet();

        for(String key : keys){
            url+="&"+key+"="+URLEncoder.encode(params.get(key),"UTF-8");
        }

        System.out.println(url);

        response = stringOfUrl(url);

        System.out.println(response);

        return response;
    }

}

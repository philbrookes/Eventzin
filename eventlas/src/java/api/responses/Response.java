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
public abstract class Response {

    public static LinkedHashMap<String, Object> createResponse(String message, int code , ValidResponse response){
        LinkedHashMap<String,Object> respMap = createResponse(message, code);
        if(response.getResponse().getClass() == HashMap.class){
            ArrayList<Object>r = new ArrayList<Object>();
            r.add(response.getResponse());
            respMap.put("response", r);
        }else{
             respMap.put("response", response.getResponse());
        }
        return respMap;
    }

    public static LinkedHashMap<String, Object> createResponse(String message, int code){
        LinkedHashMap<String,Object> respMap = new LinkedHashMap<String, Object>(3);
        ArrayList<Object>r = new ArrayList<Object>();
        respMap.put("code", code);
        respMap.put("message", message);
        respMap.put("response", r);
        return respMap;
    }

    
   

}

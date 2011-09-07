/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phil
 */
public class Responses {
    private Map<Integer, String> responseMap;

    public static final int RES_OK = 200;
    public static final int RES_FATAL = 500;
    public static final int RES_AUTH_FAILED = 501;
    public static final int RES_PERMISSION_FAILED = 301;
    public static final int RES_PARTIAL_FAIL = 502;

    public static String getMessage(int code){
        switch(code){
            case RES_OK:
                return "Succesful";
              
            case RES_FATAL:     
                return "Fatal Fail";
            case RES_AUTH_FAILED:
                return "AUTHORISATION FAILED";
            case RES_PERMISSION_FAILED:
                return "you do not have permission to preform this action ";
            case RES_PARTIAL_FAIL:
                return "the main request was completed but a part failed such as sending a mail etc";
            default:
                return null;
        }
    }
    public static Integer getcode(int code){
        return new Integer(code);
    }
    public static HashMap getHashMap(int code, String AdditionalNotes){
        ArrayList ret = new ArrayList();
        HashMap<String, String> returns = new HashMap<String, String>();
        returns.put("message", Responses.getMessage(code)+": "+AdditionalNotes);
        returns.put("code", Responses.getcode(code).toString());
        return returns;
    }
    public static HashMap getHashMap(int code){
        HashMap<String, String> returns = new HashMap<String, String>();
        returns.put("message", Responses.getMessage(code));
        returns.put("code", Responses.getcode(code).toString());
        return returns;
    }
    

    

    public static HashMap<String,Object>createResponse(String message, int code){
        HashMap<String,Object>response = new HashMap<String, Object>(2);
        response.put("message", message);
        response.put("code", new Integer(code).toString());
        return response;
    }

    public static HashMap<String,Object>getUnauthenticatedResponse(){
         HashMap<String,Object>response = new HashMap<String, Object>(2);
        response.put("message", "unauthenticated request");
        response.put("code", new Integer(Responses.RES_AUTH_FAILED).toString());
        return response;

    }

    public static HashMap<String,Object>success(String message, Integer rowid, int code){
      HashMap<String,Object>  response = Responses.createResponse(message, code);
      response.put("id", rowid.toString());
      return response;
    }

    public static HashMap<String,Object>getNoPermission(){
        HashMap<String, Object> returns = new HashMap<String, Object>();
        returns.put("message",Responses.getMessage(RES_PERMISSION_FAILED) );
        returns.put("code", Responses.getcode(RES_PERMISSION_FAILED).toString());
        return returns;

    }
}

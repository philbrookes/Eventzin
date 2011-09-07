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
public class ApiResponse extends Response implements ValidResponse {

    public static final String NO_PERMISIONS_MESSAGE = "You do not have permissions to perform this action ";
    public static final int NO_PERMISSIONS = 401;
    public static final String FATAL_ERROR_MESSAGE = "A fatal error occurred and the operation could not be completed ";
    public static final int FATAL = 501;
    public static final String OPERATION_SUCCESSFUL_MESSAGE = "The operation was successful";
    public static final int OK = 200;
    public static final String UNAUTHORISED_MESSAGE = "Unauthorised access. This method requires authorisation";
    public static final String ERROR_INFO_KEY = "error_info";
    public static final String GENERAL_INFO_KEY = "info";
    public static final int PARTIAL_FAIL = 301;
    public static final String MESSAGE_TO_USER_FAIL_MESSAGE = "Message may not have been sent";
    public static final String NO_RESULTS_MESSAGE = "There were no results found";

    private Object response;


    @Override
    public ApiResponse setArrayListResponse(ArrayList<HashMap<String,Object>>list){
        this.response = list;
        return this;
    }

    @Override
    public ApiResponse setHashMapResponse(HashMap<String,Object>map){
        this.response = map;
        return this;
    }

    @Override
    public Object getResponse(){
         return this.response;
    }

     public static LinkedHashMap<String,Object> getFatalResponse(ValidResponse response){
        return Response.createResponse(ApiResponse.FATAL_ERROR_MESSAGE, ApiResponse.FATAL, response);
    }

     public static LinkedHashMap<String,Object> getFatalResponse(){
          return Response.createResponse(ApiResponse.FATAL_ERROR_MESSAGE, ApiResponse.FATAL);
     }
     
     public static LinkedHashMap<String,Object> getNoPermissionsResponse(ValidResponse response){
         return Response.createResponse(NO_PERMISIONS_MESSAGE, FATAL, response);
     }

     public static LinkedHashMap<String,Object> getNoPermissionsResponse(){
         return Response.createResponse(NO_PERMISIONS_MESSAGE, FATAL);
     }

     public static LinkedHashMap<String,Object> getSuccessResponse(ValidResponse response){
         return createResponse(OPERATION_SUCCESSFUL_MESSAGE, OK, response);
     }
     public static LinkedHashMap<String,Object> getSuccessResponse(){
          return createResponse(OPERATION_SUCCESSFUL_MESSAGE, OK);
    }

     public static LinkedHashMap<String,Object> getUnauthorizedResponse(){
        return createResponse(UNAUTHORISED_MESSAGE, FATAL);
     }

     public static LinkedHashMap<String,Object> getUnauthorizedResponse(ValidResponse response){
        return createResponse(UNAUTHORISED_MESSAGE, FATAL,response);
     }

     public static LinkedHashMap<String,Object> getNoResultsResponse(){
         ArrayList<HashMap<String,Object>> empty  = new ArrayList<HashMap<String,Object>>();
         return createResponse(NO_RESULTS_MESSAGE, OK , new ApiResponse().setArrayListResponse(empty));
     }




}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.models.operation.Operation;
import api.responses.ApiResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author craigbrookes
 */
public class UnknownController extends ApiAbController {
    public LinkedHashMap<String, Object> process(){
        
       
        response.put(ApiResponse.ERROR_INFO_KEY, "you requested an unknown command: ");
       return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
    }

    public UnknownController(HttpServletRequest req) {
        super(req);
    }



}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Users;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class CheckUsernameAvailableController extends ApiAbController{

    public CheckUsernameAvailableController(HttpServletRequest req) {
        super(req);
        required.add("username");
    }

    @Override
    public LinkedHashMap<String, Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        if(request.getParameter("username").length() < 3){
           response.clear();
           response.put(ApiResponse.GENERAL_INFO_KEY, "username unavailable");
           return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        try{
            UsersDAO udao = factory.getUserDAO();
            udao.beginTransaction();
                Users user = udao.findUserByUserName(request.getParameter("username"));    
            udao.commitTransaction();
             if(user !=null){
                    response.clear();
                    response.put(ApiResponse.GENERAL_INFO_KEY, "unavailable");
                    return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }else{
                    response.clear();
                    response.put(ApiResponse.GENERAL_INFO_KEY, "available");
                    return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            }
            
        }catch(ObjectNotFoundException e){
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.GENERAL_INFO_KEY, "available");
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
        }
    }






}

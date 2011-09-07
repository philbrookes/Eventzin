/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.UserFriends;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class GetUserAndSessionController extends ApiAbController {

    public GetUserAndSessionController(HttpServletRequest request) {
        super(request);
        this.required.add("username");
        this.required.add("password");
    }

    @Override
    public LinkedHashMap<String,Object> process() {

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() != true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try{

           UsersDAO udao = this.factory.getUserDAO();


           udao.beginTransaction();
                LinkedHashMap<String,Object> finalResponse;
                Users user = udao.findUserByUserName(username);
                if(user !=null){
                    String userpass = user.getPassword();


                    if(ParameterHelper.md5(userpass).toLowerCase().equals(password.toLowerCase())){
                        response.clear();
                        
                        String phone = (user.getPhone() == null)?"":user.getPhone();
                        String sex = (user.getSex()== null)?"":user.getPhone();
                        String city = (user.getCity() == null)?"":user.getCity();
                        String country = (user.getCountryCode() == null)?"":user.getCountryCode();
                        String agerange = (user.getAgeRange() == null)?"":user.getAgeRange();
                        
                        HashMap<String,Object>userMap = new HashMap<String, Object>();
                        userMap.put("username", user.getUsername());
                        userMap.put("email", user.getEmail());
                        userMap.put("sex", sex);
                        userMap.put("city", city);
                        userMap.put("countrycode", country);
                        userMap.put("agerange", agerange);
                        userMap.put("id",user.getId());
                        Set<UserFriends> friends = user.getUsersfriends();
                        ArrayList<Integer>friendIds = new ArrayList<Integer>(0);
                        for(UserFriends f : friends){
                            friendIds.add(f.getFriend().getId());
                        }
                        userMap.put("friend_ids", friendIds);
                        response.put("user", userMap);
                        response.put("sessionkey", user.getMobileKey());
                       finalResponse = ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
                    }else{
                        response.clear();
                        response.put(ApiResponse.ERROR_INFO_KEY, "incorrect details");
                        finalResponse = ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                    }
                }else{
                  response.clear();
                  response.put(ApiResponse.ERROR_INFO_KEY, "incorrect details");
                  finalResponse = ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                }

           udao.commitTransaction();

           return finalResponse;



        }catch(ObjectNotFoundException e){
             HibernateUtil.closeSession();
             response.clear();
             response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
             return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }catch(HibernateException e){

            HibernateUtil.closeSession();
             response.clear();
             response.put(ApiResponse.ERROR_INFO_KEY,  "An Error occurred");
             return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }


    }





}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.dao.MobilekeysDAO;
import api.dao.ProfileDAO;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.logger.Logger;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.MobileKeys;
import com.models.Profile;
import com.models.Users;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;



/**
 *
 * @author craigbrookes
 */
public class AddMobileUserController extends ApiAbController{

    public AddMobileUserController(HttpServletRequest req) {
        super(req);
        this.required.add("mobilekey");
        this.required.add("username");
        this.required.add("password");
        this.required.add("email");
        this.required.add("udid");
    }

    @Override
    public LinkedHashMap<String, Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));



        try{

            MobilekeysDAO mkdao = factory.getTempMobileKeysDAO();
            UsersDAO udao = factory.getUserDAO();
            ApiKeysDAO kdao = factory.getKeysDAO();
            ProfileDAO profDAO = factory.getProfileDAO();
            String mobileKey = this.request.getParameter("mobilekey");
            String username = this.request.getParameter("username");
            String password = this.request.getParameter("password");
            String email = this.request.getParameter("email");
            String udid = this.request.getParameter("udid");




            mkdao.beginTransaction();

            MobileKeys mkey = mkdao.getKeyByUid(udid);
           
            if(mkey !=null &&  mkey.getKey().equals(mobileKey) ){
                Users user = new Users();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setMobileKey(mobileKey);
                if(request.getParameter("phone") !=null)
                    user.setPhone(request.getParameter("phone"));

                udao.save(user);

                Profile profile = new Profile();
                profile.setUser(user);
                profile.setProfilepic("http://eventzin.com/images/profile/blankprofile.jpg");
                profDAO.save(profile);

                ApiKeys key = new ApiKeys();
                key.setApikey(mobileKey);
                key.setUsers(user);
                key.setPermission(new Integer(1));
               

                kdao.save(key);
                mkdao.delete(mkey);
                
                //ok to add user
                mkdao.commitTransaction();
                response.put("userid", user.getId());
                ArrayList<HashMap<String,Object>>rList = new ArrayList<HashMap<String,Object>>();
                rList.add(response);
                return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(rList));
            }else{
                return ApiResponse.getNoPermissionsResponse();
            }



            
            

        }catch(ObjectNotFoundException e){
         HibernateUtil.closeSession();
                 response.clear();
                 response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
                 return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch(ConstraintViolationException e){
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"there was a problem saving adding the user "+ e.toString() + "The user may already exist");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }catch (HibernateException e) {
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY,"there was a problem adding the user "+ e.toString());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }
           

        
    }



}

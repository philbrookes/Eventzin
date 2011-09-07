/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.ApiKeysDAO;
import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.Users;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class ConfirmUserController extends ApiAbController {

    public ConfirmUserController(HttpServletRequest req) {
        super(req);
        required.add("userid");
        required.add("ccode");
    }

    @Override
    public Object process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty()!=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty()!=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        
        try{
            
            ApiKeysDAO apdao = factory.getKeysDAO();
            UsersDAO udao = factory.getUserDAO();
            apdao.beginTransaction();
            ApiKeys key = apdao.findKeyByApiKey(request.getParameter("ccode"));
            if(key != null){
                Users u = key.getUsers();
                if(u.getId().equals(new Integer(request.getParameter("userid")))){
                    String newkey = ParameterHelper.md5(request.getParameter("ccode")+new Date().toString());
                    u.setMobileKey(newkey);
                    key.setApikey(newkey);
                    key.setPermission(new Integer(1));
                    udao.save(u);
                    apdao.save(key);
                }else{
                    HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY,"failed to authenticate");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                }
            }else{
                    HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY,"failed to authenticate");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
                }
            apdao.commitTransaction();
            return ApiResponse.getSuccessResponse();
            
        }catch(ObjectNotFoundException e){
                    HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY,"failed to authenticate");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
        }catch(HibernateException e){
            HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY,"failed unknown error");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
        }catch(Exception e){
            HibernateUtil.closeSession();
                    response.clear();
                    response.put(ApiResponse.ERROR_INFO_KEY,"failed unknown error");
                    return ApiResponse.getNoPermissionsResponse(new ApiResponse().setHashMapResponse(response));
        }
    }

    
    
    
    
    
}

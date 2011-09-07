/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.FansDAO;
import api.dao.HibernateUtil;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import api.models.operation.Operation;
import api.responses.ApiResponse;
import com.models.Fans;
import com.models.ApiKeys;
import com.models.Users;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class BecomeFanOfUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>>{

    public BecomeFanOfUserController(HttpServletRequest req) {
        super(req);
        required.add("userid");
    }

    @Override
    public LinkedHashMap<String,Object> process() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>();

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        try {
                FansDAO fdao = factory.getFansDAO();
                fdao.beginTransaction();
                    ApiKeys key = factory.getKeysDAO().findKeyByApiKey(this.request.getParameter("apikey"));
                    if(authenticateUser(key) == true){

                        Fans fan = new Fans();
                        fan.setUserid(key.getUsers());
                        UsersDAO ud = factory.getUserDAO();
                        Users u = ud.findByPrimaryKey(new Integer(this.request.getParameter("userid")));

                        fan.setFanid(u);

                        fdao.save(fan);
                        fdao.commitTransaction();
                        return ApiResponse.getSuccessResponse();
                    }else{
                        return ApiResponse.getUnauthorizedResponse();
                    }
        }catch(ObjectNotFoundException e){
             HibernateUtil.closeSession();
             response.clear();
             response.put(ApiResponse.ERROR_INFO_KEY,  "there was a problem with "+e.getEntityName());
             return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
        catch (HibernateException e) {
            HibernateUtil.closeSession();
           response.put("error message", e.getMessage()+ e.getCause().toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
    }







}

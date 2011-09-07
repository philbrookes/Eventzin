/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;


import api.dao.HibernateUtil;
import api.dao.MobilekeysDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import api.responses.ValidResponse;
import com.models.MobileKeys;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class AddDevicePushTokenController extends ApiAbController {

    public AddDevicePushTokenController(HttpServletRequest req) {
        super(req);
        required.add("devicetoken");
        required.add("udid");
    }

    @Override
    public LinkedHashMap<String,Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse( response));

        response = ParameterHelper.catchNullValues(requestParams);
        if(response.isEmpty() !=true)
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        try{
            String udid = request.getParameter("udid");
            String deviceToken = request.getParameter("devicetoken");
            MobilekeysDAO mdao = factory.getTempMobileKeysDAO();
            mdao.beginTransaction();
            MobileKeys mkey = mdao.getKeyByUdid(udid);

            if(mkey != null){
                 mkey.setDeviceToken(deviceToken);
                 mdao.save(mkey);
                 mdao.commitTransaction();

                return ApiResponse.getSuccessResponse();
            }else{
                return ApiResponse.getFatalResponse();
            }

           

        }catch(ObjectNotFoundException e){
                HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }catch(HibernateException e){
                 HibernateUtil.closeSession();
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occurred and the action was not completed");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }
    }


}

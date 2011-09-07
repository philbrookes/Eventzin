/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.HibernateUtil;
import api.dao.MobilekeysDAO;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.MobileKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author craigbrookes
 */
public class AddMobileKeyController extends ApiAbController {

    public AddMobileKeyController(HttpServletRequest req) {
        super(req);
        required.add("uid");
        required.add("stamp");
        required.add("deviceos");
    }

    @Override
    public LinkedHashMap<String, Object> process() {

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        String udid = this.request.getParameter("uid");
        Long timestamp = new Long(request.getParameter("stamp"));
        String deviceOs = this.request.getParameter("deviceos").toLowerCase();
        ArrayList<String> allowedOs = new ArrayList<String>(Arrays.asList("ios","android"));
        if(!allowedOs.contains(deviceOs)){
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "the device os must be supported. Either ios or android");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        if (udid.length() > 30 ) {
            try {



                MobilekeysDAO mdao = factory.getTempMobileKeysDAO();
                mdao.beginTransaction();
                MobileKeys mkey = mdao.getKeyByUdid(udid);
                if(mkey != null){
                                     
                }else{
                    mkey = new MobileKeys();
                    mkey.setUid(udid);
                    mkey.setKey(ParameterHelper.createMobileKey(timestamp, udid).toLowerCase());
                    mkey.setDevicetype(deviceOs);
                }
                mdao.save(mkey);
                mdao.commitTransaction();
                response.clear();
                response.put("key", mkey.getKey());
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));

            } catch (ObjectNotFoundException e) {
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
        } else {
            return ApiResponse.getFatalResponse();
        }

    }
}

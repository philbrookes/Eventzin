package api.controllers;

import api.dao.HibernateUtil;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.models.ApiKeys;
import api.dao.ApiKeysDAO;
import api.dao.AppInvitesDAO;
import com.models.AppInvites;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class GetAppInviteController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public GetAppInviteController(HttpServletRequest req) {
        super(req);
        required.add("invitecode");
        
    }
    
    public LinkedHashMap<String, Object> process() {
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        
        try {
            
            ApiKeysDAO kdao = factory.getKeysDAO();
            AppInvitesDAO appInvites = factory.getAppInvitesDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            HashMap<String,Object>inviteMap = new HashMap<String, Object>(2);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
              AppInvites invite = appInvites.findByCode(request.getParameter("invitecode"));
              if(invite != null){
                 inviteMap.put("email", invite.getEmail());
                 inviteMap.put("code", invite.getMd5hash());
              }else{
                  HibernateUtil.closeSession();
                  response.put(ApiResponse.ERROR_INFO_KEY, "no invite found");
                  return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
              }
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(inviteMap));
            
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        }
    }
}

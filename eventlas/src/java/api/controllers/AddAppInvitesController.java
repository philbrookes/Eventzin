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
import com.messengers.InviteMessenger;
import com.messengers.Messenger;
import com.models.AppInvites;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class AddAppInvitesController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public AddAppInvitesController(HttpServletRequest req) {
        super(req);
        required.add("email");
        optional.add("shortmessage");
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
        
        try {
            String toInvite = request.getParameter("email");
            String shortMessage = (request.getParameter("shortmessage")!=null)?request.getParameter("shortmessage"):"";
                    
            ApiKeysDAO kdao = factory.getKeysDAO();
            AppInvitesDAO appInvites = factory.getAppInvitesDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
               
                AppInvites invite = new AppInvites();
                invite.setEmail(toInvite);
                invite.setMd5hash(ParameterHelper.md5(toInvite));
                appInvites.save(invite);
                Messenger emailInvite = new InviteMessenger(Messenger.FROM_INVITES);
                emailInvite.setContentFromTemplate("appinvite");
                HashMap<String,String>replace = new HashMap<String, String>(2);
                replace.put("#joinlink#", "<a href='http://eventzin.com/register/invited?invite="+invite.getMd5hash()+"'>Join Eventzin</a>");
                replace.put("#shortmessage#", shortMessage);
                emailInvite.replaceTagsInContent(replace);
                emailInvite.setSubject("You Have Been Invited To Join Eventzin");
                emailInvite.setTo(toInvite);
                emailInvite.send();
                
                
                
            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse();
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred User has already been invited");
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

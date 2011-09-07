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
import api.dao.EventsDAO;
import api.dao.InvitesDAO;
import api.dao.UserEventStatusDAO;
import api.helpers.EventResponses;
import com.models.Events;
import com.models.Invites;
import com.models.UserEventStatus;
import com.models.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class GetInvitesByEventController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public GetInvitesByEventController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
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

            ApiKeysDAO kdao = factory.getKeysDAO();
            InvitesDAO invDao = factory.getInvitesDAO();
            Integer eventid = (request.getParameter("eventid") != null) ? new Integer(request.getParameter("eventid")) : new Integer(0);
            EventsDAO evDao = factory.getEventDAO();
            UserEventStatusDAO uesdao = factory.getUserEventStatusDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            ArrayList<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                Events event = evDao.findByPrimaryKey(eventid);

                List<Invites> invites = invDao.getInvitesByEventId(eventid);
                for (Invites in : invites) {

                    HashMap<String, Object> inviteMap = new HashMap<String, Object>();
                    Users user = in.getUsers();
                    inviteMap.put("id", in.getId());
                    inviteMap.put("inviterid", in.getInviterid().getId());
                    UserEventStatus uestatus = uesdao.getUserEventStatusByEventAndUser(event, user);
                    if (uestatus != null) {
                        inviteMap.put("status", uestatus.getStatus());
                    } else {
                        inviteMap.put("status", "Pending");
                    }
                    inviteMap.put("event", EventResponses.makeEventHashmap(event));
                    inviteMap.put("inviter", in.getInviterid().getUsername());
                    inviteMap.put("invited",user.getUsername());
                    inviteMap.put("invitedid",user.getId());
                    resultList.add(inviteMap);
                }

            }
            kdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(resultList));


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

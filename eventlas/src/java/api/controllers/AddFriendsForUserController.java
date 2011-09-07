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
import api.dao.GroupDAO;
import api.dao.UserFriendsDAO;
import api.dao.UsersDAO;
import api.dao.UsersGroupsDAO;
import com.models.Groups;
import com.models.UserFriends;
import com.models.Users;
import com.models.Usersgroups;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class AddFriendsForUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddFriendsForUserController(HttpServletRequest req) {
        super(req);
        
        required.add("friendids[]");

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
            UsersDAO udao = factory.getUserDAO();
            GroupDAO gdao = factory.getGroupDAO();
            UserFriendsDAO ufdao = factory.getUserFriendsDAO();
            UsersGroupsDAO ugDAO = factory.getUsersGroupsDAO();
            

            
            String[] fids = request.getParameterValues("friendids[]");
            String group = request.getParameter("groupid");
            Integer groupid = null;
            if (group != null) {
                groupid = new Integer(group);
            }
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
            
           
            Users keyUser = key.getUsers();
            Groups grouped = null;
           
            ArrayList<HashMap<String,Object>>responseList = new ArrayList<HashMap<String, Object>>();
            HashMap<String,Object>idMap = new HashMap<String, Object>();
            ArrayList<Integer> idList = new ArrayList<Integer>();
            Users user = key.getUsers();
            if (this.authenticateUser(key) != true  ) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                if (groupid != null) {
                    grouped = gdao.findByPrimaryKey(groupid);
                } else {
                    grouped = new Groups();
                    grouped.setId(new Integer(6));

                }

                
              

                for(String fid : fids){
                     UserFriends userfriend = new UserFriends();
                     userfriend.setUser(user);
                     userfriend.setGroup(grouped);
                     Users friend = udao.findByPrimaryKey(new Integer(fid));
                     Usersgroups usersGroup = null;
                     usersGroup = ugDAO.findGroupMemberByGroupIdAndMemberId(grouped.getId(), new Integer(fid));
                     if(usersGroup == null){
                         usersGroup = new Usersgroups();
                     }
                     usersGroup.setGroups(grouped);
                     usersGroup.setUsersByUserid(keyUser);
                     usersGroup.setUsersByGroupmemberid(friend);
                     ugDAO.save(usersGroup);
                     userfriend.setFriend(friend);
                     userfriend.setUqhash(ParameterHelper.md5(friend.getId().toString()+user.getId().toString()));
                     ufdao.save(userfriend);
                     idList.add(userfriend.getId());

                }

                idMap.put("ids", idList);
               

            }
            kdao.commitTransaction();
            responseList.add(idMap);
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(responseList));


        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "Friend has already been added");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred  " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (Exception e) {
            HibernateUtil.closeSession();
            e.printStackTrace();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred " + e.toString());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
    }
}

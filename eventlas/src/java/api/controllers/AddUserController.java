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
import api.dao.UsersDAO;
import com.models.Users;
import java.util.Date;

/**
 *
 * @author craigbrookes
 */
public class AddUserController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public AddUserController(HttpServletRequest req) {
        super(req);
        required.add("username");
        required.add("password");
        required.add("email");
        optional.add("coutrycode");
        optional.add("city");
        optional.add("sex");
        optional.add("phone");
        
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
            
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String countrycode = request.getParameter("countrycode");
            String city = request.getParameter("city");
            String sex = request.getParameter("sex");
            String phone = request.getParameter("phone");
                    
            System.out.println(countrycode + " value optional" + city + " "+ sex);
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            Users u = new Users();
            if (!apiKey.equals("7711864380c99fe2cf965c7aa8940fc9")) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
                
                u.setUsername(username);
                u.setEmail(email);
                u.setPassword(password);
                u.setMobileKey(ParameterHelper.createMobileKey(new Date().getTime(), username));
                u.setCity(city);
                u.setCountryCode(countrycode);
                u.setSex(sex);
                u.setPhone(phone);
                udao.save(u);
                ApiKeys apkey = new ApiKeys();
                apkey.setUsers(u);
                apkey.setApikey(u.getMobileKey());
                kdao.save(apkey);
            }
            kdao.commitTransaction();
            response.clear();
            response.put("id", u.getId());
            response.put("sessionkey", u.getMobileKey());
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));
            
            
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            
        } catch (ConstraintViolationException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "the username or email address already belongs to a registered user");
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

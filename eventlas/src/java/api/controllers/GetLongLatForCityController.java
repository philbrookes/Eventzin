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
import api.dao.CityDAO;
import com.models.City;
import java.util.HashMap;

/**
 *
 * @author craigbrookes
 */
public class GetLongLatForCityController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {
    
    public GetLongLatForCityController(HttpServletRequest req) {
        super(req);
        required.add("countrycode");
        required.add("city");
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
            String city= request.getParameter("city");
            String countryCode = request.getParameter("countrycode");
            ApiKeysDAO kdao = factory.getKeysDAO();
            CityDAO cdao = factory.getCityDAO();
            /**
            Add more Dao as needed
             **/
            kdao.beginTransaction();
            ApiKeys key = kdao.findKeyByApiKey(apiKey);
             HashMap<String,Object> cityMap = new HashMap<String, Object>();
            if (this.authenticateUser(key) != true) {
                return ApiResponse.getNoPermissionsResponse();
            } else {
               
                City retCity = cdao.getByCityAndCountryCode(city, countryCode);
                if(retCity == null){
                    //query google
                }
                else{
                    cityMap.put("longitude", retCity.getLongitude());
                    cityMap.put("latitude", retCity.getLatitude());
                }
                /**
                LOGIC CODE HERE
                 **/
            }
            kdao.commitTransaction();
           
            return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(cityMap));
            
            
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

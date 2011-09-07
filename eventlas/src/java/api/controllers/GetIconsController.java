/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.IconsDAO;
import api.responses.ApiResponse;
import com.models.Icons;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class GetIconsController extends ApiAbController{

    public GetIconsController(HttpServletRequest req) {
        super(req);
    }

    @Override
    public LinkedHashMap<String, Object> process() {
       Integer limit = null;
       String type= request.getParameter("type");
       String stringLimit = request.getParameter("limit");

       try{
       IconsDAO idao = factory.getIconsDAO();
       idao.beginTransaction();
       ArrayList<HashMap<String,Object>> returnList = new ArrayList<HashMap<String, Object>>();
       ArrayList<Icons>  icons = new ArrayList<Icons>();
       if(stringLimit !=null){
           limit = new Integer(stringLimit);
           icons = (ArrayList) idao.findAll(0, limit);
       }else{

            icons = (ArrayList) idao.findAll(0, 100);
       }

       Iterator<Icons> it = icons.iterator();
           while(it.hasNext()){
               HashMap<String,Object> iconMap = new HashMap<String, Object>();
               Icons cicon = it.next();
               iconMap.put("icon", cicon.getIcon());
               iconMap.put("name", cicon.getName());
               iconMap.put("id", cicon.getId());
               returnList.add(iconMap);
           }
           idao.commitTransaction();
           return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(returnList));
       
       
       }catch(HibernateException e){
          return ApiResponse.getFatalResponse();
       }

    }





}

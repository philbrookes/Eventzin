/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import api.dao.EventCategorysDAO;
import api.dao.HibernateUtil;
import api.responses.ApiResponse;
import com.models.EventCategorys;
import com.models.Icons;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class GetCategorysController extends ApiAbController{

    public GetCategorysController(HttpServletRequest req) {
        super(req);
    }

    @Override
    public LinkedHashMap<String, Object> process() {


        try{
            EventCategorysDAO evedao = factory.getEventCategoryDAO();
            evedao.beginTransaction();
            List<EventCategorys>categorys =  evedao.findAll(0, 1000);
            
            ArrayList<HashMap<String,Object>> cats = new ArrayList<HashMap<String, Object>>();

            Iterator<EventCategorys> evcatIt = categorys.iterator();
            while(evcatIt.hasNext()){
                LinkedHashMap<String,Object>catToAdd = new LinkedHashMap<String, Object>();
                EventCategorys ccat = evcatIt.next();
                Icons icon = ccat.getIconid();
                catToAdd.put("id", ccat.getId());
                catToAdd.put("iconid", icon.getId());
                catToAdd.put("icon",icon.getIcon());
                catToAdd.put("category", ccat.getCategory());
                cats.add(catToAdd);


            }
            evedao.commitTransaction();

            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(cats));

        }catch(HibernateException e){
            HibernateUtil.closeSession();
            return ApiResponse.getFatalResponse();
        }

    }



}

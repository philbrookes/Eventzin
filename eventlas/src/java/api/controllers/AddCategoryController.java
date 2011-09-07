/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.EventCategorysDAO;
import api.dao.IconsDAO;
import api.helpers.FileHelper;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.EventCategorys;
import com.models.Icons;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUploadException;

/**
 *
 * @author craigbrookes
 */
public class AddCategoryController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddCategoryController(HttpServletRequest req) {
        super(req);
        required.add("icon");
        required.add("category");
       

    }

    @Override
    public LinkedHashMap<String, Object> process() {

        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));

        }
        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

        try {
            String catName = this.request.getParameter("category");


            IconsDAO icond = factory.getIconsDAO();
            icond.beginTransaction();
            Icons icon = new Icons();

            ArrayList<File> uploaded = FileHelper.uploadFiles(request, FileHelper.uploadedEventImgDir);

            File addImg = uploaded.get(0);

            icon.setIcon(addImg.getName());
            icon.setName(catName);





            icond.save(icon);




            EventCategorysDAO eveCatDao = factory.getEventCategoryDAO();
            EventCategorys eveCat = new EventCategorys();
            eveCat.setCategory(catName);
            eveCat.setIconid(icon);
            eveCatDao.save(eveCat);

            icond.commitTransaction();
            //no exceptions return ok



            return ApiResponse.getSuccessResponse();
        } catch (FileUploadException ex) {
            //HibernateUtil.closeSession();
            return ApiResponse.createResponse(ex.getMessage(), ApiResponse.FATAL);
        } catch (Exception ex) {
            //HibernateUtil.closeSession();
            return ApiResponse.createResponse(ex.getMessage(), ApiResponse.FATAL);
        }
    }
}

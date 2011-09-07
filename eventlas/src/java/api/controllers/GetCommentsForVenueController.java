package api.controllers;

import api.dao.HibernateUtil;
import api.models.operation.Operation;
import api.dao.VenuesDAO;
import api.helpers.CommentResponses;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Comment;
import com.models.Venues;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author philip
 */
public class GetCommentsForVenueController extends ApiAbController {

    public GetCommentsForVenueController(HttpServletRequest req) {
        super(req);
        required.add("venueid");
    }

    @Override
    public LinkedHashMap<String, Object> process() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>();

        response = ParameterHelper.checkForNeededValues(requestParams, required);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }
        response = ParameterHelper.catchNullValues(requestParams);
        if (response.isEmpty() != true) {

            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }


        try {
            ArrayList<Map> ret = new ArrayList<Map>();

            Integer venueId = new Integer(this.request.getParameter("venueid"));

            VenuesDAO vdao = factory.getVenuesDAO();
            vdao.beginTransaction();

            Venues venue = vdao.findByPrimaryKey(venueId);
            Set<Comment> comments = (Set) venue.getVenuecomments();
            ArrayList<HashMap<String, Object>> commentlist = CommentResponses.makeCommentsArrayList(comments);
            response.clear();
            vdao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(commentlist));
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {
            HibernateUtil.closeSession();
            response.clear();
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

    }
}

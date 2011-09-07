package api.controllers;

import api.dao.EventscommentDAO;
import api.dao.HibernateUtil;
import api.helpers.CommentResponses;
import api.helpers.ParameterHelper;
import api.responses.ApiResponse;
import com.models.Comment;
import com.models.Eventcomments;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

/**
 *
 * @author philip
 */
public class GetCommentsForEventController extends ApiAbController {

    public GetCommentsForEventController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
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
            Integer eventId = new Integer(this.request.getParameter("eventid"));

           EventscommentDAO ecDao = factory.getEventsCommentDAO();
           ecDao.beginTransaction();

            List<Eventcomments> comments =  ecDao.getEventCommentsByEventId(eventId);
            ArrayList<HashMap<String,Object>>commentlist = null;
            if(comments != null){
                commentlist = new ArrayList<HashMap<String, Object>>(comments.size());
                for(Eventcomments comment : comments){
                     HashMap<String, Object> returns = new HashMap<String, Object>();
                        returns.put("id", comment.getId());
                        returns.put("comment", comment.getComment());
                        returns.put("user", comment.getUser().getUsername());
                        returns.put("rating", comment.getRating());
                        returns.put("dateadded", comment.getDateAdded().toString());
                        commentlist.add(returns);
                }
            }else
                commentlist = new ArrayList<HashMap<String, Object>>(0);
            response.clear();
            ecDao.commitTransaction();
            return ApiResponse.getSuccessResponse(new ApiResponse().setArrayListResponse(commentlist));
        } catch (ObjectNotFoundException e) {
            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "there was a problem with " + e.getEntityName());
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (NumberFormatException e) {

            HibernateUtil.closeSession();
            response.clear();
            response.put(ApiResponse.ERROR_INFO_KEY, "the argument eventid could not be parsed into an integer");
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        } catch (HibernateException e) {

            HibernateUtil.closeSession();
            response.clear();
            return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
        }

    }
}

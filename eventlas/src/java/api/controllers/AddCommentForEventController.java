/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.helpers.ParameterHelper;
import api.helpers.Responses;
import api.dao.EventsDAO;
import api.dao.EventscommentDAO;
import api.dao.HibernateUtil;
import api.dao.ApiKeysDAO;
import api.responses.ApiResponse;
import com.models.Eventcomments;
import com.models.Events;
import com.models.ApiKeys;
import com.models.Users;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class AddCommentForEventController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddCommentForEventController(HttpServletRequest req) {
        super(req);
        required.add("eventid");
        required.add("comment");

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

        ApiKeysDAO kdao = factory.getKeysDAO();
        EventscommentDAO edao = factory.getEventsCommentDAO();
        edao.beginTransaction();
        ApiKeys key = kdao.findKeyByApiKey(this.request.getParameter("apikey"));


        if (this.authenticateUser(key) == true) {
            try {

                /**
                 * Start of transaction
                 */

                Eventcomments comment = new Eventcomments();
                Users user = key.getUsers();
                user.toString();
                comment.setUser(user);
                EventsDAO evdao = factory.getEventDAO();
                Events event = evdao.findByPrimaryKey(new Integer(this.request.getParameter("eventid")));
                event.toString();
                comment.setEvents(event);
                comment.setComment(this.request.getParameter("comment"));
                comment.setCommenthash(ParameterHelper.md5(comment.getEvents().getId() + comment.getComment()));
                if (this.request.getParameter("rating") != null) {
                    Integer rating = new Integer(this.request.getParameter("rating"));
                    if (rating != null && rating >= 1) {
                        if (rating > 5) {
                            rating = new Integer(5);
                        }

                        comment.setRating(rating);
                    }
                }
                edao.save(comment);
                edao.commitTransaction();
                /**
                 * END of Transaction
                 */

                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "added comment");
                response.put("commmentid", comment.getId());
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));

            } catch (ObjectNotFoundException e) {

                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "a problem occured with "+e.getEntityName());

                HibernateUtil.closeSession();

                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (NumberFormatException e) {
                response.clear();
                response.put(ApiResponse.ERROR_INFO_KEY, "the string passed for eventid could not be formmated to an Integer");
                 HibernateUtil.closeSession();
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (ConstraintViolationException e) {
                response = Responses.createResponse("no comment added this comment was already added", Responses.RES_FATAL);
                 HibernateUtil.closeSession();
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (HibernateException e) {
                 HibernateUtil.closeSession();
                response = Responses.createResponse("the comment was not added an error occurred", Responses.RES_FATAL);
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            }

        } else {

            return ApiResponse.getUnauthorizedResponse();
        }

    }
}

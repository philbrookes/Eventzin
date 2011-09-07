/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.controllers;

import api.dao.HibernateUtil;
import api.helpers.ParameterHelper;
import api.helpers.Responses;
import api.models.operation.Operation;
import api.dao.ApiKeysDAO;
import api.dao.UsersDAO;
import api.dao.VenuesDAO;
import api.dao.VenuescommentsDAO;
import api.responses.ApiResponse;
import com.models.ApiKeys;
import com.models.Users;
import com.models.Venuecomments;
import com.models.Venues;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class AddCommentForVenueController extends AuthenticatedRequest<LinkedHashMap<String, Object>> {

    public AddCommentForVenueController(HttpServletRequest req) {
        super(req);
        required.add("venueid");
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


        VenuescommentsDAO vdao = factory.getVenuesCommentDAO();
        vdao.beginTransaction();
        ApiKeysDAO keyd = factory.getKeysDAO();
        ApiKeys key = keyd.findKeyByApiKey(this.request.getParameter("apikey"));

        if (this.authenticateUser(key) == true) {

            try {
                Venuecomments comment = new Venuecomments();
                VenuesDAO vendao = factory.getVenuesDAO();
                Venues venue = vendao.findByPrimaryKey(new Integer(this.request.getParameter("venueid")));
                venue.toString();
                comment.setVenueid(venue);

                UsersDAO ud = factory.getUserDAO();
                Users user = key.getUsers();
                user.toString();
                comment.setUser(user);
                comment.setCommenthash(ParameterHelper.md5(comment.getVenueid().getId() + comment.getComment()));
                comment.setComment(this.request.getParameter("comment"));
                if (this.request.getParameter("rating") != null) {
                    Integer rating = new Integer(this.request.getParameter("rating"));
                    if (rating != null && rating >= 1) {
                        if (rating > 5) {
                            rating = new Integer(5);
                        }

                        comment.setRating(rating);
                    }
                }
                vdao.save(comment);
                vdao.commitTransaction();
                response.clear();
                response.put(ApiResponse.GENERAL_INFO_KEY, "Comment added");
                response.put("commentid", comment.getId());
                return ApiResponse.getSuccessResponse(new ApiResponse().setHashMapResponse(response));

            } catch (ObjectNotFoundException e) {
                response.clear();
                HibernateUtil.closeSession();
                response.put(ApiResponse.ERROR_INFO_KEY, "A problem occurred with " + e.getEntityName());
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (ConstraintViolationException e) {
                response.clear();
                HibernateUtil.closeSession();
                response.put(ApiResponse.ERROR_INFO_KEY, "no comment added the comment already exists for this venue");
                return ApiResponse.getFatalResponse(new ApiResponse().setHashMapResponse(response));
            } catch (HibernateException e) {
                response.clear();
                HibernateUtil.closeSession();

                return ApiResponse.getFatalResponse();
            }
        } else {
            HibernateUtil.closeSession();
            return ApiResponse.getUnauthorizedResponse();
        }


    }
}

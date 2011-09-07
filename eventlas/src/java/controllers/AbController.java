/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import api.dao.DAOFactory;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author craigbrookes
 */
public abstract class AbController implements Controller{

     protected HttpServletRequest request;
     protected HttpServletResponse httpresponse;
     protected Map<String,String[]>requestParams;
     protected DAOFactory factory = DAOFactory.getFactory();

    public AbController(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.requestParams = request.getParameterMap();
            this.httpresponse = response;
    }

    public AbController(HttpServletRequest req){
        this.request = req;
        this.requestParams = req.getParameterMap();
    }

    protected Map<String, String[]> getRequestParams() {
        return requestParams;
    }

    protected void setRequestParams(Map<String, String[]> requestParams) {
        this.requestParams = requestParams;
    }



    protected DAOFactory getFactory() {
        return factory;
    }

    protected void setFactory(DAOFactory factory) {
        this.factory = factory;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getHttpResponse() {
        return httpresponse;
    }

    public void setHttpResponse(HttpServletResponse response) {
        this.httpresponse = response;
    }

    





}

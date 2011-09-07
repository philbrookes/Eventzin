/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filters;

import api.dao.HibernateUtil;
import api.helpers.SiteHelper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;

/**
 *
 * @author craigbrookes
 * This filter looks for a /username that corrosponds with
 * a real user. then directs the request to the UserViewController Servlet
 * With the user object in tow along with the action ie event or index and any params
 * that follow the the action. If the url doesn't corrospond to a users page the
 * request chain continues on
 */
public class FrontController implements Filter {

    FilterConfig conf;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.conf = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        
        
        ServletContext context = SiteHelper.context;

        HttpServletRequest req = (HttpServletRequest)request;

         context.log( "*************** THE FILTER WAS CALLED *************************");

        String[] urlbits = req.getRequestURI().split("/");
       
        
        
        if(urlbits.length == 2){
            String username = urlbits[1];
            request.setAttribute("action", "index");
            request.setAttribute("username", username);
            RequestDispatcher dispatch = context.getNamedDispatcher("UserViewController");
            dispatch.forward(request, response);
            return;
            
        }

        else if(urlbits.length >= 2 && urlbits[2].equals("hosting")){
              context.log( "*************** "+urlbits[2] +"*************************");
            String username = urlbits[1];
            request.setAttribute("username", username);
            request.setAttribute("action", "event");
            RequestDispatcher dispatch = context.getNamedDispatcher("UserViewController");
            dispatch.forward(request, response);
            return;
        }
         else if(urlbits.length >= 2 && urlbits[2].equals("addevent")){
             String username = urlbits[1];
            request.setAttribute("username", username);
            request.setAttribute("action", "addEvent");
            //make instance of controller and pass in the request
            RequestDispatcher dispatch = context.getNamedDispatcher("UserViewController");
            dispatch.forward(request, response);
            return;
         }
         else if(urlbits.length >= 2 && urlbits[2].equals("myevents")){
            String username = urlbits[1];
            request.setAttribute("username", username);
            request.setAttribute("action", "myevents");
            //make instance of controller and pass in the request
            RequestDispatcher dispatch = context.getNamedDispatcher("UserViewController");
            dispatch.forward(request, response);
            return;
         }
          

        chain.doFilter(request, response);
        
        
    }

    @Override
    public void destroy() {
        this.conf = null;

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.filters;

import api.dao.DAOFactory;
import api.dao.UsersDAO;
import com.models.Users;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author craig
 */
public class AuthenticationFilter implements Filter {

    FilterConfig conf = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.conf = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

         HttpSession session =((HttpServletRequest)request).getSession();
         String username = (String) session.getAttribute("username");
         String key = (String)session.getAttribute("key");
         if(username != null  && key != null){
         DAOFactory factory = DAOFactory.getFactory();
         UsersDAO udao = factory.getUserDAO();
         udao.beginTransaction();
            Users user = udao.findUserByUserName(username);
            if(user != null){
                if(user.getUsername().equals(username) && key.equals(user.getMobileKey()))
                {
                    session.setAttribute("loggedin", "yes");
                }
            }

         udao.commitTransaction();
         

        }else{
             session.setAttribute("loggedin", "no");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }

    


}

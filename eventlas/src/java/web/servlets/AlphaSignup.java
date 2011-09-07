/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.servlets;

import api.dao.AlphasDAO;
import api.dao.DAOFactory;
import api.dao.HibernateDAOFactory;
import com.models.Alphas;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author craigbrookes
 */
public class AlphaSignup extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try{

            String emailAddress  = request.getParameter("email");
            if(emailAddress != null && emailAddress.equals("") != true && emailAddress.contains("@")){
                Alphas alphaSignup = new Alphas();
                alphaSignup.setEmail(emailAddress);
                DAOFactory factory = HibernateDAOFactory.getFactory();
                AlphasDAO adao = factory.getAlphasDAO();
                adao.beginTransaction();

                    adao.save(alphaSignup);
                adao.commitTransaction();

                request.setAttribute("email", emailAddress);
                response.sendRedirect("/thanks");

            }else{
                response.sendRedirect("/");
            }

        }catch(ConstraintViolationException e){
            request.getSession().setAttribute("error", "Your email address was already registerd");
            response.sendRedirect("/");
        }
        catch(Exception e){
        
           
        } finally { 
           
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

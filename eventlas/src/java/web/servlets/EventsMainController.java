/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.servlets;

import api.dao.DAOFactory;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import com.models.EventFullDetail;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class EventsMainController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            //check params
            try{
                String[] urlbits = request.getRequestURI().split("/");
                Integer id = new Integer(urlbits[3]);
                DAOFactory factory = DAOFactory.getFactory();
                EventsDAO evd = factory.getEventDAO();
                evd.beginTransaction();
                EventFullDetail event = evd.getFullEventDetail(id);

                if(event.getVisibility().getId() > 1){
                    
                }

                request.setAttribute("event", event);

                evd.commitTransaction();

                RequestDispatcher dispatch = request.getRequestDispatcher("/EventView.jsp");
                dispatch.forward(request, response);

            }catch(NumberFormatException e){
                HibernateUtil.closeSession();
                response.getWriter().write(e.toString());
            }catch(HibernateException e){
                HibernateUtil.closeSession();
                 response.getWriter().write(e.toString());
            }catch(Exception e){
                 HibernateUtil.closeSession();
                  response.getWriter().write(e.toString());
            }

            //load event

            //dispatch to jsp
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import site.lib.AddUser;

/**
 *
 * @author craigbrookes
 */
public class Register extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {


        if(request.getParameter("action")!= null && request.getParameter("action").equalsIgnoreCase("register")){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            boolean done = false;
            if(username !=null && password !=null && email !=null){
                try{
                    AddUser add = new AddUser(username, email, password);
                    done = add.addUser();
                    
                }catch(Exception e){
                        request.setAttribute("error", e.getMessage());
                        RequestDispatcher dispatch = request.getRequestDispatcher("/Register.jsp");
                        
                        dispatch.forward(request, response);
                }
                if(done == true){
                     RequestDispatcher dispatch = request.getRequestDispatcher("/Success.jsp");
                     request.setAttribute("username", username);
                     dispatch.forward(request, response);
                }

            }
        }else{
            RequestDispatcher dispatch = request.getRequestDispatcher("/Register.jsp");
            dispatch.forward(request, response);
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

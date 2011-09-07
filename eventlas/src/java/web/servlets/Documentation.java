/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.servlets;

import api.dao.DAOFactory;
import api.dao.HibernateUtil;
import api.dao.MethodsDAO;
import api.dao.ParametersDAO;
import com.models.Methods;
import com.models.Parameters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
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
public class Documentation extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

           
            

            String[] bits = request.getRequestURI().split("/");

            String methodName = bits[bits.length -1];
           
            if(methodName.equals("documentation")!=true){
                
               response.getWriter().write(methodName);

                    try{
                       

                        response.getWriter().write(methodName);
                        DAOFactory factory = DAOFactory.getFactory();
                        MethodsDAO mdao = factory.getMethodsDAO();
                        mdao.beginTransaction();
                        Query q = HibernateUtil.getSession().getNamedQuery("getMethodByName");
                        q.setString("name", methodName);
                        Methods meth = (Methods)q.uniqueResult();

                        ParametersDAO pdao = factory.getParametersDAO();
                        List<Parameters>prms = pdao.getParamsByMethodId(meth.getId());


                        mdao.commitTransaction();

                        request.setAttribute("methoddef", meth);
                        request.setAttribute("params", prms);

                        request.getRequestDispatcher("/ApiDocs.jsp").include(request, response);
                       

                    }catch(HibernateException e){
                        response.getWriter().write(e.getMessage());
                    }catch(Exception e){
                        response.getWriter().write(e.getMessage());
                    }

                    // load up documentation
                }

                
            
            else{

               try{
               MethodsDAO mdao = DAOFactory.getFactory().getMethodsDAO();
               mdao.beginTransaction();
                List<Methods>meths = mdao.findAll(0, 110);
               mdao.commitTransaction();
               request.setAttribute("methods", meths);
               RequestDispatcher dispatch = request.getRequestDispatcher("/ApiDocs.jsp");
               dispatch.include(request, response);
               
                }catch(HibernateException e){
                       e.printStackTrace();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
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

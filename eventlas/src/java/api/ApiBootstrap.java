package api;

import api.helpers.Responses;
import api.controllers.UnknownController;
import api.models.operation.Operation;
import api.out.Json;
import api.out.OutputFormatter;
import api.dao.HibernateUtil;
import api.logger.Logger;
import api.responses.ApiResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiBootstrap extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.setProperty("logfile",this.getServletContext().getRealPath("/")+"logging/logfile.log");



                if(req.getRequestURI().contains("/api/")){
                    resp.sendRedirect("http://eventlas.com/api/documentation/");
                }
                if(req.getRequestURI().equals("/")){
                    resp.sendRedirect("http://eventlas.com/api/documentation/");
                }

                resp.setContentType("application/json");

                api.controllers.ApiAbController command = new UnknownController(req);
                try {
                    command = api.controllers.Factory.getController(req);

                    Json json = new Json(OutputFormatter.formatDataToType("json", command.process()));

                    resp.getWriter().print(json.out());
                }catch(NumberFormatException e){
                     api.logger.Logger.error(ApiBootstrap.class.getName(), e.getMessage());
                    Json json = new Json(OutputFormatter.formatDataToType("json", Responses.getHashMap(Responses.RES_FATAL, "one of the arguments passed could that should be a integer could not be parsed")));
                    resp.getWriter().print(json.out());
                }
                catch(OutOfMemoryError e){
                    api.logger.Logger.error(ApiBootstrap.class.getName(), e.getMessage());
                  //  Logger.error(ApiBootstrap.class.getName(), "Out of Memory");
                    HibernateUtil.closeSession();
                     Json json = new Json(OutputFormatter.formatDataToType("json", ApiResponse.getFatalResponse()));
                     resp.getWriter().print(json.out());
                }
                catch (Exception e) {
                     api.logger.Logger.error(ApiBootstrap.class.getName(), e.getMessage());

                  //  e.printStackTrace(resp.getWriter());
                  //  Logger.error(ApiBootstrap.class.getName(), "Got Exception ("+e.getMessage()+")");
                    try {
                        Logger.warn(ApiBootstrap.class.getName(), "Rolling back transaction");
                        HibernateUtil.rollbackTransaction();
                          HibernateUtil.closeSession();
                    } catch (Exception ex) {
                       // Logger.fatal(ApiBootstrap.class.getName(), "Got Exception ("+ex.getMessage()+" "+ex.getCause()+")");
                        e.printStackTrace(resp.getWriter());
                    }


                    } finally {

                    }
            
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("is_post", "yes");
        this.doGet(req, resp);
    }




}


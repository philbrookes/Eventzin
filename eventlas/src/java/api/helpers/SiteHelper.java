/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author craigbrookes
 */
public class SiteHelper implements ServletContextListener{

    public static String SiteName  = null;
    public static String siteSalt = null;
    public static ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       SiteName = sce.getServletContext().getInitParameter("sitename");
       siteSalt = sce.getServletContext().getInitParameter("salt");
       SiteHelper.context = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       java.util.Enumeration e = java.sql.DriverManager.getDrivers();
        while (e.hasMoreElements()) {
          Object driverAsObject = e.nextElement();
          System.out.println("JDBC Driver=" + driverAsObject);

          
        }
    }

    public static String getSiteName(){
        
            return SiteName;
    }



}

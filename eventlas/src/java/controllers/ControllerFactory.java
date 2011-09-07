/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import api.controllers.ApiAbController;
import api.controllers.UnknownController;
import api.logger.Logger;
import java.lang.reflect.Constructor;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author craigbrookes
 */
public class ControllerFactory {

    public static final String API_SECTION = "/api/";

    public static Controller getController(HttpServletRequest request)
            throws Exception {



        String url = request.getRequestURI();
        String[] bits = url.split("/");
        /**
         * @todo need to change this from the hardcoded /api/
         * eventzin.com
         * /api/
         * GetEventsByLocation
         */
        Controller command = null;
        Class cls;
        String commandController = null;
        if (bits.length >= 1) {

            if (bits[1] != null) {
                String section = bits[1];

                try {
                    if (section.equals(API_SECTION) && bits[2]!=null) {
                        commandController = bits[2];

                        Logger.info(ApiAbController.class.getName(), "Command set to: " + commandController);

                        cls = Class.forName("api.controllers." + commandController + "Controller");
                        Constructor ab = cls.getConstructor(new Class[]{HttpServletRequest.class});
                        command = (Controller) ab.newInstance(new Object[]{request});
                    } else if(section.equals(API_SECTION) && bits[2]==null) {
                        commandController = "ApiIndex";
                        cls = Class.forName("controllers.web." + commandController + "Controller");
                        Constructor ab = cls.getConstructor(new Class[]{HttpServletRequest.class});
                        command = (Controller) ab.newInstance(new Object[]{request});
                    }




                } catch (ClassNotFoundException e) {
                    command = new UnknownController(request);
                } catch (Exception e) {
                    command = new UnknownController(request);
                }

            }



        }
        return command;

    }
}

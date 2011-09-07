package api.controllers;

import api.logger.Logger;
import api.models.operation.Operation;
import java.lang.reflect.Constructor;
import javax.servlet.http.HttpServletRequest;


public class Factory {

    public static ApiAbController getController(HttpServletRequest request)
            throws Exception {



        String commandController = request.getRequestURI();
	String[] bits = new String[2];
        /**
         * @todo need to change this from the hardcoded /api/
         */
	bits = commandController.split("/");
        System.out.print(bits.length);
        
        if(bits.length == 2){
            commandController = commandController.replace("/", "");
            Logger.info(ApiAbController.class.getName(), "Command set to: "+commandController);
        }
        ApiAbController command = null;
        Class cls;
        try {

            cls = Class.forName("api.controllers." + commandController + "Controller");
            Constructor ab = cls.getConstructor(new Class[]{HttpServletRequest.class});
            command = (ApiAbController) ab.newInstance(new Object[]{request});
            
        } catch (ClassNotFoundException e) {
            command = new UnknownController(request);
        } catch (Exception e) {
            throw e;
        }
        return command;
    }
}

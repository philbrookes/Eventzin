package api.models.operation;

import api.logger.Logger;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Operation {
    private static HttpServletRequest request;
    private static String Command="";
    public static void populate(HttpServletRequest req){
	request = req;
	Command = request.getRequestURI();
	String[] bits = new String[2];
        /**
         * @todo need to change this from the hardcoded /api/
         */
	bits = Command.split("/api/");
        if(bits.length > 0){
            Command = bits[1];
            Logger.info(Operation.class.getName(), "Command set to: "+Command);
        }
    }
    public static String getCommand(){
	if(checkPopulate()){
	    return Command;
	}else{ return null; }
    }
    public static String get(String name){
	if(checkPopulate()){
            Logger.info(Operation.class.getName(), "Asked for paramater: "+name+" and found: ("+request.getParameter(name)+")");
	    return request.getParameter(name);
	} else { return null; }
    }
    private static boolean checkPopulate(){
	if(Command.equals("")){
            Logger.warn(Operation.class.getName(), "Checked populate and failed is operation.populate still in apibootstrap?");
	    return false;
	}
	return true;
    }
    public static Map getParameterMap(){
	return request.getParameterMap();
    }

    public static String getRemoteAddress(){
        return request.getRemoteAddr();
    }

}

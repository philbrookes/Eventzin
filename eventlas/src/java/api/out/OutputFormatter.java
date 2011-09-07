/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.out;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author craigbrookes
 */
public class OutputFormatter {

    

    public static String formatDataToType(String type, Object data){
        String out = null ;
        if(type.equals("json")){
            if(data instanceof Map)
                return OutputFormatter.asJson((Map)data);
            else if(data instanceof ArrayList){
                return OutputFormatter.asJson((List)data);
            }
        }
        return out;
    }



    private static String  asJson(Map  data){
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
    }

    private static String  asJson(List  data){
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
    }








 

}

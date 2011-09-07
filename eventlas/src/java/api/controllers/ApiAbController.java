package api.controllers;


import controllers.AbController;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;


public abstract class ApiAbController extends AbController implements controllers.ApiController {

    
  

    public ApiAbController(HttpServletRequest req) {
        super(req);
               
    }



    protected HashMap<String, Object> response = new HashMap<String, Object>(0);
    protected ArrayList<HashMap<String,Object>> apiResponse = new ArrayList<HashMap<String, Object>>();
    protected ArrayList<String> required = new ArrayList<String>(8);
    protected ArrayList<String> optional = new ArrayList<String>(8);
    
   
    

    protected ArrayList<String> getRequired() {
        return required;
    }

    protected void setRequired(ArrayList<String> required) {
        this.required = required;
    }


    
}

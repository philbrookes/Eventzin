/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.responses;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author craigbrookes
 */
public interface ValidResponse {
     public ApiResponse setArrayListResponse(ArrayList<HashMap<String,Object>>list);
     public ApiResponse setHashMapResponse(HashMap<String,Object>map);
      public Object getResponse();

}

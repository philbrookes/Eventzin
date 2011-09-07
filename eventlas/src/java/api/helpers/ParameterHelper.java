/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;

import api.logger.Logger;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author craigbrookes
 */
public class ParameterHelper {

    public static HashMap<String , Object> catchNullValues(Map<String, String[]>params){

        HashMap<String,Object>nullVals = new HashMap<String, Object>();
        Iterator it =  params.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();


            String value = params.get(key)[0];
            if(value.equals(""))
                nullVals.put(key, value);

        }

        return nullVals;

    }


    public static HashMap<String,Object> checkForNeededValues(Map<String,String[]>params, ArrayList<String> neededParams){

        HashMap<String,Object>result = new HashMap<String, Object>();



        for(String param : neededParams){
            if(params.containsKey(param)== false)
                result.put(param, "is needed");
        }

             return result;

    }

    public static String md5(String s){
        String hashword = null;
         try{
            MessageDigest md5  = MessageDigest.getInstance("MD5");

            md5.update(s.getBytes());

            BigInteger hash = new BigInteger(1, md5.digest());
             hashword = hash.toString(16);
        }catch(NoSuchAlgorithmException e){
                return null;
        }

        return hashword;
    }

      public static Date parseEventDate(Long evtDate)throws ParseException{

         //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(evtDate));
          Logger.debug(ParameterHelper.class.getName(), "recieved the time as "+ evtDate.toString());
           
           Date ddate = new Date();
           ddate.setTime(evtDate * 1000);


Logger.debug(ParameterHelper.class.getName(), "date created as"+ ddate.toString());
        return ddate;
                  /*
          DateFormat format =  DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.SHORT);
                Date date = null;

                 date = format.parse(evtDate);
*/



    }


   public static String createMobileKey(Long timestamp, String udid){
       return ParameterHelper.md5(timestamp.toString() + udid);
   }
   
   
   public static Long getUnixTimeStamp(){
       return new Date().getTime() /1000;
   }

}

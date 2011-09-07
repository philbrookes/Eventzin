/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.tests;

import api.out.OutputFormatter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author craigbrookes
 */
public class OutputFormatTest {

    Map<String, Object>row;
    ArrayList<Map>rows;

    public OutputFormatTest() {
        row = new HashMap<String, Object>();
        row.put("title", "this is an event");
        Map Loc = new HashMap<String, Object>();
        Loc.put("latitude", new Float(52.45443545));
        Loc.put("longitude", new Float(-6.43534543));
        row.put("location", Loc);
        row.put("summary", "this is an event summary");

        rows = new ArrayList<Map>();
        rows.add(row);
        rows.add(row);

        testformatDataToType();
    }



    public static void main(String[] args){
        OutputFormatTest test = new OutputFormatTest();

    }


    public void testformatDataToType(){
        System.out.println(OutputFormatter.formatDataToType("json", row));
        System.out.println(OutputFormatter.formatDataToType("json", rows));
    }

}

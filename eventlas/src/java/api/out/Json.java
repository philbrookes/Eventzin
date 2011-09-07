/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.out;

/**
 *
 * @author craigbrookes
 */
public class Json extends Output {

    private String toOut;

    public Json(String js) {
        super();
        this.setOutPut(js);
        toOut = super.out();
    }



    @Override
    public String out() {
        
       return toOut;
    }

    public String rawOut(){
        return toOut;
    }

    

}

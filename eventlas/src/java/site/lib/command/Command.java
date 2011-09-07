/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package site.lib.command;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author craigbrookes
 */
public class Command implements RequestCommand {

    private String action;
    private String command;
    private ArrayList<String>actionParams;
    private HttpServletRequest request;


    public Command( String command , String action, ArrayList<String>actionParams) {
        this.command = command;
        this.action = action;
        this.actionParams = actionParams;

    }

    public Command(HttpServletRequest req){
        this.request = req;
    }

    private void creatCommand(){
        if(this.request != null){
            actionParams = new ArrayList<String>(7);
            String[] urlPieces = this.request.getRequestURI().split("/");
            for(int i = 0; i < urlPieces.length; i++  ){
                if(i == 1)
                    this.command = urlPieces[i];
                else if(i == 2)
                    this.action = urlPieces[i];
                else
                    this.actionParams.add(urlPieces[i]);
                
            }

        }
    }




    @Override
    public String getAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<String> getActionParams() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Command other = (Command) obj;
        if ((this.action == null) ? (other.action != null) : !this.action.equals(other.action)) {
            return false;
        }
        if ((this.command == null) ? (other.command != null) : !this.command.equals(other.command)) {
            return false;
        }
        if (this.actionParams != other.actionParams && (this.actionParams == null || !this.actionParams.equals(other.actionParams))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.action != null ? this.action.hashCode() : 0);
        hash = 53 * hash + (this.command != null ? this.command.hashCode() : 0);
        hash = 53 * hash + (this.actionParams != null ? this.actionParams.hashCode() : 0);
        return hash;
    }

    


}

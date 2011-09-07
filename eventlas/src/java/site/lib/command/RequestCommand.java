/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package site.lib.command;

import java.util.ArrayList;

/**
 *
 * @author craigbrookes
 */
public interface RequestCommand {


    public String getCommand();

    public String getAction();

    public ArrayList<String> getActionParams();


}

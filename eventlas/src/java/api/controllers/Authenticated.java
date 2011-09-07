/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.controllers;

import com.models.ApiKeys;


/**
 *
 * @author craigbrookes
 */
public interface Authenticated {
    public boolean authenticateUser(ApiKeys key);
}

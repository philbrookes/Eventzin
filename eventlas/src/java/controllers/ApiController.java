/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

/**
 *
 * @author craigbrookes
 */
public interface ApiController<T> extends Controller {
    public T process();
}

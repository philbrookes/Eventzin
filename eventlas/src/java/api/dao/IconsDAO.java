/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Icons;


/**
 *
 * @author craigbrookes
 */
public interface IconsDAO extends GenericDAO<Icons, Integer> {
    public Icons findIcon(String iconname);
}

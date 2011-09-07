/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.Twitter;
import java.io.Serializable;

/**
 *
 * @author craigbrookes
 */
public interface TwitterDAO extends GenericDAO<Twitter, Integer> {
    public Twitter fetchByTokenAndUserName(String twitterhandle);
}

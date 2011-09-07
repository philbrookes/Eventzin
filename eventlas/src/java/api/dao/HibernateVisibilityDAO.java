/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Visibility;
import java.io.Serializable;


/**
 *
 * @author craigbrookes
 */
public class HibernateVisibilityDAO extends HibernateDAO<Visibility, Integer> implements VisibiltyDAO{

    public HibernateVisibilityDAO() {
    super(Visibility.class);
    }



}

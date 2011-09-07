/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Groups;
import org.hibernate.Query;



/**
 *
 * @author craigbrookes
 */
public class HibernateGroupDAO extends HibernateDAO<Groups, Integer> implements GroupDAO{

    public HibernateGroupDAO() {
        super(Groups.class);
    }

   

    
}

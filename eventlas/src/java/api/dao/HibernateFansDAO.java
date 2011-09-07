/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Fans;


/**
 *
 * @author craigbrookes
 */
public class HibernateFansDAO extends HibernateDAO<Fans, Integer> implements FansDAO {

    public HibernateFansDAO() {
        super(Fans.class);
    }



}

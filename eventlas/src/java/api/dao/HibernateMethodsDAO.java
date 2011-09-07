/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Methods;


/**
 *
 * @author craigbrookes
 */
public class HibernateMethodsDAO extends HibernateDAO<Methods, Integer> implements MethodsDAO {

    public HibernateMethodsDAO() {
        super(Methods.class);
    }



}

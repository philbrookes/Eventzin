/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Alphas;

/**
 *
 * @author craigbrookes
 */
public class HibernateAlphasDAO extends HibernateDAO<Alphas, Integer> implements AlphasDAO{

    public HibernateAlphasDAO() {
        super(Alphas.class);
    }



}

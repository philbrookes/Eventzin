/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.TempInvite;

/**
 *
 * @author craigbrookes
 */
public class HibernateTempInvitesDAO extends HibernateDAO<TempInvite, Integer> implements TempInvitesDAO{

    public HibernateTempInvitesDAO() {
        super(TempInvite.class);
    }

}

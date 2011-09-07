/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.AppInvites;

/**
 *
 * @author craigbrookes
 */
public interface AppInvitesDAO extends GenericDAO<AppInvites, Integer> {
    public AppInvites findByCode(String code);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Venuecomments;



/**
 *
 * @author craigbrookes
 */
public class HibernateVenuescommentsDAO extends HibernateDAO<Venuecomments, Integer> implements VenuescommentsDAO {

    public HibernateVenuescommentsDAO() {
        super(Venuecomments.class);
    }

    




}

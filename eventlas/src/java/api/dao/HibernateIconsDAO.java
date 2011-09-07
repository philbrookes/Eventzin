/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Icons;
import java.io.Serializable;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author craigbrookes
 */
public class HibernateIconsDAO extends HibernateDAO<Icons, Integer> implements IconsDAO {

    public HibernateIconsDAO() {
     super(Icons.class);
    }

    public Icons findIcon(String iconname) {
       Criteria crit = HibernateUtil.getSession().createCriteria(Icons.class);
       crit.add(Restrictions.eq("icon", iconname));
       return (Icons)crit.uniqueResult();
    }





}

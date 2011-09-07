/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Parameters;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateParametersDAO extends HibernateDAO<Parameters, Integer> implements ParametersDAO {

    public HibernateParametersDAO() {
        super(Parameters.class);
    }

    @Override
    public List<Parameters> getParamsByMethodId(Integer id) {
       Criteria crit = HibernateUtil.getSession().createCriteria(Parameters.class);
       crit.add(Restrictions.eq("methodid",id ) );
       return crit.list();
    }




}

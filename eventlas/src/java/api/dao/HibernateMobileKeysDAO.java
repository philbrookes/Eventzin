/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.MobileKeys;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateMobileKeysDAO extends HibernateDAO<MobileKeys, Integer> implements MobilekeysDAO{

    public HibernateMobileKeysDAO() {
        super(MobileKeys.class);
    }

    @Override
    public MobileKeys getKeyByUid(String uid) {
        Criteria crit = HibernateUtil.getSession().createCriteria(MobileKeys.class);
        crit.add(Restrictions.eq("uid", uid));
        return (MobileKeys)crit.uniqueResult();
    }

    @Override
    public MobileKeys getKeyByUdid(String key) {
        Criteria crit = HibernateUtil.getSession().createCriteria(MobileKeys.class);
        crit.add(Restrictions.eq("uid", key));
        return (MobileKeys)crit.uniqueResult();
    }

    public MobileKeys getByMobileKey(String key){
        Query q = HibernateUtil.getSession().createQuery("from MobileKeys mk WHERE mk.key=:key");
        q.setString("key", key);
        return (MobileKeys) q.uniqueResult();
    }

    



}

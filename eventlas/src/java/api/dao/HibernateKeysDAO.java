/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.ApiKeys;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author craigbrookes
 */
public class HibernateKeysDAO extends HibernateDAO<ApiKeys, Integer> implements ApiKeysDAO {

    public HibernateKeysDAO() {
        super(ApiKeys.class);
    }

    @Override
     public ApiKeys findKeyByApiKey(String key){
         Query q = HibernateUtil.getSession().createQuery("FROM ApiKeys akey WHERE akey.apikey=:apikeyval");
         q.setString("apikeyval", key);
         System.out.print(q.getQueryString());
         return (ApiKeys) q.uniqueResult();

     }



}

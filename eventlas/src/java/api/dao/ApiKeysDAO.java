/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.ApiKeys;


/**
 *
 * @author craigbrookes
 */
public interface ApiKeysDAO extends GenericDAO<ApiKeys, Integer>{
       public ApiKeys findKeyByApiKey(String key);
}

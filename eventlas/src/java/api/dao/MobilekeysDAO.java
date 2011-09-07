/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.MobileKeys;


/**
 *
 * @author craigbrookes
 */
public interface MobilekeysDAO extends GenericDAO<MobileKeys, Integer> {
        public MobileKeys getKeyByUid(String uid);

        public MobileKeys getKeyByUdid(String key);

        public MobileKeys getByMobileKey(String key);
}

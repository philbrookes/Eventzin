/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.EventCategorys;
import java.io.Serializable;

/**
 *
 * @author craigbrookes
 */
public class HibernateEventCategorysDAO  extends HibernateDAO<EventCategorys, Integer> implements EventCategorysDAO{

    public HibernateEventCategorysDAO() {
        super(EventCategorys.class);
    }



}

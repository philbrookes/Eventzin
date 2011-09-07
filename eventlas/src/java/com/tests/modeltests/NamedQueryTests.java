/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.modeltests;

import api.dao.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author craigbrookes
 */
public class NamedQueryTests {

    public static void main(String[] args){
        Session sess = HibernateUtil.getSession();
        sess.beginTransaction();
        Query q =  sess.getNamedQuery("findNearbyLocations");
        q.setFloat("longitudeval", new Float(-7.4545));
        q.setFloat("latitudeval", new Float(53.545437));
        List l = q.list();
        
        HibernateUtil.commitTransaction();
    }

}

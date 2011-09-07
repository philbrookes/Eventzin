/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author craigbrookes
 */
public class HibernateSessionFactory implements ObjectFactory{

    @Override
    public Object getObjectInstance(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl) throws Exception {
        SessionFactory sessionFactory = null;
        RefAddr addr = null;

      try{

         Enumeration addrs = ((Reference)(o)).getAll();

         while(addrs.hasMoreElements()){
            addr = (RefAddr) addrs.nextElement();
            if("configuration".equals((String)(addr.getType()))){
               sessionFactory = (new Configuration())
                    .configure((String)addr.getContent()).buildSessionFactory();
            }
         }
      }catch(Exception ex){
         throw new javax.naming.NamingException(ex.getMessage());
      }

      return sessionFactory;

    }



}

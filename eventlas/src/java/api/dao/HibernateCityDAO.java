/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.City;
import org.hibernate.Query;

/**
 *
 * @author craigbrookes
 */
public class HibernateCityDAO extends HibernateDAO<City, Integer> implements CityDAO {

    public HibernateCityDAO() {
        super(City.class);
    }

    @Override
    public City getByCityAndCountryCode(String city, String countryCode) {
       String sql = "SELECT citys.* FROM countrys LEFT JOIN citys on citys.countryid=countrys.id WHERE "
               + "countrys.countrycode=UPPER(:country) AND citys.city=:city";
       Query q = HibernateUtil.getSession().createSQLQuery(sql).addEntity(City.class);
       q.setString("country", countryCode);
       q.setString("city", city);
       return (City) q.uniqueResult();
       
               
    }

    
    
    
    
    
}

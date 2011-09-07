/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.dao;

import com.models.City;
import java.io.Serializable;

/**
 *
 * @author craigbrookes
 */
public interface CityDAO extends GenericDAO<City, Integer> {
    public City getByCityAndCountryCode(String city, String countryCode);
}

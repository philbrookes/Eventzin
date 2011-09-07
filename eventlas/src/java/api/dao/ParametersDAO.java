/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.Parameters;
import java.util.List;



/**
 *
 * @author craigbrookes
 */
public interface ParametersDAO extends GenericDAO<Parameters, Integer>{

    public List<Parameters> getParamsByMethodId(Integer id);
}

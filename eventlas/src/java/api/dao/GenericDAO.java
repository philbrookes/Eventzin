/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public interface GenericDAO<T ,ID extends Serializable> {

    T findByPrimaryKey(ID id);
    List<T>findAll(int StartIndex, int fetchSize);
    List<T>findByExample(T example, String[] exclude);
    T save(T entity);
    T update(T entity);
    void delete(T entity);
    void beginTransaction();
    void commitTransaction();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;

/**
 *
 * @author craigbrookes
 */
public abstract class HibernateDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

    private Class<T> persistenceClass;

    public HibernateDAO(Class c){
        persistenceClass = c;
    }

    public T findByPrimaryKey(ID id) {
        return (T) HibernateUtil.getSession().load(persistenceClass, id);
    }

    public T save(T entity) {
      HibernateUtil.getSession().save(entity);
      return entity;
    }

    public void delete(T entity) {
        HibernateUtil.getSession().delete(entity);
    }

    public void beginTransaction() {
       HibernateUtil.beginTransaction();
    }

    public void commitTransaction() {
       HibernateUtil.commitTransaction();
    }

    public List<T> findByExample(T example, String[] exclude) {
      Criteria crit = HibernateUtil.getSession()
              .createCriteria(persistenceClass);
      Example exam = Example.create(example);
      if(exclude !=null){
          for(int i = 0; i < exclude.length; i++){
              exam.excludeProperty(exclude[i]);
          }
      }
      crit.add(exam);
      return crit.list();
    }

    public List<T> findAll(int startIndex, int fetchSize) {
      Criteria crit = HibernateUtil.getSession()
              .createCriteria(persistenceClass);
      crit.setFirstResult(startIndex);
      crit.setFetchSize(fetchSize);
      return crit.list();
    }

    public T update(T entity){
        HibernateUtil.getSession().update(entity);
        return entity;
    }



}

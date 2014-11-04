package com.bytearrays.onecthings.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by dobrescu on 3/5/14.
 */
@Repository
public class AbstractDaoImpl<T> implements AbstractDao<T> {


    @Autowired
    private SessionFactory sessionFactory;

    public Class<T> getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T find(Integer id) {
        T entity = (T) sessionFactory.getCurrentSession().get(getPersistentClass(), id);
        return entity;
    }

    @Override
    public List<T> findAll() {
        return findByCriteria();
    }

    @Override
    public T makePersistent(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }


    @Override
    public void delete(T obj) {
        sessionFactory.getCurrentSession().delete(obj);

    }

    @Override
    public void refresh(T obj) {
        sessionFactory.getCurrentSession().refresh(obj);
    }

    @Override
    public void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }


    @Override
    public T merge(T obj) {
        Object tmpObject = sessionFactory.getCurrentSession().merge(obj);
        return (T) tmpObject;

    }

    @Override
    public void saveOrUpdate(T obj) {
        sessionFactory.getCurrentSession().saveOrUpdate(obj);

    }

    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }
}

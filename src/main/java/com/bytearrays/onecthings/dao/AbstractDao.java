package com.bytearrays.onecthings.dao;

import java.util.List;

/**
 * Created by dobrescu on 3/5/14.
 */
public interface AbstractDao<T> {

    public T find(Integer id);

    void flush();

    public void delete(T obj);

    void refresh(T obj);

    void rollback();

    T merge(T obj);

    public void saveOrUpdate(T obj);

    public T makePersistent(T entity);

    public List<T> findAll();
}


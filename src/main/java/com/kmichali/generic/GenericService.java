package com.kmichali.generic;

import java.util.List;

public interface GenericService<T extends Object> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    void delete(long id);

    T find(long id);

    Iterable<T> findAll();

}

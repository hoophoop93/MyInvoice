package com.kmichali.repository;

import com.kmichali.model.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<Store,Long> {

    Store findByName(String name);
}

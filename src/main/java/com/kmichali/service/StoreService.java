package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Store;

public interface StoreService extends GenericService<Store> {

    boolean countProductStore(String name);
    Store findByName(String name);
}

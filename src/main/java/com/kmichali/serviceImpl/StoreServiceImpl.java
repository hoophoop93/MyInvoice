package com.kmichali.serviceImpl;

import com.kmichali.model.Store;
import com.kmichali.repository.StoreRepository;
import com.kmichali.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Override
    public Store save(Store entity) {
        entity.setAmount(entity.getAmount());
        entity.setName(entity.getName());

        return storeRepository.save(entity);
    }

    @Override
    public Store update(Store entity) {
        return null;
    }

    @Override
    public void delete(Store entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Store find(long id) {
        return null;
    }

    @Override
    public Iterable<Store> findAll() {
        return storeRepository.findAll();
    }
}

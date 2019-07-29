package com.kmichali.serviceImpl;

import com.kmichali.model.Store;
import com.kmichali.repository.CustomerRepostiory;
import com.kmichali.repository.StoreRepository;
import com.kmichali.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Store save(Store entity) {
        entity.setAmount(entity.getAmount());
        entity.setName(entity.getName());

        return storeRepository.save(entity);
    }

    @Override
    public Store update(Store entity) {
        return storeRepository.save(entity);
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


    @Override
    public boolean countProductStore(String name) {
        String queryString="Select count(c.name) from Store c where LOWER(c.name) = :name";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("name",name.toLowerCase());
        long result = (long) query.getSingleResult();
        return result > 0;
    }

    @Override
    public Store findByName(String name) {
        return storeRepository.findByName(name);
    }
}

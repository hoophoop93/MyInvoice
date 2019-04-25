package com.kmichali.serviceImpl;

import com.kmichali.model.Seller;
import com.kmichali.repository.SellerRepository;
import com.kmichali.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Seller save(Seller entity) {
        return sellerRepository.save(entity);
    }

    @Override
    public Seller update(Seller entity) {
        return sellerRepository.save(entity);
    }

    @Override
    public void delete(Seller entity) {
        sellerRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        sellerRepository.delete(id);
    }

    @Override
    public Seller find(long id) {
        return sellerRepository.findOne(id);
    }

    @Override
    public Iterable<Seller> findAll() {
        return sellerRepository.findAll();
    }
    @Override
    public Seller findByName(String name) {
        return sellerRepository.findByName(name);
    }
}

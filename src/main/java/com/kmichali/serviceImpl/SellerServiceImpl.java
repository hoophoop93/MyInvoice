package com.kmichali;

import com.kmichali.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public Iterable<Seller> findAll(){
        return sellerRepository.findAll();
    }
}

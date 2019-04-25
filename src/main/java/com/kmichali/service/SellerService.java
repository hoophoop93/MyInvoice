package com.kmichali.service;

import com.kmichali.model.Seller;
import com.kmichali.generic.GenericService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

public interface SellerService extends GenericService<Seller> {

    Seller findByName(String name);
}

package com.kmichali.repository;

import com.kmichali.model.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends CrudRepository<Seller, Long> {

    Seller findByName(String name);

}

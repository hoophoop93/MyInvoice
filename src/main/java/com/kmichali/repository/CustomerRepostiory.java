package com.kmichali.repository;

import com.kmichali.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepostiory extends CrudRepository<Customer, Long> {

    public Customer findById(long id);

}

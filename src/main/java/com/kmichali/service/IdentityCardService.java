package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Customer;
import com.kmichali.model.IdentityCard;
import org.springframework.data.repository.CrudRepository;

public interface IdentityCardService extends GenericService<IdentityCard> {
    IdentityCard findByCustomer(Customer customer);
}

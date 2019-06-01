package com.kmichali.repository;

import com.kmichali.model.Customer;
import com.kmichali.model.Date;
import com.kmichali.model.IdentityCard;
import org.springframework.data.repository.CrudRepository;

public interface IdentityCardRepository  extends CrudRepository<IdentityCard,Long> {
    IdentityCard findByCustomer(Customer customer);
}

package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Customer;

import java.util.List;

public interface CustomerService extends GenericService<Customer> {

     boolean countCustomerByPesel(String pesel);
     boolean countCustomerByNip(String nip);

}

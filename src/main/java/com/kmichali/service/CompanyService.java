package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Company;
import com.kmichali.model.Customer;
import com.kmichali.model.Seller;

public interface CompanyService extends GenericService<Company> {
    Company findByCustomer(Customer customer);
    Company findBySeller(Seller seller);
    boolean countByNip(String nip);
}

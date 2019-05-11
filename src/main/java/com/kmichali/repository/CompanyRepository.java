package com.kmichali.repository;

import com.kmichali.model.Company;
import com.kmichali.model.Customer;
import com.kmichali.model.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company,Long> {

        Company findByCustomer(Customer customer);
        Company findBySeller(Seller seller);

}

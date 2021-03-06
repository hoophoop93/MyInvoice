package com.kmichali.serviceImpl;

import com.kmichali.model.Customer;
import com.kmichali.repository.CustomerRepostiory;
import com.kmichali.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepostiory customerRepostiory;
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Customer findCustomerByNip(String nip) {
        Customer customer = null;
        String queryString = "Select o from Customer o, Company c where o.id = c.customer.id and c.nip = :nip";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("nip", nip);
        try {
            customer = (Customer) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return customer;
    }
    public Customer findCustomerByPesel(String pesel) {
        Customer customer = null;
        String queryString = "Select o from Customer o where LOWER(o.pesel) = :pesel";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("pesel", pesel.toLowerCase());
        try {
            customer = (Customer) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return customer;
    }
    public Customer findCustomer(String name, String surname, String address) {
        Customer customer = null;
        String queryString = "Select o from Customer o where LOWER(o.name) = :name and LOWER(o.surname) = :surname " +
                "and LOWER(o.address) = :address ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("name", name.toLowerCase());
        query.setParameter("surname", surname.toLowerCase());
        query.setParameter("address", address.toLowerCase());
        try {
            customer = (Customer) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return customer;
    }
    @Override
    public boolean countCustomerByNip(String nip){
       String queryString="Select count(c.nip) from Customer o, Company c where o.id = c.customer.id and c.nip = :nip";
       Query query = getEntityManager().createQuery(queryString);
       query.setParameter("nip",nip.toLowerCase());
        long result = (long) query.getSingleResult();
        return result > 0;
    }
    @Override
    public boolean countCustomerByPesel(String pesel){
        String queryString="Select count(c.pesel) from Customer c where LOWER(c.pesel) = :pesel";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("pesel",pesel.toLowerCase());
        long result = (long) query.getSingleResult();
        return result > 0;
    }

    @Override
    public Customer save(Customer customer) {
        customer.setName(customer.getName());
        customer.setSurname(customer.getSurname());
        customer.setAddress(customer.getAddress());
        customer.setCity(customer.getCity());
        customer.setPostalCode(customer.getPostalCode());
        return customerRepostiory.save(customer);
    }

    @Override
    public Customer update(Customer entity) {
        return customerRepostiory.save(entity);
    }

    @Override
    public void delete(Customer entity) {
        customerRepostiory.delete(entity);
    }

    @Override
    public void delete(long id) {
    customerRepostiory.delete(id);
    }

    @Override
    public Customer find(long id) {
        return customerRepostiory.findById(id);
    }

    @Override
    public Iterable<Customer> findAll() {
        return customerRepostiory.findAll();
    }

}

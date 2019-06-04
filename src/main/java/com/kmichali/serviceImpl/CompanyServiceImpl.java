package com.kmichali.serviceImpl;

import com.kmichali.model.Company;
import com.kmichali.model.Customer;
import com.kmichali.model.Seller;
import com.kmichali.repository.CompanyRepository;
import com.kmichali.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Company save(Company entity) {
        entity.setName(entity.getName());
        entity.setNip(entity.getNip());
        entity.setPhoneNumber(entity.getPhoneNumber());
        entity.setRegon(entity.getRegon());
        return companyRepository.save(entity);
    }

    @Override
    public Company update(Company entity) {
        return companyRepository.save(entity);
    }

    @Override
    public void delete(Company entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Company find(long id) {
        return null;
    }

    @Override
    public Iterable<Company> findAll() {
        return null;
    }

    @Override
    public Company findByCustomer(Customer customer) {
        return companyRepository.findByCustomer(customer);
    }

    @Override
    public Company findBySeller(Seller seller) {
        return companyRepository.findBySeller(seller);
    }

    @Override
    public boolean countByNip(String nip) {
        String queryString="Select count(c.nip) from Company c where LOWER(c.nip) = :nip";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("nip",nip.toLowerCase());
        long result = (long) query.getSingleResult();
        return result > 0;
    }
}

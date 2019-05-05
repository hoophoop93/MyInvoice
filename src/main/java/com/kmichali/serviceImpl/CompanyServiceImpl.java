package com.kmichali.serviceImpl;

import com.kmichali.model.Company;
import com.kmichali.repository.CompanyRepository;
import com.kmichali.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

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
        return null;
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
}

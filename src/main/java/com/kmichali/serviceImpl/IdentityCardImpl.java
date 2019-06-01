package com.kmichali.serviceImpl;

import com.kmichali.model.Customer;
import com.kmichali.model.IdentityCard;
import com.kmichali.repository.IdentityCardRepository;
import com.kmichali.service.IdentityCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityCardImpl implements IdentityCardService {

    @Autowired
    IdentityCardRepository identityCardRepository;

    @Override
    public IdentityCard save(IdentityCard entity) {
        entity.setOrganization(entity.getOrganization());
        entity.setSeriaAndNumber(entity.getSeriaAndNumber());
        entity.setReleaseDate(entity.getReleaseDate());
        return identityCardRepository.save(entity);
    }

    @Override
    public IdentityCard update(IdentityCard entity) {
        return null;
    }

    @Override
    public void delete(IdentityCard entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public IdentityCard find(long id) {
        return null;
    }

    @Override
    public Iterable<IdentityCard> findAll() {
        return null;
    }

    @Override
    public IdentityCard findByCustomer(Customer customer) {
        return identityCardRepository.findByCustomer(customer);
    }
}

package com.kmichali.serviceImpl;

import com.kmichali.model.Date;
import com.kmichali.repository.DateRepository;
import com.kmichali.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DateServiceImpl implements DateService {

    @Autowired
    DateRepository dateRepository;

    @Override
    public Date save(Date entity) {
        entity.setIssueDate(entity.getIssueDate());
        entity.setPaidDate(entity.getPaidDate());
        entity.setPaymentDate(entity.getPaymentDate());

        return dateRepository.save(entity);
    }

    @Override
    public Date update(Date entity) {
        return null;
    }

    @Override
    public void delete(Date entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Date find(long id) {
        return null;
    }

    @Override
    public Iterable<Date> findAll() {
        return null;
    }

}

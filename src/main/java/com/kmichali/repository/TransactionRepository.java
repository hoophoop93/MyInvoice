package com.kmichali.repository;

import com.kmichali.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("Select t,s,i,d from Transaction t, Store s,Invoice i, Date d where t.store.id = s.id and " +
            "t.invoice.id = i.id and d.id = i.date.id and  s.name= :name ")
    List<Transaction> findAllTransactionByProductName(@Param("name")String name);
}

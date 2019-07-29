package com.kmichali.repository;

import com.kmichali.model.EmptyTransaction;
import org.springframework.data.repository.CrudRepository;

public interface EmptyTransactionRepository extends CrudRepository<EmptyTransaction,Long> {
}

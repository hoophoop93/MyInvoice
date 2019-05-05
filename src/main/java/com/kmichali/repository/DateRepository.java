package com.kmichali.repository;

import com.kmichali.model.Date;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends CrudRepository<Date,Long> {
}

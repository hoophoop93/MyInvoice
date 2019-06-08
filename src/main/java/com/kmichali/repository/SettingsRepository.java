package com.kmichali.repository;

import com.kmichali.model.Settings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository  extends CrudRepository<Settings,Long> {
}

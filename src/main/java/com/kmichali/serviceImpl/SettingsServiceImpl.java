package com.kmichali.serviceImpl;


import com.kmichali.model.Settings;
import com.kmichali.repository.SettingsRepository;
import com.kmichali.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsRepository settingsRepository;
    @Override
    public Settings save(Settings entity) {
        return settingsRepository.save(entity);
    }

    @Override
    public Settings update(Settings entity) {
        return settingsRepository.save(entity);
    }

    @Override
    public void delete(Settings entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Settings find(long id) {
        return settingsRepository.findOne(id);
    }

    @Override
    public Iterable<Settings> findAll() {
        return null;
    }
}

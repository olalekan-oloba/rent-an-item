package com.omegalambdang.rentanitem.feature.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreConfigurationServiceImpl implements CoreConfigurationService{

    private final CoreConfigurationRepository repository;
    @Autowired
    public CoreConfigurationServiceImpl(CoreConfigurationRepository repository) {
        this.repository = repository;
    }
    @Override
    public String findConfigValueByKey(String configKey) {
        //TODO: return data from cache or db
        return this.repository.findByConfigurationKey(configKey);
    }
}

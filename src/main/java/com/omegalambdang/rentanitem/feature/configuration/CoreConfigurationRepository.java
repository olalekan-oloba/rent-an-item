package com.omegalambdang.rentanitem.feature.configuration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoreConfigurationRepository extends JpaRepository<CoreConfiguration,Long> {
    @Query("select s.value from CoreConfiguration s where s.configurationKey = ?1")
    String findByConfigurationKey(String key);
}

package com.omegalambdang.rentanitem.feature.account.rentor;

import org.springframework.data.jpa.repository.JpaRepository;
public interface RentorRepository extends JpaRepository<Rentor,Long> {
    boolean existsByEmail(String email);
}

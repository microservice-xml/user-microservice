package com.example.usermicroservice.repository;

import com.example.usermicroservice.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findALlByHostId(Long id);

    List<Rate> findAllByGuestId(Long id);

    List<Rate> findAllByHostId(Long id);
}
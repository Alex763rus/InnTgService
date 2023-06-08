package com.example.inntgservice.model.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatisticRepository extends CrudRepository<Statistic, Long> {
    @Override
    public List<Statistic> findAll();
}

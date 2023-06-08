package com.example.inntgservice.model.jpa;

import com.example.inntgservice.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InnInfoRepository extends CrudRepository<InnInfo, Long> {

}

package com.example.inntgservice.model.jpa;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InnCrossLinkRepository extends CrudRepository<InnCrosslink, Long> {

    List<InnCrosslink> findAll(Example<InnCrosslink> crosslinkExample);
    List<InnCrosslink> findAllByInnCrosslinkKeyEquals(InnCrosslinkKey innCrosslinkKey);

    List<InnCrosslink> findByInnCrosslinkKeyInnFirst(Long InnFirst);
    List<InnCrosslink> findByInnCrosslinkKeyInnSecond(Long InnFirst);
}

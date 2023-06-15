package com.example.inntgservice.model.jpa;

import com.example.inntgservice.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InnInfoRepository extends CrudRepository<InnInfo, Long> {

    List<InnInfo> findTop10ByHeadInnIs(Long Inn);
    List<InnInfo> findTop10ByPhoneContains(String phone);
    List<InnInfo> findTop10ByWebsiteContains(String website);
    List<InnInfo> findTop10ByMailContains(String website);
}

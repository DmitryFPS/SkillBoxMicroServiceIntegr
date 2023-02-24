package com.test.test.service;

import com.test.test.entity.Office;

import java.util.List;
import java.util.Optional;

public interface OfficeService {
    Optional<Office> findOffice(Long id);

    List<Office> fiendAllOffice();

    Office save(Office office);

    boolean delete(Long id);
}

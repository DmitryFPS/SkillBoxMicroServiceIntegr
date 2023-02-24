package com.test.test.repository;

import com.test.test.entity.Office;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OfficeRepository extends CrudRepository<Office, Long> {
    @NonNull
    List<Office> findAll();
}
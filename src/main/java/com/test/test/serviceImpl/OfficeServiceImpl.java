package com.test.test.serviceImpl;

import com.test.test.entity.Office;
import com.test.test.repository.OfficeRepository;
import com.test.test.service.OfficeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfficeServiceImpl implements OfficeService {

    final OfficeRepository repository;

    @Override
    public Optional<Office> findOffice(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Office> fiendAllOffice() {
        return repository.findAll();
    }

    @Override
    public Office save(Office office) {
        return repository.save(office);
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}

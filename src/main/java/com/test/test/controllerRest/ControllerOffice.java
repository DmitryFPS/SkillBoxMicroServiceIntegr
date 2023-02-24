package com.test.test.controllerRest;

import com.test.test.entity.Office;
import com.test.test.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/office")
public class ControllerOffice {

    final OfficeService service;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Office> getOffice(@PathVariable Long id) {
        Office office = service.findOffice(id).orElse(null);
        if (office == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(office);
    }

    @GetMapping
    public ResponseEntity<List<Office>> getAllOffice() {
        List<Office> offices = service.fiendAllOffice();
        if (offices.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(offices);
    }

    @PostMapping
    public ResponseEntity<Office> postOffice(@RequestBody Office office) {
        Office savedOffice = service.save(office);
        if (savedOffice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(savedOffice);
    }

    @PutMapping
    public ResponseEntity<Office> putOffice(@RequestBody Office office) {
        Office putOffice = service.save(office);
        if (putOffice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(putOffice);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> deleteOffice(@PathVariable Long id) {
        boolean isDeleted = service.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

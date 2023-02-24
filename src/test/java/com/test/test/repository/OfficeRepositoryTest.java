package com.test.test.repository;

import com.test.test.TestApplication;
import com.test.test.entity.Office;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
class OfficeRepositoryTest {
    OfficeRepository repository;
    AnnotationConfigApplicationContext context;

    @BeforeEach
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_dao_h2" + UUID.randomUUID());
        System.setProperty("jdbcUsername", "h2");
        System.setProperty("jdbcPassword", "");
        System.setProperty("file", "liquibase_repository_test.yaml");
        context = new AnnotationConfigApplicationContext(TestApplication.class);
        repository = context.getBean(OfficeRepository.class);
    }

    @AfterEach
    public void closedContext() {
        context.close();
    }

    @ParameterizedTest
    @MethodSource("parametersFindOffice")
    void testFindOfficeById(Long id, Integer number, Integer phoneOffice, String address, boolean deleted) {
        Office office = repository.findById(id).orElse(null);
        Assertions.assertNotNull(office);
        Assertions.assertEquals(id, office.getId());
        Assertions.assertEquals(number, office.getNumber());
        Assertions.assertEquals(phoneOffice, office.getPhoneOffice());
        Assertions.assertEquals(address, office.getAddress());
        Assertions.assertEquals(deleted, office.isDeleted());
    }

    private static Object[] parametersFindOffice() {
        return new Object[]{
                new Object[]{1L, 301, 778899, "Samara_1", false},
                new Object[]{2L, 303, 113355, "Samara_2", false},
                new Object[]{3L, 305, 663344, "Samara_3", false}
        };
    }

    @Test
    void testFindOfficeByIdFailed() {
        Office office = repository.findById(4L).orElse(null);
        Assertions.assertNull(office);
    }

    @Test
    void testFindAllOffice() {
        List<Office> offices = repository.findAll();
        Assertions.assertEquals(3, offices.size());
    }

    @Test
    void testPostOffice() {
        Office office = new Office(333, 996633, "Samara_777", false);
        Office savedOffice = repository.save(office);
        Assertions.assertNotNull(savedOffice.getId());
        Assertions.assertEquals(333, savedOffice.getNumber());
        Assertions.assertEquals(996633, savedOffice.getPhoneOffice());
        Assertions.assertEquals("Samara_777", savedOffice.getAddress());
        Assertions.assertFalse(savedOffice.isDeleted());
    }

    @Test
    void testPutEmployee() {
        Office office = repository.findById(1L).orElse(null);
        Assertions.assertNotNull(office);
        office.setNumber(777111);
        office.setPhoneOffice(330011);
        office.setAddress("test");
        office.setDeleted(false);

        Office updatedOffice = repository.save(office);
        Assertions.assertEquals(1L, updatedOffice.getId());
        Assertions.assertEquals(777111, updatedOffice.getNumber());
        Assertions.assertEquals(330011, updatedOffice.getPhoneOffice());
        Assertions.assertEquals("test", updatedOffice.getAddress());
        Assertions.assertFalse(updatedOffice.isDeleted());
    }

    @Test
    void testDeleteEmployeeIsSuccess() {
        repository.deleteById(1L);
    }

    @Test
    void testDeleteEmployeeIsNotSuccess() {
        Assertions.assertThrows(Exception.class, () -> {
            repository.deleteById(888L);
        });
    }
}

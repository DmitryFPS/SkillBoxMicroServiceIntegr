package com.test.test.repository;

import com.test.test.TestApplication;
import com.test.test.entity.Employee;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
class EmployeeRepositoryTest {

    EmployeeRepository repository;
    AnnotationConfigApplicationContext context;

    @BeforeEach
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_dao_h2" + UUID.randomUUID());
        System.setProperty("jdbcUsername", "h2");
        System.setProperty("jdbcPassword", "");
        System.setProperty("file", "liquibase_repository_test.yaml");
        context = new AnnotationConfigApplicationContext(TestApplication.class);
        repository = context.getBean(EmployeeRepository.class);
    }

    @AfterEach
    public void closedContext() {
        context.close();
    }

    @ParameterizedTest
    @MethodSource("parametersFindEmployee")
    void testFindOfficeById(Long id, String lastName, String firstName, String surName, Timestamp birthday,
                            String position, Integer extensionNumber, boolean deleted, Long officeId) {
        Employee employee = repository.findById(id).orElse(null);
        Assertions.assertNotNull(employee);
        Assertions.assertEquals(id, employee.getId());
        Assertions.assertEquals(lastName, employee.getLastName());
        Assertions.assertEquals(firstName, employee.getFirstName());
        Assertions.assertEquals(surName, employee.getSurName());
        Assertions.assertEquals(birthday, employee.getBirthday());
        Assertions.assertEquals(position, employee.getPosition());
        Assertions.assertEquals(extensionNumber, employee.getExtensionNumber());
        Assertions.assertFalse(deleted);
        Assertions.assertEquals(officeId, employee.getOfficeId());
    }

    private static Object[] parametersFindEmployee() {
        return new Object[]{
                new Object[]{1L, "Dima_1", "Orlov_1", "Vladimirovich_1", Timestamp.valueOf("1985-12-12 00:00:00.0"), "position_1", 111, false, 1L},
                new Object[]{2L, "Dima_2", "Orlov_2", "Vladimirovich_2", Timestamp.valueOf("1985-12-12 00:00:00.0"), "position_2", 222, false, 2L},
                new Object[]{3L, "Dima_3", "Orlov_3", "Vladimirovich_3", Timestamp.valueOf("1985-12-12 00:00:00.0"), "position_3", 333, false, 3L}
        };
    }

    @Test
    void testFindOfficeByIdFailed() {
        Employee employee = repository.findById(4L).orElse(null);
        Assertions.assertNull(employee);
    }

    @Test
    void testFindAllOffice() {
        List<Employee> employees = repository.findAll();
        Assertions.assertEquals(3, employees.size());
    }

    @Test
    void testPostOffice() {
        Employee employee = new Employee("Dima_test", "Orlov_test", "Vladimirovich_test",
                Timestamp.valueOf("1985-12-12 00:00:00.0"), "position_test", 111, false, 1L);
        Employee savedEmployee = repository.save(employee);

        Assertions.assertNotNull(savedEmployee);
        Assertions.assertNotNull(savedEmployee.getId());
        Assertions.assertEquals("Dima_test", employee.getLastName());
        Assertions.assertEquals("Orlov_test", employee.getFirstName());
        Assertions.assertEquals("Vladimirovich_test", employee.getSurName());
        Assertions.assertEquals(Timestamp.valueOf("1985-12-12 00:00:00.0"), employee.getBirthday());
        Assertions.assertEquals("position_test", employee.getPosition());
        Assertions.assertEquals(111, employee.getExtensionNumber());
        Assertions.assertFalse(savedEmployee.isDeleted());
        Assertions.assertEquals(1L, employee.getOfficeId());
    }

    @Test
    void testPutEmployee() {
        Employee employee = repository.findById(1L).orElse(null);
        Assertions.assertNotNull(employee);
        employee.setLastName("test_1");
        employee.setFirstName("test_2");
        employee.setSurName("test_3");
        employee.setPosition("test_4");
        employee.setExtensionNumber(777);

        Employee updatedEmployee = repository.save(employee);
        Assertions.assertEquals(1L, updatedEmployee.getId());
        Assertions.assertEquals("test_1", updatedEmployee.getLastName());
        Assertions.assertEquals("test_2", updatedEmployee.getFirstName());
        Assertions.assertEquals("test_3", updatedEmployee.getSurName());
        Assertions.assertEquals("test_4", updatedEmployee.getPosition());
        Assertions.assertEquals(777, updatedEmployee.getExtensionNumber());
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

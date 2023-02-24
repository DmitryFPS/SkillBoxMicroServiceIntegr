package com.test.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.test.entity.Employee;
import com.test.test.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    EmployeeRepository repository;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void testGetEmployeeIsSuccess() throws Exception {
        Employee employeeWithId = new Employee(777L, "DimaTest", "OrlovTest", "VladimirovichTest",
                Timestamp.valueOf("1990-10-10 10:10:10.3"), "position", 333, false, 1L);

        when(repository.findById(777L)).thenReturn(Optional.of(employeeWithId));
        mockMvc.perform(
                        get("http://localhost:8080/api/employee/" + 777L)
                                .content(objectMapper.writeValueAsString(employeeWithId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.lastName", Is.is("DimaTest")))
                .andExpect(jsonPath("$.firstName", Is.is("OrlovTest")))
                .andExpect(jsonPath("$.surName", Is.is("VladimirovichTest")))
                .andExpect(jsonPath("$.position", Is.is("position")))
                .andExpect(jsonPath("$.extensionNumber", Is.is(333)))
                .andExpect(jsonPath("$.deleted", Is.is(false)))
                .andExpect(jsonPath("$.officeId", Is.is(1)));

    }

    @Test
    void testGetEmployeeIsNotSuccess() throws Exception {
        mockMvc.perform(
                        get("http://localhost:8080/api/employee/" + 888L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllEmployeeIsSuccess() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(111L, "test1", "test1", "test1", Timestamp.valueOf("1990-10-10 10:10:10.3"), "test1", 111, false, 1L),
                new Employee(222L, "test2", "test2", "test2", Timestamp.valueOf("1990-11-11 11:10:10.3"), "test2", 222, false, 2L),
                new Employee(333L, "test3", "test3", "test3", Timestamp.valueOf("1990-10-10 10:10:10.3"), "test3", 333, false, 3L)
        );
        when(repository.findAll()).thenReturn(employees);
        mockMvc.perform(
                        get("http://localhost:8080/api/employee")
                                .content(objectMapper.writeValueAsString(employees))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(3)))

                .andExpect(jsonPath("$.[0].id", Is.is(111)))
                .andExpect(jsonPath("$.[0].lastName", Is.is("test1")))
                .andExpect(jsonPath("$.[0].firstName", Is.is("test1")))
                .andExpect(jsonPath("$.[0].surName", Is.is("test1")))
                .andExpect(jsonPath("$.[0].position", Is.is("test1")))
                .andExpect(jsonPath("$.[0].extensionNumber", Is.is(111)))
                .andExpect(jsonPath("$.[0].deleted", Is.is(false)))
                .andExpect(jsonPath("$.[0].officeId", Is.is(1)))

                .andExpect(jsonPath("$.[1].id", Is.is(222)))
                .andExpect(jsonPath("$.[1].lastName", Is.is("test2")))
                .andExpect(jsonPath("$.[1].firstName", Is.is("test2")))
                .andExpect(jsonPath("$.[1].surName", Is.is("test2")))
                .andExpect(jsonPath("$.[1].position", Is.is("test2")))
                .andExpect(jsonPath("$.[1].extensionNumber", Is.is(222)))
                .andExpect(jsonPath("$.[1].deleted", Is.is(false)))
                .andExpect(jsonPath("$.[1].officeId", Is.is(2)))

                .andExpect(jsonPath("$.[2].id", Is.is(333)))
                .andExpect(jsonPath("$.[2].lastName", Is.is("test3")))
                .andExpect(jsonPath("$.[2].firstName", Is.is("test3")))
                .andExpect(jsonPath("$.[2].surName", Is.is("test3")))
                .andExpect(jsonPath("$.[2].position", Is.is("test3")))
                .andExpect(jsonPath("$.[2].extensionNumber", Is.is(333)))
                .andExpect(jsonPath("$.[2].deleted", Is.is(false)))
                .andExpect(jsonPath("$.[2].officeId", Is.is(3)));
    }

    @Test
    void testPostEmployee() throws Exception {
        Employee employeeWithoutId = new Employee("DimaTest", "OrlovTest", "VladimirovichTest", Timestamp.valueOf("1990-10-10 10:10:10.3"),
                "position", 333, false, 1L);
        Employee employeeWithId = new Employee(777L, "DimaTest", "OrlovTest", "VladimirovichTest",
                Timestamp.valueOf("1990-10-10 10:10:10.3"), "position", 333, false, 1L);
        when(repository.save(employeeWithoutId)).thenReturn(employeeWithId);
        mockMvc.perform(
                        post("http://localhost:8080/api/employee")
                                .content(objectMapper.writeValueAsString(employeeWithoutId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.lastName", Is.is("DimaTest")))
                .andExpect(jsonPath("$.firstName", Is.is("OrlovTest")))
                .andExpect(jsonPath("$.surName", Is.is("VladimirovichTest")))
                .andExpect(jsonPath("$.position", Is.is("position")))
                .andExpect(jsonPath("$.extensionNumber", Is.is(333)))
                .andExpect(jsonPath("$.deleted", Is.is(false)))
                .andExpect(jsonPath("$.officeId", Is.is(1)));
    }

    @Test
    void testPutEmployee() throws Exception {
        Employee employeeWithId = new Employee(777L, "DimaTest", "OrlovTest", "VladimirovichTest",
                Timestamp.valueOf("1990-10-10 10:10:10.3"), "position", 333, false, 1L);
        Employee employeeUpdateWithId = new Employee(777L, "DimaTest1", "OrlovTest1", "VladimirovichTest1",
                Timestamp.valueOf("1990-10-10 10:10:10.3"), "position1", 333, false, 1L);
        when(repository.save(employeeWithId)).thenReturn(employeeUpdateWithId);

        mockMvc.perform(
                        put("http://localhost:8080/api/employee")
                                .content(objectMapper.writeValueAsString(employeeWithId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.lastName", Is.is("DimaTest1")))
                .andExpect(jsonPath("$.firstName", Is.is("OrlovTest1")))
                .andExpect(jsonPath("$.surName", Is.is("VladimirovichTest1")))
                .andExpect(jsonPath("$.position", Is.is("position1")))
                .andExpect(jsonPath("$.extensionNumber", Is.is(333)))
                .andExpect(jsonPath("$.deleted", Is.is(false)))
                .andExpect(jsonPath("$.officeId", Is.is(1)));
    }

    @Test
    void testDeleteEmployeeIsSuccess() throws Exception {
        when(repository.existsById(777L)).thenReturn(true);
        doNothing().when(repository).deleteById(777L);

        mockMvc.perform(
                        delete("http://localhost:8080/api/employee/" + 777L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEmployeeIsNotSuccess() throws Exception {
        when(repository.existsById(888L)).thenReturn(false);
        doNothing().when(repository).deleteById(888L);

        mockMvc.perform(
                        delete("http://localhost:8080/api/employee/" + 888L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }
}

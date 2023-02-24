package com.test.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.test.entity.Office;
import com.test.test.repository.OfficeRepository;
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

import java.util.Arrays;
import java.util.Collections;
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
public class OfficeServiceTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    OfficeRepository repository;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void testGetOfficeIsSuccess() throws Exception {
        Office officeWithId = new Office(777L, 301, 997788, "Samara_1", false, Collections.emptyList());

        when(repository.findById(777L)).thenReturn(Optional.of(officeWithId));
        mockMvc.perform(
                        get("http://localhost:8080/api/office/" + 777L)
                                .content(objectMapper.writeValueAsString(officeWithId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.number", Is.is(301)))
                .andExpect(jsonPath("$.phoneOffice", Is.is(997788)))
                .andExpect(jsonPath("$.address", Is.is("Samara_1")))
                .andExpect(jsonPath("$.deleted", Is.is(false)));
    }

    @Test
    void testGetOfficeIsNotSuccess() throws Exception {
        mockMvc.perform(
                        get("http://localhost:8080/api/office/" + 888L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllOfficeIsSuccess() throws Exception {
        List<Office> offices = Arrays.asList(
                new Office(111L, 301, 112233, "Samara_1", false, Collections.emptyList()),
                new Office(222L, 302, 445566, "Samara_2", false, Collections.emptyList()),
                new Office(333L, 303, 778899, "Samara_3", false, Collections.emptyList())
        );
        when(repository.findAll()).thenReturn(offices);
        mockMvc.perform(
                        get("http://localhost:8080/api/office")
                                .content(objectMapper.writeValueAsString(offices))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(3)))

                .andExpect(jsonPath("$.[0].id", Is.is(111)))
                .andExpect(jsonPath("$.[0].number", Is.is(301)))
                .andExpect(jsonPath("$.[0].phoneOffice", Is.is(112233)))
                .andExpect(jsonPath("$.[0].address", Is.is("Samara_1")))
                .andExpect(jsonPath("$.[0].deleted", Is.is(false)))

                .andExpect(jsonPath("$.[1].id", Is.is(222)))
                .andExpect(jsonPath("$.[1].number", Is.is(302)))
                .andExpect(jsonPath("$.[1].phoneOffice", Is.is(445566)))
                .andExpect(jsonPath("$.[1].address", Is.is("Samara_2")))
                .andExpect(jsonPath("$.[1].deleted", Is.is(false)))

                .andExpect(jsonPath("$.[2].id", Is.is(333)))
                .andExpect(jsonPath("$.[2].number", Is.is(303)))
                .andExpect(jsonPath("$.[2].phoneOffice", Is.is(778899)))
                .andExpect(jsonPath("$.[2].address", Is.is("Samara_3")))
                .andExpect(jsonPath("$.[2].deleted", Is.is(false)));
    }

    @Test
    void testPostOffice() throws Exception {
        Office officeWithoutId = new Office(301, 997788, "Samara_1", false);
        Office officeWithId = new Office(777L, 301, 997788, "Samara_1", false, Collections.emptyList());
        when(repository.save(officeWithoutId)).thenReturn(officeWithId);
        mockMvc.perform(
                        post("http://localhost:8080/api/office")
                                .content(objectMapper.writeValueAsString(officeWithoutId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.number", Is.is(301)))
                .andExpect(jsonPath("$.phoneOffice", Is.is(997788)))
                .andExpect(jsonPath("$.address", Is.is("Samara_1")))
                .andExpect(jsonPath("$.deleted", Is.is(false)));
    }

    @Test
    void testPutOffice() throws Exception {
        Office officeWithoutId = new Office(777L, 301, 997788, "Samara_1", false, Collections.emptyList());
        Office officeUpdateWithId = new Office(777L, 303, 110011, "Samara_111", false, Collections.emptyList());
        when(repository.save(officeWithoutId)).thenReturn(officeUpdateWithId);

        mockMvc.perform(
                        put("http://localhost:8080/api/office")
                                .content(objectMapper.writeValueAsString(officeWithoutId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(777)))
                .andExpect(jsonPath("$.number", Is.is(303)))
                .andExpect(jsonPath("$.phoneOffice", Is.is(110011)))
                .andExpect(jsonPath("$.address", Is.is("Samara_111")))
                .andExpect(jsonPath("$.deleted", Is.is(false)));
    }

    @Test
    void testDeleteOfficeIsSuccess() throws Exception {
        when(repository.existsById(777L)).thenReturn(true);
        doNothing().when(repository).deleteById(777L);

        mockMvc.perform(
                        delete("http://localhost:8080/api/office/" + 777L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOfficeIsNotSuccess() throws Exception {
        when(repository.existsById(888L)).thenReturn(false);
        doNothing().when(repository).deleteById(888L);

        mockMvc.perform(
                        delete("http://localhost:8080/api/office/" + 888L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }
}


package com.example.customermanagement;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import com.example.customermanagement.service.TierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository repository;

    @MockBean
    private TierService tierService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomerSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice");
        customer.setEmail("alice@example.com");

        Customer saved = new Customer();
        saved.setId(UUID.randomUUID());
        saved.setName("Alice");
        saved.setEmail("alice@example.com");

        Mockito.when(repository.save(any(Customer.class))).thenReturn(saved);
        Mockito.when(tierService.calculateTier(any())).thenReturn("Silver");

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.tier").value("Silver"));
    }

    @Test
    void testEmailValidationFail() throws Exception {
        Customer customer = new Customer();
        customer.setName("Bob");
        customer.setEmail("invalid-email");

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCustomerById() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("John");
        customer.setEmail("john@example.com");

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(customer));
        Mockito.when(tierService.calculateTier(any())).thenReturn("Silver");

        mockMvc.perform(get("/customers/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.tier").value("Silver"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        UUID id = UUID.randomUUID();
        Customer existing = new Customer();
        existing.setId(id);
        existing.setName("Tom");
        existing.setEmail("tom@example.com");

        Customer updated = new Customer();
        updated.setName("Tom Updated");
        updated.setEmail("tom.updated@example.com");

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existing));
        Mockito.when(repository.save(any(Customer.class))).thenReturn(existing);
        Mockito.when(tierService.calculateTier(any())).thenReturn("Silver");

        mockMvc.perform(put("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Tom Updated"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(repository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/customers/" + id))
            .andExpect(status().isNoContent());
    }
}

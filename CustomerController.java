
package com.example.customermanagement.controller;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import com.example.customermanagement.service.TierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;
    private final TierService tierService;

    public CustomerController(CustomerRepository repository, TierService tierService) {
        this.repository = repository;
        this.tierService = tierService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        if (customer.getId() != null) {
            return ResponseEntity.badRequest().body("ID must not be provided.");
        }
        Customer saved = repository.save(customer);
        return new ResponseEntity<>(addTier(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(c -> ResponseEntity.ok(addTier(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getByQuery(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email) {
        if (name != null) {
            return repository.findByName(name)
                    .map(c -> ResponseEntity.ok(addTier(c)))
                    .orElse(ResponseEntity.notFound().build());
        } else if (email != null) {
            return repository.findByEmail(email)
                    .map(c -> ResponseEntity.ok(addTier(c)))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body("Please provide 'name' or 'email'.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody Customer newData) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(newData.getName());
                    existing.setEmail(newData.getEmail());
                    existing.setAnnualSpend(newData.getAnnualSpend());
                    existing.setLastPurchaseDate(newData.getLastPurchaseDate());
                    return ResponseEntity.ok(addTier(repository.save(existing)));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Map<String, Object> addTier(Customer customer) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", customer.getId());
        response.put("name", customer.getName());
        response.put("email", customer.getEmail());
        response.put("annualSpend", customer.getAnnualSpend());
        response.put("lastPurchaseDate", customer.getLastPurchaseDate());
        response.put("tier", tierService.calculateTier(customer));
        return response;
    }
}

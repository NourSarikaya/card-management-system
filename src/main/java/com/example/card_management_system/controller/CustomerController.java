package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping("/create")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO request) {
        log.info("POST /customers - creating customer ");

        CustomerResponseDTO created = customerService.createNewCustomer(request);

        log.info("POST /customers - successfully created customer with id = {}", created.getCustomerID());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/find/{customerId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable String customerId) {
        log.info("GET /customers/{}- retrieving customer", customerId);

        CustomerResponseDTO updated = customerService.getCustomerById(customerId);

        log.info("GET /customers/{}- successfully retrieved customer", customerId);
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/update/{customerId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CustomerResponseDTO> updateCustomerById(@PathVariable String customerId, @Valid @RequestBody CustomerUpdateDTO update) {
        log.info("PUT /customers/{}- updating customer", customerId);

        CustomerResponseDTO updated = customerService.updateCustomer(customerId, update);

        log.info("PUT /customers/{}- successfully updated customer", customerId);
        return ResponseEntity.ok(updated);
    }

}

package com.example.card_management_system.service;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    public CustomerResponseDTO createCustomer(@Valid CreateCustomerRequestDTO request) {
        return null;
    }

    public CustomerResponseDTO getCustomerById(UUID customerId) {
        return null;
    }

    public CustomerResponseDTO updateCustomer(UUID customerId, @Valid CustomerUpdateDTO update) {
        return null;
    }
}

package com.example.card_management_system.service;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.mapper.CustomerMapper;
import com.example.card_management_system.repository.CustomerRepository;
import com.example.card_management_system.util.UUIDUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponseDTO getCustomerById(String customerId) {
        UUID customerUuid = UUIDUtils.toUUID(customerId);

        Customer customer = customerRepository.findById(customerUuid)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        log.info("Found customer: {}", customerId);
        return customerMapper.customerToResponseDto(customer);
    }

    public CustomerResponseDTO createNewCustomer(CreateCustomerRequestDTO createCustomerRequestDTO) {
        try {
            log.info("New create customer request for customer: {}", createCustomerRequestDTO.getEmailAddress());
            Customer newCustomer = customerMapper.requestDtoToCustomer(createCustomerRequestDTO);

            newCustomer.setCustomerId(UUID.randomUUID());

            Customer savedCustomer = customerRepository.save(newCustomer);

            log.info("Created and saved new customer: {}", savedCustomer.getCustomerId());
            return customerMapper.customerToResponseDto(savedCustomer);

        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to process customer request: " + e.getMessage());
        }
    }

    public CustomerResponseDTO updateCustomer(String customerId, CustomerUpdateDTO customerUpdateDTO) {
        try {
            UUID customerUuid = UUIDUtils.toUUID(customerId);

            Customer existingCustomer = customerRepository.findById(customerUuid)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

            customerMapper.updateCustomerFromDto(customerUpdateDTO, existingCustomer);

            log.info("Successfully updated customer: {}", customerId);
            Customer savedCustomer = customerRepository.save(existingCustomer);

            return customerMapper.customerToResponseDto(savedCustomer);
        } catch (RuntimeException e) {
            log.error("Could not update customer: {}", customerId);
            throw new RuntimeException("Failed to update customer: " + e.getMessage());
        }
    }

}

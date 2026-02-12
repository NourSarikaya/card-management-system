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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor//Inject mapper via constructor injection
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponseDTO getCustomerById(String customerId) {
        UUID customerUuid = UUIDUtils.toUUID(customerId);

        Customer customer = customerRepository.findById(customerUuid)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        return customerMapper.customerToResponseDto(customer);
    }

    public CustomerResponseDTO createNewCustomer(CreateCustomerRequestDTO createCustomerRequestDTO) {
        try {
            Customer newCustomer = customerMapper.requestDtoToCustomer(createCustomerRequestDTO);

            newCustomer.setCustomerId(UUID.randomUUID());

            Customer savedCustomer = customerRepository.save(newCustomer);
            return customerMapper.customerToResponseDto(savedCustomer);

        } catch (RuntimeException e) {
            // TODO: Custom Exception?
            throw new RuntimeException("Failed to process customer request: " + e.getMessage());
        }
    }

    public CustomerResponseDTO updateCustomer(String customerId, CustomerUpdateDTO customerUpdateDTO) {
        try {
            UUID customerUuid = UUIDUtils.toUUID(customerId);

            Customer existingCustomer = customerRepository.findById(customerUuid)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

            customerMapper.updateCustomerFromDto(customerUpdateDTO, existingCustomer);
            Customer savedCustomer = customerRepository.save(existingCustomer);

            return customerMapper.customerToResponseDto(savedCustomer);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update customer: " + e.getMessage());
        }
    }

}

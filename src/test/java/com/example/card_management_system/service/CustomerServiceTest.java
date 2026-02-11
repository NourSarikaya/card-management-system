package com.example.card_management_system.service;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.mapper.CustomerMapper;
import com.example.card_management_system.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void givenAValidCustomerId_whenGetByCustomerIdCalled_thenReturnCustomerResponseDto() {
        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder().customerId(customerId).build();
        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                                                                     .customerID(customerId.toString())
                                                                     .build();


        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToResponseDto(customer)).thenReturn(customerResponseDTO);

        CustomerResponseDTO result = customerService.getCustomerById(customerId.toString());

        assertThat(result).isNotNull();
        assertThat(result.getCustomerID()).isEqualTo(customerId.toString());

        verify(customerRepository).findById(customerId);
        verify(customerMapper).customerToResponseDto(customer);
    }

    @Test
    void givenAnInvalidCustomerId_whenGetByCustomerIdCalled_thenThrowsException() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomerById(customerId.toString());
        });

        assertTrue(exception.getMessage().contains("Customer not found with ID"));

        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void givenAValidCreateCustomerRequest_whenCreateNewCustomerCalled_thenReturnCustomerResponseDto() {
        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder().build();
        CreateCustomerRequestDTO request = CreateCustomerRequestDTO.builder()
                                                                   .build();
        Customer savedCustomer = Customer.builder().firstName(customer.getFirstName()).build();
        CustomerResponseDTO response = CustomerResponseDTO.builder().build();

        when(customerMapper.requestDtoToCustomer(any())).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerMapper.customerToResponseDto(any(Customer.class))).thenReturn(response);

        CustomerResponseDTO result = customerService.createNewCustomer(request);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getCustomerId()).isNotNull();
        assertThat(result).isNotNull();

        verify(customerMapper).customerToResponseDto(savedCustomer);
    }

    @Test
    void givenAnInvalidCreateCustomerRequest_whenCreateNewCustomerCalled_thenThrowsException() {
        String invalidEmail = "john.com";
        CreateCustomerRequestDTO request = CreateCustomerRequestDTO.builder().emailAddress(invalidEmail).build();
        Customer customer = Customer.builder().emailAddress(invalidEmail).build();

        when(customerMapper.requestDtoToCustomer(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenThrow(RuntimeException.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.createNewCustomer(request);
        });

        assertTrue(exception.getMessage().contains("Failed to process customer request"));

        verify(customerMapper, never()).customerToResponseDto(any());
    }

    @Test
    void givenValidCustomerUpdateRequest_whenUpdateCustomerCalled_thenReturnUpdatedResponse() {
        UUID customerId = UUID.randomUUID();

        Customer existingCustomer = Customer.builder()
                                            .customerId(customerId)
                                            .firstName("John")
                                            .emailAddress("old@email.com")
                                            .build();

        CustomerUpdateDTO customerUpdateDTO = CustomerUpdateDTO.builder()
                                                               .emailAddress("new@email.com")
                                                               .build();


        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.customerToResponseDto(any(Customer.class))).thenReturn(new CustomerResponseDTO());

        customerService.updateCustomer(customerId.toString(), customerUpdateDTO);

        verify(customerRepository).save(existingCustomer);
        verify(customerMapper).updateCustomerFromDto(customerUpdateDTO, existingCustomer);
        verify(customerMapper).customerToResponseDto(existingCustomer);
    }

    @Test
    public void givenInvalidIdForCustomerUpdate_whenUpdateCustomerCalled_thenThrowsException() {
        UUID invalidId = UUID.randomUUID();

        CustomerUpdateDTO customerUpdateDTO = CustomerUpdateDTO.builder()
                                                               .emailAddress("new@email.com")
                                                               .build();

        when(customerRepository.findById(invalidId)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, ()-> {
            customerService.updateCustomer(invalidId.toString(), customerUpdateDTO);
        });

        assertTrue(exception.getMessage().contains("Failed to update customer"));
        verify(customerMapper, never()).customerToResponseDto(any());
        verify(customerRepository, never()).save(any());
    }
}
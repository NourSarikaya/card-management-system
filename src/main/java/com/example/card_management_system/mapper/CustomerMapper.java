package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerMapper {

    public CustomerResponseDTO customerToResponseDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponseDTO.builder()
                                  .customerID(customer.getCustomerId().toString())
                                  .firstName(customer.getFirstName())
                                  .middleInitial(customer.getMiddleInitial())
                                  .lastName(customer.getLastName())
                                  .emailAddress(customer.getEmailAddress())
                                  .phoneNumber(customer.getPhoneNumber())
                                  .phoneType(customer.getPhoneType().toString())
                                  .addressLine1(customer.getAddressLine1())
                                  .addressLine2(customer.getAddressLine2() != null ? customer.getAddressLine2() : null)
                                  .cityName(customer.getCityName())
                                  .state(customer.getState())
                                  .zipCode(customer.getZipcode())
                                  .build();

    }

    public Customer requestDtoToCustomer(CreateCustomerRequestDTO createCustomerRequestDTO) {
        return Customer.builder()
                       .firstName(createCustomerRequestDTO.getFirstName())
                       .middleInitial(createCustomerRequestDTO.getMiddleInitial())
                       .lastName(createCustomerRequestDTO.getLastName())
                       .emailAddress(createCustomerRequestDTO.getEmailAddress())
                       .phoneNumber(createCustomerRequestDTO.getPhoneNumber())
                       .phoneType(Customer.PhoneType.valueOf(createCustomerRequestDTO.getPhoneType()))
                       .addressLine1(createCustomerRequestDTO.getAddressLine1())
                       .addressLine2(createCustomerRequestDTO.getAddressLine2() != null ? createCustomerRequestDTO.getAddressLine2() : null)
                       .cityName(createCustomerRequestDTO.getCityName())
                       .state(createCustomerRequestDTO.getState())
                       .zipcode(createCustomerRequestDTO.getZipCode())
                       .build();
    }

    public void updateCustomerFromDto(CustomerUpdateDTO dto, Customer existingCustomer) {
        if (dto == null || existingCustomer == null) {
            return;
        }
        if (dto.getEmailAddress() != null) {
            existingCustomer.setEmailAddress(dto.getEmailAddress());
        }
        if (dto.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddressLine1() != null) {
            existingCustomer.setAddressLine1(dto.getAddressLine1());
        }
        if (dto.getAddressLine2() != null) {
            existingCustomer.setAddressLine2(dto.getAddressLine2());
        }
        if (dto.getCityName() != null) {
            existingCustomer.setCityName(dto.getCityName());
        }
        if (dto.getState() != null) {
            existingCustomer.setState(dto.getState());
        }
        if (dto.getZipCode() != null) {
            existingCustomer.setZipcode(dto.getZipCode());
        }

    }
}

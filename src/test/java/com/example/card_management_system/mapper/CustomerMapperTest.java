package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerResponseDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void givenValidCustomerEntity_whenCustomerToResponseDtoCalled_thenReturnCustomerResponseDto() {
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.builder()
                                    .customerId(customerId)
                                    .firstName("John")
                                    .middleInitial("M")
                                    .lastName("Smith")
                                    .emailAddress("john@email.com")
                                    .phoneNumber("1231231234")
                                    .phoneType(Customer.PhoneType.MOBILE)
                                    .addressLine1("123 main st")
                                    .addressLine2("unit 24")
                                    .cityName("Chicago")
                                    .state("IL")
                                    .zipcode("60606")
                                    .build();

        CustomerResponseDTO result = customerMapper.customerToResponseDto(customer);

        assertThat(result.getCustomerID()).isEqualTo(customer.getCustomerId().toString());
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getMiddleInitial()).isEqualTo(customer.getMiddleInitial());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmailAddress()).isEqualTo(customer.getEmailAddress());
        assertThat(result.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(result.getPhoneType()).isEqualTo(customer.getPhoneType().toString());
        assertThat(result.getAddressLine1()).isEqualTo(customer.getAddressLine1());
        assertThat(result.getAddressLine2()).isEqualTo(customer.getAddressLine2());
        assertThat(result.getCityName()).isEqualTo(customer.getCityName());
        assertThat(result.getState()).isEqualTo(customer.getState());
        assertThat(result.getZipCode()).isEqualTo(customer.getZipcode());
    }


    @Test
    void givenValidRequestDto_whenMapped_thenCustomerFieldsMatch() {
        CreateCustomerRequestDTO dto = CreateCustomerRequestDTO.builder()
                                                               .firstName("John")
                                                               .middleInitial("H")
                                                               .lastName("Smith")
                                                               .emailAddress("john@email.com")
                                                               .phoneNumber("1231231234")
                                                               .phoneType("MOBILE") // must match the enum constant name for valueOf()
                                                               .addressLine1("123 main st")
                                                               .addressLine2("unit 24")
                                                               .cityName("Chicago")
                                                               .state("IL")
                                                               .zipCode("60606")
                                                               .build();

        // Act
        Customer result = customerMapper.requestDtoToCustomer(dto);

        assertThat(result.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(result.getMiddleInitial()).isEqualTo(dto.getMiddleInitial());
        assertThat(result.getLastName()).isEqualTo(dto.getLastName());
        assertThat(result.getEmailAddress()).isEqualTo(dto.getEmailAddress());
        assertThat(result.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(result.getPhoneType()).isEqualTo(Customer.PhoneType.valueOf(dto.getPhoneType()));
        assertThat(result.getAddressLine1()).isEqualTo(dto.getAddressLine1());
        assertThat(result.getAddressLine2()).isEqualTo(dto.getAddressLine2());
        assertThat(result.getCityName()).isEqualTo(dto.getCityName());
        assertThat(result.getState()).isEqualTo(dto.getState());
        assertThat(result.getZipcode()).isEqualTo(dto.getZipCode());
    }

    @Test
    public void givenCustomerUpdateDto_whenMapped_thenCustomerFieldsUpdated() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = Customer.builder()
                                            .customerId(customerId)
                                            .firstName("John")
                                            .middleInitial("M")
                                            .lastName("Smith")
                                            .emailAddress("old@email.com")
                                            .phoneNumber("1231231234")
                                            .phoneType(Customer.PhoneType.MOBILE)
                                            .addressLine1("123 Main St.")
                                            .addressLine2("Unit 24")
                                            .cityName("Chicago")
                                            .state("IL")
                                            .zipcode("60606")
                                            .build();

        CustomerUpdateDTO updateDto = CustomerUpdateDTO.builder()
                                                       .emailAddress("new@email.com")
                                                       .phoneNumber("3213214321")
                                                       .addressLine1("321 New St.")
                                                       .addressLine2("Unit 50")
                                                       .cityName("Houston")
                                                       .state("TX")
                                                       .zipCode("40203")
                                                       .build();

        customerMapper.updateCustomerFromDto(updateDto, existingCustomer);


        assertThat(existingCustomer.getCustomerId()).isEqualTo(customerId);
        assertThat(existingCustomer.getFirstName()).isEqualTo("John");
        assertThat(existingCustomer.getMiddleInitial()).isEqualTo("M");
        assertThat(existingCustomer.getLastName()).isEqualTo("Smith");
        assertThat(existingCustomer.getPhoneType()).isEqualTo(Customer.PhoneType.MOBILE);
        assertThat(existingCustomer.getEmailAddress()).isEqualTo(updateDto.getEmailAddress());
        assertThat(existingCustomer.getPhoneNumber()).isEqualTo(updateDto.getPhoneNumber());
        assertThat(existingCustomer.getAddressLine1()).isEqualTo(updateDto.getAddressLine1());
        assertThat(existingCustomer.getAddressLine2()).isEqualTo(updateDto.getAddressLine2());
        assertThat(existingCustomer.getCityName()).isEqualTo(updateDto.getCityName());
        assertThat(existingCustomer.getState()).isEqualTo(updateDto.getState());
        assertThat(existingCustomer.getZipcode()).isEqualTo(updateDto.getZipCode());


    }

}
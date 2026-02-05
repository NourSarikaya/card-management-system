package com.example.card_management_system.repository;

import com.example.card_management_system.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testCustomer = new Customer("firstname","lastname",
                "E", "nnn@gmail.com","123123123121332",
                Customer.PhoneType.MOBILE, "Street","apt","chi","IL",
                "60606");

        customerRepository.save(testCustomer);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
    }

    @Test
    void givenCustomer_whenSaved_thenCanBeFoundByCustomerId() {
        Customer savedCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElse(null);
        assertNotNull(savedCustomer);
        assertEquals(testCustomer.getAddressLine1(), savedCustomer.getAddressLine1());
        assertEquals(testCustomer.getPhoneNumber(), savedCustomer.getPhoneNumber());
    }

    @Test
    void givenCustomer_whenDeleted_thenCanNotBeFoundByCustomerId() {
        assertThat(testCustomer.getCustomerId()).isNotNull();

        //Deleting Customer
        customerRepository.deleteById(testCustomer.getCustomerId());
        assertThat(customerRepository.findById(testCustomer.getCustomerId())).isEmpty();
    }

    @Test
    void givenCustomer_whenUpdated_thenCanBeFoundByCustomerIdWithUpdatedData() {
        //Updating State of Customer
        testCustomer.setState("NY");
        customerRepository.save(testCustomer);

        Customer updatedCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElse(null);
        assertNotNull(updatedCustomer);
        assertEquals(testCustomer.getState(), updatedCustomer.getState());
    }


}
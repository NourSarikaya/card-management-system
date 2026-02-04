package com.example.card_management_system.repository;

import com.example.card_management_system.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findByCustomer_CustomerId(UUID customerId);

}

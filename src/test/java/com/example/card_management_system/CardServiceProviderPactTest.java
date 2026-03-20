package com.example.card_management_system;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.example.card_management_system.config.SecurityConfig;
import com.example.card_management_system.config.TestSecurityConfig;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.service.CardService;
import com.example.card_management_system.util.UUIDUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Provider("CardService")
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CardServiceProviderPactTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private CardService cardService;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Customer 3f8c2c4e-9b8a-4e2d-9f3b-71d4c9a6c812 has active cards")
    void customerHasActiveCards() {
        Card card = Card.builder()
                        .accountId(UUIDUtils.toUUID("3f8c2c4e-9b8a-4e2d-9f3b-71d4c9a6c812"))
                        .accountNumber(UUIDUtils.toUUID("3f8c2c4e-9b8a-4e2d-9f3b-000000000001"))
                        .cardNumber("1111222233334444")
                        .cardType(Card.CardType.CREDIT)
                        .cardHolderName("John")
                        .expiryDate(LocalDate.of(2030, 12, 31))
                        .active(true)
                        .creditLimit(BigDecimal.valueOf(5000))
                        .build();

        when(cardService.getAllCardsByCustomerId(anyString())).thenReturn(List.of(card));
    }
}

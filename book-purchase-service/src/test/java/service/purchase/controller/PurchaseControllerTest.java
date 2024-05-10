package service.purchase.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import service.purchase.repository.PurchaseRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({})
@SpringBootTest(webEnvironment = RANDOM_PORT)
class PurchaseControllerTest extends DatabaseSuite {
  @Autowired
  private TestRestTemplate rest;

  @Autowired
  private PurchaseController purchaseController;

  @Autowired
  private PurchaseRepository purchaseRepository;

  @Test
  void shouldPutMoney() {
    ResponseEntity<Void> putMoneyResponse;
    putMoneyResponse = rest.exchange("/api/purchase/put/{money}/{id}",
        HttpMethod.POST,
        new HttpEntity<>(new HttpHeaders()),
        void.class,
        Map.of("money", 1000, "id", 20));
    assertTrue(putMoneyResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + putMoneyResponse.getStatusCode());
    assertEquals(1000, purchaseRepository.findById(20L).orElseThrow().getMoney());
  }
}
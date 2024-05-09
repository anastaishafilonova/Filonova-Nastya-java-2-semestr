package service.purchase;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import service.purchase.consumer.BookPurchaseMessage;
import service.purchase.controller.DatabaseSuite;
import service.purchase.entity.Purchase;
import service.purchase.producer.BookPurchaseResult;
import service.purchase.repository.PurchaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(
    properties = {"topic1-to-send-message=some-test-topic3",
        "topic1-to-consume-message=some-test-topic2",
        "spring.kafka.consumer.auto-offset-reset=earliest"
    }
)
@Import({KafkaAutoConfiguration.class, CancelledPaymentTest.ObjectMapperTestConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DirtiesContext
class CancelledPaymentTest extends DatabaseSuite {
  @TestConfiguration
  static class ObjectMapperTestConfig {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }
  }

  @Container
  @ServiceConnection
  public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @MockBean
  private PurchaseRepository purchaseRepository;

  @Test
  public void shouldPaidSucceed() throws JsonProcessingException, InterruptedException {
    Purchase purchase = new Purchase(50, 1L);
    when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));

    CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send("some-test-topic2",
        objectMapper.writeValueAsString(new BookPurchaseMessage(1L, 1L)));
    Thread.sleep(15000);
    KafkaTestConsumer consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "book-service-group1mvn");
    consumer.subscribe(List.of("some-test-topic3"));
    ConsumerRecords<String, String> records = consumer.poll();
    assertEquals(1, records.count());
    records.iterator().forEachRemaining(
        record -> {
          BookPurchaseResult message = null;
          try {
            message = objectMapper.readValue(record.value(), BookPurchaseResult.class);
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
          assertEquals(new BookPurchaseResult(1L, "cancelled"), message);
        }
    );
  }
}
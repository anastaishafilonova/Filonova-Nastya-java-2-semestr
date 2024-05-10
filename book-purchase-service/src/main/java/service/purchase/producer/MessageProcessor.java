package service.purchase.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import service.purchase.entity.Outbox;
import service.purchase.entity.Purchase;
import service.purchase.repository.OutboxRepository;
import service.purchase.repository.PurchaseRepository;

import java.util.NoSuchElementException;

@Service
public class MessageProcessor {
  private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
  private final PurchaseRepository purchaseRepository;
  private final OutboxRepository outboxRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  @Autowired
  public MessageProcessor(PurchaseRepository purchaseRepository, OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, @Value("${topic1-to-send-message}") String topic) {
    this.purchaseRepository = purchaseRepository;
    this.outboxRepository = outboxRepository;
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }


  public void process(Long bookId, Long userId) throws JsonProcessingException {
    String resultStatus = "cancelled";
    try {
      Purchase purchase = purchaseRepository.findById(userId).orElseThrow();
      if (purchase.getMoney() >= 100) {
        purchase.setMoney(purchase.getMoney() - 100);
        purchaseRepository.save(purchase);
        resultStatus = "paid";
      } else {
        resultStatus = "cancelled";
      }

    } catch (NoSuchElementException e) {
      logger.warn("There is not such a user");
      resultStatus = "cancelled";
    } finally {
      var message = objectMapper.writeValueAsString(new BookPurchaseResult(bookId, resultStatus));
      outboxRepository.save(new Outbox(message));
    }
  }
}

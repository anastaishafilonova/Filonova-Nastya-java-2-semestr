package service.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PurchaseConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseConsumer.class);

  private final ObjectMapper objectMapper;
  private final MessageProcessor messageProcessor;

  @Autowired
  public PurchaseConsumer(ObjectMapper objectMapper, MessageProcessor messageProcessor) {
    this.objectMapper = objectMapper;
    this.messageProcessor = messageProcessor;
  }

  @KafkaListener(topics = {"${topic1-to-consume-message}"})
  public void processCookieMatching(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
    BookPurchaseMessage parsedMessage = objectMapper.readValue(message, BookPurchaseMessage.class);
    LOGGER.info("Retrieved message {}", message);
    messageProcessor.process(parsedMessage.bookId(), parsedMessage.userId());
    acknowledgment.acknowledge();
  }
}


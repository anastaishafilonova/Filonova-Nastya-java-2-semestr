package rating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class RatingConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(RatingConsumer.class);

  private final ObjectMapper objectMapper;
  private final MessageProcessor messageProcessor;

  @Autowired
  public RatingConsumer(ObjectMapper objectMapper, MessageProcessor messageProcessor) {
    this.objectMapper = objectMapper;
    this.messageProcessor = messageProcessor;
  }

  @KafkaListener(topics = {"${topic-to-consume-message}"})
  public void processCookieMatching(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
    RatingBookMessage parsedMessage = objectMapper.readValue(message, RatingBookMessage.class);
    LOGGER.info("Retrieved message {}", message);
    messageProcessor.process(parsedMessage.bookId());
    acknowledgment.acknowledge();
  }


}
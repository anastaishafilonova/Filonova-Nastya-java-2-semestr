package rating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageProcessor {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  @Autowired
  public MessageProcessor(KafkaTemplate<String, String> kafkaTemplate,
                       ObjectMapper objectMapper,
                       @Value("${topic-to-send-message}") String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  public int process(Long bookId) throws JsonProcessingException {
    int rating = (int) (1 + Math.random() * 9);
    LOGGER.info("rating equals" + rating);
    String message = objectMapper.writeValueAsString(new RatingResult(bookId, rating));
    CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(topic, bookId.toString(), message);
    return rating;
  }
}
record RatingResult(Long bookId, int rating) {}

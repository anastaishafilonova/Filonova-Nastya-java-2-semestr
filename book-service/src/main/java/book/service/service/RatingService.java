package book.service.service;

import book.service.entity.Book;
import book.service.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@Validated
public class RatingService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;
  private BookRepository bookRepository;

  @Autowired
  public RatingService(KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper,
                           @Value("${topic-to-send-message}") String topic, BookRepository bookRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
    this.bookRepository = bookRepository;
  }

  @PostMapping("/books/{bookId}:getRating")
  public void rating(@PathVariable @NotNull Long bookId) throws JsonProcessingException {
    String message = objectMapper.writeValueAsString(new RatingBookMessage(bookId));
    CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(topic, message);
  }

  @KafkaListener(topics = {"${topic-to-consume-message}"})
  @Transactional
  public void onRatingReceived(String message) throws JsonProcessingException {
    var result = objectMapper.readValue(message, RatingResult.class);
    LOGGER.info("Received result: " + result.rating());
    Book book = bookRepository.findById(result.bookId()).orElseThrow();
    book.setRating(result.rating());
    bookRepository.save(book);
  }
}

record RatingBookMessage(Long bookId) {
}record RatingResult(Long bookId, int rating) {}
package book.service.controller;

import book.service.entity.Book;
import book.service.repository.BookRepository;
import book.service.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GettingRatingConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(GettingRatingConsumer.class);

  private final ObjectMapper objectMapper;
  private final BookService bookService;

  @Autowired
  public GettingRatingConsumer(ObjectMapper objectMapper, BookService bookService) {
    this.objectMapper = objectMapper;
    this.bookService = bookService;
  }

  @KafkaListener(topics = {"${topic-to-consume-message}"})
  @Transactional
  public void onRatingReceived(String message) throws JsonProcessingException {
    var result = objectMapper.readValue(message, RatingResult.class);
    LOGGER.info("Received result: " + result.rating());
    bookService.process(message);
  }
}



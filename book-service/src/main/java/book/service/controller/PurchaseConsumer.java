package book.service.controller;

import book.service.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseConsumer {
  private final ObjectMapper objectMapper;
  private final BookService bookService;

  @Autowired
  public PurchaseConsumer(ObjectMapper objectMapper, BookService bookService) {
    this.objectMapper = objectMapper;
    this.bookService = bookService;
  }

  @KafkaListener(topics = {"${topic1-to-consume-message}"})
  @Transactional
  public void purchaseReceived(String message) throws JsonProcessingException {
    var result = objectMapper.readValue(message, BookPurchaseResult.class);
    bookService.purchaseProcess(message);
  }
}

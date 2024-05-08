package book.service.service;

import book.service.entity.Book;
import book.service.entity.BookPurchaseMessage;
import book.service.entity.Outbox;
import book.service.repository.BookRepository;
import book.service.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookPurchaseService {
  private static final Logger logger = LoggerFactory.getLogger(BookPurchaseService.class);
  private final OutboxRepository outboxRepository;
  private final BookRepository bookRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  public BookPurchaseService(OutboxRepository outboxRepository, BookRepository bookRepository) {
    this.outboxRepository = outboxRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public void buyBook(Long id, Long userId) throws JsonProcessingException {
    try {
      Book book = bookRepository.findById(id).orElseThrow();
      if (!book.getStatus().equals("payment-pending")) {
        book.setStatus("payment-pending");
        bookRepository.save(book);
        Outbox outbox = new Outbox(objectMapper.writeValueAsString(new BookPurchaseMessage(id, userId)));
        outboxRepository.save(outbox);
      }
    } catch (NoSuchElementException e) {
      logger.warn("No such book, so you cannot buy it now");
    }
  }
}

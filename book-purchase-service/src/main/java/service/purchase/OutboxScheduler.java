package service.purchase;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import service.purchase.entity.Outbox;
import service.purchase.repository.OutboxRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OutboxScheduler {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topic;
  private final OutboxRepository outboxRepository;

  @Autowired
  public OutboxScheduler(KafkaTemplate<String, String> kafkaTemplate, @Value("${topic1-to-send-message}") String topic, OutboxRepository outboxRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
    this.outboxRepository = outboxRepository;
  }

  @Transactional
  @Scheduled(fixedDelay = 10000)
  public void processOutbox() {
    List<Outbox> result = outboxRepository.findAll();
    for (Outbox outboxRecord : result) {
      CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(topic, outboxRecord.getData());
    }
    outboxRepository.deleteAll(result);
  }
}
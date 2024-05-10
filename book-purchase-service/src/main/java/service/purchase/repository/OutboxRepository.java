package service.purchase.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.purchase.entity.Outbox;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
}

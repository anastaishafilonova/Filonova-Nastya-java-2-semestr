package service.purchase.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.purchase.entity.Purchase;
import service.purchase.repository.PurchaseRepository;

import java.util.NoSuchElementException;

@Service
public class PurchaseService {
  private final PurchaseRepository purchaseRepository;

  @Autowired
  public PurchaseService(PurchaseRepository purchaseRepository) {
    this.purchaseRepository = purchaseRepository;
  }

  @Transactional
  public void putMoney(int money, Long id) {
    try {
      Purchase purchase = purchaseRepository.findById(id).orElseThrow();
      purchase.setMoney(purchase.getMoney() + money);
      purchaseRepository.save(purchase);
    } catch (NoSuchElementException e) {
      purchaseRepository.save(new Purchase(money, id));
    }
  }
}

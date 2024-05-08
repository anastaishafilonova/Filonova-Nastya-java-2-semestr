package service.purchase.controller;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.purchase.PurchaseService;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
  private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);
  private final PurchaseService purchaseService;

  @Autowired
  public PurchaseController(PurchaseService purchaseService) {
    this.purchaseService = purchaseService;
  }

  @GetMapping("/put/{money}/{id}")
  public void put(@PathVariable int money, @PathVariable @NotNull Long id) {
    LOGGER.info("Money: " + money + " id: " + id);
    purchaseService.putMoney(money, id);
  }
}

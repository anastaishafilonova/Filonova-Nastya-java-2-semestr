package service.purchase.consumer;

public record BookPurchaseMessage(Long bookId, Long userId) {
}

package author.service.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthorRegistryService {
  private Map<String, BookInfo> createdBooks = new ConcurrentHashMap<>();
  private Map<String, Boolean> arrayOfResults = new ConcurrentHashMap<>();

  protected AuthorRegistryService() {

  }

  public void addBook(String firstName, String lastName, String title, String requestId) {
    createdBooks.putIfAbsent(requestId, new BookInfo(firstName, lastName, title));
  }

  public boolean checkBook(String requestId, BookInfo book) {
    Boolean check = arrayOfResults.computeIfAbsent(
        requestId,
        k -> createdBooks.containsValue(book)
    );
    return check;
  }
}

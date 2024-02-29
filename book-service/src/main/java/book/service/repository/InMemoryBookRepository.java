package book.service.repository;

import book.service.entity.Book;
import org.springframework.stereotype.Component;
import book.service.repository.exception.BookNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InMemoryBookRepository implements BookRepository {
  CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();
  private int currentId = 0;

  public InMemoryBookRepository() {}

  private synchronized int generateId() {
    return ++currentId;
  }

  @Override
  public Book createBook(String author, String title, Set<String> tags) {
    Book newBook = new Book(author, title, tags);
    books.add(newBook);
    newBook.setId(generateId());
    return newBook;
  }

  @Override
  public Book updateBook(int id, String author, String title) {
    try {
      Book changedBook = findById(id);
      changedBook.setAuthor(author);
      changedBook.setTitle(title);
      return changedBook;
    } catch (BookNotFoundException e) {
      throw new BookNotFoundException("Book with this id is not found");
    }
  }

  @Override
  public void deleteBookById(int id) {
    for (Book book : books) {
      if (book.getId() == id) {
        books.remove(book);
        return;
      }
    }
    throw new BookNotFoundException("Book with this id is not found");
  }

  @Override
  public Book getBookWithTag(String tag) {
    for (Book book : books) {
      if (book.getTags().contains(tag)) {
        return book;
      }
    }
    throw new BookNotFoundException("Book with this tag is not found :(");
  }

  public Book findById(int id) {
    for (Book book : books) {
      if (book.getId() == id) {
        return book;
      }
    }
    throw new BookNotFoundException("Book with this id is not found");
  }

  public List<Book> findAll() {
    return books.stream().toList();
  }

  @Override
  public void deleteAll() {
    books = new CopyOnWriteArrayList<>();
  }
}

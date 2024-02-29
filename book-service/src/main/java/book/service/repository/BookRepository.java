package book.service.repository;

import book.service.entity.Book;

import java.util.List;
import java.util.Set;

public interface BookRepository {
  Book createBook(String author, String title, Set<String> tags);
  Book updateBook(int id, String author, String title);
  void deleteBookById(int id);
  Book getBookWithTag(String tag);
  List<Book> findAll();
  void deleteAll();
}

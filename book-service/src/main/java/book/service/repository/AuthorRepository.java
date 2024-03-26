package book.service.repository;

import book.service.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
  List<Author> findAllBy();
  Author findByFirstNameAndLastName(String firstName, String lastName);
}

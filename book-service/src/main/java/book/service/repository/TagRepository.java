package book.service.repository;

import book.service.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findAllBy();
  Tag findTagByName(String name);
}

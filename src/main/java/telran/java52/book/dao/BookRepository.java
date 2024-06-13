package telran.java52.book.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import telran.java52.book.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	Stream<Book> findByAuthorsName(String authorName);
	
	Stream<Book> findByPublisherPublisherName(String publisherName);
	
	void deleteByAuthorsName(String name);
	
//	@Query("select b from Book b join b.authors a where a.name = :author")
//	List<Book> findBooksByAuthor(@Param("author") String author);
//	
//	@Query("select b from Book b join b.publisher p where p.publisherName = :publisher")
//	List<Book> findBooksByPublisher(@Param("publisher") String publisher);
}

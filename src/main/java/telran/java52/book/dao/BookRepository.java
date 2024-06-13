package telran.java52.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import telran.java52.book.model.Author;
import telran.java52.book.model.Book;
import telran.java52.book.model.Publisher;

public interface BookRepository {

 Stream<Book> findByAuthorsName(String authorName);
	
	Stream<Book> findByPublisherPublisherName(String publisherName);
	
	void deleteByAuthorsName(String name);

	boolean existsById(String isbn);

	Book save(Book book);

	Optional<Book> findById(String isbn);

	void deleteById(String isbn);
	
	
	
//	@Query("select b from Book b join b.authors a where a.name = :author")
//	List<Book> findBooksByAuthor(@Param("author") String author);
//	
//	@Query("select b from Book b join b.publisher p where p.publisherName = :publisher")
//	List<Book> findBooksByPublisher(@Param("publisher") String publisher);
}

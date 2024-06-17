package telran.java52.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import telran.java52.book.model.Publisher;

@Repository
public class PublisherRepositoryImpl implements PublisherRepository {
	
	@PersistenceContext
	EntityManager em;

	@Override
	public List<String> findPublishersByAuthor(String authorName) {
		TypedQuery<String> query = em.createQuery("select distinct p.publisherName from Book b join b.publisher p join b.authors a where a.name=?1", String.class);
		query.setParameter(1, authorName);
		return query.getResultList();
	}

	@Override
	public Stream<Publisher> findDistinctByBooksAuthorsName(String authorName) {
		return em.createQuery("select distinct p from Book b join b.publisher p join b.authors a where a.name=?1", Publisher.class)
				.setParameter(1, authorName)
				.getResultStream();
	}

	@Override
	public Optional<Publisher> findById(String publisher) {
		return Optional.ofNullable(em.find(Publisher.class, publisher));
	}

//	@Transactional
	@Override
	public Publisher save(Publisher publisher) {
		em.persist(publisher);
//		em.merge(publisher);
		return publisher;
	}

}

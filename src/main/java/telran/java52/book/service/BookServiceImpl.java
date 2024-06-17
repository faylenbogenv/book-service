package telran.java52.book.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java52.book.dao.AuthorRepository;
import telran.java52.book.dao.BookRepository;
import telran.java52.book.dao.PublisherRepository;
import telran.java52.book.dto.AuthorDto;
import telran.java52.book.dto.BookDto;
import telran.java52.book.dto.exception.EntityNotFoundException;
import telran.java52.book.model.Author;
import telran.java52.book.model.Book;
import telran.java52.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;
	
	@Transactional
	@Override
	public Boolean addBook(BookDto bookDto) {
		if(bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(new Publisher(bookDto.getPublisher()));
		
		Set<Author> authors = bookDto.getAuthors().stream()
			.map(a -> authorRepository.findById(a.getName())
					.orElse(new Author(a.getName(), a.getBirthDate())))
					.collect(Collectors.toSet());
		
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Transactional
	@Override
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.getAuthors().size();
		bookRepository.delete(book);
		return modelMapper.map(book, BookDto.class);
	}

	@Transactional
	@Override
	public BookDto updateBookTitle(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		if(title != null) {
			book.setTitle(title);
			bookRepository.save(book);
		}
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public BookDto[] findBooksByAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		return bookRepository.findBooksByAuthor(author.getName()).stream()
					.map(b -> modelMapper.map(b, BookDto.class))
					.toArray(BookDto[]::new);
	}

	@Override
	public BookDto[] findBooksByPublisher(String publisherName) {
		Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(EntityNotFoundException::new);
		return bookRepository.findBooksByPublisher(publisher.getPublisherName()).stream()
				.map(b -> modelMapper.map(b, BookDto.class))
				.toArray(BookDto[]::new);
	}

	@Override
	public AuthorDto[] findBookAuthors(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		Set<Author> authors = book.getAuthors();
		return authors.stream()
				.map(a -> modelMapper.map(a, AuthorDto.class))
				.toArray(AuthorDto[]::new);
	}

	@Override
	public String[] findPublishersByAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		List<Book> books = bookRepository.findBooksByAuthor(author.getName());
		Set<String> publishers = books.stream()
									  .map(b -> b.getPublisher().getPublisherName())
									  .collect(Collectors.toSet());
		return publishers.toArray(new String[0]);
	}

	@Transactional
	@Override
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
	    BookDto[] bookDtos = findBooksByAuthor(authorName);
	    Set<String> bookIsbns = Arrays.stream(bookDtos)
	                                   .map(BookDto::getIsbn)
	                                   .collect(Collectors.toSet());
	    bookIsbns.forEach(isbn -> {
	        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
	        book.getAuthors().remove(author);
	        bookRepository.save(book);
	    });

	    bookIsbns.forEach(isbn -> bookRepository.deleteById(isbn));
	    authorRepository.delete(author);
	    return modelMapper.map(author, AuthorDto.class);
	}

}

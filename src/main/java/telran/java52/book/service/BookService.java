package telran.java52.book.service;

import telran.java52.book.dto.AuthorDto;
import telran.java52.book.dto.BookDto;

public interface BookService {

	Boolean addBook(BookDto bookDto);
	
	BookDto findBookByIsbn(String isbn);
	
	BookDto remove(String isbn);
	
	BookDto updateBookTitle(String isbn, String title);
	
	Iterable<BookDto> findBooksByAuthor(String author);
	
	Iterable<BookDto> findBooksByPublisher(String publisher);
	
	Iterable<AuthorDto> findBookAuthors(String isbn);
	
	Iterable<String> findPublishersByAuthor(String author);
	
	AuthorDto removeAuthor(String author);
}

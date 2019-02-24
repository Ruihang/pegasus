package net.brilliance.service;

import net.brilliance.domain.entity.Book;
import net.brilliance.exceptions.ObjectNotFoundException;
import net.brilliance.framework.service.GenericService;

public interface BookService extends GenericService<Book, Long> {
	Book getByIsbn(String isbn) throws ObjectNotFoundException;
	Book getByIsbn13(String isbn13) throws ObjectNotFoundException;
}

package net.brilliance.manager.catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.brilliance.domain.entity.general.RefBook;
import net.brilliance.framework.logging.LogService;
import net.brilliance.repository.general.RefBookRepository;


@Service
@Transactional
public class RefBookManager {
	@Inject 
	private LogService log;

	@Inject
	private RefBookRepository bookRepository;

	@Transactional(readOnly = true)
	public List<RefBook> findAll() {
		List<RefBook> results = new ArrayList<>();
		results.addAll(bookRepository.findAll());
		return results;
	}

	@Transactional(readOnly = true)
	public RefBook findById(Long id) {
		Optional<RefBook> result = bookRepository.findOneById(id);
		return result.isPresent()?result.get():null;
	}

	public RefBook save(RefBook book) {
		bookRepository.save(book);
		return book;
	}

	public void delete(RefBook book) {
		bookRepository.delete(book);
	}

	/**
	 * Removes all Book entities from database.
	 */
	public void deleteAll() {
		bookRepository.deleteAll();
	}

	/**
	 * Restore the original set of books to the database.
	 */
	public void restoreDefaultBooks() {
		ClassPathResource resource = new ClassPathResource("/config/liquibase/data/books.csv");

		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(
					resource.getInputStream()));

			// Skip first line that only holds headers.
			br.readLine();

			String line;

			while ((line = br.readLine()) != null) {
				String[] words = line.split("~");

				Integer version = Integer.valueOf(words[0]);
				String name = words[1];
				String publisher = words[2];

				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dateOfPublication = null;
				try {
					dateOfPublication = sdf.parse(words[3]);
				} catch (ParseException e) {
					log.error(e.getMessage(), e);
				}

				String description = words[4];
				String photo = words[5];

				RefBook b = new RefBook(version, name, publisher, dateOfPublication,
						description, photo);

				bookRepository.save(b);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Release resources.
				br.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

	}

	public RefBook getByName(String name) {
		return bookRepository.findByName(name);
	}
}

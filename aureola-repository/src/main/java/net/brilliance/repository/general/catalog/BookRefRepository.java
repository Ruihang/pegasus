/**
 * 
 */
package net.brilliance.repository.general.catalog;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.brilliance.domain.entity.general.RefBook;
import net.brilliance.framework.repository.JBaseRepository;

/**
 * @author ducbq
 *
 */
@Repository
public interface BookRefRepository extends JBaseRepository<RefBook, Long> {
	Optional<RefBook> findByIsbn(String license);
	Optional<RefBook> findByName(String name);
}

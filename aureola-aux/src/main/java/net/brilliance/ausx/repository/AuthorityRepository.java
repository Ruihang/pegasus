/**
 * 
 */
package net.brilliance.ausx.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.brilliance.ausx.domain.security.Authority;
import net.brilliance.framework.repository.BaseRepository;

/**
 * @author ducbq
 *
 */
@Repository
public interface AuthorityRepository extends BaseRepository<Authority, Long> {
	Page<Authority> findAll(Pageable pageable);
	Page<Authority> findAllByOrderByIdAsc(Pageable pageable);
	Optional<Authority> findByName(String name);

	Optional<Authority> findByCode(String code);
	Long countByCode(String code);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%'))"
			+ "or LOWER(entity.displayName) like LOWER(CONCAT('%',:keyword,'%')) "
			+ "or LOWER(entity.description) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Authority> search(@Param("keyword") String keyword, Pageable pageable);
}

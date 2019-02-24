/**
 * 
 */
package net.brilliance.ausx.repository;

import org.springframework.stereotype.Repository;

import net.brilliance.ausx.domain.security.AccessRight;
import net.brilliance.framework.repository.BaseRepository;

/**
 * @author ducbq
 *
 */
@Repository
public interface AccessRightRepository extends BaseRepository<AccessRight, Long> {
	AccessRight findByName(String name);
}

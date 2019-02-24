/**
 * 
 */
package net.brilliance.ausx.repository;

import org.springframework.stereotype.Repository;

import net.brilliance.ausx.domain.security.Permission;
import net.brilliance.framework.repository.BaseRepository;

/**
 * @author ducbq
 *
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {
}

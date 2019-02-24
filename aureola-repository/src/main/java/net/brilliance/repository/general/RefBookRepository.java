package net.brilliance.repository.general;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.brilliance.domain.entity.general.RefBook;
import net.brilliance.framework.repository.IBaseRepository;

@Repository
public interface RefBookRepository extends IBaseRepository<RefBook, Long>{
	Optional<RefBook> findOneById(Long id);
	RefBook findByName(String name);
}

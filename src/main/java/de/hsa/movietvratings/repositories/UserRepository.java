package de.hsa.movietvratings.repositories;

import de.hsa.movietvratings.models.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Long> {
    UserData findUserDataByUsername(String username);
}

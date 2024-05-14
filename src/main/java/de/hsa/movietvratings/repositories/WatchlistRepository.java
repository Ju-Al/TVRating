package de.hsa.movietvratings.repositories;

import de.hsa.movietvratings.models.WatchlistEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends CrudRepository<WatchlistEntry, Long> {

    List<WatchlistEntry> findAllByUserId(long userId);

    Optional<WatchlistEntry> findWatchlistEntryByUserIdAndMediaId(long userId, int mediaId);
}

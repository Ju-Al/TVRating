package de.hsa.movietvratings.repositories;

import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    List<Rating> findAllByUserId(long userId);
    Optional<Rating> findRatingByUserIdAndMediaId(long userId, int mediaId);
    List<Rating> findAllByMediaId(int mediaId);
    int countByMediaIdAndRating(int mediaId, int rating);
    int countByMediaId(int mediaId);
}

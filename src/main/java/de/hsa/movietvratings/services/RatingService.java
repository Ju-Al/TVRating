package de.hsa.movietvratings.services;

import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.Rating;
import de.hsa.movietvratings.models.UserData;
import de.hsa.movietvratings.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratings;
    private final UserService userService;

    public RatingService(RatingRepository ratings, UserService userService) {
        this.ratings = ratings;
        this.userService = userService;
    }

    public Iterable<Rating> allRatings() {
        return ratings.findAll();
    }

    public float getAverage(int mediaId) {
        final List<Rating> ratingsList = ratings.findAllByMediaId(mediaId);
        if (ratingsList.size() == 0) {
            return 0;
        }
        float result = 0;
        for (Rating r : ratingsList) {
            result += r.getRating();
        }
        return result / ratingsList.size();
    }

    public void newRating(int mediaId, MediaType mediaType, int ratingNum) {
        final UserData user = userService.getCurrentUser();
        final Optional<Rating> existingRating = ratings.findRatingByUserIdAndMediaId(user.getId(), mediaId);
        if (existingRating.isPresent()) {
            throw new IllegalStateException("Rating already exists");
        }
        final Rating rating = new Rating(user, mediaId, mediaType, ratingNum);
        ratings.save(rating);
    }

    public Optional<Rating> findUserRating(int mediaId) {
        final UserData user = userService.getCurrentUser();
        return ratings.findRatingByUserIdAndMediaId(user.getId(), mediaId);
    }

    public void deleteRating(Rating rating) {
        ratings.delete(rating);
    }

    public void updateRating(int mediaId, MediaType mediaType, int rating){
        Optional<Rating> existingRating = findUserRating(mediaId);
        existingRating.ifPresent(this::deleteRating);
        newRating(mediaId, mediaType, rating);
    }

    public int getAmountOfRatingsForMedia(int mediaId, int rating) {
        return ratings.countByMediaIdAndRating(mediaId, rating);
    }

    public int getAmountOfRatingsForMedia(int mediaId) {
        return ratings.countByMediaId(mediaId);
    }

    public List<Rating> getUserRatings() {
        final UserData user = userService.getCurrentUser();
        return ratings.findAllByUserId(user.getId());
    }
}

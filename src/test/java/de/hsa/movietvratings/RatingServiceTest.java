package de.hsa.movietvratings;

import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.Rating;
import de.hsa.movietvratings.models.UserData;
import de.hsa.movietvratings.repositories.RatingRepository;
import de.hsa.movietvratings.services.RatingService;
import de.hsa.movietvratings.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RatingServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;
    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    public void setUp() {
        userService.newUser("test", "testpw");
    }

    @Test
    @WithMockUser(username = "test")
    public void createRating() {
        ratingService.newRating(0, MediaType.MOVIE, 1);
        final Optional<Rating> found = ratingService.findUserRating(0);
        assertThat(found).isPresent();
        assertThat(found.get().getRating()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "test")
    public void deleteRating() {
        ratingService.newRating(0, MediaType.MOVIE, 1);
        final Optional<Rating> found = ratingService.findUserRating(0);
        assertThat(found).isPresent();
        ratingService.deleteRating(found.get());
        assertThat(ratingService.findUserRating(0)).isNotPresent();
    }

    @Test
    public void average() {
        final UserData user1 = userService.newUser("test1", "testpw");
        final UserData user2 = userService.newUser("test2", "testpw");
        final UserData user3 = userService.newUser("test3", "testpw");

        ratingRepository.save(new Rating(user1, 0, MediaType.MOVIE, 1));
        ratingRepository.save(new Rating(user2, 0, MediaType.MOVIE, 2));
        ratingRepository.save(new Rating(user3, 0, MediaType.MOVIE, 3));

        assertThat(ratingService.getAverage(0)).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "test")
    public void duplicate() {
        ratingService.newRating(0, MediaType.MOVIE, 2);
        assertThatThrownBy(() -> {
            ratingService.newRating(0, MediaType.MOVIE, 1);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void count() {
        final UserData user1 = userService.newUser("test1", "testpw");
        final UserData user2 = userService.newUser("test2", "testpw");
        final UserData user3 = userService.newUser("test3", "testpw");

        ratingRepository.save(new Rating(user1, 0, MediaType.MOVIE, 1));
        ratingRepository.save(new Rating(user2, 0, MediaType.MOVIE, 1));
        ratingRepository.save(new Rating(user3, 0, MediaType.MOVIE, 2));

        assertThat(ratingService.getAmountOfRatingsForMedia(0, 1)).isEqualTo(2);
        assertThat(ratingService.getAmountOfRatingsForMedia(0, 2)).isEqualTo(1);
        assertThat(ratingService.getAmountOfRatingsForMedia(0, 3)).isEqualTo(0);
        assertThat(ratingService.getAmountOfRatingsForMedia(0, 4)).isEqualTo(0);
        assertThat(ratingService.getAmountOfRatingsForMedia(0, 5)).isEqualTo(0);

        assertThat(ratingService.getAmountOfRatingsForMedia(0)).isEqualTo(3);
    }
}

package de.hsa.movietvratings;

import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.RatingService;
import de.hsa.movietvratings.services.UserService;
import de.hsa.movietvratings.services.WatchlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WatchlistServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WatchlistService watchlistService;

    @BeforeEach
    public void setUp() {
        userService.newUser("test", "testpw");
    }

    @Test
    @WithMockUser(username = "test")
    public void addToWatchlist() {
        watchlistService.addToWatchlist(0, MediaType.MOVIE);
        assertThat(watchlistService.isInWatchlist(0)).isTrue();
        assertThat(watchlistService.getWatchlist()).hasSize(1);
    }

    @Test
    @WithMockUser(username = "test")
    public void removeFromWatchlist() {
        watchlistService.addToWatchlist(0, MediaType.MOVIE);
        assertThat(watchlistService.isInWatchlist(0)).isTrue();
        assertThat(watchlistService.getWatchlist()).hasSize(1);

        watchlistService.removeFromWatchlist(0);
        assertThat(watchlistService.isInWatchlist(0)).isFalse();
        assertThat(watchlistService.getWatchlist()).hasSize(0);
    }
}

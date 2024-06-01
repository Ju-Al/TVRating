package de.hsa.movietvratings;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieDBTests {

    @Autowired
    TmdbApi tmdbApi;

    @Test
    void testTmdbApiExists() {
        assertThat(tmdbApi).isNotNull();
    }

    @Test
    void testSingleMovie() {
        final MovieDb movie = tmdbApi.getMovies().getMovie(348, "en");
        assertThat(movie).isNotNull();
        assertThat(movie.getTitle()).isEqualTo("Alien");
        assertThat(movie.getReleaseDate()).isEqualTo("1979-05-25");

        final TvSeries series = tmdbApi.getTvSeries().getSeries(1399, "en");
        final TvEpisode episode = tmdbApi.getTvEpisodes().getEpisode(1399, 1, 1, "en");
        System.out.println(episode.getName());
    }
}

package de.hsa.movietvratings.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import de.hsa.movietvratings.components.DetailComponent;
import de.hsa.movietvratings.layout.MainLayout;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.RatingService;
import de.hsa.movietvratings.services.WatchlistService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Filmdetails")
@Route(value = "movie", layout = MainLayout.class)
@PermitAll
public class MovieDetailView extends HorizontalLayout implements HasUrlParameter<String> {

    private final TmdbApi tmdbApi;
    private final DetailComponent movieDetailComponent;

    public MovieDetailView(TmdbApi tmdbApi, WatchlistService watchlistService, CommentService commentService, RatingService ratingService) {
        this.tmdbApi = tmdbApi;
        this.movieDetailComponent = new DetailComponent(watchlistService, commentService, ratingService);
        add(movieDetailComponent);
        addClassName("detail-view");
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        MovieDb movie = tmdbApi.getMovies().getMovie(Integer.parseInt(parameter), "de");
        List<Genre> genres = movie.getGenres();
        StringBuilder genreText = new StringBuilder();
        for (Genre genre : genres) {
            genreText.append(formatGenre(genre.toString())).append(", ");
        }
        LocalDate firstAirDate = LocalDate.parse(movie.getReleaseDate());
        DateTimeFormatter germanDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = firstAirDate.format(germanDateFormatter);
        movieDetailComponent.setDetails(movie.getId(), "https://image.tmdb.org/t/p/original" + movie.getPosterPath(),
                movie.getTitle(), movie.getOverview(), MediaType.MOVIE,
                "Länge: " + movie.getRuntime() + " Minuten", "Genre: " + genreText,
                "Erscheinungsdatum: " + formattedDate);
    }

    private static String formatGenre(String genre) {
        // Entfernen der Zahlen und eckigen Klammern
        return genre.replaceAll("\\[\\d+\\]", "").trim();
    }
}

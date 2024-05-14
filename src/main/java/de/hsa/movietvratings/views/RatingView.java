package de.hsa.movietvratings.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hsa.movietvratings.components.RatingComponent;
import de.hsa.movietvratings.components.TitleImageComponent;
import de.hsa.movietvratings.layout.MainLayout;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.Rating;
import de.hsa.movietvratings.services.RatingService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Ratings")
@Route(value = "ratings", layout = MainLayout.class)
@PermitAll
public class RatingView extends FlexLayout {
    public RatingView(TmdbApi tmdbApi, RatingService ratingService) {
        setFlexWrap(FlexLayout.FlexWrap.WRAP);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final List<Rating> ratings = ratingService.getUserRatings();
        for (Rating r : ratings) {
            final HorizontalLayout layout = new HorizontalLayout();
            if (r.getMediaType() == MediaType.SERIES) {
                final TvSeries series = tmdbApi.getTvSeries().getSeries(r.getMediaId(), "de");
                layout.add(new TitleImageComponent("https://image.tmdb.org/t/p/original" + series.getPosterPath(),
                        series.getName(), "/tv/" + series.getId(), 12));
            } if (r.getMediaType() == MediaType.MOVIE) {
                final MovieDb movie = tmdbApi.getMovies().getMovie(r.getMediaId(), "de");
                layout.add(new TitleImageComponent("https://image.tmdb.org/t/p/original" + movie.getPosterPath(),
                        movie.getTitle(), "/movie/" + movie.getId(), 12));
            }
            final RatingComponent ratingComponent = new RatingComponent(ratingService, r.getMediaId(), r.getMediaType());
            ratingComponent.getStyle().set("margin-top", "5rem");
            layout.add(ratingComponent);
            add(layout);
        }
    }
}

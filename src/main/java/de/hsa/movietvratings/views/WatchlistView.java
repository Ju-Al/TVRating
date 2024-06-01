package de.hsa.movietvratings.views;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hsa.movietvratings.components.TitleImageComponent;
import de.hsa.movietvratings.layout.MainLayout;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.WatchlistEntry;
import de.hsa.movietvratings.services.WatchlistService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.List;

@PageTitle("Merkliste")
@Route(value = "media", layout = MainLayout.class)
@AnonymousAllowed
public class WatchlistView extends VerticalLayout {

    private final TmdbApi tmdbApi;

    private final WatchlistService watchlistService;

    public WatchlistView(TmdbApi tmdbApi, WatchlistService watchlistService) {
        this.tmdbApi = tmdbApi;
        this.watchlistService = watchlistService;
        displayWatchlist();
    }

    public void displayWatchlist() {
        final FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        List<WatchlistEntry> watchlist = this.watchlistService.getWatchlist();
        for (WatchlistEntry entry : watchlist) {
            if (entry.getMediaType() == MediaType.MOVIE) {
                MovieDb movie = tmdbApi.getMovies().getMovie(entry.getMediaId(), "de");
                TitleImageComponent titleImageComponent = new TitleImageComponent(
                        "https://image.tmdb.org/t/p/original" + movie.getPosterPath(),
                        movie.getTitle(),
                        "/movie/" + movie.getId(), 20);
                layout.add(titleImageComponent);
            } else if(entry.getMediaType() == MediaType.SERIES){
                TvSeries series = tmdbApi.getTvSeries().getSeries(entry.getMediaId(), "de");
                TitleImageComponent titleImageComponent = new TitleImageComponent(
                        "https://image.tmdb.org/t/p/original" + series.getPosterPath(),
                        series.getName(),
                        "/tv/" + series.getId(), 20);
                layout.add(titleImageComponent);
            }
        }
        add(layout);
    }
}



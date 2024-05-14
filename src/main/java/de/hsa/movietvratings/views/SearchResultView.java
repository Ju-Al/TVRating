package de.hsa.movietvratings.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hsa.movietvratings.components.TitleImageComponent;
import de.hsa.movietvratings.layout.MainLayout;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jakarta.annotation.security.PermitAll;

@PageTitle("Suchergebnisse")
@Route(value = "search", layout = MainLayout.class)
@PermitAll
public class SearchResultView extends VerticalLayout implements HasUrlParameter<String> {

    private final TmdbApi tmdbApi;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {

        setSizeFull();

        add(new H2("Filme:"));
        final FlexLayout movieLayout = new FlexLayout();
        movieLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        add(movieLayout);
        final MovieResultsPage movieResult = tmdbApi.getSearch().searchMovie(parameter, 0, "de",false, 0);
        for (MovieDb movie : movieResult.getResults()) {
            if(movie.getPosterPath() != null){
                String imageUrl = "https://image.tmdb.org/t/p/original" + movie.getPosterPath();
                movieLayout.add(new TitleImageComponent(imageUrl, movie.getTitle(), "/movie/" + movie.getId(), 20));
            }
        }

        add(new H2("Serien:"));
        final FlexLayout tvLayout = new FlexLayout();
        tvLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        tvLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        add(tvLayout);
        final TvResultsPage tvResult = tmdbApi.getSearch().searchTv(parameter,"de", 0);
        for (TvSeries series : tvResult.getResults()) {
            if(series.getPosterPath() != null){
                String imageUrl = "https://image.tmdb.org/t/p/original" + series.getPosterPath();
                tvLayout.add(new TitleImageComponent(imageUrl, series.getName(), "/tv/" + series.getId(),20));
            }
        }
    }

    public SearchResultView(TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }
}

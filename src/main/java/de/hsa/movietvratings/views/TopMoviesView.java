package de.hsa.movietvratings.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import de.hsa.movietvratings.components.TitleImageComponent;
import de.hsa.movietvratings.layout.MainLayout;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import jakarta.annotation.security.PermitAll;

@PageTitle("Top Filme")
@Route(value = "movies/top", layout = MainLayout.class)
@PermitAll
public class TopMoviesView extends VerticalLayout {

    public TopMoviesView(TmdbApi tmdbApi) {
        setSpacing(false);

        H2 header = new H2("Top Filme in Deutschland");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        final FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);

        final MovieResultsPage page = tmdbApi.getMovies().getPopularMovies("de", 0);
        for (MovieDb movie : page) {
            if(movie.getPosterPath() != null) {
                layout.add(new TitleImageComponent(
                        "https://image.tmdb.org/t/p/original" + movie.getPosterPath(),
                        movie.getTitle(),
                        "/movie/" + movie.getId(), 20));
            }
        }
        add(layout);
    }
}

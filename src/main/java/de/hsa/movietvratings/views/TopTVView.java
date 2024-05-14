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
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jakarta.annotation.security.PermitAll;

@PageTitle("Top Serien")
@Route(value = "tv/top", layout = MainLayout.class)
@PermitAll
public class TopTVView extends VerticalLayout {

    public TopTVView(TmdbApi tmdbApi) {
        setSpacing(false);

        H2 header = new H2("Top Serien in Deutschland");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        final FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);

        final TvResultsPage page = tmdbApi.getTvSeries().getPopular("de", 0);
        for (TvSeries series : page) {
            if(series.getPosterPath() != null) {
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

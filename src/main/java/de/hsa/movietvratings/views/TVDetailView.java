package de.hsa.movietvratings.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.hsa.movietvratings.components.DetailComponent;
import de.hsa.movietvratings.layout.MainLayout;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.RatingService;
import de.hsa.movietvratings.services.WatchlistService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Seriendetails")
@Route(value = "tv", layout = MainLayout.class)
@PermitAll
public class TVDetailView extends HorizontalLayout implements HasUrlParameter<String> {

    private final TmdbApi tmdbApi;
    private final WatchlistService watchlistService;
    private final CommentService commentService;

    private final RatingService ratingService;
    private final DetailComponent tvDetailComponent;
    private final ComboBox<TvSeason> seasonComboBox;

    private final VerticalLayout episodesLayout;
    private final VerticalLayout listOfEpisodes;

    public TVDetailView(TmdbApi tmdbApi, WatchlistService watchlistService, CommentService commentService, RatingService ratingService) {
        this.tmdbApi = tmdbApi;
        this.watchlistService = watchlistService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.tvDetailComponent = new DetailComponent(watchlistService, commentService, ratingService);
        this.seasonComboBox = new ComboBox<>();
        this.episodesLayout = new VerticalLayout();
        this.listOfEpisodes = new VerticalLayout();
        addClassName("detail-view");
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        TvSeries series = tmdbApi.getTvSeries().getSeries(Integer.parseInt(parameter), "de");
        List<Genre> genres = series.getGenres();
        StringBuilder genreText = new StringBuilder();
        for (Genre genre : genres) {
            genreText.append(formatGenre(genre.toString())).append(", ");
        }
        LocalDate firstAirDate = LocalDate.parse(series.getFirstAirDate());
        DateTimeFormatter germanDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = firstAirDate.format(germanDateFormatter);
        tvDetailComponent.setDetails(series.getId(),
                "https://image.tmdb.org/t/p/original" + series.getPosterPath(),
                series.getName(),
                series.getOverview(),
                MediaType.SERIES,
                "Genre: " + genreText,
                "Erstausstrahlungsdatum: " + formattedDate,
                "Anzahl der Staffeln: " + series.getSeasons().size());
        seasonComboBox.setLabel("Staffel auswählen");
        seasonComboBox.setItems(series.getSeasons());
        seasonComboBox.setItemLabelGenerator(TvSeason::getName);
        seasonComboBox.addValueChangeListener(e -> updateEpisodesLayout(series, e.getValue()));
        seasonComboBox.getStyle().set("margin-left", "20px");
        episodesLayout.add(tvDetailComponent, seasonComboBox, listOfEpisodes);
        add(episodesLayout);
    }

    private void updateEpisodesLayout(TvSeries series, TvSeason seasonNumber) {
        try {
            TvSeason season = tmdbApi.getTvSeasons().getSeason(series.getId(), seasonNumber.getSeasonNumber(), "de");
            List<TvEpisode> episodes = season.getEpisodes();
            int episodeCount = 1;
            if (episodes != null) {
                listOfEpisodes.removeAll();
                for (TvEpisode episode : episodes) {
                    DetailComponent ne = new DetailComponent(watchlistService, commentService, ratingService);
                    ne.setDetails(episode.getId(), "https://image.tmdb.org/t/p/original" + series.getPosterPath(),
                            "Episode " + episodeCount + ": " + episode.getName(),
                            episode.getOverview(),
                            MediaType.EPISODE);
                    episodeCount++;
                    this.listOfEpisodes.add(ne);
                }
            } else {
                System.out.println("Episodes nicht gefunden");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatGenre(String genre) {
        return genre.replaceAll("\\[\\d+\\]", "").trim();
    }
}

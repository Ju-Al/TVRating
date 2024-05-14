package de.hsa.movietvratings.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hsa.movietvratings.components.CommentsComponent;
import de.hsa.movietvratings.components.TitleImageComponent;
import de.hsa.movietvratings.layout.MainLayout;
import de.hsa.movietvratings.models.Comment;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.UserService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jakarta.annotation.security.PermitAll;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Kommentare")
@Route(value = "comments", layout = MainLayout.class)
@PermitAll
public class CommentView extends TabSheet {
    private final TmdbApi tmdbApi;

    private final CommentService commentService;

    private final UserService userService;

    private final FlexLayout movieCommentArea;
    private final FlexLayout seriesCommentArea;
    private final String imageUrl = "https://image.tmdb.org/t/p/original";

    public CommentView(TmdbApi tmdbApi, CommentService commentService, UserService userService){
        this.tmdbApi = tmdbApi;
        this.commentService = commentService;
        this.userService = userService;
        List<Comment> commentList = commentService.getCommentsByUser();
        Tab movies = new Tab(VaadinIcon.MOVIE.create(), new Span("Filme"));
        Tab series = new Tab(LineAwesomeIcon.PLAY_CIRCLE_SOLID.create(), new Span("Serien"));

        for (Tab tab : new Tab[] { movies, series}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        movieCommentArea = new FlexLayout();
        movieCommentArea.setSizeFull();
        movieCommentArea.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        seriesCommentArea = new FlexLayout();
        seriesCommentArea.setSizeFull();
        seriesCommentArea.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        add(movies, movieCommentArea);
        add(series, seriesCommentArea);

        displayComments(commentList);
    }

    private void displayComments(List<Comment> commentList){
        Map<MediaType, List<Comment>> commentsByMediaType = sortAndSplitCommentsByMediaType(commentList);
        List<Comment> seriesComments = commentsByMediaType.getOrDefault(MediaType.SERIES, Collections.emptyList());
        List<Comment> movieComments = commentsByMediaType.getOrDefault(MediaType.MOVIE, Collections.emptyList());
        List<Comment> episodeComments = commentsByMediaType.getOrDefault(MediaType.EPISODE, Collections.emptyList());

        if(!movieComments.isEmpty()){
            System.out.println("dsjf");
            loadAllMovieComments(movieComments);
        } else {
            movieCommentArea.add(new Text("Es sind noch keine Kommentare vorhanden"));
        }
        if(!seriesComments.isEmpty()){
            loadAllSeriesComments(seriesComments);
        } else {
            seriesCommentArea.add(new Text("Es sind noch keine Kommentare vorhanden"));
        }
    }

    private void loadAllSeriesComments(List<Comment> seriesComments) {
        seriesCommentArea.removeAll();
        for(Comment comment: seriesComments){
            TvSeries series = tmdbApi.getTvSeries().getSeries(comment.getMediaId(), "de");
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.getStyle().set("margin-right", "10px");
            horizontalLayout.add(new TitleImageComponent( imageUrl + series.getPosterPath(),
                    series.getName(),
                    "/tv/" + series.getId(), 10), new CommentsComponent(commentService, userService, comment.getMediaId(), comment.getMediaType(), "Ihre Kommentare"));
            seriesCommentArea.add(horizontalLayout);
        }
    }

    private void loadAllMovieComments(List<Comment> movieComments) {
        movieCommentArea.removeAll();
        for(Comment comment: movieComments){
            MovieDb movies = tmdbApi.getMovies().getMovie(comment.getMediaId(), "de");
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(new TitleImageComponent( imageUrl + movies.getPosterPath(),
                    movies.getTitle(),
                    "/movie/" + movies.getId(), 10), new CommentsComponent(commentService, userService, comment.getMediaId(), comment.getMediaType(), "Ihre Kommentare"));
            movieCommentArea.add(horizontalLayout);
        }
    }

    public static Map<MediaType, List<Comment>> sortAndSplitCommentsByMediaType(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyMap();
        }
        Comparator<Comment> comparator = Comparator.comparing(Comment::getMediaType);

        comments.sort(comparator);

        Map<MediaType, List<Comment>> commentsByMediaType = comments.stream()
                .collect(Collectors.groupingBy(Comment::getMediaType));

        commentsByMediaType.forEach((mediaType, mediaTypeComments) ->
                commentsByMediaType.put(mediaType, removeDuplicateComments(mediaTypeComments)));

        return commentsByMediaType;
    }

    private static List<Comment> removeDuplicateComments(List<Comment> comments) {
        Set<Integer> uniqueMediaIds = new HashSet<>();
        List<Comment> uniqueComments = new ArrayList<>();

        for (Comment comment : comments) {
            if (uniqueMediaIds.add(comment.getMediaId())) {
                uniqueComments.add(comment);
            }
        }

        return uniqueComments;
    }

}

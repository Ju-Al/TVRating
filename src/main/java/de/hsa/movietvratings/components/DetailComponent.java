package de.hsa.movietvratings.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.RatingService;
import de.hsa.movietvratings.services.WatchlistService;

public class DetailComponent extends HorizontalLayout {

    private final CommentService commentService;

    private final WatchlistService watchlistService;
    private final Image image;
    private final VerticalLayout textLayout = new VerticalLayout();

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final H2 title;

    private final RatingService ratingService;
    private final Paragraph overview;
    private final Div optionalDetailsContainer;

    public DetailComponent(WatchlistService watchlistService, CommentService commentService, RatingService ratingService) {
        this.watchlistService = watchlistService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        image = new Image();
        title = new H2();
        overview = new Paragraph();
        optionalDetailsContainer = new Div();

        addClassName("detail-view");
        image.addClassName("detail-image");
        title.addClassName("detail-title");
        overview.addClassName("detail-overview");
        optionalDetailsContainer.addClassName("optional-details-container");
    }

    public void setDetails(int id, String imagePath, String titleText, String overviewText,
                           MediaType type, String... optionalDetails) {

        image.setSrc(imagePath);
        title.setText(titleText);
        overview.setText(overviewText);
        overview.getStyle().set("text-align", "justify").set("max-width", "800px");
        RatingComponent ratingComponent = new RatingComponent(ratingService, id, type);

        WatchListComponent watchListComponent = new WatchListComponent(watchlistService, id, type);

        if (optionalDetails != null && optionalDetails.length > 0) {
            watchListComponent.setVisible(true);
            image.setMaxWidth(20, Unit.REM);
        } else {
            watchListComponent.setVisible(false);
            image.setMaxWidth(10, Unit.REM);
            image.setMaxHeight(15, Unit.REM);
            image.getStyle().set("margin-bottom", "30px");
        }

        optionalDetailsContainer.removeAll();
        assert optionalDetails != null;
        for (String detail : optionalDetails) {
            Span detailSpan = new Span(detail);
            optionalDetailsContainer.add(detailSpan, new Paragraph());
        }

        this.buttonLayout.add(title, watchListComponent);

        textLayout.add(buttonLayout, ratingComponent, overview, optionalDetailsContainer);
        add(image, textLayout);

        textLayout.add(new CommentsComponent(commentService, null, id, type, "Kommentare"));
        textLayout.getStyle().set("padding-top", "0px").set("padding-bottom", "0px");
    }
}
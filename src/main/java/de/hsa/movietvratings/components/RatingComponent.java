package de.hsa.movietvratings.components;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.shared.Tooltip;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.Rating;
import de.hsa.movietvratings.services.RatingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag("div")
public class RatingComponent extends HorizontalLayout {

    private final RatingService ratingService;
    private final int mediaId;
    private final MediaType mediaType;
    private final List<Button> starButtons;
    private final Div starButtonContainer;

    private H4 ratingTitle;
    private ProgressBar fiveStarBar;
    private ProgressBar fourStarBar;
    private ProgressBar threeStarBar;
    private ProgressBar twoStarBar;
    private ProgressBar oneStarBar;

    public RatingComponent(RatingService ratingService, int mediaId, MediaType mediaType) {
        this.ratingService = ratingService;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.starButtons = new ArrayList<>();
        this.starButtonContainer = new Div();

        setupRatingBars();

        createStarButtons();
        loadUserRating();
        add(starButtonContainer);

        Tooltip.forComponent(starButtonContainer)
                .withText("Um die Bewertung zu löschen, wählen Sie den aktuellen Stern erneut aus")
                .withPosition(Tooltip.TooltipPosition.BOTTOM);

        add(this.ratingTitle, this.fiveStarBar, this.fourStarBar, this.threeStarBar, this.twoStarBar, this.oneStarBar);
    }

    private void setupRatingBars() {
        this.ratingTitle = new H4(formatAverageText(0, 0));
        this.ratingTitle.getStyle().set("margin-top", "1rem");
        this.fiveStarBar = new ProgressBar(0, 1, 0);
        this.fourStarBar = new ProgressBar(0, 1, 0);
        this.threeStarBar = new ProgressBar(0, 1, 0);
        this.twoStarBar = new ProgressBar(0, 1, 0);
        this.oneStarBar = new ProgressBar(0, 1, 0);
    }

    public void setFilledStars(int filledStars) {
        starButtons.forEach(button -> {
            int index = starButtons.indexOf(button);
            Icon starIcon = createStarIcon(index < filledStars);
            button.setIcon(starIcon);
        });
    }

    private Icon createStarIcon(boolean filled) {
        VaadinIcon icon = filled ? VaadinIcon.STAR : VaadinIcon.STAR_O;
        Icon starIcon = new Icon(icon);
        starIcon.setSize("24px");
        return starIcon;
    }

    private void onStarClick(int rating) {
        final Optional<Rating> currentRating = ratingService.findUserRating(mediaId);
        if (currentRating.isPresent() && currentRating.get().getRating() == rating) {
            // delete the rating if the user clicks on the current star again
            ratingService.deleteRating(currentRating.get());
        } else {
            this.ratingService.updateRating(mediaId, mediaType, rating);
        }
        loadUserRating();
    }

    private static String formatAverageText(float average, int amount) {
        if (amount == 0) {
            return "Noch keine Bewertungen";
        }
        if (amount == 1) {
            return "Durchschnitt: " + String.format("%.1f", average) + " (1 Bewertung)";
        }
        return "Durchschnitt: " + String.format("%.1f", average) + " (" + amount + " Bewertungen)";
    }

    private void loadUserRating() {
        int userRating = ratingService.findUserRating(mediaId).map(Rating::getRating).orElse(0);

        setFilledStars(userRating);

        int totalCount = ratingService.getAmountOfRatingsForMedia(mediaId);
        final float average = ratingService.getAverage(mediaId);

        ratingTitle.setText(formatAverageText(average, totalCount));
        if (totalCount == 0) {
            fiveStarBar.setVisible(false);
            fourStarBar.setVisible(false);
            threeStarBar.setVisible(false);
            twoStarBar.setVisible(false);
            oneStarBar.setVisible(false);
        } else {
            fiveStarBar.setVisible(true);
            fourStarBar.setVisible(true);
            threeStarBar.setVisible(true);
            twoStarBar.setVisible(true);
            oneStarBar.setVisible(true);
        }

        fiveStarBar.setMax(totalCount);
        fourStarBar.setMax(totalCount);
        threeStarBar.setMax(totalCount);
        twoStarBar.setMax(totalCount);
        oneStarBar.setMax(totalCount);

        fiveStarBar.setValue(ratingService.getAmountOfRatingsForMedia(mediaId, 5));
        fourStarBar.setValue(ratingService.getAmountOfRatingsForMedia(mediaId, 4));
        threeStarBar.setValue(ratingService.getAmountOfRatingsForMedia(mediaId, 3));
        twoStarBar.setValue(ratingService.getAmountOfRatingsForMedia(mediaId, 2));
        oneStarBar.setValue(ratingService.getAmountOfRatingsForMedia(mediaId, 1));
    }

    private void createStarButtons() {
        for (int i = 0; i < 5; i++) {
            Icon starIcon = createStarIcon(false);
            Button starButton = new Button(starIcon);
            starButton.getStyle().set("margin-right", "5px");
            int finalI = i;
            starButton.addClickListener(e -> onStarClick(finalI + 1));
            starButtons.add(starButton);
            starButtonContainer.add(starButton);
        }
    }
}

package de.hsa.movietvratings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.WatchlistService;

public class WatchListComponent extends Button {
    private final WatchlistService watchlistService;
    private final int mediaId;

    private final MediaType mediaType;

    public WatchListComponent(WatchlistService watchlistService, int mediaId, MediaType mediaType){
        this.watchlistService = watchlistService;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        getStyle().set("margin-bottom", "10px");
        getStyle().set("margin-top", "0px");
        setTooltipText("Zur Merkliste hinzufügen");
        setButtonIcon();
        addClickListener(e -> toggleWatchListStatus());

    }
    private void setButtonIcon() {
        if (this.watchlistService.isInWatchlist(mediaId)) {
            setIcon(new Icon(VaadinIcon.CHECK));
        } else {
            setIcon(new Icon(VaadinIcon.PLUS));
        }
    }

    private void toggleWatchListStatus() {
        if (!this.watchlistService.isInWatchlist(mediaId)) {
            this.watchlistService.addToWatchlist(mediaId, mediaType);
        } else {
            this.watchlistService.removeFromWatchlist(mediaId);
        }
        setButtonIcon();
    }
}

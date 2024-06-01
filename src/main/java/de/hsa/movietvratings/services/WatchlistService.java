package de.hsa.movietvratings.services;

import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.UserData;
import de.hsa.movietvratings.models.WatchlistEntry;
import de.hsa.movietvratings.repositories.WatchlistRepository;
import de.hsa.movietvratings.security.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService {

    private WatchlistRepository watchlistRepository;
    private SecurityService securityService;
    private UserService userService;

    public WatchlistService(WatchlistRepository watchlistRepository, SecurityService securityService, UserService userService) {
        this.watchlistRepository = watchlistRepository;
        this.securityService = securityService;
        this.userService = userService;
    }

    public List<WatchlistEntry> getWatchlist() {
        final UserData user = userService.getCurrentUser();
        return watchlistRepository.findAllByUserId(user.getId());
    }

    public void addToWatchlist(int mediaId, MediaType mediaType) {
        final UserData user = userService.getCurrentUser();
        final Optional<WatchlistEntry> existing = watchlistRepository.findWatchlistEntryByUserIdAndMediaId(user.getId(), mediaId);

        if (existing.isEmpty()) {
            watchlistRepository.save(new WatchlistEntry(user, mediaId, mediaType));
        }
    }

    public void removeFromWatchlist(int mediaId) {
        final UserData user = userService.getCurrentUser();
        final Optional<WatchlistEntry> existing = watchlistRepository.findWatchlistEntryByUserIdAndMediaId(user.getId(), mediaId);

        if (existing.isPresent()) {
            watchlistRepository.delete(existing.get());
        }
    }

    public boolean isInWatchlist(int mediaId) {
        final UserData user = userService.getCurrentUser();
        final Optional<WatchlistEntry> existing = watchlistRepository.findWatchlistEntryByUserIdAndMediaId(user.getId(), mediaId);
        return existing.isPresent();
    }
}
package de.hsa.movietvratings.models;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "mediaId"}))
public class WatchlistEntry {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserData user;

    private int mediaId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    public WatchlistEntry() {
    }

    public WatchlistEntry(UserData user, int mediaId, MediaType mediaType) {
        this.user = user;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
    }
}

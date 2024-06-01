package de.hsa.movietvratings.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "mediaId"}))
public class Rating {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserData user;

    private int mediaId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Setter
    private int rating;

    public Rating() {
    }

    public Rating(UserData user, int mediaId, MediaType mediaType, int rating) {
        this.user = user;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.rating = rating;
    }
}

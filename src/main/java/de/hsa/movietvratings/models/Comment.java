package de.hsa.movietvratings.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserData user;

    private int mediaId;

    @Size(max = 1024)
    private String comment;

    private Instant time;

    private MediaType mediaType;

    public Comment() {
    }

    public Comment(UserData user, int mediaId, String comment, Instant time, MediaType mediaType) {
        this.user = user;
        this.mediaId = mediaId;
        this.comment = comment;
        this.time = time;
        this.mediaType = mediaType;
    }
}

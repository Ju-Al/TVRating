package de.hsa.movietvratings.services;

import de.hsa.movietvratings.models.Comment;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.models.UserData;
import de.hsa.movietvratings.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository comments;
    private final UserService userService;

    public CommentService(CommentRepository comments, UserService userService) {
        this.comments = comments;
        this.userService = userService;
    }

    public Comment newComment(int mediaId, String comment, MediaType mediaType) {
        final UserData user = userService.getCurrentUser();
        if (comment.length() > 1024) {
            throw new IllegalArgumentException("comment must be <= 1024 chars");
        }
        final Comment newComment = new Comment(user, mediaId, comment, Instant.now(), mediaType);
        comments.save(newComment);
        return newComment;
    }

    public List<Comment> getCommentsForMediaId(int mediaId) {
        return comments.findAllByMediaId(mediaId);
    }

    public List<Comment> getUserCommentsForMediaId(int mediaId, UserData user) {
        return comments.findAllByMediaIdAndUserId(mediaId, user.getId());
    }

    public List<Comment> getCommentsByUser() {
        final UserData user = userService.getCurrentUser();
        return comments.findAllByUserId(user.getId());
    }

    public void deleteComment(Comment comment) {
        comments.delete(comment);
    }
}

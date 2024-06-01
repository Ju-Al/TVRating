package de.hsa.movietvratings.repositories;

import de.hsa.movietvratings.models.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByUserId(long userId);
    List<Comment> findAllByMediaId(int mediaId);
    List<Comment> findAllByMediaIdAndUserId(int mediaId, long userId);
}

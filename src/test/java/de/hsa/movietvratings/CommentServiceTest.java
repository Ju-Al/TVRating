package de.hsa.movietvratings;

import de.hsa.movietvratings.models.Comment;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        userService.newUser("test", "testpw");
    }

    @Test
    @WithMockUser(username = "test")
    public void createComment() {
        assertThat(commentService.getCommentsByUser()).hasSize(0);
        assertThat(commentService.getCommentsForMediaId(0)).hasSize(0);
        commentService.newComment(0, "Great movie!", MediaType.MOVIE);
        assertThat(commentService.getCommentsByUser()).hasSize(1);
        assertThat(commentService.getCommentsForMediaId(0)).hasSize(1);
    }

    @Test
    @WithMockUser(username = "test")
    public void deleteComment() {
        assertThat(commentService.getCommentsByUser()).hasSize(0);
        assertThat(commentService.getCommentsForMediaId(0)).hasSize(0);
        final Comment comment = commentService.newComment(0, "Great movie!", MediaType.MOVIE);
        assertThat(commentService.getCommentsByUser()).hasSize(1);
        assertThat(commentService.getCommentsForMediaId(0)).hasSize(1);
        commentService.deleteComment(comment);
        assertThat(commentService.getCommentsByUser()).hasSize(0);
        assertThat(commentService.getCommentsForMediaId(0)).hasSize(0);
    }

    @Test
    @WithMockUser(username = "test")
    public void tooLong() {
        final String tooLong = "SPAM".repeat(1024);
        assertThatThrownBy(() -> commentService.newComment(0, tooLong, MediaType.MOVIE)).isInstanceOf(IllegalArgumentException.class);
    }
}

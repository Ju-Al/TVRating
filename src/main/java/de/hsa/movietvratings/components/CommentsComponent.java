package de.hsa.movietvratings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import de.hsa.movietvratings.models.Comment;
import de.hsa.movietvratings.models.MediaType;
import de.hsa.movietvratings.services.CommentService;
import de.hsa.movietvratings.services.UserService;

import java.util.List;
import java.util.ArrayList;

public class CommentsComponent extends Details {

    private final CommentService commentService;
    private final UserService userService;
    private final MessageList list;
    private ComboBox<Comment> commentsComboBox;

    public CommentsComponent(CommentService commentService, UserService userService, int mediaId, MediaType mediaType, String title) {
        super(title);
        this.commentService = commentService;
        this.userService = userService;

        list = new MessageList();
        add(list);

        final MessageInput input = new MessageInput();
        input.addSubmitListener(submitEvent -> {
            commentService.newComment(mediaId, submitEvent.getValue(), mediaType);
            loadComments(mediaId);
        });

        if (userService != null) {
            getStyle().set("transform", "scale(0.8").set("transform-origin", "top left");
            createComboBoxAndDeleteButton(mediaId);
        } else {
            add(input);
        }

        loadComments(mediaId);
    }

    private void createComboBoxAndDeleteButton(int mediaId) {
        this.commentsComboBox = new ComboBox<>("Kommentare auswählen");
        this.commentsComboBox.getStyle().set("width", "fit-content").set("margin-left", "16px");
        loadUserComments(mediaId);

        Button deleteButton = new Button("Löschen");
        deleteButton.getStyle().setBackground("red").setColor("white").set("margin-left", "10px");
        deleteButton.addClickListener(event -> {
            Comment selectedComment = this.commentsComboBox.getValue();
            if (selectedComment != null) {
                deleteComment(selectedComment, mediaId);
            }
        });

        add(commentsComboBox, deleteButton);
    }

    private void loadComments(int mediaId) {
        final List<Comment> comments = commentService.getCommentsForMediaId(mediaId);
        updateMessageList(comments);
    }

    private void loadUserComments(int mediaId) {
        final List<Comment> userComments = commentService.getUserCommentsForMediaId(mediaId, userService.getCurrentUser());
        updateMessageList(userComments);
        this.commentsComboBox.setItemLabelGenerator(Comment::getComment);
        this.commentsComboBox.setItems(userComments);
    }

    private void deleteComment(Comment comment, int mediaId) {
        commentService.deleteComment(comment);
        loadUserComments(mediaId);
    }

    private void updateMessageList(List<Comment> comments) {
        final List<MessageListItem> messages = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            final Comment c = comments.get(i);
            final MessageListItem item = new MessageListItem(c.getComment(), c.getTime(), c.getUser().getUsername());
            messages.add(item);
            item.setUserColorIndex(i);
        }
        list.setItems(messages);
    }
}

package models.repositories.implementations.inmemory;

import models.entities.Comment;
import models.repositories.interfaces.ICommentRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CommentRepositoryInMemory implements ICommentRepository {
    private List<Comment> comments = new LinkedList<>();


    @Override
    public void add(Comment comment) {
        int maxIndex = this.comments.stream().max(Comparator.comparing(c -> c.getCommentId())).get().getCommentId();
        comment.setRequestId(maxIndex + 1);
        comments.add(comment);
    }

    @Override
    public Comment getCommentById(int commentId) {
        return comments.stream().filter(c -> c.getCommentId() == commentId).findFirst().get();
    }

    @Override
    public void updateComment(Comment comment) {
        Comment temp = getCommentById(comment.getCommentId());
        temp.updateFromObject(comment);
    }

    @Override
    public void deleteComment(int commentId) {
        comments.removeIf(c -> c.getCommentId() == commentId);
    }
}

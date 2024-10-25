package models.repositories.implementations.inmemory;

import models.entities.Comment;
import models.repositories.interfaces.ICommentRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentRepositoryInMemory implements ICommentRepository {
    private List<Comment> comments = new LinkedList<>();


    @Override
    public void add(Comment comment) {
        int maxIndex = comments.size() == 0 ? 0
                : comments.stream().max(Comparator.comparing(r -> r.getCommentId())).get().getCommentId();

        comment.setCommentId(maxIndex + 1);
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

    @Override
    public List<Comment> getCommentToRequest(int requestId) {
        return comments.stream().filter(c -> c.getRequestId() == requestId).collect(Collectors.toList());
    }
}

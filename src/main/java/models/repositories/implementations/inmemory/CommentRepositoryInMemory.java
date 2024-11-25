package models.repositories.implementations.inmemory;

import models.entities.Comment;
import models.repositories.interfaces.ICommentRepository;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentRepositoryInMemory implements ICommentRepository {
    private List<Comment> comments = new LinkedList<>();


    @Override
    public int add(Comment comment) {
        int maxIndex = comments.size() == 0 ? 0
                : comments.stream().max(Comparator.comparing(r -> r.getCommentId())).get().getCommentId();
        comment.setTime(Date.from(ZonedDateTime.now().toInstant()));
        comment.setCommentId(maxIndex + 1);
        comments.add(comment);
        return maxIndex + 1;
    }

    @Override
    public Comment getCommentById(int commentId) {
        return comments.stream().filter(c -> c.getCommentId() == commentId).findFirst().get();
    }

    @Override
    public int updateComment(Comment comment) {
        if (comment.getCommentId() == -1)
        {
            add(comment);
        } else {
            Comment temp = getCommentById(comment.getCommentId());
            temp.updateFromObject(comment);
        }
        return comment.getCommentId();
    }

    @Override
    public void deleteComment(int commentId) {
        Comment comment = comments.stream().filter(c -> c.getCommentId() == commentId).findFirst().get();
//        comment.setDeleted(true);
    }

    @Override
    public List<Comment> getCommentToRequest(int requestId) {
        return comments.stream().filter(c -> c.getRequestId() == requestId).collect(Collectors.toList());
    }
}

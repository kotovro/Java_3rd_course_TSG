package models.repositories.interfaces;

import models.entities.Comment;

import java.util.List;

public interface ICommentRepository {
    void add(Comment comment);
    Comment getCommentById(int commentId);
    void updateComment(Comment comment);
    void deleteComment(int commentId);

    List<Comment> getCommentToRequest(int requestId);
}

package models.repositories.interfaces;

import models.entities.Comment;

import java.util.List;

public interface ICommentRepository {
    int add(Comment comment);
    Comment getCommentById(int commentId);
    int updateComment(Comment comment);
    void deleteComment(int commentId);

    List<Comment> getCommentToRequest(int requestId);
}

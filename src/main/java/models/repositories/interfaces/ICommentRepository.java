package models.repositories.interfaces;

import models.entities.Comment;

public interface ICommentRepository {
    void add(Comment comment);
    Comment getCommentById(int commentId);
    void updateComment(Comment comment);
    void deleteComment(int commentId);
}

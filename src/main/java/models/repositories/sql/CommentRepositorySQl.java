package models.repositories.sql;

import models.entities.Comment;
import models.entities.RequestState;
import models.repositories.interfaces.ICommentRepository;

import java.io.StringReader;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CommentRepositorySQl extends PostgreDBRepository implements ICommentRepository {


    public CommentRepositorySQl(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public int add(Comment comment) {
        int commentId = -1;
        Connection connection = getConnection(this.url, this.username, this.password);
        if (connection == null) {
            return -1;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into \"comment\" (date, text, state, author_id, request_id) values (?, ?, ?, ?, ?) returning comment_id");
            Date date = Date.from(Date.from(ZonedDateTime.now().toInstant()).toInstant());
            statement.setDate(1, new java.sql.Date(date.getTime()));
            StringReader stringReader = new StringReader(comment.getBody());
            statement.setCharacterStream(2, stringReader);
            PreparedStatement statementForState = connection.prepareStatement("select state from \"request\" where request_id = ?");
            statementForState.setInt(1, comment.getRequestId());
            ResultSet resultSetForState = statementForState.executeQuery();
            statement.setInt(3, resultSetForState.next() ? resultSetForState.getInt(1) : 0);
            statement.setInt(4, comment.getAuthorId());
            statement.setInt(5, comment.getRequestId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                commentId = resultSet.getInt("comment_id");
            }
//            statement = connection.prepareStatement("select max(comment_id) from comment");
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                commentId = resultSet.getInt("comment_id") - 1;
//            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return commentId;
    }

    @Override
    public Comment getCommentById(int commentId) {
        Connection connection = getConnection(this.url, this.username, this.password);
        PreparedStatement statement = null;
        Comment comment = null;
        try {
            statement = connection.prepareStatement("select * from \"comment\" where comment_id = ?");
            statement.setInt(1, commentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                commentId = resultSet.getInt("comment_id");
                Date date = resultSet.getDate("date");
                int authorId = resultSet.getInt("author_id");
                String text = resultSet.getString("text");
                int stateId = resultSet.getInt("state");
                int requestId = resultSet.getInt("request_id");
                RequestState state = getRequestState(stateId);
                comment = new Comment(commentId, authorId, requestId, state, date, text);
            }
        } catch (Exception e) {
        }
        finally {
            closeConnection(connection, statement);
        }
        return comment;
    }

    private RequestState getRequestState(int stateId) {
        List<RequestState> requestStates = new LinkedList<>();
        requestStates.add(RequestState.STARTED);
        requestStates.add(RequestState.STOPPED);
        requestStates.add(RequestState.FINISHED);
        return requestStates.stream().filter(s -> s.getStateId() == stateId).findFirst().get();
    }

    @Override
    public int updateComment(Comment comment) {
        Connection connection = getConnection(this.url, this.username, this.password);
        PreparedStatement statement = null;
        if (comment.getCommentId() == -1)
        {
            return add(comment);
        } else {
            try {
                statement = connection.prepareStatement("update \"comment\" set date = ?, text = ?, state = ?, author_id = ? where comment_id = ? returning comment_id");
                java.sql.Date dt  = new java.sql.Date(comment.getTime().getTime());
                statement.setDate(1, dt);
                statement.setString(2, comment.getBody());
                statement.setInt(3, comment.getState().getStateId());
                statement.setInt(4, comment.getAuthorId());
                statement.setInt(5, comment.getCommentId());
                ResultSet resSet = statement.executeQuery();
                comment.setCommentId(resSet.getInt("comment_id"));
            } catch (Exception e) {

            }
            finally {
                closeConnection(connection, statement);
            }
        }
        return comment.getCommentId();
    }

    @Override
    public void deleteComment(int commentId) {
        Connection connection = getConnection(this.url, this.username, this.password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("delete from \"comment\" where comment_id = ?");
            statement.setInt(1, commentId);
            statement.executeUpdate();
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
    }

    @Override
    public List<Comment> getCommentToRequest(int requestId) {
        Connection connection = getConnection(this.url, this.username, this.password);
        List<Comment> comments = new ArrayList<>();
        if (connection == null) {
            return comments;
        }
        PreparedStatement statement = null;
        try
        {
            statement = connection.prepareStatement("select * from \"comment\" where request_id = ?");
            statement.setInt(1, requestId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int commentId = resultSet.getInt("comment_id");
                int stateId = resultSet.getInt("state");
                int authorId = resultSet.getInt("author_id");
                Date date = resultSet.getDate("date");
                String text = resultSet.getString("text");
                RequestState state = getRequestState(stateId);
                Comment comment = new Comment(commentId, authorId, requestId, state, date, text);
                comments.add(comment);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeConnection(connection, statement);
        }
        return comments;
    }

}

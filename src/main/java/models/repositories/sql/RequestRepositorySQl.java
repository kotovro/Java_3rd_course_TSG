package models.repositories.sql;

import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.repositories.interfaces.IRequestRepository;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class RequestRepositorySQl extends PostgreDBRepository implements IRequestRepository {


    public RequestRepositorySQl(String url, String username, String password)
    {
        super(url, username, password);
    }
    @Override
    public int add(Request request) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return -1;
        }
        PreparedStatement statement = null;
        int addedRequestId = -1;
        try {
            request.getAuthorId();
            request.getTime();
            statement = connection.prepareStatement("insert into \"request\" (date, state, author_id, description, type, resident_id) values (?, ?, ?, ?, ?, ?) returning request_id" );
            request.setTime(new java.util.Date(ZonedDateTime.now().toInstant().toEpochMilli()));
            statement.setDate(1, new java.sql.Date(ZonedDateTime.now().toInstant().toEpochMilli()));
            statement.setInt(2, request.getState().getState());
            statement.setInt(3, request.getAuthorId());
            statement.setString(4, request.getDescription());
            statement.setInt(5, request.getType().getRequestTypeId());
            statement.setInt(6, request.getResidentId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                addedRequestId = resultSet.getInt("request_id");
                request.setRequestId(addedRequestId);
            }
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
        return addedRequestId;
    }

    @Override
    public Request getRequestById(int requestId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        Request request = null;
        PreparedStatement preparedStatement = null;
        try {
            String query = "select * from \"request\" where request_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, requestId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date date = new Date(resultSet.getTimestamp("date").getTime());
                String descr = resultSet.getString("description");
                int status = resultSet.getInt("state");
                int typeId = resultSet.getInt("type");
                int residentId = resultSet.getInt("resident_id");
                int authorId = resultSet.getInt("author_id");
                request = new Request();
                request.setAuthorId(authorId);
                request.setDescription(descr);
                request.setResidentId(residentId);
                request.setState(RequestState.name(status));
                request.setTime(date);
                request.setRequestId(requestId);
                request.setType(RequestType.name(typeId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeConnection(connection, preparedStatement);
        }
        return request;
    }

    @Override
    public int updateRequest(Request request) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return -1;
        }
        PreparedStatement preparedStatement = null;
        if (request.getRequestId() == -1)
        {
            request.setRequestId(add(request));
        } else {
            try {
                String query = "update \"request\" set date = ?, description = ?, " +
                        "type = ?, state = ?, resident_id = ?, author_id = ? where request_id = ? returning request_id";
                preparedStatement = connection.prepareStatement(query);
                java.sql.Date date = new java.sql.Date(request.getTime().getTime());
                preparedStatement.setDate(1, date);
                preparedStatement.setString(2, request.getDescription());
                preparedStatement.setInt(3, request.getType().getRequestTypeId());
                preparedStatement.setInt(4, request.getState().getState());
                preparedStatement.setInt(5, request.getResidentId());
                preparedStatement.setInt(6, request.getAuthorId());
                preparedStatement.setInt(7, request.getRequestId());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    request.setRequestId(resultSet.getInt("request_id"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                closeConnection(connection, preparedStatement);
            }
        }
        return request.getRequestId();
    }

    @Override
    public void deleteRequest(int requestId) {

    }

    @Override
    public List<Request> getRequestList() {
        List<Request> requestList = new LinkedList<>();
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return new LinkedList<>();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select * from \"request\"");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Request r = new Request();
                r.setRequestId(resultSet.getInt("request_id"));
                r.setAuthorId(resultSet.getInt("author_id"));
                r.setTime(new Date(resultSet.getTimestamp("date").getTime()));
                r.setDescription(resultSet.getString("description"));
                int stateId = resultSet.getInt("state");
                RequestState state = RequestState.name(stateId);
                r.setState(state);
                int requestType = resultSet.getInt("type");
                r.setType(RequestType.name(requestType));
                requestList.add(r);
            }
        } catch (Exception e) {

        }
        finally {
            closeConnection(connection, preparedStatement);
        }

        return requestList;
    }
}

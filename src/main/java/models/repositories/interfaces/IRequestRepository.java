package models.repositories.interfaces;

import models.entities.Request;

import java.util.List;

public interface IRequestRepository {
    int add(Request request);
    Request getRequestById(int RequestId);
    int updateRequest(Request request);
    void deleteRequest(int requestId);
    List<Request> getRequestList();
    int getRequestByUser(int userId);
    default List<Request> getRequestList(int pageNumber, int pageSize) {
        return getRequestList();
    }

    int getRequestCount();
}

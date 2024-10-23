package models.repositories.interfaces;

import models.entities.Request;

import java.util.List;

public interface IRequestRepository {
    void add(Request request);
    Request getRequestById(int RequestId);
    void updateRequest(Request request);
    void deleteRequest(int requestId);
    List<Request> getRequestList();
}

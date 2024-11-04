package models.repositories.implementations.inmemory;


import lombok.Setter;
import models.entities.Comment;
import models.entities.Request;
import models.repositories.interfaces.IRequestRepository;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Setter
public class RequestRepositoryInMemory implements IRequestRepository {

    private List<Request> requests = new LinkedList<>();


    @Override
    public int add(Request request) {
        int maxIndex = requests.size() == 0 ? 0
                : requests.stream().max(Comparator.comparing(r -> r.getRequestId())).get().getRequestId();
        request.setTime(ZonedDateTime.now());
        request.setRequestId(maxIndex + 1);
        requests.add(request);
        return maxIndex + 1;
    }

    @Override
    public Request getRequestById(int requestId) {
        return requests.stream().filter(r -> r.getRequestId() == requestId)
                .findFirst().get();
    }

    @Override
    public int updateRequest(Request request) {
        if (request.getRequestId() == -1)
        {
            add(request);
        } else {
            Request temp = getRequestById(request.getRequestId());
            temp.updateFromObject(request);
        }
        return request.getRequestId();
    }

    @Override
    public void deleteRequest(int requestId) {
        requests.removeIf(r -> r.getRequestId() == requestId);
    }

    @Override
    public List<Request> getRequestList() {
        return requests;
    }
}

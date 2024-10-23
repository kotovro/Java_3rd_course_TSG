package models.repositories.implementations.inmemory;


import lombok.Setter;
import models.entities.Request;
import models.repositories.interfaces.IRequestRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Setter
public class RequestRepositoryInMemory implements IRequestRepository {

    private List<Request> requests = new LinkedList<>();


    @Override
    public void add(Request request) {

        int maxIndex = requests.size() == 0 ? 0
                : requests.stream().max(Comparator.comparing(r -> r.getRequestId())).get().getRequestId();

        request.setRequestId(maxIndex + 1);
        requests.add(request);
    }

    @Override
    public Request getRequestById(int requestId) {
        return requests.stream().filter(r -> r.getRequestId() == requestId)
                .findFirst().get();
    }

    @Override
    public void updateRequest(Request request) {
        Request temp = getRequestById(request.getRequestId());
        temp.updateFromObject(request);
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

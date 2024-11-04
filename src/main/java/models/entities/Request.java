package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Request {
    private int requestId = -1;
    private RequestType type = RequestType.SINGLE_PERSON;
    private RequestState state = RequestState.STARTED;
    private String description;
    private int authorId = -1;
    private int residentId = -1;
    private ZonedDateTime time = ZonedDateTime.now();
    private boolean isDeleted = false;


    public Request()
    {};

    public void updateFromObject(Request request) {
        state = request.getState();
        type = request.getType();
        description = request.getDescription();
        authorId = request.getAuthorId();
        residentId = request.getResidentId();
        time = request.getTime();

    }
}

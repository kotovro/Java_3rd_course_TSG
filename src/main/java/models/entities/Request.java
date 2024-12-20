package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Request {
    private int requestId = -1;
    private RequestType type = RequestType.SINGLE_PERSON;
    private RequestState state = RequestState.STARTED;
    private String description = "";
    private int authorId = -1;
    private int residentId = -1;
    private Date time = Date.from(ZonedDateTime.now().toInstant());
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

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
    private int requestId;
    private RequestType type;

    private RequestState state;
    private String description;
    private int authorId;
    private int residentId;
    private ZonedDateTime time;


    public void updateFromObject(Request request) {
        state = request.getState();
        type = request.getType();
        description = request.getDescription();
        authorId = request.getAuthorId();
        residentId = request.getResidentId();
        time = request.getTime();

    }
}

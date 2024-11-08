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
public class Comment {
    private int commentId = -1;
    private int authorId;
    private int requestId;
    private String state;
    private ZonedDateTime time;
    private String body;
//    private boolean ;

    public void updateFromObject(Comment comment) {
        this.setState(comment.getState());
        this.setTime(comment.getTime());
        this.setBody(comment.getBody());
    }

    public Comment(int requestId)
    {
        this.requestId = requestId;
    }
}

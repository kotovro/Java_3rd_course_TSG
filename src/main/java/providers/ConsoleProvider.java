package providers;

import models.entities.Request;
import models.entities.StaffMember;
import models.repositories.implementations.inmemory.CommentRepositoryInMemory;
import models.repositories.implementations.inmemory.RequestRepositoryInMemory;

import java.util.LinkedList;

public class ConsoleProvider {

    private RequestRepositoryInMemory requestRep;
    private CommentRepositoryInMemory commentRep;
    private StaffMember staffMemberRep;
    private boolean isStaffMemberRepExists = false;
    private boolean isUserRepExists = false;
    private boolean isRepExists = false;

    public ConsoleProvider()
    {
        LinkedList<Request> requests = new LinkedList<>();
//        requests.add(new Request());
//        requests.add(new Request());
//        requests.add(new Request());
//        LinkedList<Comment> comments = new LinkedList<>();
//        comments.add();
//        comments.add();
//        comments.add();
    }

//    public RequestRepositoryInMemory getRequestRep() {
//        return requestRep;
//    }
//
//    public CommentRepositoryInMemory getCommentRep() {
//        return requestRep;
//    }
//    public RequestRepositoryInMemory getRequestRep() {
//        return requestRep;
//    }
}

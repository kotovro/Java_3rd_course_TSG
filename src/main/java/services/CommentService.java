package services;

import lombok.Setter;
import models.entities.Comment;
import models.entities.Request;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import models.repositories.RepositoryProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

public class CommentService {

    @Setter
    RepositoryProvider repositoryProvider;


    public ViewModel fillEmptyView(String requestId) {
        ViewModel commentMdlView = new ViewModel();

        Comment comment = new Comment(Integer.parseInt(requestId));

        List<ViewField> parameters = commentMdlView.getParameters();
        parameters.add(new ViewField("Comment Id", "-1", false, false));
        parameters.add(new ViewField("Request Id", requestId, false, false));
        parameters.add(new ViewField("Description", "", true, true));
        parameters.add(new ViewField("Author Id", "", true, false));
        parameters.add(new ViewField("Author", "", false, true));
        parameters.add(new ViewField("Date", "", false, true));

        return commentMdlView;
    }

    public ViewModel fillView(String commentIdStr) {
        return fillView(Integer.parseInt(commentIdStr));
    }

    public ViewModel fillView(int commentId) {
        ViewModel commentMdlView = new ViewModel();

        ICommentRepository commentRepository = repositoryProvider.getCommentRepository();
        Comment comment = commentRepository.getCommentById(commentId);

        IResidentRepository residentRep = repositoryProvider.getResidentRepository();
        IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
        String authorName = staffRep.getNameByUserId(comment.getAuthorId()) == null
                ? staffRep.getNameByUserId(comment.getAuthorId())
                : residentRep.getNameByUserId(comment.getAuthorId());
        int requestId = comment.getRequestId();

        commentMdlView.setTitle("Comment to request " + requestId);
        String commentIdStr = Integer.toString(commentId);
        List<ViewField> parameters = commentMdlView.getParameters();
        parameters.add(new ViewField("Comment Id", commentIdStr, false, false));
        parameters.add(new ViewField("Request Id", Integer.toString(comment.getRequestId()), false, false));
        parameters.add(new ViewField("Description", comment.getBody(), true, true));
        parameters.add(new ViewField("Author Id", Integer.toString(comment.getAuthorId()), true, false));
        parameters.add(new ViewField("Author", authorName, false, true));
        parameters.add(new ViewField("Date", comment.getTime().toString(), false, true));


        Action show = new Action(Action.ActionType.SHOW, "Comment/show", commentIdStr, "", null, null, false);

        Action update = new Action(Action.ActionType.UPDATE, "Comment/update", commentIdStr,
                "Edit comment", show, show, true);
        commentMdlView.addCommand(update);

        Action add = new Action(Action.ActionType.ADD, "Comment/add", Integer.toString(requestId),
                    "Add new comment", update, update, true);
        commentMdlView.addCommand(add);

        Action delete = new Action();
        delete.setActionType(Action.ActionType.DELETE);
        commentMdlView.addCommand(delete);

        Action back = new Action(Action.ActionType.SHOW, "Request/show", Integer.toString(requestId), "Back to request");
        commentMdlView.addCommand(back);

        Action showComments = new Action(Action.ActionType.SHOW, "Comment/getList", Integer.toString(requestId), "Show comments");
        commentMdlView.addCommand(showComments);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        commentMdlView.addCommand(exit);


        return commentMdlView;
    }

    public ViewModel getList(String requestId) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Comments list");
        ICommentRepository rep = repositoryProvider.getCommentRepository();
        List<Comment> commentList = rep.getCommentToRequest(Integer.parseInt(requestId));

        IResidentRepository residentRep = repositoryProvider.getResidentRepository();
        IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
        for (Comment comment : commentList)
        {
            String authorName = staffRep.getNameByUserId(comment.getAuthorId()) == null
                    ? staffRep.getNameByUserId(comment.getAuthorId())
                    : residentRep.getNameByUserId(comment.getAuthorId());
            String commentMetaData = comment.getTime().toString() + " " + authorName;
            Action action = new Action(Action.ActionType.SHOW, "Comment/show", Integer.toString(comment.getCommentId()),
                    commentMetaData + "\n");
            viewModel.addCommand(action);
        }
        Action update = new Action(Action.ActionType.UPDATE, "Comment/update", "",
                "");
        Action add = new Action(Action.ActionType.ADD, "Comment/add", requestId,
                "Add new comment", update, update, true);
        viewModel.addCommand(add);
        Action back = new Action(Action.ActionType.SHOW, "Request/show", requestId,
                    "Back to request");
        viewModel.addCommand(back);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.addCommand(exit);
        return viewModel;
    }


    public ViewModel update(ViewModel viewModel)
    {
        int commentId = Integer.parseInt(viewModel.getFieldValueByAttributeName("Comment Id"));
        int requestId = Integer.parseInt(viewModel.getFieldValueByAttributeName("Request Id"));

        ICommentRepository rep = repositoryProvider.getCommentRepository();
        Comment comment = commentId < 0 ? new Comment(requestId) : rep.getCommentById(commentId);
        comment.setBody(viewModel.getFieldValueByAttributeName("Description"));
        comment.setAuthorId(Integer.parseInt(viewModel.getFieldValueByAttributeName("Author Id")));

        commentId = rep.updateComment(comment);

        return fillView(commentId);
    }

}

package services;

import lombok.Setter;
import models.entities.Comment;
import models.entities.Request;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import controller.RepositoryProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

@Controller(name = "comment")
public class CommentService implements IService {

    @Setter
    RepositoryProvider repositoryProvider;

    @services.Action(name = "show")
    public ViewModel fillView(String commentIdStr) {
        int requestId = Integer.parseInt(commentIdStr);
        ViewModel commentMdlView = new ViewModel();

        if (requestId > 0) {
            commentMdlView.setTitle("Comment" + Integer.toString(requestId));
        }
        else {
            commentMdlView.setTitle("New comment");
        }

        ICommentRepository commentRepository = repositoryProvider.getCommentRepository();
        Comment comment = commentRepository.getCommentById(Integer.parseInt(commentIdStr));

        IRequestRepository requestRep = repositoryProvider.getRequestRepository();
        Request request = requestRep.getRequestById(comment.getRequestId());
        if (request == null) {
            return commentMdlView;
        }
        IResidentRepository residentRep = repositoryProvider.getResidentRepository();
        IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
        String authorName = staffRep.getNameByUserId(comment.getAuthorId()) == null
                ? staffRep.getNameByUserId(comment.getAuthorId())
                : residentRep.getNameByUserId(comment.getAuthorId());



        List<ViewField> parameters = commentMdlView.getParameters();
        parameters.add(new ViewField("Comment Id", commentIdStr, false, false));
        parameters.add(new ViewField("Request Id", Integer.toString(request.getRequestId()), false, false));
        parameters.add(new ViewField("Description", comment.getBody(), true, true));
        parameters.add(new ViewField("Author Id", Integer.toString(comment.getAuthorId()), true, false));
        parameters.add(new ViewField("Author", authorName, false, true));
        parameters.add(new ViewField("Date", comment.getTime().toString(), false, true));

        List<Action> commands = commentMdlView.getActionsList();
        Action update = new Action();
        update.setActionName("Edit comment");
        update.setActionType(Action.ActionType.UPDATE);
        update.setRoute("services.CommentService/update");
        update.setParameter(Integer.toString(requestId));
        commands.add(update);

        Action add = new Action();
        add.setActionType(Action.ActionType.ADD);
        add.setRoute("services.CommentService/update");
        add.setParameter("-1");
        commands.add(add);


        Action delete = new Action();
        delete.setActionType(Action.ActionType.DELETE);
        commands.add(delete);

        Action back = new Action();
        back.setActionName("Back to request");
        back.setActionType(Action.ActionType.SHOW);
        back.setRoute("services.RequestService/fillView");
        back.setParameter(Integer.toString(comment.getRequestId()));
        back.setInteractive(true);
        commands.add(back);

        Action showComments = new Action();
        showComments.setActionName("Show other comments");
        showComments.setActionType(Action.ActionType.SHOW);
        showComments.setRoute("services.CommentService/getList");

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        commands.add(exit);

        Action show = new Action();
        show.setActionType(Action.ActionType.SHOW);
        show.setRoute("services.CommentService/fillView");
        show.setParameter(Integer.toString(requestId));
        show.setInteractive(false);
        commands.add(show);

        return commentMdlView;
    }

    @services.Action(name = "showAll")
    public ViewModel getList(String requestId) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Comments list");
        ICommentRepository rep = repositoryProvider.getCommentRepository();
        List<Comment> reqList = rep.getCommentToRequest(Integer.parseInt(requestId));

        IResidentRepository residentRep = repositoryProvider.getResidentRepository();
        IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
        for (Comment comment : reqList)
        {
            String authorName = staffRep.getNameByUserId(comment.getAuthorId()) == null
                    ? staffRep.getNameByUserId(comment.getAuthorId())
                    : residentRep.getNameByUserId(comment.getAuthorId());
            String commentMetaData = authorName + " " + comment.getTime().toString();
            ViewField field = new ViewField(commentMetaData, comment.getBody(), false, true);
            viewModel.getParameters().add(field);
        }

        Action back = new Action();
        back.setActionName("Back to request");
        back.setActionType(Action.ActionType.SHOW);
        back.setRoute("services.RequestService/fillView");
        back.setParameter(requestId);
        viewModel.addCommand(back);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.addCommand(exit);
        return viewModel;
    }

    @services.Action(name = "update")
    public ViewModel update(ViewModel viewModel)
    {
        int id = Integer.parseInt(
                viewModel.getParameters()
                        .stream()
                        .filter(p -> p.getAttributeName().equals("Comment Id"))
                        .findFirst()
                        .get()
                        .getAttributeValue());

        ICommentRepository rep = repositoryProvider.getCommentRepository();
        Comment comment = rep.getCommentById(id);
        comment.setBody(viewModel.getParameters()
                .stream()
                .filter(f -> f.getAttributeName().equals("Description"))
                .findFirst()
                .get()
                .getAttributeValue());
        comment.setAuthorId(Integer.parseInt(
                viewModel.getParameters()
                        .stream()
                        .filter(f -> f.getAttributeName().equals("Author Id"))
                        .findFirst()
                        .get()
                        .getAttributeValue()));
        rep.updateComment(comment);
        return viewModel;
    }
}

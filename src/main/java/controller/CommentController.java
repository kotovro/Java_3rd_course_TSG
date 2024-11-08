package controller;

import models.repositories.RepositoryProvider;
import services.CommentActionService;
import services.CommentService;
import services.IActionProvider;
import services.RequestActionService;
import view.ViewModel;

public class CommentController {
    RepositoryProvider rep = RepositoryProvider.getInstance();
    IActionProvider commentActionService = new CommentActionService();
    IActionProvider requestActionService = new RequestActionService();
    CommentService service = new CommentService();

    public CommentController() {
        service.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return service.fillView(id, commentActionService, requestActionService);
    }

    public ViewModel update(ViewModel viewModel) {
        return service.update(viewModel, commentActionService, requestActionService);

    }
    public ViewModel add(String requestId) {
        return service.fillEmptyView(requestId);
    }
    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String requestId) {
        return service.getList(requestId, commentActionService, requestActionService);
    }
}

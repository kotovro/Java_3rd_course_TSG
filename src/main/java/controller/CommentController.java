package controller;

import models.repositories.RepositoryProvider;
import services.CommentService;
import view.ViewModel;

public class CommentController {
    RepositoryProvider rep = RepositoryProvider.getInstance();
    CommentService service = new CommentService();

    public CommentController() {
        service.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return service.fillView(id);
    }

    public ViewModel update(ViewModel viewModel) {
        return service.update(viewModel);

    }
    public ViewModel add(String requestId) {
        return service.fillEmptyView(requestId);
    }
    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String requestId) {
        return service.getList(requestId);
    }
}

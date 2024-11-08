package controller;

import models.repositories.RepositoryProvider;
import services.CommentActionService;
import services.IActionProvider;
import services.RequestActionService;
import services.RequestService;
import view.ViewModel;

public class RequestController {

    RepositoryProvider rep = RepositoryProvider.getInstance();
    RequestService requestService = new RequestService();
    IActionProvider requestActionProvider = new RequestActionService();
    IActionProvider commentActionProvider = new CommentActionService();


    public RequestController() {
        requestService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return requestService.fillView(id, requestActionProvider, commentActionProvider);
    }

    public ViewModel update(ViewModel viewModel) {
        return requestService.update(viewModel, requestActionProvider, commentActionProvider);

    }
    public ViewModel add(String str) {
        return requestService.fillView("-1", requestActionProvider, commentActionProvider);
    }
        public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        return requestService.getList(requestActionProvider);
    }

}

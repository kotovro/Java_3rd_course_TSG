package controller;

import models.repositories.RepositoryProvider;
import services.RequestService;
import view.ViewModel;

public class RequestController {

    RepositoryProvider rep = RepositoryProvider.getInstance();
    RequestService requestService = new RequestService();


    public RequestController() {
        requestService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return requestService.fillView(id);
    }

    public ViewModel update(ViewModel viewModel) {
        return requestService.update(viewModel);

    }
    public ViewModel add(String str) {
        return requestService.fillView("-1");
    }
        public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        return requestService.getList();
    }

}

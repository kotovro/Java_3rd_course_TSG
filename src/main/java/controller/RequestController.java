package controller;

import models.repositories.RepositoryProvider;
import services.RequestService;
import view.ViewModel;

public class RequestController {

    RepositoryProvider rep = RepositoryProvider.getInstance();
    RequestService service = new RequestService();


    public RequestController() {
        service.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return service.fillView(id);
    }

    public ViewModel update(ViewModel viewModel) {
        return service.update(viewModel);

    }
    public ViewModel add(String str) {
        return service.fillView("-1");
    }
        public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        return service.getList();
    }

}

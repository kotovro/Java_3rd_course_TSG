    package web;

    import controller.ControllerService;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import services.actionProviders.ActionProviderContainer;
    import services.actionProviders.IActionProvider;
    import view.Action;
    import view.ViewField;
    import view.ViewModel;

    import java.io.IOException;

    import static view.Action.ActionType.UPDATE;

    @WebServlet(urlPatterns = {"/login", "/index.html"})
    public class StartPageServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String token = request.getParameter("token");
            if (token != null) {
                ControllerService controllerService = new ControllerService();
                Action getMenu = ActionProviderContainer.getLoginActionProvider().getActionList("", "", null, null);
                ViewModel viewModel = controllerService.doAction(getMenu, null, token);
                StringBuilder content = new StringBuilder();
                for (Action action: viewModel.getActionsList())
                {
                    if (action.isInteractive()) {
                        content.append("<li class='nav-item'>")
                                    .append("<a class='nav-link' href='#' onclick='getContent(\"")
                                        .append(action.getRoute()).append("?token=").append(token)
                                        .append("&param=").append(action.getParameter())
                                        .append("\");'>")
                                        .append(action.getActionName())
                                    .append("</a>")
                                .append("</li>");
                    }
                }
                request.setAttribute("menuContent", content.toString());
                request.getRequestDispatcher("/WEB-INF/pages/generic_page.jsp").forward(request, response);
            }
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            IActionProvider loginActionProvider = ActionProviderContainer.getLoginActionProvider();
            Action getLoginForm = loginActionProvider.getActionShow("", "", null, null, false);
            ControllerService controllerService = new ControllerService();
            ViewModel viewModel = controllerService.doAction(getLoginForm, null, null);
            if (viewModel != null) {
                Action auth = viewModel.getActionsList().stream().filter(a -> a.getActionType().equals(UPDATE)).findFirst().get();
                for (ViewField vf: viewModel.getParameters()) {
                    vf.setAttributeValue(req.getParameter(vf.getAttributeName()));
                }
                viewModel = controllerService.doAction(auth, viewModel, viewModel.getUserToken());
                if (viewModel.getUserToken() != null && viewModel.getErrorMessage() == null) {
                    resp.sendRedirect("./?token=" + viewModel.getUserToken());
                } else {
                    // Failed login
                    req.setAttribute("errorMessage", viewModel.getErrorMessage());
                    req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
                }
            }

        }
    }

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Maintenance Company - Login</title>
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Maintenance Company</a>
        </div>
    </nav>
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4 mt-5">
            <h1 class="text-center mb-4">Login</h1>

            <!-- Display Error Message if Present -->
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger text-center">${errorMessage}</div>
            <% } %>

            <!-- Login Form -->
            <form id="loginForm" method="post" action="./login" class="border p-4 rounded bg-light shadow">
                <div class="mb-3">
                    <label for="username" class="form-label">Username:</label>
                    <input type="text" class="form-control" id="username" name="Login" placeholder="Enter your username" required>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="Password" placeholder="Enter your password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block w-100">Login</button>
            </form>
        </div>
    </div>
    <script type="text/javascript"></script>
</div>

<!-- Include Bootstrap JS and Dependencies -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
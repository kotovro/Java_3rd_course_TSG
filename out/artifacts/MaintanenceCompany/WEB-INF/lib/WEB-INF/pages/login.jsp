<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="/static/css/styles.css"> <!-- Adjust the path to your CSS -->
</head>
<body>
<div class="content">
    <div class="container col-md-6 col-lg-4 mt-md-5">
        <h1 class="text-center mb-4">Login</h1>

        <!-- Display Error Message if Present -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <!-- Login Form -->
        <form id="loginForm" method="post" action="./hello">
            <div class="form-group mb-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" class="form-control" id="username" name="Login" placeholder="Enter your username" required>
            </div>

            <div class="form-group mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" name="Password" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary btn-block w-100">Login</button>
        </form>

        <!-- Register Link -->
        <div class="text-center mt-3">
            <p>Don't have an account? <a href="/signup" class="text-decoration-none">Sign Up</a></p>
        </div>
    </div>
</div>
</body>
</html>
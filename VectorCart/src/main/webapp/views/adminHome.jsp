<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="/static/images/logo.png" height="40" alt="Logo" />
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="/admin/">Home</a></li>
                    <li class="nav-item"><a class="nav-link" href="/admin/logout">Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container text-center my-5">
        <h1 class="display-4">Welcome Back, Admin</h1>
        <p class="lead">Manage your data from this Admin Panel</p>
    </div>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-4 mb-4">
                <div class="card text-center">
                    <div class="card-body">
                        <h4 class="card-title">Categories</h4>
                        <p class="card-text">Manage the categories section here.</p>
                        <a href="/admin/categories" class="btn btn-primary">Manage</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card text-center">
                    <div class="card-body">
                        <h4 class="card-title">Products</h4>
                        <p class="card-text">Manage all the products here.</p>
                        <a href="/admin/products" class="btn btn-primary">Manage</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card text-center">
                    <div class="card-body">
                        <h4 class="card-title">Users</h4>
                        <p class="card-text">Manage all the users here.</p>
                        <a href="/admin/users" class="btn btn-primary">Manage</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

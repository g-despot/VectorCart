<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" crossorigin="anonymous">
    <title>User Profile</title>
</head>
<body>

<!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="/static/images/logo.png" height="40" class="d-inline-block align-top" alt="Logo" />
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link active" href="Dashboard">Home Page</a></li>
                    <li class="nav-item"><a class="nav-link" href="logout">Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

<div class="container mt-4">
    <div class="col-sm-6 mx-auto">
        <h3>User Profile</h3>
        <form action="updateuser" method="post">
            <div class="form-group">
                <label for="firstName">User Name</label>
                <input type="hidden" name="userid" value="${userid}">
                <input type="text" name="username" id="firstName" value="${username}" placeholder="Your Username*" required class="form-control form-control-lg">
            </div>

            <div class="form-group">
                <label for="email">Email address</label>
                <input type="email" name="email" id="email" value="${email}" placeholder="Email*" required class="form-control form-control-lg" aria-describedby="emailHelp">
                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" name="password" id="password" value="${password}" placeholder="Password*" required class="form-control form-control-lg">
            </div>

            <div class="form-group">
                <label for="address">Address</label>
                <textarea name="address" id="address" class="form-control form-control-lg" rows="3" placeholder="Enter Your Address">${address}</textarea>
            </div>

            <input type="submit" value="Update Profile" class="btn btn-primary btn-block mt-3">
        </form>
    </div>
</div>

<!-- Bootstrap 5 JS (no jQuery or Popper.js needed anymore) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pzjw8f+ua7Kw1TIq0k6F4gZmf1aI1oPzOm1yRRS0mFVrLlGT4c1WDEd5l0Kcyfvs" crossorigin="anonymous"></script>

</body>
</html>
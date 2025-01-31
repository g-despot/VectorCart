<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Update Product</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="../static/images/logo.png" height="40" class="d-inline-block align-top" alt="Logo" />
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link active" href="/dashboard">Home Page</a></li>
                    <li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Update Product Form -->
    <div class="container mt-4">
        <div class="jumbotron border border-info p-4">
            <h3>Update Existing Product</h3>
            <form action="/admin/products/update/${product.id}" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-sm-5">
                        <div class="mb-3">
                            <label for="id" class="form-label">Id</label>
                            <input type="number" readonly class="form-control" name="id" value="${product.id}">
                        </div>
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input type="text" class="form-control" name="name" required value="${product.name}" placeholder="Enter name">
                        </div>
                        <div class="mb-3">
                            <label for="category" class="form-label">Select Category</label>
                            <select class="form-select" name="categoryid" required>
                                <option selected>Select a Category</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="price" class="form-label">Price</label>
                            <input type="number" class="form-control" name="price" required value="${product.price}" min="1" placeholder="Price">
                        </div>
                        <div class="mb-3">
                            <label for="weight" class="form-label">Weight in grams</label>
                            <input type="number" class="form-control" name="weight" required value="${product.weight}" min="1" placeholder="Weight">
                        </div>
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Available Quantity</label>
                            <input type="number" class="form-control" name="quantity" required value="${product.quantity}" min="1" placeholder="Quantity">
                        </div>
                    </div>

                    <div class="col-sm-5">
                        <div class="mb-3">
                            <label for="description" class="form-label">Product Description</label>
                            <textarea class="form-control" rows="4" name="description" placeholder="Product Details">${product.description}</textarea>
                        </div>
                        <div class="mb-3">
                            <label for="imageUrl" class="form-label">Product Image URL</label>
                            <input type="text" class="form-control" id="imageUrl" name="imageUrl" value="${product.image}" placeholder="Enter image URL" required>
                        </div>
                        <input type="hidden" name="imgName">
                        <button type="submit" class="btn btn-primary">Update Details</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap 5 JS and Popper -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>
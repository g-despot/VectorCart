<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Set Page Title --%>
<c:set var="pageTitle" value="Homepage" />

<jsp:include page="header.jsp"/>

<!-- Main Content -->
<div class="container mt-4">
    <h1>Welcome to VectorCart!</h1>
    <div class="row">
        <c:forEach var="product" items="${products}">
            <div class="col-md-3">
                <div class="card mb-4">
                    <img class="card-img-top" src="${product.image}" alt="Product">
                    <div class="card-body">
                        <h4 class="card-title">${product.name}</h4>
                        <h5 class="card-text">Category: ${product.category.name}</h5>
                        <h5 class="card-text">Price: ${product.price}</h5>
                        <p class="card-text">Description: ${product.description}</p>
                        <a href="#" class="btn btn-primary">Add to Cart</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="footer.jsp"/>

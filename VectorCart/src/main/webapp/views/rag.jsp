<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Set Page Title --%>
<c:set var="pageTitle" value="Search Products" />

<jsp:include page="includes/header.jsp"/>

<!-- Search Form -->
<div class="container mt-4">
    <form action="rag" method="get" class="d-flex">
        <input type="text" name="searchQuery" class="form-control me-2" placeholder="What are you looking for?" value="${param.searchQuery}" required>
        <input type="text" name="ragQuery" class="form-control me-2" placeholder="RAG query?" value="${param.ragQuery}" required>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>
</div>

<!-- Search Results -->
<div class="container mt-4">
    <h2>RAG Query Result</h2>
    <form>
        <label for="result">Query Response:</label><br>
        <textarea id="generativeGroupedResult" name="result" rows="4" cols="50" readonly>
            ${generativeGroupedResult}
        </textarea>
    </form>
    <h2>Search Results</h2>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">Serial No.</th>
                <th scope="col">Product Name</th>
                <th scope="col">Category</th>
                <th scope="col">Preview</th>
                <th scope="col">Quantity</th>
                <th scope="col">Price</th>
                <th scope="col">Description</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.category.name}</td>
                    <td><img src="${product.image}" height="100" width="100"></td>
                    <td>${product.quantity}</td>
                    <td>${product.price}</td>
                    <td>${product.description}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="includes/footer.jsp"/>

<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Set Page Title --%>
<c:set var="pageTitle" value="User profile" />

<jsp:include page="includes/header.jsp"/>

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

<jsp:include page="includes/footer.jsp"/>

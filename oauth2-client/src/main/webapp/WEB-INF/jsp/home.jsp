<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OAuth2 authroization authentication</title>

<body>

<form action="getUserInfo" method="post">
<p>
    This is OAuth 2.0 Client Web application, client the button below to retrieve user information from another web service that has OAuth 2.0 provider service implemented.
</p>
<input type="submit" value="continue"/>
</form>

</body>

</html>

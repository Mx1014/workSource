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

<form action="confirm" method="post">

mobile/email:<input type="text" name="identifier"/><br/><br/>
Password:<input type="password" name="password"/><br/><br/>
<input type="submit" value="login"/>
<input type="hidden" name="viewState" value="${viewState}"/>
</form>

</body>

</html>

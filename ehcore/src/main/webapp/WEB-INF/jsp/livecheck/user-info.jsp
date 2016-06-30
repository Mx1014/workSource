<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ page import="java.util.List" %>
<%@ page import="com.everhomes.user.UserLogin" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../static/css/global.css">
</head>
<body>

<script src="../static/jquery-2.1.3.min.js"></script>

<h1 class="tcenter" style="margin-top: 10px;">User Live Information Summary</h1>

<div id="top" class="dropshadow padding" style="margin: 20px 20px 20px 20px;">
<table>
<tr>
<td style="text-align: right" width="200px"><b>User ID</b></td><td>${userInfo.id}</td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Nick name</b></td><td>${userInfo.nickName}</td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Avatar</b></td><td><img src="${userInfo.avatarUrl}"></img></td>
</tr>

</table>
</div>

<div class="clearfix"></div>

<div id="left" style="float: left; width:240px; margin: 0px 20px 20px 20px;" class="dropshadow padding">
<ul>
    <c:forEach var="element" items="${logins}" varStatus="loop">
        <li onclick="loadLoginDetail(${element.userId}, ${element.loginId})">Login: ${element.loginId}</li>
    </c:forEach>
</ul>

</div>

<div id="right" style="margin: 20px 20px 20px 300px;" class="dropshadow padding">
<p>We don't have anything to show as no login is selected</p>
</div>

<script type="text/javascript">

<% 
UserLogin firstLogin = null; 
if( ((List)request.getAttribute("logins")).size() > 0) { 
    firstLogin = (UserLogin)((List)request.getAttribute("logins")).get(0);
}
%>

<c:choose>
<c:when test="${firstLogin != 'null'}">

$(document).ready(function() {
		loadLoginDetail(<%= firstLogin.getUserId() %>, <%= firstLogin.getLoginId() %>);
});

</c:when>
</c:choose>

function loadLoginDetail(uid, loginId) {
    $('#right').load("loginDetail?uid=" + uid + "&loginId=" + loginId);	
}

</script>

</body>
</html>

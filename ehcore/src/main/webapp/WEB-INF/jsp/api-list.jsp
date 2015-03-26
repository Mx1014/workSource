<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<title>Core Server Rest API list</title>
<body>

<c:forEach var="element" items="${restMethods}">

<table border="1" width="800" align="center">

<tr><td colspan="2">
<b>
<c:choose>
 <c:when test="${element.params.size() > 0}">
    <a href="/api${element.uri}">${element.uri}</a>
 </c:when>
 <c:otherwise>
    <a href="${element.uri}">${element.uri}</a>
 </c:otherwise>
</c:choose>
</b>
</td></tr>

<tr>
    <td width="100">Parameter</td>
    <td>
		<c:forEach var="p" items="${element.params}">
		<p>
		${p.description}
		</p>
		<p>
		<c:choose>
		  <c:when test="${p.required}">
            <b>${p.paramName}:${p.typeName}, required: ${p.required}</b>
		  </c:when>
		  <c:otherwise>
            ${p.paramName}:${p.typeName}, required: ${p.required}
		  </c:otherwise>
		</c:choose>
		</p>
		</c:forEach>
    </td>
</tr>
<tr>
    <td>Response</td><td>${element.returnTemplate}</td>
</tr>
</table>

<div style="height:30px"> </div>

</c:forEach>

</body>
</html>


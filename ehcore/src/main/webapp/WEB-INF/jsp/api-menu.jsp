<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
</head>
<body>

<table border="0" align="left top" style="width:100%; margin:auto;">
	<c:forEach var="element" items="${restMethods}" varStatus="loop">
	<tr>
		<td>
		    <a name="${element.uri}" href="/api?index=${loop.index}" target="_top">${element.uri}</a>
		</td>
	</tr>
	</c:forEach>
</table>

</body>
</html>


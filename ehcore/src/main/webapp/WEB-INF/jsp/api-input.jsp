<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<title>Core Server Rest API list</title>
<body>
<table border="1" width="800" align="center">
<tr><td>
<b>${restMethod.uri}</b>
</td></tr>
<tr><td>

<form method="POST" action="${restMethod.uri}" enctype="multipart/form-data">

<table border="1" width="100%">
<c:forEach var="p" items="${restMethod.params}">
    <tr>
        <td width="120">
        <c:choose>
            <c:when test="${p.required}">
                <b>${p.paramName}</b>
            </c:when>
            <c:otherwise>
                ${p.paramName}
            </c:otherwise>
        </c:choose>
        </td>
        <c:choose>
	        <c:when test="${fn:contains(p.paramName,'attachment')}">
	        	<td><input style="width:99%;" type="file" name="${p.paramName}" /></td>
	        </c:when>
	        <c:otherwise>
	        	<td><input style="width:99%;" name="${p.paramName}" /></td>
	        </c:otherwise>
        </c:choose>
    </tr>
</c:forEach>

		<tr>
		    <td>Response</td><td>${restMethod.returnTemplate}</td>
		</tr>

<tr>
<td colspan="2" align="center"><input type="submit" value="Submit"></td>
</tr>
</table>


</form>

</td></tr>

</table>

</body>
</html>

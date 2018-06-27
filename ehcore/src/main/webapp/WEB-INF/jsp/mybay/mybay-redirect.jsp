<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	 <script type="text/javascript">
	// alert("到底有没有提交表单啊");
	window.onload = function() {
		document.getElementById('submitForm').submit();
	}	 
	 </script>
</head>
<body>
	<form id="submitForm" name="submitForm" action="${signInfoURL}" method="post">
	<input type="hidden" name="AccessUserId" value="${AccessUserId}"/>
	<input type="hidden" name="AppSecurity" value="${AppSecurity}"/>
	<input type="hidden" name="Appid" value="${Appid}"/>
	<input type="hidden" name="CorpPayType" value="${CorpPayType}"/>
	<input type="hidden" name="InitPage" value="${InitPage}"/>
	<input type="hidden" name="Callback" value="${Callback}"/>
	<input type="hidden" name="CostCenter1" value="${CostCenter1}"/>
	<input type="hidden" name="CostCenter2" value="${CostCenter2}"/>
	<input type="hidden" name="CostCenter3" value="${CostCenter3}"/>
	<input type="hidden" name="EmployeeID" value="${EmployeeID}"/>
	<input type="hidden" name="Token" value="${Token}"/>
	<input type="hidden" name="Signature" value="${Signature}"/>
	<div style="display:none;"><input type="submit" /></div>
	</form>
</body>

</html>
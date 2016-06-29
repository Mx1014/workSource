<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <link rel="stylesheet" type="text/css" href="../static/css/global.css">
</head>
<body>

<script src="../static/jquery-2.1.3.min.js"></script>

<div id="outer" style="padding: 50px 0;">
<div id="top" class="dropshadow padding" style="margin: 0 auto;width:800px;">

<p>If you know UID directly, enter and submit to go</p>
<form>
<table border="1" width="100%">
    <tr>
        <td width="120px" style="text-align: right;"><b>UID:</b></td>
        <td width="300px" style="align: left;">
            <input name="uid" /><input type="submit" style="margin-left:16px; width:100px"  value="Go">
        </td>
    </tr>
</table>
</form>

<div style="height: 20px;"></div>

<p>Or, enter the mobile phone number to find a user to go</p>

<form>
<table border="1" width="100%">
    <tr>
        <td width="120px" style="text-align: right;"><b>Namespace ID:</b></td>
        <td width="300px" style="align: left;">
            <input name="namespaceId" />
        </td>
    </tr>

    <tr>
        <td width="120px" style="text-align: right;"><b>Mobile number:</b></td>
        <td width="300px" style="align: left;">
            <input name="mobile" /><input type="submit" style="margin-left: 16px; width:100px" value="Go">
        </td>
    </tr>
</table>

</form>

</div>
</div>


</body>
</html>


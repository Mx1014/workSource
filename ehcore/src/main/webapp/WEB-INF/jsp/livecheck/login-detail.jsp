<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  

<%@ page import="java.util.Date" %>
<%@ page import="com.everhomes.user.UserLogin" %>
<%@ page import="com.everhomes.rest.messaging.MessageDTO" %>

<script src="../static/jquery-2.1.3.min.js"></script>

<table>

<tr>
<td style="text-align: right" width="200px"><b>Login ID</b></td><td>${login.loginId}</td>
</tr>
<tr>

<td style="text-align: right" width="200px"><b>Namespace ID</b></td><td>${login.namespaceId}</td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Device Identifier</b></td><td>${login.deviceIdentifier}</td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Connected Border ID</b></td><td>${login.loginBorderId}</td>
</tr>

<tr>
<%
UserLogin login = (UserLogin)request.getAttribute("login");
%>
<td style="text-align: right" width="200px"><b>Last Access Time</b></td><td><%= new Date(login.getLastAccessTick()) %></td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Login Status</b></td><td>${login.status.toString()}</td>
</tr>

<tr>
<td style="text-align: right" width="200px"><b>Message Anchor</b></td>
<td>
<form id="loadMessage">
<input id="uid" name="uid" type="hidden" value="${login.userId}"></input>
<input id="loginId" name="loginId" type="hidden" value="${login.loginId}"></input>
<input id="anchor" name="anchor" value="${anchor}"></input>
<input type="submit" value="Load messages"></input>
</form>
</td>
</tr>

</table>

<div style="height:20px"></div>
<table>
<tr>
<th width="120px">Message seq</th><th width="80px">Sender UID</th><th width="160px">Time</th><th width="*%">Content</th>
</tr>

<c:forEach var="p" items="${messages}" varStatus="loop">

<% MessageDTO dto = (MessageDTO) pageContext.getAttribute("p"); %>

<tr class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
<td style="vertical-align:top"><p class="wrappable">${p.storeSequence}</p></td>
<td style="vertical-align:top"><p class="wrappable">${p.senderUid}</p></td>
<td style="vertical-align:top"><p class="wrappable"><%= new Date(dto.getCreateTime()) %></p></td>
<td style="vertical-align:top"><p class="wrappable"><%= dto.getContentDisplayText() %></p></td>
</tr>

</c:forEach>

</table>

<script type="text/javascript">
$(document).ready(function() {
	$('#loadMessage').submit(function(event) {
		event.preventDefault();
		
		var url = "loginDetail?uid=" + $('#uid').val() + "&loginId=" + $('#loginId').val() + "&anchor=" + $('#anchor').val();
		$('#right').load(url);
	});
});

</script>

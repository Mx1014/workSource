<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">  
<title>Core Server Rest API list</title>
<link rel="stylesheet" href="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/css/amazeui.min.css"/>
<style>
        .eh_auto_nav {
            max-height: 100%;
            overflow-y: auto;
        }

        .eh_auto_nav nav {
            border: 1px solid #bbb;
            font-size: 14px;
        }

        .eh_auto_nav ol {
            margin: 0 0 10px 10px;
            padding: 0;
        }
        .eh_auto_nav nav> ol {
            margin: 0;
        }
        .eh_auto_nav .am-nav li{
            margin: 0;
            padding: 0;
        }
        .eh_auto_nav .am-nav li a {
            color: #777;
            margin: 0;
            padding: 0 10px;
            display: block;
        }
        .eh_auto_nav .am-nav li a.am-active {
            background: #eee;
        }
    </style>

<body>

<div id="navto" style="width:300px;position:absolute; top: 0; left: 0;"></div>


<table border="0" align="left top" style="width:800px; margin:auto;">
	<tr>
		<td>
		<c:forEach var="element" items="${restMethods}">
		
		<table border="1" width="800" align="center">
		
		<tr><td colspan="2">
		
		<c:choose>
		 <c:when test="${element.params.size() > 0}">
		    <a name="/api${element.uri}" href="/api${element.uri}"><h2>${element.uri}</h2></a>
		 </c:when>
		 <c:otherwise>
		    <a name="${element.uri}" href="${element.uri}"><h2>${element.uri}</h2></a>
		 </c:otherwise>
		</c:choose>
		
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
		</td>
	</tr>
</table>



<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/jquery-1.11.1.min.js"></script>
<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/getBasePath.js"></script>
<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/sea.js"></script>
<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/seajs-css.js"></script>
<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/seajs_config.js"></script>
<script src="http://alpha.lab.everhomes.com:8080/eh_trunk/pub/js/amazeui.min.js"></script>

<script>
    seajs.use('js/common/eh_auto_nav', function (M) {
        var m = new M($('body>table')[0], $('#navto')[0]);
        m.init();
    });
</script>
</body>
</html>


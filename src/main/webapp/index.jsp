<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>redis简易控制台</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<base href="<%=basePath%>">
<script type="text/javascript" src="./jquery-1.7.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

</head>
<body>
	<a href="redisManager!manager.action">redis控制台</a>
	<br>
	<a href="dinner!init.action">订餐系统</a>
	<br>
</body>
</html>
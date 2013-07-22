<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ page import="bean.*"%>
<%@ page import="redis.*"%>
<%@ page import="dinnerorder.*"%>
<%@ page import="java.util.*"%>
<html>
<head>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>redis简易控制台</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/"; 
		IDinner dinner = (IDinner) SpringContextUtil.getBean("dinnerImpl");
		 List<Login> logins = dinner.getLogins();
	
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<base href="<%=basePath%>"> 
<script type="text/javascript"
	src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
function login(){
	var p = [];
	p.push("groupName=" + $('#groupName').val());
	p.push("groupNameList=" + $('#groupNameList').val());
	p.push("pass=" + $('#pass').val());
	if ( $('#groupName').val()== '' && $('#groupNameList').val() == '-1') {
			alert("必须填写登录分组名或者选择一个!");
			return false;
		}
	var param = p.join('&');
	$.ajax({
			url : "dinner!login.action",
			data : param,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.href = "dinner!init.action"; 
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.href = "dinner!init.action"; 
			}
		});
}
</script>

</head>
<body>
	分组:<input name="groupName" id="groupName"/>
	<select id="groupNameList"><option value="-1">请选择</option>
	<%
	if(logins!=null&&logins.size()>0)
	for(Login g:logins){%>
		<option value="<%=g.getSno()%>"><%=g.getGroupName()%></option>
	<%}%></select>
	密码:<input type="password" id="pass"/><br>
	<button onclick="login()">登录(若是新分组，则自动注册新分组,记住密码,下次选择分组即可.)</button> 
	<a href="redisManager!manager.action">redis控制台</a>
	<br> 
</body>
</html>
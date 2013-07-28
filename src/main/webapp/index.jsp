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
function login() {
		var p = [];
		p.push("groupName=" + $('#groupName').val());
		p.push("groupNameList=" + $('#groupNameList').val());
		p.push("pass=" + $('#pass').val());
		if ($('#groupName').val() == '' && $('#groupNameList').val() == '-1') {
			alert("必须填写'订餐小组'或者选择一个'小组'!");
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
	<table >
		<tr>
			<td>订餐小组</td>
			<td><select id="groupNameList" style="width: 200px"><option
						value="-1">请选择</option>
					<%
						if (logins != null && logins.size() > 0)
							for (Login g : logins) {
					%>
					<option value="<%=g.getSno()%>"><%=g.getGroupName()%></option>
					<%
						}
					%></select></td>
		</tr>
		<tr>
			<td>新建小组</td>
			<td><input name="groupName" id="groupName" /></td>
		</tr>
		<tr>
			<td>小组管理员密码</td>
			<td><input type="password" id="pass" />(初始密码为第一次"新建小组"时，输入的密码)</td>
		</tr> 
		<tr>
			<td colspan="2">
				<button onclick="login()" style="width:100px">登录</button>(非管理员不用输入密码)
			</td>
		</tr>
	</table> 
	<br><br>
	使用说明：<br>
	1.第一次直接在"新建小组"中输入一个组名，以及管理员密码（此次即初始密码,没有输入则为''）<br>
	2.非第一次进入，请选择 '订餐小组'中所在的组名，直接登录即可,管理员要使用密码<br> 
	3.普通用户权限：下单,查看排行榜,查看充值记录<br>
	4.管理员额外权限：充值,删除订单,删除充值记录.<br>
</body>
</html>
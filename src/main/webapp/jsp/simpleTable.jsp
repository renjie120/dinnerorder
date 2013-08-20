<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ page import="bean.*"%>
<%@ page import="redis.*"%>
<%@ page import="dinnerorder.*"%>
<%@ page
	import="org.springframework.data.redis.connection.RedisZSetCommands.Tuple"%>
<%@ page import="java.util.*"%>
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>报表展示</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<People> ps = (List<People>) request.getAttribute("rankPeople");
	List<Order> orders = (List<Order>) request.getAttribute("peopleOrders");
	String title = (String) request.getAttribute("title");
	List<Dinner> dinners = (List<Dinner>) request
			.getAttribute("rankDinner");
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<base href="<%=basePath%>">
<script type="text/javascript"
	src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/common.css">
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/reset.css">
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/style.css">
<style type="text/css">
.divClass {
	height: 250px;
	width: 100%;
	overflow: auto;
}

td {
	height: 22px;
	border-bottom: 1px solid black;
	border-right: 1px solid black;
	cursor: default;
}

th {
	height: 20px;
	font-size: 15px;
	font-weight: normal;
	border-bottom: 2px solid black;
	border-right: 1px solid black;
	background-color: #999999
}

table {
	font-size: 14px;
	border-color: #ff6600;
	bg-color: #FFD2D2;
	cell-spacing: 0;
	cell-padding: 0;
	align: center;
	border: 1;
}

input {
	border: 1px solid black;
}
 
</style>
</style>
</head>
<body>
	<div style="width:100%;height:100%;overflow:auto;">
	<table>
		<tr><th colspan="4"><h3><%=title%></h3></th></tr>
		<%
		if(orders!=null&&orders.size()>0){
		%>
		<tr>
			<td>订餐时间</td>
			<td>菜名</td>
			<td>金额</td>
			<td>是否单点</td>
		</tr>
		<%
		int index = 1;
			for (Order p : orders) {
		%>
		<tr>
			<td><%="("+(index++)+")"+p.getTime()%></td>
			<td><%="("+p.getSno()+")"+p.getDinnerName()%></td>
			<td><%=p.getMoney()%></td>
			<td><%="1".equals(p.getIsSingle())?"是":"否"%></td>
		</tr>
		<%
			}
		}
		%>
		<%
		if(ps!=null&&ps.size()>0)
			for (People p : ps) {
		%>
		<tr>
			<td><%=p.getName()%></td>
			<td colspan="3"><%=p.getRechargeSum()%></td>
		</tr>
		<%
			}
		%>
		<%
		if(dinners!=null&&dinners.size()>0)
			for (Dinner p : dinners) {
		%>
		<tr>
			<td><%=p.getDinnerName()%></td>
			<td  colspan="3"><%=p.getScore()%></td>
		</tr>
		<%
			}
		%>
		<tr><td colspan="4" style="align:center"><button onclick="window.close();">关闭</button></td></tr>
	</table>
	</div>
</body>
</html>
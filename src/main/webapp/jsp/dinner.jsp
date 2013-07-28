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
<title>订餐界面</title>
<%
	BaseRedisTool tool = (BaseRedisTool) SpringContextUtil
			.getBean("redisTool");
	String path = request.getContextPath();
	String groupName = "" + session.getAttribute("groupName");
	String groupSno = "" + session.getAttribute("groupSno");
	String userId = "" + session.getAttribute("userNo");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	IDinner dinner = (IDinner) SpringContextUtil.getBean("dinnerImpl");
	List<People> ps = (List<People>) request.getAttribute("peoples");
	List<ReCharge> rechargs = (List<ReCharge>) request
			.getAttribute("rechargs"); 
	List<Dinner> dinners = (List<Dinner>) request
			.getAttribute("dinners");
	List<Menu> menus = (List<Menu>) request.getAttribute("menus");
	List<String> allTime = (List<String>) request
			.getAttribute("allTime");
	List<String> thisGroupRecharge = new ArrayList<String>();
	if(groupSno!=null&&!"null".equals(groupSno)){
		thisGroupRecharge = dinner
				.getRechargeByGroup(Integer.parseInt(groupSno));
	}
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<base href="<%=basePath%>">
<script language="javascript" type="text/javascript"
	src="<%=basePath%>/js/calendar/WdatePicker.js"></script>
<LINK rel=stylesheet type=text/css
	href="<%=basePath%>/js/common/red.css">
<script type="text/javascript"
	src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/common.css">
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/reset.css">
<LINK rel=stylesheet type=text/css href="<%=basePath%>/css/style.css">
<script type="text/javascript"
	src="<%=basePath%>/jsp/dinner.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<%if (userId == null || "-1".equals(userId + "")) {%>
			$('img[tag!=1]').remove();
			$('.set').remove();
		<%}%>
		if ($('#gName').text() == 'null') {
			$('img[tag!=1]').remove();
		}
	}); 
</script>
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
	font-size: 12px;
	font-weight: normal;
	border-bottom: 2px solid black;
	border-right: 1px solid black;
	background-color: #999999
}

table {
	font-size: 13px;
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

.set {
	background: url(./jsp/bg.gif) no-repeat -333px -76px;
	display:inline-block; *display:inline; *zoom:1;
	width: 18px;
	height: 21px;
}

a {
	font-size: 12px;
}
</style>
</style>
</head>
<body style="overflow: auto;">
	<h1>
		<font color="red">当前分组：<label id="gName"><%=groupName%></label></font>
	</h1>
	<%if(groupName==null||"null".equals(groupName)){ %>
		<a href="<%=basePath%>"><font size="12">请从首页登录</font></a>
	<%}else{ %>
	<input id="groupSno" type="hidden" value="<%=groupSno%>">
	<table>
		<tr>
			<td>
				<table  >
					<tr>
						<td>
							<table style="width:300px">
								<th colspan="2">订餐</th>
								<tr>
									<td>时间</td>
									<td><input id="moneyTime" type="text" readOnly='true'>
										<img onclick="WdatePicker({el:$dp.$('moneyTime')})" tag='1'
										src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
									</td>
								</tr>
								<tr>
									<td>人员</td>
									<td><input id='peopleName' /> <select id='peopleList'>
											<option value="-1">请选择</option>
											<%
											if(ps!=null&&ps.size()>0)
												for (People p : ps) {
											%>
											<option value="<%=p.getSno()%>"><%=p.getName()%></option>
											<%
												}
											%>
									</select></td>
								</tr>
								<tr>
									<td>菜名</td>
									<td><input id='dinnerName' /> <select
										id='dinnerNameList'>
											<option value="-1">请选择</option>
											<%
												for (Dinner p : dinners) {
											%>
											<option value="<%=p.getSno()%>"><%=p.getDinnerName()%></option>
											<%
												}
											%>
									</select></td>
								</tr>
								<tr>
									<td>金额</td>
									<td><input id='money' /></td>
								</tr>
								<tr>
									<td>是否单点</td>
									<td><select id="single">
											<option value="0">否</option>
											<option value="1">是</option>
									</select>(含义:有人请客多付钱等)</td>
								</tr>
								<tr>
									<td colspan="2">
										<button onclick='saveOrder()'>保存</button>
									</td>
								</tr>
							</table>
						</td>
						
						<% 
							if (!(userId == null || "-1".equals(userId + ""))) {
						%>
						<td>
							<table style="width:200px">
								<tr>
									<th colspan="2">充值</th>
								</tr>
								<tr>
									<td>时间</td>
									<td><input id="rechargeMoneyTime" type="text"
										readOnly='true'> <img tag='1'
										onclick="WdatePicker({el:$dp.$('rechargeMoneyTime')})"
										src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
									</td>
								</tr>
								<tr>
									<td>人员</td>
									<td><select id='rechargePeopleList'>
											<option value="-1">请选择</option>
											<%
											if(ps!=null&&ps.size()>0)
												for (People p : ps) {
											%>
											<option value="<%=p.getSno()%>"><%=p.getName()%></option>
											<%
												}
											%>
									</select></td>
								</tr>
								<tr>
									<td>金额</td>
									<td><input id='rechargeMoney' /></td>
								</tr>
								<tr>
									<td colspan="2">
										<button onclick='saveRecharge()'>充值</button>
									</td>
								</tr>
							</table>
						</td>
						<td>
							<table style="width:200px">
								<tr>
									<th colspan="2">添加排行榜</th>
								</tr>
								<tr>
									<td>排行榜名</td>
									<td><input id='menuName' /></td>
								</tr>
								<tr>
									<td>后台链接</td>
									<td><input id='menuUrl' /></td>
								</tr>
								<td colspan="2">
									<button onclick='saveMenu()'>添加</button>
								</td>
							</table>
						</td>
						<%
							}%>
						<td>
						<table style="width:200px">
								<tr>
									<th colspan="2">趣味排行榜</th>
								</tr>
								<tr>
									<td>排行榜</td>
									<td>查看次数</td>
								</tr>
								<%
									for (Menu m : menus) {
								%>
								<tr>
									<td><a url="#" onclick="goUrl('<%=m.getSno()%>')"><%=m.getMenuName()%></a></td>
									<td><%=m.getClickTimes()%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td> 
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
		<tr>
			<td style="valign: top">
				<table>
					<%
						//按照订餐人数的次数优先级显示出来全部的人员 
					%>
					<tr>
						<th colspan="<%=ps.size() + 2%>">查看订餐历史</th>
					</tr>
					<tr>
						<td width='80px' algin="center">时间</td>
						<td width='80px' algin="center">人均</td>
						<% 
							for (People _pp:ps) { 
								int _pid =  _pp.getSno() ; 
						%>
						<td width='100px'  algin="center"><%=tool.getKey(RedisColumn.peopleName(_pid))%></td>
						<%
							}
						%> 
					</tr> 
					<%
						//打印出来每一天的订餐的情况
						if(allTime!=null&&allTime.size()>0)
						for (String t : allTime) {
							if (!tool.existsKey(RedisColumn.timeToOrder(t))) {
								continue;
							}
							//得到当前分组号
							int _gid = Integer.parseInt(groupSno); 
							String avg =  tool.getKey(RedisColumn.timeAndGroupToAvg(t, Integer.parseInt(groupSno)));
							if(avg==null||"null".equals(avg)){
								avg="0.0";
							}
					%>
					<tr>
						<td><%=t + "(" + dinner.getSumByDay(t,_gid) + ")"%></td>
						<td><input id="avg" style="width:30px" value="<%=avg%>"/><a class="set" onclick="javascript:setAvg(this,'<%=t%>','<%=groupSno%>')" /></a></td>
						<%
							//按照订餐人数的次数优先级显示出来全部的人员.
									for (People _pp:ps) {  
									int _pid =  _pp.getSno() ; 
									List<String> thisDayOrders = dinner
											.getOrderByPeopleAndGroupInOneDay(_pid, t,_gid);
						%>
						<td>
							<%
								for (String _o : thisDayOrders) { 
							%> <%=tool.getKey(RedisColumn.orderDinner(Integer.parseInt(_o)))
									+ "("
									+ tool.getKey(RedisColumn
											.orderMoney(Integer.parseInt(_o)))
									+ ")"%> <img
							src="<%=basePath%>/jsp/onError.gif"
							onclick="javascript:deleteThisOrder('<%=_o%>')" /> <%
 	} 
 %>

						</td>
						<%
							}
						%>

					</tr>
					<%
						}
					%>
				</table>
			</td> 
			<td style="valign: top">
				<table>
					<tr>
						<th colspan="4">查看充值历史</th>
					</tr>
					<tr>
						<td>时间</td>
						<td>人员</td>
						<td>金额</td>
						<td>删除</td>
					</tr>
					<%
						for (ReCharge o : rechargs) {
							if (thisGroupRecharge.indexOf(o.getSno() + "")!=-1) {
					%>
					<tr>
						<td><%=o.getTime()%></td>
						<td><%=tool.getKey(RedisColumn.peopleName(o
							.getPeopleSno()))%>
						</td>
						<td><%=o.getMoney()%></td>
						<td><img src="<%=basePath%>/jsp/onError.gif"
							onclick="javascript:deleteThisRecharge('<%=o.getSno()%>')" /></td>
					</tr>
					<%
						}
						}
					%>
				</table>
			</td>
	</table>
<%} %>
</body>
</html>
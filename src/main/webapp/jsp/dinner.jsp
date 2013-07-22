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
<title>redis简易控制台</title>
<%
	BaseRedisTool tool = (BaseRedisTool) SpringContextUtil
			.getBean("redisTool");
	String path = request.getContextPath();
	String groupName =""+session.getAttribute("groupName");
	String groupSno =""+session.getAttribute("groupSno");
	String userId =""+session.getAttribute("userNo");
	 String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	IDinner dinner = (IDinner) SpringContextUtil.getBean("dinnerImpl");
	List<People> ps = (List<People>) request.getAttribute("peoples");
	List<ReCharge> rechargs = (List<ReCharge>) request
			.getAttribute("rechargs");
	List<Order> orders = (List<Order>) request.getAttribute("orders");
	List<Dinner> dinners = (List<Dinner>) request.getAttribute("dinners");
	List<Menu> menus = (List<Menu>) request.getAttribute("menus");
	List<String> allTime = (List<String>) request
			.getAttribute("allTime");
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
<script type="text/javascript">
	 function initload() {  
		<%if(userId==null||"-1".equals(userId+"")){ %>
			$('img').remove();
		<%}%>
	}

	/**充值.
	 */
	function saveRecharge() {
		var p = [];
		if ($('#rechargeMoneyTime').val() == ''
				|| $('#rechargeMoney').val() == ''
				|| $('#rechargePeopleList').val() == '-1') {
			alert("数据没有填写完全!");
			return false;
		}
		p.push("rechargeMoneyTime=" + $('#rechargeMoneyTime').val());
		p.push("rechargePeopleList=" + $('#rechargePeopleList').val());
		p.push("rechargeMoney=" + $('#rechargeMoney').val());
		if (isNaN($('#rechargeMoney').val() ) ){
			alert("请输入有效金额!");
			return false;
		}
		var param = p.join('&');
		$.ajax({
			url : "dinner!saveRecharge.action",
			data : param,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
				$('#rechargeMoney').val('');
				$('#rechargePeopleList').val('-1');
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();
				$('#rechargeMoney').val('');
				$('#rechargePeopleList').val('-1');
			}
		});
	}
	
	function findVByName(dName){
		var result = -1;
		$('#dinnerNameList option').each(function(){ 
			if($(this).html()==dName){
				result = $(this).val();
				return ;
			}
		});
		return result;
	}
	//下订单.
	function saveOrder() {
		var p = [];
		if ($('#moneyTime').val() == '' || $('#money').val() == '') {
			alert("数据没有填写完全!");
			return false;
		}
		if ($('#peopleName').val() == '' && $('#peopleList').val() == '-1') {
			alert("必须填写人名或者选择人名!");
			return false;
		}
		if (isNaN($('#money').val() ) ){
			alert("请输入有效金额!");
			return false;
		}
		var dName = $('#dinnerName').val();
		if ( dName== '' && $('#dinnerNameList').val() == '-1') {
			alert("必须填写菜名或者选择菜名!");
			return false;
		}
		if(dName != ''){ 
			var _v = findVByName(dName); 
			if(_v!=-1){
			 	$('#dinnerNameList').val(_v);
			 	$('#dinnerName').val('');
			}
		}
		p.push("moneyTime=" + $('#moneyTime').val());
		p.push("peopleName=" + $('#peopleName').val());
		p.push("peopleList=" + $('#peopleList').val());
		p.push("dinnerNameList=" + $('#dinnerNameList').val());
		p.push("dinnerName=" + $('#dinnerName').val());
		p.push("money=" + $('#money').val());
		p.push("single=" + $('#single').val());
		var param = p.join('&');
		$.ajax({
			url : "dinner!saveOrder.action",
			data : param,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
				$('#money').val('');
				$('#peopleName').val('');
				$('#peopleList').val('-1');
				$('#dinnerName').val('');
				$('#dinnerNameList').val('-1');
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();
				$('#money').val('');
				$('#peopleName').val('');
				$('#peopleList').val('-1');
				$('#dinnerName').val('');
				$('#dinnerNameList').val('-1');
			}
		});
	}

	function deleteThisRecharge(od) {
		$.ajax({
			url : "dinner!deleteRecharge.action",
			data : "id=" + od,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();
			}
		});
	}

	function deleteThisDinner(od) {
		$.ajax({
			url : "dinner!deleteDinner.action",
			data : "id=" + od,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();
			}
		});
	}

	function deleteThisOrder(od) { 
		$.ajax({
			url : "dinner!deleteOrder.action",
			data : "id=" + od,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();

			}
		});
	}

	function goUrl(sno) {
		window.open(
						 'dinner!goUrl.action?id=' + sno,
						'newwindow',
						'height=500, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no');
		location.reload();
	}
	function saveMenu() {
		var p = [];
		if ($('#menuName').val() == '' || $('#menuUrl').val() == '') {
			alert("数据没有填写完全!");
			return false;
		}
		p.push("menuName=" + $('#menuName').val());
		p.push("menuUrl=" + $('#menuUrl').val());
		var param = p.join('&');
		$.ajax({
			url : "dinner!saveMenu.action",
			data : param,
			type : 'POST',
			dataType : 'json',
			success : function(x) {
				alert(x);
				location.reload();
				$('#menuName').val('');
				$('#menuName').val('');
			},
			error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				location.reload();
				$('#menuName').val('');
				$('#menuName').val('');
			}
		});
	}
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

a {
	font-size: 12px;
}
</style>
</style>
</head>
<body style="overflow: auto;" onload="initload()">
	当前分组：<%=groupName%>
	<input id="groupSno" type="hidden" value="<%=groupSno%>">
	<table>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<table>
								<th colspan="2">订餐</th>
								<tr>
									<td>时间</td>
									<td><input id="moneyTime" type="text" readOnly='true' value="2013-7-1">
										<img onclick="WdatePicker({el:$dp.$('moneyTime')})"
										src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
									</td>
								</tr>
								<tr>
									<td>人员</td>
									<td><input id='peopleName' /> <select id='peopleList'>
											<option value="-1">请选择</option>
											<%
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
									<td><input id='dinnerName'  /> <select id='dinnerNameList' >
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
									<td><input id='money'  /></td>
								</tr>
								<tr>
									<td>是否单点</td>
									<td><select id="single">
											<option value="0">否</option>
											<option value="1">是</option>
									</select></td>
								</tr>
								<tr>
									<td colspan="2">
										<button onclick='saveOrder()'>保存</button>
									</td>
								</tr>
							</table>
						</td>
						<td>
							<table>
								<tr>
									<th colspan="2">充值</th>
								</tr>
								<tr>
									<td>时间</td>
									<td><input id="rechargeMoneyTime" type="text"
										readOnly='true'> <img
										onclick="WdatePicker({el:$dp.$('rechargeMoneyTime')})"
										src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
									</td>
								</tr>
								<tr>
									<td>人员</td>
									<td><select id='rechargePeopleList'>
											<option value="-1">请选择</option>
											<%
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
							<table>
								<tr>
									<th colspan="2">添加菜单</th>
								</tr>
								<tr>
									<td>菜单名</td>
									<td><input id='menuName' /></td>
								</tr>
								<tr>
									<td>菜单链接</td>
									<td><input id='menuUrl' /></td>
								</tr>
								<td colspan="2">
									<button onclick='saveMenu()'>添加</button>
								</td>
							</table>
						</td>
						<td>
							<table>
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
			<td style="valign:top">
				<table >
					<%
						//按照订餐人数的次数优先级显示出来全部的人员.
						Set<Tuple> peopleSet = tool.getListWithScore(RedisColumn
								.orderPeopleWithScore());
					%>
					<tr>
						<th colspan="<%=peopleSet.size() + 1%>">查看订餐历史</th>
					</tr>
					<tr>
						<td>时间</td>
						<%
							Iterator<Tuple> itt = peopleSet.iterator();
							while (itt.hasNext()) {
								Tuple tt = itt.next();
								int _pid = Integer.parseInt(new String(tt.getValue()));
								double score = tt.getScore();
								if (score < 1)
									continue;
						%>
						<td><%=tool.getKey(RedisColumn.peopleName(_pid))%></td>
						<%
							}
						%>

					</tr>

					<%
						//打印出来每一天的订餐的情况
						for (String t : allTime) {
							if (!tool.existsKey(RedisColumn.timeToOrder(t))) {
								continue;
							}
					%>
					<tr>
						<td><%=t+"("+dinner.getSumByDay(t)+")"%></td>
						<%
							//按照订餐人数的次数优先级显示出来全部的人员.
								Iterator<Tuple> it2 = peopleSet.iterator();
								while (it2.hasNext()) {
									Tuple tt = it2.next();
									int _pid = Integer.parseInt(new String(tt.getValue()));
									if (tt.getScore() < 1)
										continue;
									List<String> thisDayOrders = dinner
											.getOrderByPeopleInOneDay(_pid, t);
						%>
						<td>
							<%
								for (String _o : thisDayOrders) {
							%> <%=tool.getKey(RedisColumn.orderDinner(Integer
								.parseInt(_o)))
								+ "("
								+ tool.getKey(RedisColumn.orderMoney(Integer
										.parseInt(_o))) + ")"%> <img
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
			<td style="valign:top">
				<table>
					<tr>
						<th colspan="5">查看订单历史</th>
					</tr>
					<tr>
						<td>时间</td>
						<td>人员</td>
						<td>菜名</td>
						<td>金额</td>
						<td>删除</td>
					</tr>
					<%
						for (Order o : orders) {
					%>
					<tr>
						<td><%=o.getTime()%></td>
						<td><%=tool.getKey(RedisColumn.peopleName(o.getPeopleSno()))%>
						</td>
						<td><%=o.getDinner()%></td>
						<td><%=o.getMoney()%></td>
						<td><img src="<%=basePath%>/jsp/onError.gif"
							onclick="javascript:deleteThisOrder('<%=o.getSno()%>')" /></td>
					</tr>
					<%
						}
					%>
				</table>
			</td>
			<td style="valign:top">
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
					%>
					<tr>
						<td><%=o.getTime()%></td>
						<td><%=tool.getKey(RedisColumn.peopleName(o.getPeopleSno()))%>
						</td>
						<td><%=o.getMoney()%></td>
						<td><img src="<%=basePath%>/jsp/onError.gif"
							onclick="javascript:deleteThisRecharge('<%=o.getSno()%>')" /></td>
					</tr>
					<%
						}
					%>
				</table>
			</td>
	</table>

</body>
</html>
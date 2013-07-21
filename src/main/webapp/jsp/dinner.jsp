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
		BaseRedisTool tool  = (BaseRedisTool)SpringContextUtil.getBean("redisTool");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
		IDinner	dinner  =(IDinner)SpringContextUtil.getBean("dinnerImpl");
	List<People> ps = (List<People>)request.getAttribute("peoples");
	List<ReCharge> rechargs = (List<ReCharge>)request.getAttribute("rechargs");
	List<Order> orders = (List<Order>)request.getAttribute("orders");
	List<String> allTime = (List<String>)request.getAttribute("allTime");
%>
		<c:set var="ctx" value="${pageContext.request.contextPath}" />
		<base href="<%=basePath%>">
		<script language="javascript" type="text/javascript"
			src="<%=basePath%>/js/calendar/WdatePicker.js"></script>
		<LINK rel=stylesheet type=text/css
			href="<%=basePath%>/js/common/red.css">
		<script type="text/javascript"
			src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript"> 
	$(function() {
		 var d = new Date();
	     $('#moneyTime').val(d.asString('yyyy-mm-dd'));  
	});
	
	/**充值.
	 */
	function saveRecharge(){
		var p = [];
		p.push("rechargeMoneyTime="+$('#rechargeMoneyTime').val()); 
		p.push("rechargePeopleList="+$('#rechargePeopleList').val());
		p.push("dinnerName="+$('#dinnerName').val());
		p.push("rechargeMoney="+$('#rechargeMoney').val()); 
		var param = p.join('&');
		$.ajax({
				url :   "dinner!saveRecharge.action",
				data : param,
				type : 'POST',
				dataType : 'json',
				success:function(x){
					alert(x);
					location.reload();
				} ,
				error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				 		location.reload();
				}
			});
	}
	//下订单.
	function saveOrder(){
		var p = [];
		p.push("moneyTime="+$('#moneyTime').val());
		p.push("peopleName="+$('#peopleName').val());
		p.push("peopleList="+$('#peopleList').val());
		p.push("dinnerName="+$('#dinnerName').val());
		p.push("money="+$('#money').val());
		p.push("single="+$('#single').val());
		var param = p.join('&');
		$.ajax({
				url :   "dinner!saveOrder.action",
				data : param,
				type : 'POST',
				dataType : 'json',
				success:function(x){
					alert(x);
					location.reload();
				} ,
				error : function(x, textStatus, errorThrown) {
				alert(x.responseText);
				 		location.reload();
				}
			});
	}
</script>
	</head>
	<body>
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<table>
									<td colspan="2">
										订餐操作
									</td>
									<tr>
										<td>
											时间
										</td>
										<td>
											<input id="moneyTime" type="text" readOnly='true'>
											<img onclick="WdatePicker({el:$dp.$('moneyTime')})"
												src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
										</td>
									</tr>
									<tr>
										<td>
											人员
										</td>
										<td>
											<input id='peopleName' />
											<select id='peopleList'>
												<option value="-1">
													请选择
												</option>
												<%for(People p:ps){%>
												<option value="<%=p.getSno()%>"><%=p.getName()%></option>
												<%}%>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											菜名
										</td>
										<td>
											<input id='dinnerName' />
										</td>
									</tr>
									<tr>
										<td>
											金额
										</td>
										<td>
											<input id='money' />
										</td>
									</tr>
									<tr>
										<td>
											是否单点
										</td>
										<td>
											<select id="single">
												<option value="0">
													否
												</option>
												<option value="1">
													是
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<button onclick='saveOrder()'>
												保存
											</button>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<table>
									<tr>
										<td colspan="2">
											充值操作
										</td>
									</tr>
									<tr>
										<td>
											时间
										</td>
										<td>
											<input id="rechargeMoneyTime" type="text" readOnly='true'>
											<img onclick="WdatePicker({el:$dp.$('rechargeMoneyTime')})"
												src="<%=basePath%>/js/common/datepicker/images/calendar.gif">
										</td>
									</tr>
									<tr>
										<td>
											人员
										</td>
										<td>
											<select id='rechargePeopleList'>
												<option value="-1">
													请选择
												</option>
												<%for(People p:ps){%>
												<option value="<%=p.getSno()%>"><%=p.getName()%></option>
												<%}%>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											金额
										</td>
										<td>
											<input id='rechargeMoney' />
										</td>
									</tr>
									<tr>
										<td>
											<button onclick='saveRecharge()'>
												充值
											</button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table style="border:1px">
						<tr>
							<td>
								查看订餐历史
							</td>
						</tr>
						<tr>
							<td>
								时间
							</td>
							<%	
								//按照订餐人数的次数优先级显示出来全部的人员.
								Set<Tuple> peopleSet = tool.getListWithScore(RedisColumn.orderPeopleWithScore());
									Iterator<Tuple> itt = peopleSet.iterator(); 
									while(itt.hasNext()){
										Tuple tt = itt.next();
										int _pid = Integer.parseInt(new String(tt.getValue()));
										double score = tt.getScore(); 
										%>
							<td><%=tool.getKey(RedisColumn.peopleName(_pid))+"("+score+")"%></td>
							<%
									}
								%>

						</tr>

						<%
						//打印出来每一天的订餐的情况
						 for(String t:allTime){%>
						<tr>
							<td>
								<%=t%></td>
							<%	
									//按照订餐人数的次数优先级显示出来全部的人员.
								 	Iterator<Tuple> it2 = peopleSet.iterator(); 
									while(it2.hasNext()){
										Tuple tt = it2.next();
										int _pid = Integer.parseInt(new String(tt.getValue())); 
										List<String> thisDayOrders = dinner.getOrderByPeopleInOneDay(_pid,t);
										%>
							<td  >
								<%
										for(String _o:thisDayOrders){
										%>
								<%=tool.getKey(RedisColumn.orderDinner(Integer.parseInt(_o)))+"("
												+tool.getKey(RedisColumn.orderMoney(Integer.parseInt(_o)))+")"%>
								<%}%>
							</td>
							<%
									}
								%>

						</tr>
						<%}%>
					</table>
				</td>
				<td>
					<table>
						<tr>
							<td>
								查看订单历史
							</td>
						</tr>
						<tr>
							<td>
								时间
							</td>
							<td>
								人员
							</td>
							<td>
								菜名
							</td>
							<td>
								金额
							</td>
						</tr>
						<% for(Order o: orders){%>
						<tr>
							<td><%=o.getTime()%></td>
							<td><%=tool.getKey(RedisColumn.peopleName(o.getPeopleSno()))%>
							</td>
							<td><%=o.getDinner()%></td>
							<td><%=o.getMoney()%></td>
						</tr>
						<%}%>
					</table>
				</td>
				<td>
					<table>
						<tr>
							<td>
								查看充值历史
							</td>
						</tr>
						<tr>
							<td>
								时间
							</td>
							<td>
								人员
							</td>
							<td>
								金额
							</td>
						</tr>
						<% for(ReCharge o: rechargs){%>
						<tr>
							<td><%=o.getTime()%></td>
							<td><%=tool.getKey(RedisColumn.peopleName(o.getPeopleSno()))%>
							</td>
							<td><%=o.getMoney()%></td>
						</tr>
						<%}%>
					</table>
				</td>
		</table>

	</body>
</html>
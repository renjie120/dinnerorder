<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ page import="bean.*"%>
<%@ page import="redis.*"%>
<%@ page import="dinnerorder.*"%>
<%@ page import="org.springframework.data.redis.connection.RedisZSetCommands.Tuple"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>订餐界面</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	BaseRedisTool tool = (BaseRedisTool) SpringContextUtil.getBean("redisTool");
	String groupName = "" + session.getAttribute("groupName");
	String groupSno = "" + session.getAttribute("groupSno");
	String userId = "" + session.getAttribute("userNo");
	IDinner dinner = (IDinner) SpringContextUtil.getBean("dinnerImpl");
	List<People> ps = (List<People>) request.getAttribute("peoples");
	List<ReCharge> rechargs = (List<ReCharge>) request.getAttribute("rechargs"); 
	List<Dinner> dinners = (List<Dinner>) request.getAttribute("dinners");
	List<Menu> menus = (List<Menu>) request.getAttribute("menus");
	List<String> allTime = (List<String>) request.getAttribute("allTime");
	List<String> thisGroupRecharge = new ArrayList<String>();
	if(groupSno!=null&&!"null".equals(groupSno)){
		thisGroupRecharge = dinner.getRechargeByGroup(Integer.parseInt(groupSno));
	}
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<base href="<%=basePath%>" />
<script type="text/javascript" src="<%=basePath%>/js/calendar/WdatePicker.js"></script>
<link rel=stylesheet type=text/css href="<%=basePath%>/css/order.css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/jsp/dinner.js"></script>
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
	function getOrdersByPeople(peopleId){
		window
		.open(
				'dinner!getPeopleOrders.action?peopleSno=' + peopleId,
				'newwindow',
				'height=600, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto,resizable=no,location=no, status=no'); 
	}
	
</script>
</head>
<body>
	<%if(groupName==null||"null".equals(groupName)){ %>
		<a href="<%=basePath%>"><font size="12">请从首页登录</font></a>
	<%}else{ %>
	<input id="groupSno" type="hidden" value="<%=groupSno%>">
	<div class="main">
		<div class="header">
			<div class="icon"></div>
        	<div class="welcome">欢迎订餐</div>
		</div>
		<div class="left">
        	<div class="titl">订餐</div>
        	<div>
				<table >
                    <tr>
                        <td>时间</td>
                        <td><input id="moneyTime" type="text" readonly="readonly" onclick="WdatePicker({el:$dp.$('moneyTime')})" />
							<img onclick="WdatePicker({el:$dp.$('moneyTime')})" 
							src="<%=basePath%>/js/common/datepicker/images/calendar.gif" />
                        </td>
                    </tr>
                    <tr>
                        <td>人员</td>
                        <td>
                            <input id="peopleName" /> <select id="peopleList">
							<option selected="selected" value="-1">请选择</option>
									<%
									if(ps!=null&&ps.size()>0)
										for (People p : ps) {
									%>
									<option value="<%=p.getSno()%>"><%=p.getName()%></option>
									<%
										}
									%>
							</select>
                        </td>
                     </tr>
                        <tr>
                            <td>菜名</td>
                            <td>
                            <input id="dinnerName" type="text" /> <select id="dinnerNameList">
							<option selected="selected" value="-1">请选择</option>
								<%
									for (Dinner p : dinners) {
								%>
								<option value="<%=p.getSno()%>"><%=p.getDinnerName()%></option>
								<%
									}
								%>
							</select>
                            </td>
                        </tr>
                        <tr>
                            <td>金额</td>
                            <td><input id="money" /></td>
                        </tr>
                        <tr>
                            <td>是否单点</td>
                            <td>
                            <select id="single">
                                    <option selected="selected" value="0">否</option>
                                    <option value="1">是</option>
                            </select>(含义:有人请客多付钱等)
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <center><button onclick="saveOrder()">保存</button></center>
                            </td>
                        </tr>
				</table>
                </div>  
				<hr />
				<center>
        		<div class="titl"><b>趣味排行榜</b></div>
        		<div>
					<table style="width:200px;">
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
             	 </div>
          		 </center>
				 	<hr />



				<center>
        		<% 
							if (!(userId == null || "-1".equals(userId + ""))) {
						%>
						<div class="titl"><b>充值</b></div>
						<div>
							<table style="width:200px"> 
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
						</div>
							<hr />
						<div class="titl"><b>添加排行榜</b></div>
						<div>
							<table style="width:200px"> 
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
						</div>
						<%
				 }%>
          		 </center> 
		  	</div> 
	<div class="right" >
		<div class="titl">查看充值历史</div>
	<div style="overflow:auto;height:95%;">
	<table border="1" style="border-collapse:collapse" bordercolor="#000000" class="tb2">
					<tr>
						<th>时间</th>
						<th>人员</th>
						<th>金额</th>
						<th>删除</th>
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
			</div>

		</div>
		<div class="center" >
        	<div class="titl">查看订餐历史</div>
            <div style="overflow:auto; height:95%;">
       <table class="tb3" cellspacing="0" width="1000px"  border="1" bordercolor="#36F" style="border-collapse:collapse" >
					<tr>
						<th >时间</th>
						<th >人均</th>
						<% 
						for (People _pp:ps) { 
							int _pid =  _pp.getSno() ; 
						%>
						<th ><a onclick="getOrdersByPeople('<%=_pid%>')"><%=tool.getKey(RedisColumn.peopleName(_pid))%></a></th>
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
						<td><input id="avg" style="width:30px" value="<%=avg%>"/><button class="set" onclick="javascript:setAvg(this,'<%=t%>','<%=groupSno%>')" />均</button></td>
						<%
						//按照订餐人数的次数优先级显示出来全部的人员.
						for (People _pp:ps) {  
							int _pid =  _pp.getSno() ; 
							List<String> thisDayOrders = dinner.getOrderByPeopleAndGroupInOneDay(_pid, t,_gid);
						%>
						<td>
							<%
								for (String _o : thisDayOrders) { 
							%> 
							<%=tool.getKey(RedisColumn.orderDinner(Integer.parseInt(_o)))
									+ "(" + tool.getKey(RedisColumn.orderMoney(Integer.parseInt(_o))) + ")"%> 
									<img src="<%=basePath%>/jsp/onError.gif" onclick="javascript:deleteThisOrder('<%=_o%>')" /> 
							<%
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
					<tr>
						<th >时间</th>
						<th >人均</th>
						<% 
						for (People _pp:ps) { 
							int _pid =  _pp.getSno() ; 
						%>
						<th><font size="2"><a url="#" onclick="getOrdersByPeople('<%=_pid%>')"><%=tool.getKey(RedisColumn.peopleName(_pid))%></a></font></th>
						<%
							}
						%> 
					</tr> 
				</table>
           </div>
	 </div>
</div>
<div class="buttom"></div>
</div>
<%}%>
</body>
</html>

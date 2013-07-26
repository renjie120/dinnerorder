<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html>
<head>
<%@ page import="bean.*"%>
<%@ page import="redis.*"%>
<%@ page import="dinnerorder.*"%>
<%@ page import="java.util.*"%>
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
<script type="text/javascript"
	src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>js/gridTree/core.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>js/gridTree/style.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>js/gridTree/GridTree.css">
<script type="text/javascript"
	src="<%=basePath%>js/gridTree/hashMap.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/gridTree/GridTree.All.js"></script>
<script type="text/javascript" src="<%=basePath%>/jsp/redis.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.open {
	background: url(./jsp/bg.gif) no-repeat -443px -80px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 15px;
	height: 18px;
}

.open2 {
	background: url(./jsp/bg.gif) no-repeat -443px -80px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 15px;
	height: 18px;
}

.close2 {
	background: url(./jsp/bg.gif) no-repeat -335px -119px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 15px;
	height: 15px;
}


.delete {
	background: url(./jsp/bg.gif) no-repeat -443px -80px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 15px;
	height: 18px;
}

.close {
	background: url(./jsp/bg.gif) no-repeat -335px -119px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 15px;
	height: 15px;
}

.set {
	background: url(./jsp/bg.gif) no-repeat -333px -76px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 18px;
	height: 21px;
}

.title {
	font-size: 14px;
	color: red;
	font-weight: bold;
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
 
</style>
<%
	List<Menu> allSystem = (List<Menu>) request
			.getAttribute("allSystem");
%>
</head>
<body>
		<input type='hidden' value="<%=basePath%>" id='wpath' />
		全部keys数量:${count}
		<hr>
		<input name="config">
		<button id="seeConfig">
			查看配置
		</button>
		(例如：config*)
		<br>
		<table id='configTable'></table>
		<hr>
		<input name="keys">
		<button id="findKey">
			匹配key
		</button>
		(例如:lsq*)
		<br>
		<table id='keysTable'></table>

		<hr>
		<input name="exists">
		<button id="exisKey">
			查询键值
		</button>
		(对指定键值进行查询，删除，修改,重命名等操作)
		<br>
		<table id='existsTable'></table>

		<hr>
		类型=
		<select id="keytype" onchange="changeType()">
			<option value="str">
				字符串
			</option>
			<option value="strnx">
				字符串NX
			</option>
			<option value="hash">
				哈希表
			</option>
			<option value="hashnx">
				哈希表NX
			</option>
			<option value="listL">
				插入列头
			</option>
			<option value="listR">
				插入表尾
			</option>
			<option value="set">
				集合
			</option>
			<option value="zset">
				有序集合
			</option>
		</select>
		<br>
		key=
		<input id="newKeyName" />
		<span>输入键值,有则追加或者覆盖,无则新建.</span>
		<br>
		val1=
		<input id="val1" />
		<span style='color: red; fontSize: 10px;' id='tip1'></span>
		<span id="arg2" style="display: none"><br>val2=<input
				id="val2" /><span style='color: red; fontSize: 8px;' id='tip2'></span>
		</span>
		<br>
		<button id="addKey">
			设置键值
		</button>
		(在原有基础上添加，无则新建,不会删除以前数据)
		<br>
		<table id='addKeyTable'></table>

		<hr>
		维护注册表结构(添加键的自动生成策略)
		<br>
		<table id='regiestTable'>
			<tr>
				<td>
					系统名
				</td>
				<td>
					<input id="registerSystem" />
					<span>描述:</span>
					<input id="desc" />
					<span class="set" onClick="addSystem(this)"></span>
				</td>
			</tr>
			<tr>
				<td>
					表名
				</td>
				<td>
					<select name="system" id="sel1">
						<option value="-1">
							请选择系统
						</option>
						<%
							if (allSystem != null && allSystem.size() > 0) {
								for (Menu m : allSystem) {
						%>
						<option value="<%=m.getSno()%>" code="<%=m.getMenuUrl()%>">
							<%=m.getMenuName()%></option>
						<%
							}
							}
						%>
					</select>
					<input id="registerTable" />
					描述:
					<input id="desc" />
					<span class="set" onclick="addTable(this)"></span>
				</td>
			</tr>
			<tr>
				<td>
					列名
				</td>
				<td>
					<select id="sel2" name="system" onchange="changeSystem(this)">
						<option value="-1">
							请选择系统
						</option>
						<%
							if (allSystem != null && allSystem.size() > 0) {
								for (Menu m : allSystem) {
						%>
						<option value="<%=m.getSno()%>" code="<%=m.getMenuUrl()%>"><%=m.getMenuName()%></option>
						<%
							}
							}
						%>
					</select>
					<select id="sel3" name="table">
						<option value="-1">
							请选择表名
						</option>
					</select>
					<input id="registerColumn" />
					描述:
					<input id="desc" />
					<span class="set" onclick="addColumn(this)"></span>
				</td>
			</tr>
			<tr>
				<td>
					设置列内容
				</td>
				<td>
					<select id="sel4" name="system" onchange="changeSystem(this)">
						<option value="-1">
							请选择系统
						</option>
						<%
							if (allSystem != null && allSystem.size() > 0) {
								for (Menu m : allSystem) {
						%>
						<option value="<%=m.getSno()%>" code="<%=m.getMenuUrl()%>"><%=m.getMenuName()%></option>
						<%
							}
							}
						%>
					</select>
					<select name="table" id="sel5" onchange="changeTable(this)">
						<option value="-1">
							请选择表名
						</option>
					</select>
					<select name="column" id="sel6" onchange="changeColumn(this)">
						<option value="-1">
							请选择列名
						</option>
					</select>
					列格式
					<input id="registerFormater" style="width: 300px" />
					描述:
					<input id="desc" />
					<span class="set" onclick="addFormater(this)"></span>
				</td>
			</tr>
		</table>
		<hr>
		根据维护键值对查询键值.
		 <DIV id="gridTree"></div>
	</body>
</html>
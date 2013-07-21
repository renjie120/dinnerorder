<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%> 
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>redis简易控制台</title> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/> 
<base href="<%=basePath%>"> 
<script type="text/javascript" src="<%=basePath%>/js/jquery-1.7.1.min.js"></script> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
	function init(str) {

	} 
	$(function() {
		$('#seeConfig').click(function() {
			alert(1);
			var s = $('[name=config]').val();
			$.ajax({
						url : "redisManager!getConfig.action",
						data:"config="+s,
						success : function(d) {
							 eval("var data="+d);
							 var i =0;
							 var buf = [];
							 for(var ii in data){
								 if(i%2==0){
									buf.push("<tr><td>"+data[ii]+"</td>"); 
								 }else{
									 buf.push("<td>"+data[ii]+"</td></tr>");
								 }
								 i++;
							 }
							 $('#configTable').html('').html(buf.join(''));
						}
					});
		});

		$('#exisKey').click(function() {
							$.ajax({
								url : "redisManager!exists.action",
								data : "exists="+ $('[name=exists]').val(),
								type : "post",
								success : function(d) {
									 eval("var data="+d);
									if(data.type=='string'){
										$('#existsTable').prepend('<tr><td colspan="2">'+$('[name=exists]').val()+'</td></tr><tr><td>字符串</td><td>'+data.value+'</td></tr>');
									}else if(data.type=='hash'){ 
										 var buf = [];
										 buf.push("<tr><td>类型</td><td>hash</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td>"+data.value[ii].key+"</td><td>"+data.value[ii].value+"</td></tr>");  
										 }
										 $('#existsTable').prepend("<tr><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+buf.join(''));
									}else if(data.type=='list'){ 
										 var buf = [];
										 buf.push("<tr><td>类型</td><td>list</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td colspan='2'>"+data.value[ii]+"</td></tr>");   
										 }
										 $('#existsTable').prepend("<tr><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+buf.join(''));
									}else if(data.type=='set'){ 
										 var buf = [];
										 buf.push("<tr><td>类型</td><td>set</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td colspan='2'>"+data.value[ii]+"</td></tr>");   
										 }
										 $('#existsTable').prepend("<tr><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+buf.join(''));
									}else if(data.type=='zset'){ 
										 var buf = [];
										 buf.push("<tr><td>类型</td><td>zset</td></tr><tr><td>键</td><td>分数</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td>"+data.value[ii].value+"</td><td>"+data.value[ii].score+"</td></tr>");   
										 }
										 $('#existsTable').prepend("<tr><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+buf.join(''));
									}else{
										 $('#existsTable').prepend("<tr><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+'<tr><td>没有查找到</td></tr>');
									}
								}
							});
						});

		$('#findKey').click(function() {
							$.ajax({
										url : "redisManager!keys.action",
										data : "keys=" + $('[name=keys]').val(),
										type : "post",
										success : function(d) {
											 eval("var data="+d);
											 var i =0;
											 var buf = [];
											 buf.push("<tr><td>"+$('[name=keys]').val()+"</td></tr>");
											 for(var ii in data){ 
													buf.push("<tr><td>"+data[ii]+"</td></tr>");   
											 }
											 $('#keysTable').prepend(buf.join(''));
										}
									});
						});
	});
</script>
</head>
<body>
	全部keys数量:${count}
	<hr>
	<br>
	<input name="config">
	<button id="seeConfig">查看配置信息</button>
	<br>
	<table id='configTable'></table>
	
	<hr>
	<input name="keys">
	<button id="findKey">匹配key(例如:lsq*)</button>
	<br>
	<table id='keysTable'></table>
	
	<hr>
	<input name="exists">
	<button id="exisKey">查询键值</button>
	<br>
	<table id='existsTable'></table>
</body>
</html>
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
<style type="text/css">
.title {
	font-size:14px;
	color:red;
	font-weight:bold;
}
</style>
<script type="text/javascript">
	function init(str) {

	} 
	$(function() {
			$('.title').live('click', function() { 
			  $(this).next().toggle();
			});
		$('#seeConfig').click(function() { 
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
										$('#existsTable').prepend('<tr  class="title"><td  >'+$('[name=exists]').val()+'</td><td ><img src="<%=basePath%>/jsp/onError.gif" onclick="deletethis(this)"/></td><td>重命名:<input rename id="'+_v+'" /><img src="<%=basePath%>/jsp/onError.gif" onclick="rename(this)"/></td></tr><tr><td>字符串</td><td>'+data.value+'</td></tr>');
									}else if(data.type=='hash'){ 
										 var buf = [];
										 var _v = $('[name=exists]').val();
										 buf.push("<tr><td><div><table>");
										  buf.push("<tr><td>类型11</td><td>hash</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td>"+data.value[ii].key+"</td><td>"+data.value[ii].value+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' lv='"+_v+"' onclick='removeHash(this)'/></td></tr>");  
										 }
										 buf.push('</table></div></td></tr>');
										 $('#existsTable').prepend("<tr  class='title'><td  >"+_v+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"+_v+"' /><img src='<%=basePath%>/jsp/onError.gif' onclick='rename(this)'/></td></tr>"+buf.join(''));
									}else if(data.type=='list'){ 
										 var buf = [];
										 var _v = $('[name=exists]').val();
										  buf.push("<tr><td><div><table>");
										 buf.push("<tr><td>类型</td><td>list</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td >"+data.value[ii]+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' lv='"+_v+"' onclick='removeList(this)'/></td></tr>");   
										 }
										  buf.push('</table></div></td></tr>');
										$('#existsTable').prepend("<tr class='title'><td >"+_v+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"+_v+"' /><img src='<%=basePath%>/jsp/onError.gif' onclick='rename(this)'/></td></tr>"+buf.join(''));
									}else if(data.type=='set'){ 
										 var buf = [];
										 var _v = $('[name=exists]').val();
										  buf.push("<tr><td><div><table>");
										 buf.push("<tr><td>类型</td><td>set</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td >"+data.value[ii]+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' lv='"+_v+"' onclick='removeSet(this)'/></td></tr>");   
										 }
										  buf.push('</table></div></td></tr>');
										$('#existsTable').prepend("<tr  class='title'><td >"+_v+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"+_v+"' /><img src='<%=basePath%>/jsp/onError.gif' onclick='rename(this)'/></td></tr>"+buf.join(''));
									}else if(data.type=='zset'){ 
										 var buf = [];
										 var _v = $('[name=exists]').val();
										  buf.push("<tr><td><div><table>");
										 buf.push("<tr><td>类型</td><td>zset</td></tr><tr><td>键</td><td>分数</td></tr>");
										 for(var ii in data.value){
											 	buf.push("<tr><td>"+data.value[ii].value+"</td><td>"+data.value[ii].score+"</td><td ><img src='<%=basePath%>/jsp/onError.gif' lv='"+_v+"' onclick='removeZScore(this)'/></td></tr>");   
										 }
										 buf.push('</table></div></td></tr>');
										 $('#existsTable').prepend("<tr  class='title'><td  >"+$('[name=exists]').val()
												 +"</td><td ><img src='<%=basePath%>/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"+_v+"' /><img src='<%=basePath%>/jsp/onError.gif' onclick='rename(this)'/></td></tr>"+buf.join(''));
									}else{
										 $('#existsTable').prepend("<tr  class='title'><td colspan='2'>"+$('[name=exists]').val()+"</td></tr>"+'<tr><td>没有查找到</td></tr>');
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
											 buf.push("<tr class='title'><td >查找："+$('[name=keys]').val()+"结果如下:</td></tr>");
											  buf.push("<tr><td><div><table>");
										 for(var ii in data){ 
													buf.push("<tr><td>"+data[ii]+"</td></tr>");   
											 } 
											 buf.push('</table></div></td></tr>');
										 $('#keysTable').prepend(buf.join(''));
										}
									});
						});
		
		$('#addKey').click(function() {
			var p  =[];
			if ($('#newKeyName').val() == ''
				|| $('#val1').val() == ''
				|| $('#val2:visible').val() == '') {
				alert("数据没有填写完全!");
				return false;
			}
			p.push("keytype=" + $('#keytype').val());
			p.push("newKeyName=" + $('#newKeyName').val());
			p.push("val1=" + $('#val1').val());
			p.push("val2=" + $('#val2').val());
			var param = p.join('&');
			$.ajax({
						url : "redisManager!addKey.action",
						data : param,
						type : "post",
						success : function(d) { 
							 $('#addKeyTable').prepend("<tr><td>"+d+"</td></tr>");
						}
					});
		});
	});
	
	function rename(obj){
		var v = $(obj).parent().parent().find('td').eq(0).html();
		var newV = $(obj).prev().val();
		$.ajax({
			url : "redisManager!rename.action",
			data : "keys=" + v+"&value="+newV,
			type : "post",
			success : function(d) {
				$(obj).parent().parent().find('td').eq(0).html(newV);
				$(obj).prev().val("");
			}
		}); 
	}
	function deletethis(obj){
		if(confirm('确定删除么?')){
			var v = $(obj).parent().prev().html();
			 $.ajax({
				url : "redisManager!deleteKey.action",
				data : "keys=" + v,
				type : "post",
				success : function(d) {
					 $(obj).parent().parent().remove();
				}
			}); 
		} 
	}
	
	function removeList(obj){
		if(confirm('确定删除么?')){
			var v = $(obj).parent().prev().html();
			var lv = $(obj).attr('lv');
			 $.ajax({
				url : "redisManager!removeListValue.action",
				data : "value=" + v+"&keys="+lv,
				type : "post",
				success : function(d) {
					 $(obj).parent().parent().remove();
				}
			}); 
		}
	}
	
	function removeSet(obj){
		if(confirm('确定11删除么?')){
			var v = $(obj).parent().prev().html();
			var lv = $(obj).attr('lv');
			 $.ajax({
				url : "redisManager!removeSetValue.action",
				data : "value=" + v+"&keys="+lv,
				type : "post",
				success : function(d) {
					 $(obj).parent().parent().remove();
				}
			}); 
		}
		
	}
	
	function removeZScore(obj){
		if(confirm('确定删除么?')){
			var v = $(obj).parent().prev().prev().html();
			var lv = $(obj).attr('lv');
			 $.ajax({
				url : "redisManager!removeZScore.action",
				data : "value=" + v+"&keys="+lv,
				type : "post",
				success : function(d) {
					 $(obj).parent().parent().remove();
				}
			}); 
		}
	}
	function removeHash(obj){
		if(confirm('确定删除么?')){
			var v = $(obj).parent().prev().prev().html();
			var lv = $(obj).attr('lv');
			 $.ajax({
				url : "redisManager!removeHashValue.action",
				data : "value=" + v+"&keys="+lv,
				type : "post",
				success : function(d) {
					 $(obj).parent().parent().remove();
				}
			}); 
		}
	}
	
	function changeType(){
		var tp  =$('#keytype').val();
		if(tp=='str'||tp=='strnx'||tp=='listL'||tp=='set'||tp=='listR'){
			$('#arg2').hide();
		}else{
			$('#arg2').show();
		}
		$('#val1').val('');
		$('#val2').val('');
		if(tp=='zset'){
			$('#tip2').html('必须输入数字类型!');
		}else{
			$('#tip2').html('');
		}
	}
</script>
</head>
<body>
	全部keys数量:${count}
	<hr>
	<br>
	<input name="config">
	<button id="seeConfig">查看配置</button>(例如：config*)
	<br>
	<table id='configTable'></table>
	
	<hr>
	<input name="keys">
	<button id="findKey">匹配key</button>(例如:lsq*)
	<br>
	<table id='keysTable'></table>
	
	<hr>
	<input name="exists">
	<button id="exisKey">查询键值</button>(对指定键值进行查询，删除，修改,重命名等操作)
	<br>
	<table id='existsTable'></table>
	
	<hr>
	类型=<select id="keytype" onchange="changeType()">
	<option value="str">字符串</option>
	<option value="strnx">字符串NX</option>
	<option value="hash">哈希表</option>
	<option value="hashnx">哈希表NX</option>
	<option value="listL">插入列头</option>
	<option value="listR">插入表尾</option>
	<option value="set">集合</option>
	<option value="zset">有序集合</option></select>
	<br>key=<input id="newKeyName"/><span>输入键值,有则追加或者覆盖,无则新建.</span>
	<br>val1=<input id="val1"/><span style='color:red;fontSize:10px;' id='tip2'></span>
	<span id="arg2" style="display:none"><br>val2=<input id="val2" /><span style='color:red;fontSize:8px;' id='tip2'></span></span>
	<br><button id="addKey">设置键值</button>(在原有基础上添加，无则新建,不会删除以前数据)<br>
	<table id='addKeyTable'></table>
</body>
</html>
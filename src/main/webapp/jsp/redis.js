function init(str) {

}
var _path ;
$(function() {
	_path=  $('#wpath').val(); 
	$('select').width(100).val(-1);
	$('#registerFormater').val('');
	$('.title a[.open,.close]').live('click', function() {
		$(this).toggleClass("open").toggleClass("close");
		$(this).parent().parent().next().toggle();
	});
	$('#seeConfig').click(function() {
		var s = $('[name=config]').val();
		$.ajax({
			url : "redisManager!getConfig.action",
			data : "config=" + s,
			success : function(d) {
				eval("var data=" + d);
				var i = 0;
				var buf = [];
				for ( var ii in data) {
					if (i % 2 == 0) {
						buf.push("<tr><td>" + data[ii] + "</td>");
					} else {
						buf.push("<td>" + data[ii] + "</td></tr>");
					}
					i++;
				}
				$('#configTable').html('').html(buf.join(''));
			}
		});
	});

	$('#exisKey')
			.click(
					function() {
						$
								.ajax({
									url : "redisManager!exists.action",
									data : "exists=" + $('[name=exists]').val(),
									type : "post",
									success : function(d) {
										eval("var data=" + d);
										if (data.type == 'string') {
											$('#existsTable')
													.prepend(
															'<tr  class="title"><td  ><a class="close"></a><span>'
																	+ $(
																			'[name=exists]')
																			.val()
																	+ '</span></td><td ><img src="'+_path+'/jsp/onError.gif" onclick="deletethis(this)"/></td><td>重命名:<input rename id="'
																	+ _v
																	+ '" /><a  class="set" onclick="rename(this)"></a></td></tr><tr><td>字符串</td><td>'
																	+ data.value
																	+ '</td></tr>');
										} else if (data.type == 'hash') {
											var buf = [];
											var _v = $('[name=exists]').val();
											buf.push("<tr><td><div><table>");
											buf
													.push("<tr><td>类型11</td><td>hash</td></tr>");
											for ( var ii in data.value) {
												buf
														.push("<tr><td>"
																+ data.value[ii].key
																+ "</td><td>"
																+ data.value[ii].value
																+ "</td><td ><img src='"+_path+"/jsp/onError.gif' lv='"
																+ _v
																+ "' onclick='removeHash(this)'/></td></tr>");
											}
											buf
													.push('</table></div></td></tr>');
											$('#existsTable')
													.prepend(
															"<tr  class='title'><td  ><a class='close'></a><span>"
																	+ _v
																	+ "</span></td><td ><img src='"+_path+"/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"
																	+ _v
																	+ "' /><a  class='set' onclick='rename(this)'></a></td></tr>"
																	+ buf
																			.join(''));
										} else if (data.type == 'list') {
											var buf = [];
											var _v = $('[name=exists]').val();
											buf.push("<tr><td><div><table>");
											buf
													.push("<tr><td>类型</td><td>list</td></tr>");
											for ( var ii in data.value) {
												buf
														.push("<tr><td >"
																+ data.value[ii]
																+ "</td><td ><img src='"+_path+"/jsp/onError.gif' lv='"
																+ _v
																+ "' onclick='removeList(this)'/></td></tr>");
											}
											buf
													.push('</table></div></td></tr>');
											$('#existsTable')
													.prepend(
															"<tr class='title'><td ><a class='close'></a><span>"
																	+ _v
																	+ "</span></td><td ><img src='"+_path+"/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"
																	+ _v
																	+ "' /><a  class='set' onclick='rename(this)'></a></td></tr>"
																	+ buf
																			.join(''));
										} else if (data.type == 'set') {
											var buf = [];
											var _v = $('[name=exists]').val();
											buf.push("<tr><td><div><table>");
											buf
													.push("<tr><td>类型</td><td>set</td></tr>");
											for ( var ii in data.value) {
												buf
														.push("<tr><td >"
																+ data.value[ii]
																+ "</td><td ><img src='"+_path+"/jsp/onError.gif' lv='"
																+ _v
																+ "' onclick='removeSet(this)'/></td></tr>");
											}
											buf
													.push('</table></div></td></tr>');
											$('#existsTable')
													.prepend(
															"<tr  class='title'><td ><a class='close'></a><span>"
																	+ _v
																	+ "</span></td><td ><img src='"+_path+"/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"
																	+ _v
																	+ "' /><a  class='set' onclick='rename(this)'></a></td></tr>"
																	+ buf
																			.join(''));
										} else if (data.type == 'zset') {
											var buf = [];
											var _v = $('[name=exists]').val();
											buf.push("<tr><td><div><table>");
											buf
													.push("<tr><td>类型</td><td>zset</td></tr><tr><td>键</td><td>分数</td></tr>");
											for ( var ii in data.value) {
												buf
														.push("<tr><td>"
																+ data.value[ii].value
																+ "</td><td>"
																+ data.value[ii].score
																+ "</td><td ><img src='"+_path+"/jsp/onError.gif' lv='"
																+ _v
																+ "' onclick='removeZScore(this)'/></td></tr>");
											}
											buf
													.push('</table></div></td></tr>');
											$('#existsTable')
													.prepend(
															"<tr  class='title'><td  ><a class='close'></a><span>"
																	+ $(
																			'[name=exists]')
																			.val()
																	+ "</span></td><td ><img src='"+_path+"/jsp/onError.gif' onclick='deletethis(this)'/></td><td>重命名:<input rename id='"
																	+ _v
																	+ "' />"
																	+ "<a  class='set' onclick='rename(this)'></a></td></tr>"
																	+ buf
																			.join(''));
										} else {
											$('#existsTable')
													.prepend(
															"<tr  class='title'><td colspan='2'>"
																	+ $(
																			'[name=exists]')
																			.val()
																	+ "</td></tr>"
																	+ '<tr><td>没有查找到</td></tr>');
										}
									}
								});
					});

	$('#findKey').click(			
			function() {
				var _kk = $('[name=keys]').val();
				$.ajax({
					url : "redisManager!keys.action",
					data : "keys=" + _kk,
					type : "post",
					success : function(d) {
						eval("var data=" + d);
						var i = 0;
						var buf = [];
						buf.push("<tr class='title'><td >查找："
								+ $('[name=keys]').val() + "结果如下:</td></tr>");
						buf.push("<tr><td><div><table>");
						for ( var ii in data) {
							buf.push("<tr><td><img src='"+_path+"/jsp/onError.gif' onclick='deleteQuick(this)'/><span>" + data[ii] + "</span></td></tr>");
						}
						buf.push('</table></div></td></tr>');
						$('#keysTable').prepend(buf.join(''));
					}
				});
			});

	$('#addKey').click(
			function() {
				var p = [];
				if ($('#newKeyName').val() == '' || $('#val1').val() == ''
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
						$('#addKeyTable')
								.prepend("<tr><td>" + d + "</td></tr>");
					}
				});
			});
});

function rename(obj) {
	var v = $(obj).parent().parent().find('td').eq(0).text();
	var newV = $(obj).prev().val();
	if (newV == '') {
		alert('不得命名为空!');
		return false;
	}
	$.ajax({
		url : "redisManager!rename.action",
		data : "keys=" + v + "&value=" + newV,
		type : "post",
		success : function(d) {
			$(obj).parent().parent().find('td').eq(0).find('span').text(newV);
			$(obj).prev().val("");
		}
	});
}
function deletethis(obj) {
	if (confirm('确定删除么?')) {
		var v = $(obj).parent().prev().find('span').html();
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

function deleteQuick(obj) { 
	var v = $(obj).next().html();
	$.ajax({
		url : "redisManager!deleteKey.action",
		data : "keys=" + v,
		type : "post",
		success : function(d) {
			$(obj).parent().parent().remove();
		}
	}); 
}

function removeList(obj) {
	if (confirm('确定删除么?')) {
		var v = $(obj).parent().prev().html();
		var lv = $(obj).attr('lv');
		$.ajax({
			url : "redisManager!removeListValue.action",
			data : "value=" + v + "&keys=" + lv,
			type : "post",
			success : function(d) {
				$(obj).parent().parent().remove();
			}
		});
	}
}

function removeSet(obj) {
	if (confirm('确定11删除么?')) {
		var v = $(obj).parent().prev().html();
		var lv = $(obj).attr('lv');
		$.ajax({
			url : "redisManager!removeSetValue.action",
			data : "value=" + v + "&keys=" + lv,
			type : "post",
			success : function(d) {
				$(obj).parent().parent().remove();
			}
		});
	}

}

function removeZScore(obj) {
	if (confirm('确定删除么?')) {
		var v = $(obj).parent().prev().prev().html();
		var lv = $(obj).attr('lv');
		$.ajax({
			url : "redisManager!removeZScore.action",
			data : "value=" + v + "&keys=" + lv,
			type : "post",
			success : function(d) {
				$(obj).parent().parent().remove();
			}
		});
	}
}
function removeHash(obj) {
	if (confirm('确定删除么?')) {
		var v = $(obj).parent().prev().prev().html();
		var lv = $(obj).attr('lv');
		$.ajax({
			url : "redisManager!removeHashValue.action",
			data : "value=" + v + "&keys=" + lv,
			type : "post",
			success : function(d) {
				$(obj).parent().parent().remove();
			}
		});
	}
}

function changeType() {
	var tp = $('#keytype').val();
	if (tp == 'str' || tp == 'strnx' || tp == 'listL' || tp == 'set'
			|| tp == 'listR') {
		$('#arg2').hide();
	} else {
		$('#arg2').show();
	}
	$('#val1').val('');
	$('#val2').val('');
	if (tp == 'zset') {
		$('#tip2').html('必须输入数字类型!');
	} else {
		$('#tip2').html('');
	}
}

function addSystem(obj) {
	var _s = $(obj).parent().find('#registerSystem');
	var system = _s.val();
	var desc = $(obj).prev().val();
	if (system == '') {
		alert('必填！');
		return false;
	}
	$.ajax({
		url : "redisManager!regiestSystem.action",
		data : "system=" + system + "&desc=" + desc,
		type : "post",
		success : function(d) {
			eval("var _d = " + d); 
			if (_d.result == 1) {
				$('select[name=system]').append(
						"<option value='" + _d.code + "' code='"+system+"'>" + desc
								+ "</option>");
				_s.val('');
				$(obj).prev().val('');
				$('select').val(-1);
			} else {
				alert('添加失败!');
			}
		}
	});
}

function addTable(obj) {
	var _s = $(obj).parent().find('#registerTable');
	var tbname = _s.val();
	var desc = $(obj).prev().val();
	var system = $(obj).parent().find('#sel1').val();
	if (tbname == '' || system == '-1') {
		alert('必填！');
		return false;
	}
	$.ajax({
		url : "redisManager!regiestTable.action",
		data : "system=" + system + "&tbname=" + tbname + "&desc=" + desc,
		type : "post",
		success : function(d) {
			eval("var _d = " + d);
			if (_d.result == 1) {
				$('select[name=table]').append(
						"<option value='" + _d.code + "' code='"+tbname+"'>" + desc
								+ "</option>");
				_s.val('');
				$(obj).prev().val(''); 
				$('select').val(-1);
			} else {
				alert('添加失败!');
			}
		}
	});
}

function addColumn(obj) {
	var _s = $(obj).parent().find('#registerColumn');
	var columnName = _s.val();
	var desc = $(obj).prev().val();
	var system = $(obj).parent().find('#sel2').val();
	var tbname = $(obj).parent().find('#sel3').val();
	if (columnName == '' || system == '-1' || tbname == '-1') {
		alert('必填！');
		return false;
	}
	$.ajax({
		url : "redisManager!regiestColumn.action",
		data : "system=" + system + "&tbname=" + tbname + "&columnName="
				+ columnName + "&desc=" + desc,
		type : "post",
		success : function(d) {
			eval("var _d = " + d);
			if (_d.result == 1) {
				$('select[name=column]').append(
						"<option value='" + _d.code + "' code='"+columnName+"'>" + desc
								+ "</option>");
				_s.val('');
				$(obj).prev().val(''); 
				$('select').val(-1);
			} else {
				alert('添加失败!');
			}
		}
	});
}

function addFormater(obj) {
	var _s = $(obj).parent().find('#registerFormater');
	var formater = _s.val();
	var desc = $(obj).prev().val();
	var system = $(obj).parent().find('#sel4').val();
	var tbname = $(obj).parent().find('#sel5').val();
	var columnName = $(obj).parent().find('#sel6').val();
	if (formater == '' || columnName == '-1' || system == '-1'
			|| tbname == '-1') {
		alert('必填！');
		return false;
	}
	$.ajax({
		url : "redisManager!regiestFormat.action",
		data : "system=" + system + "&tbname=" + tbname + "&columnName="
				+ columnName + "&formater=" + formater + "&desc=" + desc,
		type : "post",
		success : function(d) {
			eval("var _d = " + d);
			if (_d.result == 1) {
				_s.val('');
				$(obj).prev().val(''); 
				$('select').val(-1);
			} else {
				alert('添加失败!');
			}
		}
	});
}

//改变系统的时候自动添加对应的表到下拉菜单中.
function changeSystem(obj){
	if(obj.value!=-1)
	$.ajax({
		url : "redisManager!getTableBySystem.action",
		data : "systemId=" + obj.value ,
		type : "post",
		success : function(d) {  
			$(obj).next().html('<option value="-1">请选择表名</option>'+d);  
		}
	});
}

function changeTable(obj){ 
	var systemId = $(obj).prev().val(); 
	if(systemId!='-1'&&obj.value!=-1)
	$.ajax({
		url : "redisManager!getColumnByTable.action",
		data : "systemId=" + systemId+"&tableId="+ obj.value,
		type : "post",
		success : function(d) {   
			$(obj).next().html('<option value="-1">请选择列名</option>'+d);  
		}
	});
}

function changeColumn(obj){ 
	var systemId = $(obj).prev().prev(); 
	var tableId = $(obj).prev(); 
	if(systemId.val()!='-1'&&obj.value!=-1&&tableId.val()!=-1)
	{
		$('#registerFormater').val(systemId.find('option:checked').attr('code')+":"+tableId.find('option:checked').attr('code')+":"+$(obj).find('option:checked').attr('code')+":{id}");
	}
	else {
		$('#registerFormater').val('');
	}
} 
 
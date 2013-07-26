/**
 * 充值.
 */
function saveRecharge() {
	var p = [];
	if ($('#rechargeMoneyTime').val() == '' || $('#rechargeMoney').val() == ''
			|| $('#rechargePeopleList').val() == '-1') {
		alert("数据没有填写完全!");
		return false;
	}
	p.push("rechargeMoneyTime=" + $('#rechargeMoneyTime').val());
	p.push("groupSno=" + $('#groupSno').val());
	p.push("rechargePeopleList=" + $('#rechargePeopleList').val());
	p.push("rechargeMoney=" + $('#rechargeMoney').val());
	if (isNaN($('#rechargeMoney').val())) {
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

function findVByName(v, dName) {
	var result = -1;
	$('#' + v + ' option').each(function() {
		if ($(this).html() == dName) {
			result = $(this).val();
			return;
		}
	});
	return result;
}
// 下订单.
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
	if (isNaN($('#money').val())) {
		alert("请输入有效金额!");
		return false;
	}
	var dName = $('#dinnerName').val();
	if (dName == '' && $('#dinnerNameList').val() == '-1') {
		alert("必须填写菜名或者选择菜名!");
		return false;
	}
	if (dName != '') {
		var _v = findVByName('dinnerNameList', dName);
		if (_v != -1) {
			$('#dinnerNameList').val(_v);
			$('#dinnerName').val('');
		}
	}
	p.push("moneyTime=" + $('#moneyTime').val());
	p.push("groupSno=" + $('#groupSno').val());
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
	window
			.open(
					'dinner!goUrl.action?id=' + sno,
					'newwindow',
					'height=500, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto,resizable=no,location=no, status=no');
	location.reload();
}
function setAvg(obj, t, gSno) {
	var v = $(obj).prev().val();
	if (isNaN(v)) {
		alert('必须输入数字类型');
		return false;
	}
	var p = [];
	var param = p.join('&');
	$.ajax({
		url : "dinner!setAvg.action",
		data : "money=" + v + "&moneyTime=" + t + "&groupSno=" + gSno,
		type : 'POST',
		dataType : 'json',
		success : function(x) {
			alert(x);
		},
		error : function(x, textStatus, errorThrown) {
			alert(x.responseText);
		}
	});
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
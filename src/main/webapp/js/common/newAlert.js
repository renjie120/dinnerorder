
/**
 * 
 * 重写window.alert/ window.confirm mender:Du DongHui version:1.0 source:network
 * author:dimness
 */

var alternateFrame = null;// 生成的iframe
var alternateWin = null;

//如果是ie就进行替换！
if(jQuery.browser.msie){
window.alert = showAlert;
window.confirm = showConfirm;
}

/**
 * 人机交互窗口，覆盖自带的
 */
function alternateWindow() {
	this.win = null;// 生成对话框的窗口对象
	this.pBody = null;// 生成的body容器对象
	this.pBg = null;
	this.type = "alert";// 默认的种类是alert
	this.FocusWhere = "_OK";// 焦点在哪个按钮上
}
/**
 * 模仿的alert窗口
 */
function showAlert(info) {
	// alert(typeof(info));
	var _info = info;
	if (typeof(info) == 'undefined') {
		// alert(222);
		_info = '';
	}
	if (!info) {
		_info = '';
	}
	alternateWin = new alternateWindow();
	var pBody = alternateWin.init();
	alternateWin.initAlertBody(pBody, _info);
	alternateWin.type = "alert";
}
/**
 * 模仿的confirm窗口
 */
function showConfirm(info, ok_func, notok_func, ok_str, not_okstr) {
	var _info = info;
	if (typeof(info) == 'undefined') {
		// alert(222);
		_info = '';
	}
	if (!info) {
		_info = '';
	}

	alternateWin = new alternateWindow();
	var pBody = alternateWin.init();
	alternateWin.initConfirmBody(pBody, info, ok_func, notok_func, ok_str,
			not_okstr);
	alternateWin.type = "confirm";
}
/**
 * 作用：初始基本信息
 */
alternateWindow.prototype.init = function() {
	if (alternateFrame == null) {
		alternateFrame = document
				.createElement("<iframe allowTransparency='true' id='popframe' frameborder=0 marginheight=0 src='about:blank' marginwidth=0 hspace=0 vspace=0 scrolling=no></iframe>")
		alternateFrame.style.position = "absolute";
		document.body.appendChild(alternateFrame);
	} else {
		alternateFrame.style.visibility = "visible";
	}
	alternateFrame.style.width = screen.availWidth;
	alternateFrame.style.height = document.body.scrollHeight;
	// screen.availHeight;

	alternateFrame.style.left = document.body.scrollLeft;
	alternateFrame.style.top = document.body.scrollTop;
	alternateFrame.name = alternateFrame.uniqueID;

	this.win = window.frames[alternateFrame.name];
	this.win.document
			.write("<body leftmargin=0 topmargin=0 oncontextmenu='self.event.returnValue=false'><div id=popbg></div><div id=popbody></div><div></div></body>");
	this.win.document.body.style.backgroundColor = "transparent";
	document.body.style.overflow = "hidden";
	this.pBody = this.win.document.body.children[1];
	this.pBg = this.win.document.body.children[0];
	this.hideAllSelect();
	this.hideAllUL();
	this.initBg();

	return this.pBody;
}

/**
 * 作用：初始化背景层
 */
alternateWindow.prototype.initBg = function() {
	with (this.pBg.style) {
		position = "absolute";
		left = "0";
		top = "0";
		width = "100%";
		height = "100%";
		visibility = "hidden";
		backgroundColor = "#333333";
		filter = "blendTrans(duration=1) alpha(opacity=30)";
	}
	this.pBg.filters.blendTrans.apply();
	this.pBg.style.visibility = "visible";
	this.pBg.filters.blendTrans.play();
}
/**
 * 作用：初始化显示层
 */
alternateWindow.prototype.initAlertBody = function(obj, info) {
	with (obj.style) {
		position = "absolute";
		width = "400";
		height = "110";
		backgroundColor = "#999999";
	}
	obj.style.left = window.document.body.clientWidth / 2 - 200;
	obj.style.top = window.document.body.clientHeight / 3;
	var str;
	if (info == "") {
		info = "对不起，此对象未定义！！！";
	}
	str = "<table border=0 cellpadding=0 cellspacing=0 width=100% height=100%><tr height=20>";
	str += "<td align=left style='border-left:1px solid #999;border-right:1px solid #999;border-top:1px solid #999;color:#ffffff;font-size:12px;font-weight:bold;background:#cccbcb url(../_themes/default/images/window/ow_title_bg.jpg) no-repeat;'>温馨提示</td></tr>";
	str += "<tr><td align=center bgcolor=#ffffcc style='border-left:1px solid #999;border-right:1px solid #999;font-size:12px;color:#000000;vertical-align: middle;'>";
	str += info
			+ "</td></tr><tr height=30 bgcolor=#ffffcc><td align=center style='border-left:1px solid #999;border-right:1px solid #999;border-bottom:1px solid #999;'>"
			+ "<input style='' type='button' value=' 确定 ' id='_OK'"
			+ " onkeydown='parent.alternateWin.onKeyDown(event,this)'"
			+ " onclick='parent.alternateWin.closeWin()' style='border:solid 1px #cccccc;background:#ffffcc'>"
			+ "</td></tr></table>";
	obj.innerHTML = str;
	this.win.document.body.all._OK.focus();
	this.FocusWhere = "_OK";
}

alternateWindow.prototype.onKeyDown = function(event, obj) {
	// alert(event.keyCode);
	switch (event.keyCode) {
		case 9 :
			event.keyCode = -1;
			if (this.type == "confirm") {
				if (this.FocusWhere == "_OK") {
					this.win.document.body.all.NO.focus();
					this.FocusWhere = "NO";
				} else {
					this.win.document.body.all._OK.focus();
					this.FocusWhere = "_OK";
				}
			}
			break;
		case 13 :
			obj.click();
			break;
		case 27 :
			this.closeWin();
			break;
	}

}
/**
 * 作用：初始化显示层 conFirm提示层
 */
alternateWindow.prototype.initConfirmBody = function(obj, info, ok_func,
		notok_func, ok_str, notok_str) {
	with (obj.style) {
		position = "absolute";
		width = "400";
		height = "150";
		backgroundColor = "#ffffff";
	}
	if (ok_str == null) {
		ok_str = " 确定 ";
	}
	if (notok_str == null) {
		notok_str = " 取消 "
	}
	// default ok_func
	if (!ok_func) {
		ok_func = "_ok";
	}
	if (!notok_func) {
		notok_func = "_notok";
	}
	// default notok_func
	obj.style.left = window.document.body.clientWidth / 2 - 200;
	obj.style.top = window.document.body.clientHeight / 3;
	var str;
	str = "<table border=0 cellpadding=0 cellspacing=0 width=100% height=100%><tr height=20>";
	str += "<td align=left style='border-left:1px solid #999;border-right:1px solid #999;border-top:1px solid #999;color:#ffffff;font-size:12px;font-weight:bold;background:#cccbcb url(../_themes/default/images/window/ow_title_bg.jpg) no-repeat;'>温馨提示</td></tr>";
	str += "<tr><td align=center bgcolor=#ffffcc style='border-left:1px solid #999;border-right:1px solid #999;font-size:12px;color:#000000;vertical-align: middle;'>";
	str += info
			+ "</td></tr><tr height=30 bgcolor=#ffffcc ><td align=center style='border-left:1px solid #999;border-right:1px solid #999;border-bottom:1px solid #999;'>"
			+ "<input type='button' id='_OK'"
			+ " onkeydown='parent.alternateWin.onKeyDown(event,this)'"
			+ " onclick='parent.alternateWin.closeWin();parent."
			+ ok_func
			+ "();' "
			+ " value='"
			+ ok_str
			+ "' style='border:solid 1px #cccccc;background:#ffffcc'>"
			+ "&nbsp;&nbsp;&nbsp;<input type='button' value='"
			+ notok_str
			+ "' id='NO'"
			+ " onkeydown='parent.alternateWin.onKeyDown(event,this)'"
			+ " onclick='parent.alternateWin.closeWin();"
			+ " parent."
			+ notok_func
			+ "();' style='border:solid 1px #cccccc;background:#ffffcc'></td></tr></table>";
	obj.innerHTML = str;
	this.win.document.body.all._OK.focus();
}

function _ok() {
	alert('ok!');
}

function _notok() {
	alert('not ok!');
}

/**
 * 作用：关闭一切
 */
alternateWindow.prototype.closeWin = function() {
	alternateFrame.style.visibility = "hidden";
	this.showAllSelect();
	this.showAllUL();
	document.body.style.overflow = "auto";

}

/**
 * 作用:隐藏所有的select
 */
alternateWindow.prototype.hideAllSelect = function() {
	var obj;
	obj = document.getElementsByTagName("SELECT");
	var i;
	for (i = 0; i < obj.length; i++)
		obj[i].style.visibility = "hidden";
}
/**
 * 显示所有的select
 */
alternateWindow.prototype.showAllSelect = function() {
	var obj;
	obj = document.getElementsByTagName("SELECT");
	var i;
	for (i = 0; i < obj.length; i++)
		obj[i].style.visibility = "visible";
}

/**
 * 作用:隐藏所有的UL
 */
alternateWindow.prototype.hideAllUL = function() {
	var obj;
	obj = document.getElementsByTagName("UL");
	var i;
	for (i = 0; i < obj.length; i++)
		obj[i].style.visibility = "hidden";
}
/**
 * 显示所有的UL
 */
alternateWindow.prototype.showAllUL = function() {
	var obj;
	obj = document.getElementsByTagName("UL");
	var i;
	for (i = 0; i < obj.length; i++)
		obj[i].style.visibility = "visible";
}
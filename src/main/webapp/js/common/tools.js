function openNewWin(strUrl, strStyle) {
	if (!isNaN(strStyle)) {
		strStyle = "scroll=yes;status=no;resizable=yes;Minimize=yes;Maximize=yes;center=yes;dialogWidth=" + (window.screen.width * 0.6) + "px;dialogHeight=" + strStyle + "px; help=no";
	} else {
		if ("fullscreen" == strStyle) {
			strStyle = "scroll=yes;status=no;resizable=yes;Minimize=yes;Maximize=yes;center=yes;dialogWidth=" + window.screen.width + "px;dialogHeight=" + window.screen.height + "px; help=no";
		} else {
			if (typeof strStyle == "null" || typeof strStyle == "undefined" || strStyle == "") {
				strStyle = "scroll=yes;status=no;resizable=yes;Minimize=yes;Maximize=yes;center=yes;dialogWidth=" + (window.screen.width * 0.6) + "px;dialogHeight=" + (window.screen.height * 0.6) + "px; help=no";
			} else {
				strStyle = strStyle + "help=no;resizable=yes;Minimize=yes;Maximize=yes;";
			}
		}
	}
	var date = Date.parse(new Date());
	timestamp = date / 1000;
	if (strUrl.indexOf("?") != -1) {
		return window.showModalDialog(strUrl + "&timestamp=" + timestamp, window, strStyle);
	} else {
		return window.showModalDialog(strUrl + "?timestamp=" + timestamp, window, strStyle);
	}
}

//单选的类型树。
function getTallyTypeTree(idDom,nameDom) {
	var ans = openNewWin(appPath+"/js/xyTree/MoneyTypeTree.jsp","Maximize=yes;center=yes;dialogWidth=300px;dialogHeight=300px;help=no");
	var arr = [];
	if(typeof ans!='undefined'){
		if(ans!='undefined'&&ans.indexOf('$')!=-1)
			{
			arr = ans.split('$');
			if(document.getElementById(idDom))
				document.getElementById(idDom).value=arr[0];
			if(document.getElementById(nameDom))
				document.getElementById(nameDom).value=arr[1];
		}
	}
}

//多选的类型树。
function getMultiTallyTypeTree(idDom,nameDom) {
	var ans = openNewWin(appPath+"/js/xyTree/MultiMoneyTypeTree.jsp","Maximize=yes;center=yes;dialogWidth=300px;dialogHeight=300px;help=no");
	var arr = [];
	if(typeof ans!='undefined'){
		if(ans!='undefined'&&ans.indexOf('$')!=-1)
			{
			arr = ans.split('$');
			if(document.getElementById(idDom))
				document.getElementById(idDom).value=arr[0];
			if(document.getElementById(nameDom))
				document.getElementById(nameDom).value=arr[1];
		}
	}
}

//得到字符串的实际长度，汉字算2
String.prototype.LengthW = function()
{
        return this.replace(/[^\x00-\xff]/g,"**").length;
}


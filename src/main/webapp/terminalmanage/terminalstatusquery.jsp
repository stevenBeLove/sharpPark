<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style>
.datagrid-cell-rownumber {
	width: 50px;
	text-align: center;
	margin: 0px;
	padding: 3px 0px;
	color: #000;
}

.datagrid-header-rownumber {
	width: 50px;
	text-align: center;
	margin: 0px;
	padding: 3px 0px;
}
</style>
<script type="text/javascript">
	$(window).resize(function() {
		$('#viewTerminalstatus').datagrid('resize', {
			width : cs()
		});
	});
	var num = 0;
	function cs() {
		return $(window).width() - 8;
	}

	function checkTerminal() {
		var rows = $('#viewTerminalstatus').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要更改状态的状态");
			return;
		}
		/* 
		var terminalCode='';
		for ( var i = 0; i < rows.length; i++) {
			if(rows[i].terminalStatus=='1'){
				terminalCode+= rows[i].terminalCode ;
			}
		} */

		$('#btnCheckSuc').linkbutton('disable');

		if (num < 1) {
			return;
		}
		var terminalCode = $("#terminalCode").val();
		terminalCode = terminalCode.substring(0, 15)
		$.post('${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalstatus', {
			terminalCode : terminalCode
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");

	}

	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$.close();
		$('#btnCheckSuc').linkbutton('enable');
		$('#btnCheckFail').linkbutton('enable');
		$.viewTerminalexamine();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#btnCheckSuc').linkbutton('enable');
		$('#btnCheckFail').linkbutton('enable');
		$.close();
	};
	$.close = function() {
		$.hideDivShade();
	};

	//加载数据表格
	$.viewTerminalstatus = function() {
		var agencyNameId = $("#agencyName").combotree("getValues");
		var startDate = $("#yearmonthdatestart").val();
		var endDate = $("#yearmonthdateend").val();
		if (agencyNameId.length > 0 ) {
			for (var i = 0; i < agencyNameId.length; i++) {
				if ($.inArray(agencyNameId[i], agencyNameId) < 0) {
					agencyNameId.push(agencyNameId[i]);
				}
			}
		} else {
			agencyNameId = agencyNameId.concat(agencyNameId);
		}

		$('#viewTerminalstatus').datagrid({
			title : '终端数量查询',
			width : $(window).width() - 8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns : false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageStatusByAgencyId",
			queryParams : {
				agencyId : agencyNameId,
				startDate : startDate,
				endDate : endDate
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess : function(data) {
				num = data.total;
			},
			columns : [ [ {
				field : "agencyName",
				title : "机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "selfCount",
				title : "当前机构终端量",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "selfCountActivity",
				title : "当前机构激活终端量",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "childCount",
				title : "归属机构终端量",
				width : 300,
				align : "center",
				sortable : true
			}, {
				field : "childCountActivity",
				title : "归属机构激活终端量",
				width : 200,
				align : "center",
				sortable : true
			}, ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		//分页
		var p = $('#viewTerminalstatus').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});

	};
	function textToId(agencyCode) {
		agencyCode.text = agencyCode.id;
		if (agencyCode.children) {
			for (var i = 0; i < agencyCode.children.length; i++) {
				textToId(agencyCode.children[i]);
			}
		}
	}

	$(function() {
		/* 		$.ajax({
		 url : '${ctx}/agency/agency.do?method=getAgencyTree',
		 method : 'get',
		 dataType : 'json',
		 onBeforeExpand:function(node){
		 $("#").combotree("tree").tree("options").url='?a='+node.id
		 },
		 success : function(data) {
		 $('#agencyName').combotree('loadData', data);
		 textToId(data[0]);
		 $('#agencyId').combotree('loadData', data);
		 }
		 });
		 */

		$("#agencyName").combotree({
			 
			url : '${ctx}/agency/agency.do?method=getChildAgencyList&agencyId=0',
			onBeforeExpand : function(node) {
				console.info(node);
				$("#agencyName").tree("options").url = '${ctx}/agency/agency.do?method=getChildAgencyList&agencyId=' + node.id;
			},
			loadFilter : function(data) {
				if (data.msg) {
					return data.msg;
				} else {
					return data;
				}
			},
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});

	 
		

	})
	$.terminalCountExport = function() {
		var agencyNameId = $("#agencyName").combotree("getValues");
		var startDate = $("#yearmonthdatestart").val();
		var endDate = $("#yearmonthdateend").val();
		if (agencyNameId.length == 0 ) {
			$.messager.alert("提示 ", "请选择要导出的机构");
			return false;
		}
		if (agencyNameId.length > 0 ) {
			for (var i = 0; i < agencyNameId.length; i++) {
				if ($.inArray(agencyNameId[i], agencyNameId) < 0) {
					agencyNameId.push(agenagencyNameIdcyId[i]);
				}
			}
		} else {
			agencyNameId = agencyNameId.concat(agencyNameId);
		}

		$.getToPost("${ctx}/terminalmanage/terminalmanage.do?method=terminalCountExport", {
			agencyId : agencyNameId,
			startDate : startDate,
			endDate : endDate
		});
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="left">机构名称：</td>
			<td align="left">
				<input class="easyui-combotree" id="agencyName" style="width: 300px;" />

			</td>
		</tr>
		<tr>
			<td align="left">创建日期：</td>
			<td align="left">
				<input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}"
					style="width: 110px; height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" />
				-
				<input id="yearmonthdateend" name="yearmonthdateend" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}"
					style="width: 110px; height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})" />
			</td>
			<td />
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalstatus()">查询</a>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.terminalCountExport()">导出</a>
			</td>
		</tr>
	</table>
	<table id="viewTerminalstatus"></table>

</body>
</html>
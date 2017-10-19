<%@page import="com.compass.utils.ConstantUtils"%>
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
	var operFlag = false;
	$(window).resize(function() {
		$('#viewTerminalDeal').datagrid('resize', {
			width : cs()
		});
	});

	function cs() {
		return $(window).width() - 8;
	}

	$(function() {
		$('#activateStandard').combobox({
			url : "${ctx}/terminaldeal/terminaldeal.do?method=getActivateStandard&flag=1",
			valueField : "id",
			textField : "text"
		});
		$('#dealTypeId').combobox({
			url : "${ctx}/dealtype/dealtype.do?method=getCombDealTypes&flag=1",
			valueField : "id",
			textField : "text",
			textField : 'text',
			panelHeight : 'auto',
			multiple : true
		});

		$('#flagStatus').combobox({
			onSelect : function(row) {
				var valArr = $('#flagStatus').combobox('getValues');
				if ("0" != row.value && -1 != $.inArray("0", valArr)) {
					$.messager.alert("提示 ", "未标记状态不可与其它标记状态同时查询!");
					$(this).combobox('unselect', row.value);
					return;
				} else if ("0" == row.value && valArr.length > 1) {
					$.messager.alert("提示 ", "未标记状态不可与其它标记状态同时查询!");
					$(this).combobox('unselect', row.value);
					return;
				}
			}
		});
		$('#agencytreeId').combotree({
			url : "${ctx}/agency/agency.do?method=getAgencyTree&flag=profit",
			valueField : "id",
			textField : "text",
			panelWidth : 350
		});

		$(".combo-text").bind("input onchange", function(a, b) {
			$('#agencytreeId').combotree('tree').tree("search", $(this).val());
			if ($(this).val() == "" || null == $(this).val()) {
				$('#agencytreeId').combotree('tree').tree("collapseAll");
			}
		});

		var myDate = new Date();
		var months = myDate.getMonth() + 1;
		var day = myDate.getDate();

		if (months < 10) {
			months = "0" + months;
		}
		if (day < 10) {
			day = "0" + day;
		}
		var yearmonthdatestart = myDate.getFullYear() + '' + months + day;
		$("#yearmonthdatestart").val(yearmonthdatestart);
		var yearmonthdateend = myDate.getFullYear() + '' + months + day;
		$("#yearmonthdateend").val(yearmonthdateend);
	});

	//加载数据表格
	$.viewTerminalDeal = function() {
		var startDate = $('#yearmonthdatestart').val();
		var endDate = $('#yearmonthdateend').val();
		var activateStandard = $("#activateStandard").combobox('getValue');
		var dealTypeId ="'"+$('#dealTypeId').combobox('getValues').join("','")+"'";
		var startCode = $("#startCode1").val();
		var endCode = $("#endCode1").val();
		var agencyId = $("#agencytreeId").combotree('getValue');
		var agencyName = $("#agencyName").val();
		var flagStatus = $("#flagStatus").combobox('getValue');
		var activeDateStart = $("#activeDateStart").val();
		var activeDateEnd = $("#activeDateEnd").val();
		var customerId = $.trim($("#customerId").val());
		if (dealTypeId == null || $.trim(dealTypeId) == '-1') {
			dealTypeId = "";
		}
		if (flagStatus == null || $.trim(flagStatus) == '-1') {
			flagStatus = "";
		}
		if (agencyId == null || $.trim(agencyId) == '-1') {
			agencyId = "";
		}

		if (activateStandard == null || $.trim(activateStandard) == '-1') {
			activateStandard = "";
		}
		if (activeDateStart == activeDateEnd) {
			activeDateStart = "";
			activeDateEnd = "";
		}

		if ($.trim(endCode) != "" || $.trim(startCode) != "") {
			if ($.trim(startCode) != "" && ($.trim(startCode).length < 15 || $.trim(startCode).length > 16)) {
				$.messager.alert("提示 ", "开始终端编号请输入15位或16位");
				return false;
			}
			if ($.trim(endCode) != "" && ($.trim(endCode).length < 15 || $.trim(endCode).length > 16)) {
				$.messager.alert("提示 ", "结束终端编号请输入15位或16位");
				return false;
			} else if ($.trim(endCode) == "" || $.trim(startCode) == "") {
				$.messager.alert("提示 ", "请同时输入开始编号和结束编号");
				return false;
			}
		}

		$('#viewTerminalDeal').datagrid({
			title : '终端交易额查询',
			width : $(window).width() - 8,
			height : $(window).height() * 0.85,
			pageSize : 50,
			pageNumber : 1,
			fitColumns : false,
			url : "${ctx}/terminaldeal/terminaldeal.do?method=getOldTerminalDeal",
			queryParams : {
				startDate : startDate,
				endDate : endDate,
				activateStandard : activateStandard,
				dealTypeId : dealTypeId,
				startCode : startCode,
				endCode : endCode,
				agencyId : agencyId,
				agencyName : agencyName,
				flagStatus : flagStatus,
				activeDateStart : startDate,
				activeDateEnd : endDate,
				customerId : customerId
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : 'ck',
				checkbox : true
			}, {
				field : "serialId",
				title : " 序列号",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "startDate",
				title : "开始时间",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "endDate",
				title : "截止时间",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "agencyName",
				title : "所属机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "terminalCode",
				title : "终端编号",
				width : 150,
				align : "center",
				sortable : true
			}, {
				field : "customerId",
				title : "客户编号",
				width : 150,
				align : "center",
				sortable : true
			}, {
				field : "transAmount",
				title : "交易金额",
				width : 120,
				align : "center",
				sortable : true
			}, {
				field : "flag",
				title : "不可选原因 ",
				width : 120,
				align : "center",
				sortable : true,
				formatter : function(value, row, index) {
					if (value == '1') {
						return " ";
					} else {
						return "用户重复";
					}
				}
			}, {
				field : "flagStatus",
				title : "标记状态",
				width : 100,
				align : "center",
				sortable : true,
				formatter : function(value, row, index) {
					if (value == '0') {
						return "未标记";
					} else if (value == '1') {
						return "已标记";
					} else if (value == '2') {
						return "已分配";
					} else {
						return "未标记";
					}
				}
			}, {
				field : "standardDate",
				title : "达标时间",
				width : 120,
				align : "center",
				sortable : true
			}, {
				field : "standardAmount",
				title : "达标金额",
				width : 120,
				align : "center",
				sortable : true
			}, {
				field : "flagOperator",
				title : "标记操作人",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "flagOperTime",
				title : "标记时间",
				width : 180,
				align : "center",
				sortable : true
			} ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			onLoadSuccess : function(data) {
				if (data) {
					$.each(data.rows, function(index, item) {
						if (item.flag == '0') {
							debugger;
							$('#tDeal input:checkbox').eq(index + 1).attr("disabled", 'disabled');
							$('#tDeal input:checkbox').eq(index + 1).attr("readonly", 'readonly');
						}
					});
				}
				$('input:checkbox[name!="ck"]').unbind();
				//重写DataGrid全选复选框事件
				$('input:checkbox[name!="ck"]').bind("click", function() {
					if ($('input:checkbox[name!="ck"]').prop("checked")) {
						//遍历复选框组，符合条件的不选中
						$('input:checkbox[name="ck"]').each(function(index, el) {
							if (el.disabled) {
								el.checked = false;
							} else {
								el.checked = true;
							}
						});
					} else {
						$('input:checkbox[name="ck"]').each(function(index, el) {
							el.checked = false;
						});
					}
				});
			},
			toolbar : [ {
				id : 'terminalBatchFlag',
				text : '批量标记|取消',
				iconCls : 'icon-redo',
				handler : function() {
					$.bashFlagStatus();
				}
			}, {
				id : 'terminalBatchDistribute',
				text : '批量分配',
				iconCls : 'icon-redo',
				handler : function() {
					$.bashFlagDistribute();
				}
			} ],

		});

		if (flagStatus == 2) {
			$('#terminalBatchFlag').linkbutton('disable');
			$('#terminalBatchDistribute').linkbutton('disable');
		}
		if (flagStatus == 0) {
			$('#terminalBatchDistribute').linkbutton('disable');
		}

		//分页
		var p = $('#viewTerminalDeal').datagrid('getPager');
		$(p).pagination({

			pageList : [ 50, 100, 500, 1000 ],
			beforePageText : '第',
			afterPageText : '页	   共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录	 共 {total} 条记录'
		});

	};

	//批处理标记终端，取消标记终端的操作
	$.bashFlagStatus = function() {
		debugger;
		var rows = $('#viewTerminalDeal').datagrid('getChecked');
		debugger;
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要操作的终端记录!");
			return false;
		}
		var idStr = "";
		var terminalCodeStr = "";
		for (var i = 0; i < rows.length; i++) {
			if ("2" == rows[i].flagStatus) {
				$.messager.alert("提示 ", "已分配不能进行操作！");
				return false;
			}
			if (i == rows.length - 1) {
				idStr = idStr + "'" + rows[i].serialId + "'";
				terminalCodeStr = terminalCodeStr + "'" + rows[i].terminalCode + "'";
			} else {
				idStr = idStr + "'" + rows[i].serialId + "',";
				terminalCodeStr = terminalCodeStr + "'" + rows[i].terminalCode + "',";
			}
		}

		var activateStandard = $("#activateStandard").combobox("getValue");
		var startDate = $('#yearmonthdatestart').val();
		var endDate = $('#yearmonthdateend').val();
		var activateStandard = $("#activateStandard").combobox('getValue');
		var dealTypeId = $('#dealTypeId').combobox('getValues').join(",");
		var startCode = $("#startCode1").val();
		var endCode = $("#endCode1").val();
		var agencyId = $("#agencytreeId").combotree('getValue');
		var agencyName = $("#agencyName").val();
		var flagStatus = $("#flagStatus").combobox('getValue');
		debugger;
		if ("0" == flagStatus && (activateStandard == null || $.trim(activateStandard) == '-1')) {
			$.messager.alert("提示 ", "激活标准必填项");
			return false;
		}
		if (dealTypeId == null || $.trim(dealTypeId) == '-1') {
			dealTypeId = "";
		}
		if (flagStatus == null || $.trim(flagStatus) == '-1') {
			flagStatus = "";
		}
		if (agencyId == null || $.trim(agencyId) == '-1') {
			agencyId = "";
		}
		if (activateStandard == null || $.trim(activateStandard) == '-1') {
			activateStandard = "";
		}
		if ($.trim(startCode) == "" && $.trim(endCode) != "") {
			$.messager.alert("提示 ", "请同时输入开始编号和结束编号");
			return false;
		}
		if ($.trim(endCode) == "" && $.trim(startCode) != "") {
			$.messager.alert("提示 ", "请同时输入开始编号和结束编号");
			return false;
		}
		if (operFlag) {
			$.messager.alert("提示 ", "正在处理，请稍等。。。");
			return;
		}
		operFlag = true;
		$.post("${ctx}/terminaldeal/terminaldeal.do?method=batchFlagOPtTerminal", {
			optFlag : flagStatus,
			idStr : idStr,
			terminalCodeStr : terminalCodeStr,
			startDate : startDate,
			endDate : endDate,
			activateStandard : activateStandard,
			dealTypeId : dealTypeId,
			startCode : startCode,
			endCode : endCode,
			agencyId : agencyId,
			agencyName : agencyName,
			flag : 2
		}, function(data) {
			operFlag = false;
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		operFlag = false;
	}
	//批处理分配终端的操作
	$.bashFlagDistribute = function() {
		var flagStatus = $("#flagStatus").combobox("getValue");
		var rows = $('#viewTerminalDeal').datagrid('getChecked');
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要分配的终端记录!");
			return false;
		}
		if (flagStatus == 1) {
			$('#terminalBatchFlag').linkbutton('disable');
		}
		var idStr = "";
		for (var i = 0; i < rows.length; i++) {
			if ("2" == rows[i].flagStatus) {
				$.messager.alert("提示 ", "已分配不能进行操作！");
				return false;
			}
			if (i == rows.length - 1) {
				idStr += rows[i].serialId;
			} else {
				idStr += rows[i].serialId + ",";
			}
		}
		if (operFlag) {
			$.messager.alert("提示 ", "正在处理，请稍等。。。");
			return;
		}
		operFlag = true;
		$.post("${ctx}/terminaldeal/terminaldeal.do?method=batchFlagDistribute", {
			optFlag : "batchDistribute",
			idStr : idStr,
		}, function(data) {
			operFlag = false;
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		operFlag = false;
	}

	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$.viewTerminalDeal();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
	};

	$.exportDealDetail = function() {
		var startDate = $('#yearmonthdatestart').val();
		var endDate = $('#yearmonthdateend').val();
		var activateStandard = $("#activateStandard").combobox('getValue');
		var dealTypeId = "'"+$('#dealTypeId').combobox('getValues').join("','")+"'";
		var startCode = $("#startCode1").val();
		var endCode = $("#endCode1").val();
		var agencyId = $("#agencytreeId").combotree('getValue');
		var agencyName = $("#agencyName").val();
		var flagStatus = $("#flagStatus").combobox('getValue');
		var customerId = $.trim($("#customerId").val());
		if (dealTypeId == null || $.trim(dealTypeId) == '-1') {
			dealTypeId = "";
		}
		if (flagStatus == null || $.trim(flagStatus) == '-1') {
			flagStatus = "";
		}

		if (agencyId == null || $.trim(agencyId) == '-1') {
			agencyId = "";
		}
		if (activateStandard == null || $.trim(activateStandard) == '-1') {
			activateStandard = "";
		}
		if ($.trim(startCode) == "" && $.trim(endCode) != "") {
			$.messager.alert("提示 ", "请同时输入开始编号和结束编号");
			return false;
		}
		if ($.trim(endCode) == "" && $.trim(startCode) != "") {
			$.messager.alert("提示 ", "请同时输入开始编号和结束编号");
			return false;
		}
		$.getToPost('${ctx}/terminaldeal/terminaldeal.do?method=exportOldDealDetailCSV', {
			startDate : startDate,
			endDate : endDate,
			activateStandard : activateStandard,
			dealTypeId : dealTypeId,
			startCode : startCode,
			endCode : endCode,
			agencyId : agencyId,
			agencyName : agencyName,
			flagStatus : flagStatus,
			activeDateStart : startDate,
			activeDateEnd : endDate,
			customerId : customerId
		});
	};
</script>
</head>
<body>
	<table>
		<tr>
			<tr style="height: 30px">
				<td align="left">交易开始日期：</td>
				<td align="left">
					<input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}"
						style="width: 110px; height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})" />
				</td>
				<td align="left">交易结束日期：</td>
				<td align="left">
					<input id="yearmonthdateend" name="yearmonthdateend" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}"
						style="width: 110px; height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" />
				</td>
				<td align="left">激活标准：</td>
				<td align="left">
					<select id="activateStandard" editable="false" class="easyui-combobox" name="activateStandard" style="width: 180px;">

					</select>
				</td>
			</tr>
			<td align="left">起始编号：</td>
			<td align="left">
				<input type="text" name="startCode1" id="startCode1" maxlength="100" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" />
			</td>
			<td align="left">结束编号：</td>
			<td align="left">
				<input type="text" name="endCode1" id="endCode1" maxlength="100" style="width: 150px;"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" />
			</td>
			<td align="left">交易类型：</td>
			<td>
				<select id="dealTypeId" class="easyui-combobox" name="dealTypeId" data-options="panelHeight:'auto'" style="width: 130px;" editable="false">
				</select>
			</td>
		</tr>
		<tr>
			<td align="left">机构名称：</td>
			<td align="left">
				<input type="text" name="agencyName" id="agencyName" maxlength="100" style="width: 150px;" />
			</td>
			<td align="left">机构名称：</td>
			<td align="left">
				<select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true">
				</select>
			</td>
			<td align="left">标记状态：</td>
			<td>
				<select id="flagStatus" class="easyui-combobox" name="flagStatus" data-options="panelHeight:'auto'" style="width: 100px;" editable="false">
					<option value="0" selected="selected">未标记</option>
					<option value="1">已标记</option>
					<option value="2">已分配</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="left">客户编号：</td>
			<td align="left">
				<input type="text" name="customerId" id="customerId" maxlength="100" style="width: 150px;" />
			</td>

			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalDeal()"> 查询 </a>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.exportDealDetail()">导出</a>
			</td>
		</tr>

	</table>
	<div id="tDeal">
		<table id="viewTerminalDeal">
		</table>
	</div>


</body>
</html>

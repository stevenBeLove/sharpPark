<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
.button1 {
	color: #444;
	background-repeat: no-repeat;
	background: #f5f5f5;
	background-repeat: repeat-x;
	border: 1px solid #bbb;
	background: -webkit-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -moz-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -o-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: linear-gradient(to bottom, #ffffff 0, #e6e6e6 100%);
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff, endColorstr=#e6e6e6, GradientType=0);
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
}
</style>
<script type="text/javascript">
	$(window).resize(function() {
		$('#viewRole').datagrid('resize', {
			width : cs()
		});
	});
	function cs() {
		return $(window).width() - 6;
	}
	var flag;
	var userCount;
	$.openWin = function(obj) {
		$.showDivShade('${ctx}');
		flag = obj;
		if (flag == "2") {
			var rows = $('#viewRole').datagrid('getSelections');
			if (rows.length == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要审核的记录");
				return;
			}
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].status != "0") {
					$.messager.alert("提示 ", "审核过的终端不能再次审核");
					$.close();
					return;

				} else {
					$('#roleSave').window({
						title : '终端审核'
					});
				}
			}
		}
		$("#roleSave").window('open').window('center');
	};
	$.save = function() {
		var batchNos = '';
		var beginTerminalIds = '';
		var endTerminalIds = '';
		var oldAgencyId = '';
		var agencyId = '';
		var reasonDesc = $("#reasonDesc").combobox('getValue');
		if ($.trim(reasonDesc) == 0) {
			$.messager.alert("提示 ", "请选择审核意见");
			return false;
		}
		$('#save').linkbutton('disable');
		var rows = $('#viewRole').datagrid('getSelections');
		for (var i = 0; i < rows.length; i++) {
			batchNos += "'" + rows[i].batchNo + "',";
			beginTerminalIds += "'" + rows[i].beginTerminalId + "',";
			endTerminalIds += "'" + rows[i].endTerminaId + "',";
			oldAgencyId = rows[i].oldAgencyId;
			agencyId = rows[i].agencyId;
		}

		$
				.post(
						"${ctx}/terminalmanage/terminalmanage.do?method=saveTerminalSuggest",
						{
							reasonDesc : reasonDesc,
							batchNos : batchNos,
							beginTerminalIds : beginTerminalIds,
							oldAgencyId : oldAgencyId,
							endTerminalIds : endTerminalIds
						}, function(data) {
							$.parseAjaxReturnInfo(data, $.success, $.failed);
							// 					$.post("${ctx}/terminalmanage/terminalmanage.do?method=getTerminalBack");
						}, "json");

	};

	$.success = function(message, data) {
		var reasonDesc = $("#reasonDesc").combobox('getValue');
		if (reasonDesc == 1) {
			$.messager.alert("提示 ", "审核完成，本次操作成功" + data.countOfSuccess
					+ "条，失败" + data.countOfFail + "条");
		}
		if (reasonDesc == 2) {
			$.messager.alert("提示 ", "操作成功");
		}
		$('#save').linkbutton('enable');
		$.close();
		$.viewRole();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
	$.close = function() {
		$.hideDivShade();
		$("#roleSave").window('close');
	};

	$.viewRole = function() {
		var beginTerminalId = $("#beginTerminalId").val();
		var endTerminaId = $("#endTerminaId").val();
		var terminalTypeId = $('#terminalTypeId').combobox('getValue');
		var yearmonthdatestart = $("#yearmonthdatestart").val();
		var yearmonthdateend = $("#yearmonthdateend").val();
		var checkDateStart = $("#checkDateStart").val();
		var checkDateEnd = $("#checkDateEnd").val();
		$('#viewRole')
				.datagrid(
						{
							title : '回拨记录审核',
							width : $(window).width() - 6,
							height : $(window).height() * 0.9,
							nowrap : true,
							fitColumns : true,
							url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalBack",
							pageSize : 20,
							pageNumber : 1,
							singleSelect : true,
							queryParams : {
								beginTerminalId : beginTerminalId,
								endTerminaId : endTerminaId,
								terminalTypeId : terminalTypeId,
								yearmonthdatestart : yearmonthdatestart,
								yearmonthdateend : yearmonthdateend,
								checkDateStart : checkDateStart,
								checkDateEnd : checkDateEnd
							},
							loadMsg : '数据载入中,请稍等！',
							remoteSort : false,
							columns : [ [
									{
										field : "ck",
										checkbox : true,
										width : "50"
									},
									{
										field : "beginTerminalId",
										title : "开始终端编号",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "agencyName",
										title : "所在机构名称",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "oldAgencyName",
										title : "原机构名称",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "createId",
										title : "创建人编号",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "createDt",
										title : "创建时间",
										width : $(window).width() * 0.15,
										align : "center",
										sortable : true
									},
									{
										field : "checkId",
										title : "审核人编号",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "checkDt",
										title : "审核时间",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "batchNo",
										title : "批次号",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "statusDesc",
										title : "数据状态",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "endTerminaId",
										title : "结束终端编号",
										width : $(window).width() * 0.2,
										align : "center",
										sortable : true
									},
									{
										field : "applyUserid2",
										title : "执行详情",
										width : $(window).width() * 0.2,
										align : "center",
										formatter : function(value, row, index) {
											if (row.status != '1') {
												return "<a href='javascript:void(0);' onclick=\"batchDetail('"
														+ row.batchNo
														+ "')\"><font color='red'>查看详情</font></a>&nbsp;&nbsp;";
											}
										}
									} ] ],
							hideColumn : [ [ {
								field : "agencyId"
							}, {
								field : "oldAgencyId"
							}, {
								field : "terminalTypeId"
							}, {
								field : "status"
							} ] ],
							pagination : true,
							rownumbers : true,
							showFooter : true,
							toolbar : [ {
								id : 'btncut',
								text : '审核',
								iconCls : 'icon-edit',
								handler : function() {
									$.openWin("2");
								}
							} ]
						});
		var p = $('#viewRole').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	$(function() {
		$('#terminalTypeId')
				.combobox(
						{
							url : "${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
							valueField : "id",
							textField : "text"
						});
		$.viewRole();
		$("select").combobox({
			editable : false
		});
	});

	function batchDetail(batchNo) {
		$("#viewDetailWin").window('open').window('refresh');
		$('#viewTerminalBatch')
				.datagrid(
						{
							title : '批次执行详情查看',
							width : $(window).width(),
							height : $(window).height() * 0.8,
							pageSize : 20,
							pageNumber : 1,
							url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalChangeDetail",
							queryParams : {
								batchNo : batchNo
							},
							loadMsg : '数据载入中,请稍等！',
							remoteSort : false,
							columns : [ [ {
								field : "batchNo",
								title : "批次号",
								width : 100,
								align : "center"
							}, {
								field : "terminalCode",
								title : "终端编号",
								width : 100,
								align : "center"
							}, {
								field : "terminalStatus",
								title : "终端状态",
								width : 150,
								formatter : function(value, row, index) {
									if (value == "0") {
										return '未激活';
									}
									if (value == "1") {
										return '已激活';
									}
									if (value == "2") {
										return '冻结';
									}
									if (value == "3") {
										return '回拨';
									}
									if (value == "4") {
										return '作废';
									}

								}
							}, {
								field : "createDt",
								title : "执行时间",
								width : 150,
								align : "center"
							}, {
								field : "statusDesc",
								title : "执行状态",
								width : 100,
								align : "center"
							} ] ],
							pagination : true,
							rownumbers : true,
							showFooter : true
						});

		//分页
		var batchP = $('#viewTerminalBatch').datagrid('getPager');
		$(batchP).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	}
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>
			<td width="90">终端编号开始：</td>
			<td align="left"><input type="text" name="beginTerminalId" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" id="beginTerminalId" maxlength="100" /></td>
			<td width="90">终端编号结束：</td>
			<td align="left"><input type="text" name="endTerminaId" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" id="endTerminaId" maxlength="100" /></td>
		</tr>
		<tr>
			<td width="90">终端类型：</td>
			<td align="left"><select id="terminalTypeId" editable="false" class="easyui-combobox" name="terminalTypeId" style="width: 150px; height: 30px"></select></td>
			<td width="90">创建的日期：</td>
			<td align="left"><input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px"
				onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})" /> - <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate" type="text"
				SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" /></td>
		</tr>
		<tr>
			<td width="90">审核日期：</td>
			<td align="left"><input id="checkDateStart" name="checkDateStart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px"
				onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'checkDateEnd\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})" /> - <input id="checkDateEnd" name="checkDateEnd" class="Wdate" type="text"
				SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'checkDateStart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" /></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewRole()">查询</a></td>
		</tr>
	</table>
	<table id="viewRole"></table>

	<div id="roleSave" class="easyui-window" title="角色更新"  draggable="true" closable="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false"
		resizable="true" style="width: 450px; height: 200px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="roleId" name="roleId" />
				<table width="380">
					<tr style="height: 50px">
						<td align="right">审核意见：</td>
						<td align="left"><select id="reasonDesc" class="easyui-combobox" name="reasonDesc" data-options="panelHeight:'auto'" style="width: 150px;">
								<option value="0"></option>
								<option value="1">同意</option>
								<option value="2">驳回</option>
						</select></td>
					</tr>
					<tr style="height: 50px">
						<td align="center" colspan="6"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()">审核</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close" id="close" href="#" class="easyui-linkbutton"
							data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div id="viewDetailWin" class="easyui-window" title="查看详情" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width: 800px; height: 400px; padding: 5px;">
		<table id="viewTerminalBatch"></table>
	</div>
</body>
</html>

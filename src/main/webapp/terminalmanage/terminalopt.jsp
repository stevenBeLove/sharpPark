<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>

<script type="text/javascript">
	$(function() {
		$('#cg').combogrid({
			panelWidth : 380,
			url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
			queryParams : {
				rows : 40
			},
			idField : 'agency_id',
			textField : 'companyName',
			mode : 'remote',
			fitColumns : true,
			columns : [ [ {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			} ] ]
		});
		$('#cg2').combogrid({
			panelWidth : 380,
			url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
			queryParams : {
				rows : 40
			},
			idField : 'agency_id',
			textField : 'companyName',
			mode : 'remote',
			fitColumns : true,
			columns : [ [ {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			} ] ]
		});
	});

	function searchCost() {
		var agencyId = $('#cg').combogrid('getValue');
		var effect = "";

		var opts = $("#viewAgencyGroup").datagrid("options");
		opts.queryParams = {
			agency_id : agencyId,
			status : effect
		};
		$("#viewAgencyGroup").datagrid("load");
	}
</script>
<script type="text/javascript">
	$(function() {
		$('#terminalManageIssued').window({
			/* data-options="modal:false,closed:true,iconCls:'icon-save',onBeforeClose:function(){$('#terminalBack').linkbutton('enable');}" collapsible="false" minimizable="false" maximizable="false"
				icon="icon-save" */
			modal : false,
			closed : true,
			onBeforeClose : function() {
				$('#terminalBack').linkbutton('enable');
			},
			iconCls : 'icon-save',
			collapsible : "false",
			minimizable : "false",
			maximizable : "false",
			icon : "icon-save"

		});

	})
</script>
<script type="text/javascript">
	$(window).resize(function() {
		$('#viewTerminalManage').datagrid('resize', {
			width : cs()
		});
	});
	function cs() {
		return $(window).width() - 8;
	}
	$(function() {
		$("#batchWin").window('close');
		$.viewTerminalManage();
		$('#agencytreeId').combotree({
			url : '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',
			method : 'get',
			readonly : false,
			onLoadSuccess : function(a, b) {
			},
			panelWidth : 350
		});

		$(".combo-text").bind("input onchange", function(a, b) {
			$('#agencytreeId').combotree('tree').tree("search", $(this).val());
			if ($(this).val() == "" || null == $(this).val()) {
				$('#agencytreeId').combotree('tree').tree("collapseAll");
			}
		});

		$('#terminalType')
				.combobox(
						{
							url : "${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
							valueField : "id",
							textField : "text"
						});
		$('#terminalTypeF')
				.combobox(
						{
							url : "${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
							valueField : "id",
							textField : "text"
						});

		function searchCost() {
			var agencyId = $('#agencyId').combogrid('getValue');
			var effect = "";

			var opts = $("#viewAgencyGroup").datagrid("options");
			opts.queryParams = {
				agency_id : agencyId,
				status : effect
			};
			$("#viewAgencyGroup").datagrid("load");
		}
	});

	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#terminalBack').linkbutton('enable');
		$('#submitForm').linkbutton('enable');
		$.close();
		$.viewTerminalManage();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#terminalBack').linkbutton('enable');
		$('#submitForm').linkbutton('enable');
		$.close();
	};
	$.close = function() {
		$.hideDivShade();
		$("#terminalManageIssued").window('close');
		$('#submitForm').linkbutton('enable');
		$("#terminalBack").linkbutton('enable');
	};

	//加载数据表格
	$.viewTerminalManage = function() {
		var status = "0";
		var terminalCode = $("#terminalCode").val();
		var terminalTypeId = $('#terminalTypeF').combobox('getValue');
		var terminalCodeEnd = $("#terminalCodeEnd").val();
		var agencytreeId = $('#agencytreeId').combobox('getValue');
		var datestart = $('#yearmonthdatestart').val();
		var dateend = $('#yearmonthdateend').val();
		if (status == '-1') {
			status = '';
		}
		if (terminalTypeId == '-1') {
			terminalTypeId = '';
		}
		if (agencytreeId == '-1') {
			agencytreeId = '';
		}

		$('#viewTerminalManage')
				.datagrid(
						{
							title : '终端回拨',
							width : $(window).width() - 8,
							height : $(window).height() * 0.9,
							pageSize : 20,
							pageNumber : 1,
							fitColumns : true,
							url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageback",
							queryParams : {
								terminalCode : terminalCode,
								terminalTypeId : terminalTypeId,
								status : status,
								terminalCodeEnd : terminalCodeEnd,
								agencytreeId : agencytreeId,
								datestart : datestart,
								dateend : dateend
							},
							loadMsg : '数据载入中,请稍等！',
							remoteSort : false,
							columns : [ [ {
								field : "ck",
								checkbox : true,
								width : "50"
							}, {
								field : "terminalCode",
								title : "终端识别码",
								width : $(window).width() * 0.25,
								align : "center",
								sortable : true
							}, {
								field : "terminaltypeName",
								title : "终端类型",
								width : $(window).width() * 0.2,
								align : "center",
								sortable : true
							}, {
								field : "agencyName",
								title : "所属机构",
								width : $(window).width() * 0.2,
								align : "center",
								sortable : true
							}, {
								field : "terminalStatusStr",
								title : "终端状态",
								width : $(window).width() * 0.15,
								align : "center",
								sortable : true
							}, {
								field : "createId",
								title : "下发人",
								width : $(window).width() * 0.2,
								align : "center",
								sortable : true
							}, {
								field : "createDt",
								title : "下发时间",
								width : $(window).width() * 0.2,
								align : "center",
								sortable : true
							} ] ],
							hideColumn : [ [ {
								field : "onlyCode"
							}, {
								field : "agencyId"
							}, {
								field : "terminaltypeId"
							}, {
								field : "terminalStatus"
							} ] ],
							pagination : true,
							rownumbers : true,
							showFooter : true,
						});
		//分页
		var p = $('#viewTerminalManage').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	//回拨
	$.terminalBack = function() {
		$
				.post(
						'${ctx}/terminalmanage/terminalmanage.do?method=checkSuperAdmin',
						function(data) {
							if (data.message == 1) {
								$.messager.alert("提示",
										"很抱歉，超级管理员无权操作终端回拨！请切换用户后重试！");
								return;
							} else {
								var rows = $('#viewTerminalManage').datagrid(
										'getSelections');
								if (rows.length == 0) {
									$.messager.alert("提示 ", "请选择要回拨的记录!");
									return;
								}
								var terminalCodes = "";
								var agencyId = $('#cg').combogrid('getValue');
								for (var i = 0; i < rows.length; i++) {
									if (rows[i].terminalStatus == '3') {
										$.messager.alert("提示 ", "已回拨的终端不能再次回拨");
										return;

									}
									if (rows[i].terminalStatus == '1') {
										$.messager.alert("提示 ", "已激活的终端不能回拨");
										return;

									}
									if (rows[i].terminalStatus == '4') {
										$.messager.alert("提示 ", "该终端已作废,不能回拨");
										return;

									}
									if (rows[i].agencyId == '1') {
										$.messager.alert("提示 ", "该机构属于中心，不能回拨");
										return;
									}
									if (rows[i].terminalStatus == '0')
										terminalCodes += "'" + rows[i].onlyCode
												+ "',";
									agencyId = rows[i].agencyId;
								}
								$('#terminalBack').linkbutton('disable');

								$("#terminalManageIssued").window('open')
										.window('refresh');
							}
						}, "json");

	};
	$.save = function() {
		var terminalCodes = "";
		var rows = $('#viewTerminalManage').datagrid('getSelections');
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].terminalStatus == '0') {
				terminalCodes += "'" + rows[i].onlyCode.substring(0, 15) + "',";
			}
		}
		var oldAgencyId = $('#cg').combogrid('getValue');
		if (null == oldAgencyId || '' == oldAgencyId) {
			alert("请选择机构 !");
			return;
		}
		$
				.post(
						'${ctx}/terminalmanage/terminalmanage.do?method=saveTerminalCallBack',
						{
							terminalCodes : terminalCodes,
							oldAgencyId : oldAgencyId
						}, function(data) {
							if (data.message == "count") {
								$.messager.alert("提示", "存在与机构不匹配的终端，请重新选择！");
							} else {
								$
										.parseAjaxReturnInfo(data, $.success,
												$.failed);
							}
						}, "json");
	};

	//创建申请回拨批次框
	$.terminalBackMore = function() {
		$
				.post(
						'${ctx}/terminalmanage/terminalmanage.do?method=checkSuperAdmin',
						function(data) {
							if (data.message == 1) {
								$.messager.alert("提示",
										"很抱歉，超级管理员无权操作终端回拨！请切换用户后重试！");
								return;
							} else {
								$.windowOpen();
							}
						});
	}
	$.windowOpen = function() {
		$('#batchWin').window({
			title : '创建回拨批次',
			width : 450,
			closable : true,
			height : 300,
			resizable : false
		});
	}
	function clearForm() {
		$('#ff').form('clear');
	}

	//批量回拨
	function submitForm() {

		var terminalCodeStart = $("#terminalCodeStart").val();
		var terminalCodeFinish = $("#terminalCodeFinish").val();
		var rows = $('#viewTerminalManage').datagrid('getSelections');
		var terminaltypeId = '';
		var oldAgencyId = $('#cg2').combogrid('getValue');
		if (oldAgencyId == '') {
			$.messager.alert("提示 ", "请选择机构");
			return;
		}
		if (terminalCodeStart == '' && terminalCodeFinish == '') {
			$.messager.alert("提示 ", "起始终端编号不能为空！");
			return;
		}

		if (terminalCodeStart > terminalCodeFinish) {
			$.messager.alert("提示 ", "起始终端编号不能大于结束终端编号！");
			return;
		}
		if (terminalCodeStart.length != 15 || terminalCodeFinish.length != 15) {
			$.messager.alert("提示 ", "输入的终端号有误！起始终端编号应为15位！");
			return;
		}
		$.messager
				.confirm(
						"确认",
						"依照搜索条件创建 回拨 批次请求？",
						function(r) {
							if (r) {
								$
										.post(
												'${ctx}/terminalmanage/terminalmanage.do?method=saveTerminalBackMore',
												{
													oldAgencyId : oldAgencyId,
													terminalCode : terminalCodeStart,
													terminalCodeEnd : terminalCodeFinish,
													terminaltypeId : terminaltypeId
												},
												function(data) {
													if (data.message == "countStart") {
														$.messager
																.alert("提示",
																		"开始终端编号不在所选机构之下，请重新输入！");
														return false;
													}
// 													if (data.message == "countEnd") {
// 														$.messager
// 																.alert("提示",
// 																		"结束终端编号不在所选机构之下，请重新输入！");
// 														return false;
// 													}
													$
															.parseAjaxReturnInfo(
																	data,
																	$.success,
																	$.failed);
													$("#batchWin").window(
															'close');
													$.viewTerminalManage();
												}, "json");

							}
						});

	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="left">终端编号开始：</td>
			<td align="left"><input type="text" name="terminalCode" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" id="terminalCode" maxlength="100" /></td>
			<td align="left">终端编号结束：</td>
			<td align="left"><input type="text" name="terminalCodeEnd" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" id="terminalCodeEnd" maxlength="100" /></td>
			<td align="left">机构名称：</td>
			<td align="left"><select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true"></select></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalManage()">查询</a></td>
		</tr>
		<tr>
			<td align="left">终端类型：</td>
			<td align="left"><select id="terminalTypeF" editable="false" class="easyui-combobox" name="terminalTypeF" style="width: 150px; height: 30px"></select></td>
			<td align="left">下发日期：</td>
			<td align="left"><input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px"
				onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})" /> - <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate" type="text"
				SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px; height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" /></td>
			<td align="left">
				<!--终端状态：</td>-->
				<td align="left"></td>
				<td><a id="terminalBack" name="terminalBack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.terminalBack()">回拨</a></td>
				<td><a id="terminalBackMore" name="terminalBackMore" href="#" class="easyui-linkbutton" data-options="modal:false,closed:true,iconCls:'icon-undo'" onclick="$.terminalBackMore()">创建申请回拨批次</a>
			</td>
		</tr>
	</table>
	<table id="viewTerminalManage"></table>
	<!-- 终端回拨窗口 -->
	<div id="terminalManageIssued" class="easyui-window" title="创建回拨批次" data-options="modal:false,closed:true,iconCls:'icon-save'" collapsible="false" minimizable="false" maximizable="false"
		icon="icon-save" style="width: 350px; height: 200px; padding: 5px; background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 30px; background: #fff; overflow: hidden;">
				<input type="hidden" id="terminalTypeId" name="terminalTypeId" />
				<table width="100%">
					<tr>
						<td align="left">机构名称：</td>
						<td><input id="cg" style="width: 150px"></input></td>
					</tr>
					<tr style="height: 80px">
						<td align="center" colspan="4"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close" id="close" href="#"
							class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div id="batchWin" class="easyui-window" title="创建回拨批次" data-options="closable:true,iconCls:'icon-save'" collapsible="false" minimizable="false" maximizable="false" icon="icon-save"
		style="width: 450px; height: 300px; padding: 5px; background: #fafafa;">
		<form id="ff" method="post">
			<div style="margin-bottom: 20px">
				机构名称：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="cg2" style="width: 150px;"></input>
			</div>
			<div style="margin-bottom: 20px">
				终端编号开始： <input type="text" id="terminalCodeStart" name="terminalCodeStart" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="100" />
			</div>
			<div style="margin-bottom: 20px">
				终端编号结束： <input type="text" id="terminalCodeFinish" name="terminalCodeFinish" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="100" />
			</div>
		</form>
		<div style="text-align: center; padding: 5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width: 125px">提交申请回拨批次</a> <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()"
				style="width: 80px">清空</a>
		</div>
	</div>

	<div id="viewDetailWin" class="easyui-window" title="查看详情" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width: 800px; height: 400px; padding: 5px;">
		<table id="viewTerminalBatch"></table>
	</div>
</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;"%>
<%
    String agencyId = session.getAttribute(ConstantUtils.AGENCYID)
					.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
.boxUl {
	text-align: center;
	text-align: left
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">
	$(function() {
		$('#agencyId').combogrid({
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
</script>
<script type="text/javascript">	
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString()
					.trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString()
					.trim()%>';
var parentagencyId='<%=session.getAttribute(ConstantUtils.PARENTAGENCYID)
					.toString().trim()%>';
var staticagecyId='<%=ConstantUtils.CENTERCODE%>';
var roleid=${sessionScope.roletypeId};
var onlineControl='<%=session.getAttribute(ConstantUtils.ONLINEFLAG)%>';
var agencyControl='<%=session.getAttribute(ConstantUtils.AGENCYFLAG)%>';

	//判断是否为中文
	function checkChinese(str) {
		var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
		return reg.test(str);
	}

	function cs() {
		return $(window).width() - 8;
	}

	$(function() {
		$.viewAgency();
	});
	var agencyrate;
	var agencyfix;
	var flag;
	var feeMin;
	var fixMin;
	var feeMax;
	var fixMax;
	/* 清空（重置） */
	$.cleanAgency = function() {
		$("#startPsamId").val("");
		$("#endPsamId").val("");
		$("#shopno").val("");
		$("#fixFee").val("");
		$("#businesstype").combobox('setValue');
		$("#termstatus").combobox('setValue');
		$("#agencyId").combogrid('setValue');
		$("#terminalstatus").combobox('setValue');
	}
	//点击设置终端费率弹框
	$.openWin = function() {
		$.setPsmFee();
		//$.viewPsamFee();
		//$.loadTransTypeDesc();
	};

	$.save = function() {
		var startPSAMId = $("#startPSAMId").val();
		var endPSAMId = $("#endPSAMId").val();
		var shopno = $("#fastfee").val();
		var fixFee = $("#fasttimes").val();
		var agencyId = "";
		var systemDesc = $("#systemDesc").val();
		if ($.trim(startPSAMId) == "") {
			$.messager.alert("提示 ", "请输入终端起始PSAM号");
			return false;
		}
		if ($.trim(endPSAMId) == "") {
			$.messager.alert("提示 ", "请输入终端截止PSAM号");
			return false;
		}
		if ($.trim(shopno) == "") {
			$.messager.alert("提示 ", "请输入百分比");
			return false;
		}
		if ($.trim(fixFee) == "") {
			$.messager.alert("提示 ", "请输入单笔金额");
			return false;
		}
		if ($.trim(startPSAMId).length < 15 || $.trim(startPSAMId).length > 16) {
			$.messager.alert("提示 ", "终端起始PSAM号必须是15位或16位");
			return false;
		}
		if ($.trim(endPSAMId).length < 15 || $.trim(endPSAMId).length > 16) {
			$.messager.alert("提示 ", "终端截止PSAM号必须是15位或16位");
			return false;
		}

		$('#save').linkbutton('disable');
		$.post("${ctx}/fee/psmfee.do?method=setPsmFeebyPasmId", {
			startPSAMId : startPSAMId,
			endPSAMId : endPSAMId,
			shopno : shopno,
			fixFee : fixFee,
			agencyId : agencyId
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	};
	$.saveUpdate = function() {

		var shopno = $("#fastfeeUp").val();
		var fixFee = $("#fasttimesUp").val();
		var shopnop = $("#commonfeeUpp").val();
		var fixFeep = $("#commontimesUpp").val();
		var agencyId = "";
		var psamId = $("#psamid").val();
		var systemDesc = $("#systemDesc").val();
		if ($.trim(shopno) == "") {
			$.messager.alert("提示 ", "请输入百分比");
			return false;
		}
		if ($.trim(fixFee) == "") {
			$.messager.alert("提示 ", "请输入单笔金额");
			return false;
		}
		if ($.trim(shopnop) == "") {
			$.messager.alert("提示 ", "请输入百分比");
			return false;
		}
		if ($.trim(fixFeep) == "") {
			$.messager.alert("提示 ", "请输入单笔金额");
			return false;
		}
		$('#saveUpdate').linkbutton('disable');
		$.post("${ctx}/fee/psmfee.do?method=setPsmFeebyPasmIdUP", {

			shopno : shopno,
			fixFee : fixFee,
			shopnop : shopnop,
			fixFeep : fixFeep,
			agencyId : agencyId,
			psamId : psamId
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.successUpdate, $.failedUpdate);
		}, "json");
	};

	$.success = function(message, data) {
		$.messager.alert("设置完成", "终端费率设置成功，即时生效！");
		$('#save').linkbutton('enable');
		$.close();
		$.viewAgency();
	};
	$.successUpdate = function(message, data) {
		$.messager.alert("设置完成", "终端费率设置成功，即时生效！");
		$('#saveUpdate').linkbutton('enable');
		$.closeUpdate();
		$.viewAgency();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
	$.failedUpdate = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#saveUpdate').linkbutton('enable');
	};
	$.close = function() {
		$.hideDivShade();
		$("#setPsmFee").window('close');
	}
	$.closeUpdate = function() {
		$.hideDivShade();
		$("#setPsmFeeUpdate").window('close');
	}

	//判断选中状态和条数
	$.setPsmFee = function() {

		var rows = $('#viewAgency').datagrid('getSelections');
		if (rows.length == 0) {
			//走原逻辑
			$.viewPsamFee();
		} else {

			if (rows.length == 1) {
				//判断选中的激活状态
				//已激活情况一
				//未激活情况二

				if (1 == rows[0].terminalStatus) {
					if (agid == "RTB00000000") {
						$.messager.alert("提示 ", "对不起！ 您没有权限修改 !");
						return;
					} else {
						//判断是否是同一机构
						$.setPsmFeeBranch();
						//弹出修改窗

					}
				} else {
					if (0 == rows[0].terminalStatus && agid != "RTB00000000") {
						$.messager.alert("提示 ", "请选择单条已激活的终端进行修改!");
					} else {
						$.messager.alert("提示 ", "对不起！ 您没有权限修改!");
					}
				}
			} else {
				$.messager.alert("提示 ", "请选择单条已激活的终端进行修改!");
				return;
			}

		}

	}

	$.viewPsamFee = function() {
		var agencyId = "";
		$('#viewPsamFee')
				.datagrid(
						{
							title : '费率设置',
							width : 530, //$(window).width() - 880,
							height : 180, //$(window).height() * 0.3,
							pageSize : 4,
							pageNumber : 1,
							url : "${ctx}/fee/psmfee.do?method=getBusinessFeebyAgencyId",
							queryParams : {
								agencyId : agencyId,
							},
							remoteSort : false,
							pagination : false,
							columns : [ [
									{
										field : "businessName",
										title : "交易类型",
										width : 150,
										align : "center",
										sortable : false
									},
									{
										field : "shopno",
										title : "百分比（%）最小单位0.01",
										width : 175,
										align : "center",
										editor : 'text',
										sortable : false,
										formatter : function(val, row) {
											if (row["transtype"] == "fast") {
												return "<input maxlength='4' id='"+row["transtype"]+"fee' , name='"+row["transtype"]+"fee'  value='' />"
											}
											return val;
										}
									},
									{
										field : "fixFee",
										title : "单笔（元）最小单位0.1",
										width : 175,
										align : "center",
										editor : 'text',
										sortable : false,
										formatter : function(val, row) {
											if (row["transtype"] == "fast") {
												return "<input id='"+row["transtype"]+"times' , name='"+row["transtype"]+"times'  value='' />"
											}
											return val;
										}
									} ] ],
							pagination : false,
							rownumbers : false,
							showFooter : false,
						});
		$('#setPsmFee').window({
			title : '设置终端费率'
		});
		$.showDivShade('${ctx}');
		$('#startPSAMId').val('');
		$('#endPSAMId').val('');
		$("#fastfee").val('');
		$("#fasttimes").val('');
		$("#setPsmFee").window('open').window('refresh');
	}

	$.viewPsamFeeUpdate = function() {
		var rows = $('#viewAgency').datagrid('getSelections');
		var psamId = rows[0].psamid;
		var agencyId = "";
		$("#psamid").val(" ");
		$("#psamid").val(psamId);
		$('#viewPsamFeeUpdate')
				.datagrid(
						{
							title : '费率设置',
							width : 530, //$(window).width() - 880,
							height : 180, //$(window).height() * 0.3,
							pageSize : 4,
							pageNumber : 1,
							url : "${ctx}/fee/psmfee.do?method=getBusinessFeebyAgencyIdUpdate",
							queryParams : {
								agencyId : agencyId,
								psamId : psamId,
							},
							remoteSort : false,
							pagination : false,
							columns : [ [
									{
										field : "businessName",
										title : "交易类型",
										width : 150,
										align : "center",
										sortable : false
									},
									{
										field : "shopno",
										title : "百分比（%）最小单位0.01",
										width : 175,
										align : "center",
										editor : 'text',
										sortable : false,
										formatter : function(val, row) {
											if (row["transtype"] == "fast") {
												return "<input maxlength='4' id='"+row["transtype"]+"feeUp' , name='"+row["transtype"]+"feeUp'  value='"+row["shopno"]+"' />";
											}
											if (row["transtype"] == "common") {
												return "<input maxlength='4' id='"+row["transtype"]+"feeUpp' , name='"+row["transtype"]+"feeUpp'  value='"+row["shopno"]+"' />";
											}
											return val;
										}
									},
									{
										field : "fixFee",
										title : "单笔（元）最小单位0.1",
										width : 175,
										align : "center",
										editor : 'text',
										sortable : false,
										formatter : function(val, row) {
											if (row["transtype"] == "fast") {
												return "<input id='"+row["transtype"]+"timesUp' , name='"+row["transtype"]+"timesUp'  value='"+row["fixFee"]+"' />";
											}
											if (row["transtype"] == "common") {
												return "<input maxlength='4' id='"+row["transtype"]+"timesUpp' , name='"+row["transtype"]+"timesUpp'  value='"+row["fixFee"]+"' />";
											}
											return val;
										}
									} ] ],
							pagination : false,
							rownumbers : false,
							showFooter : false,
						});
		$('#setPsmFeeUpdate').window({
			title : '设置终端费率'
		});
		$.showDivShade('${ctx}');
		//$('#startPSAMId').val('');
		//	$('#endPSAMId').val('');

		$("#fastfee").val('');
		$("#fasttimes").val('');
		//flag = obj;
		$("#setPsmFeeUpdate").window('open').window('refresh');
	}
	$.viewAgency = function() {
		var startPsamId = $("#startPsamId").val();
		var endPsamId = $("#endPsamId").val();
		var businesstype = $("#businesstype").combobox('getValue');
		var termstatus = $("#termstatus").combobox('getValue');
		var shopno = $("#shopno").val();
		var fixFee = $("#fixFee").val();
		var agencyId = $("#agencyId").combogrid('getValue');
		var terminalstatus = $("#terminalstatus").combobox('getValue');
		if (businesstype == "-1") {
			businesstype = "";
		}
		if (termstatus == "-1") {
			termstatus = "";
		}

		$('#viewAgency').datagrid({
			title : '费率设置',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/fee/psmfee.do?method=getPsmFeeByParam",
			queryParams : {
				agencyId : agencyId,
				startPsamId : startPsamId,
				endPsamId : endPsamId,
				businesstype : businesstype,
				termstatus : termstatus,
				shopno : shopno,
				fixFee : fixFee,
				terminalstatus : terminalstatus
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ {
				field : "ck",
				checkbox : true,
				width : "50"
			}, {
				field : "agencyId",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "agencyName",
				title : "机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "psamid",
				title : "终端PSAM号",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "psamstatus",
				title : "终端状态",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "terminalStatusStr",
				title : "激活状态",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "businessType",
				title : "交易类型",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "shopno",
				title : "百分比（%）",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "fixFee",
				title : "单笔（元）",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "oper",
				title : "操作人",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "operdt",
				title : "操作时间",
				width : 200,
				align : "center",
				sortable : true
			} ] ],
			hideColumn : [ {
				field : "terminalStatus"
			} ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar : [ {
				id : 'btncut',
				text : '设置终端费率',
				iconCls : 'icon-edit',
				handler : function() {
					//$.openWin(-1);
					$.openWin();
				}
			} ]
		});
		var p = $('#viewPsmFee').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	//判断终端所属机构和登录人所属机构是否一样
	$.setPsmFeeBranch = function() {
		//var agid = agid;
		var rows = $('#viewAgency').datagrid('getSelections');
		var agencyId = '';
		var psamId = rows[0].psamid;
		$.post("${ctx}/fee/psmfee.do?method=selPsmFeebyPasmIdBranch", {
			agencyId : agencyId,
			psamId : psamId
		}, function(data) {
			if (data == 1) {
				$.viewPsamFeeUpdate();
			} else {
				alert("对不起！您没有权限");
				return;
			}
		}, "json");
	};

	$(function() {
		$.loadTransTypeDesc();
	});
</script>

</head>

<body id="indexd">
	<table>
		<tr>
			<td></td>
			<td>终端起始PSAM号：</td>
			<td><input class="text" id="startPsamId" name="startPsamId" editable="true" style="width: 150px;" /></td>
			<td>终端截止PSAM号：</td>
			<td><input type="text" id="endPsamId" name="endPsamId" style="width: 150px;" /></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.cleanAgency()">清空</a></td>
		</tr>
		<tr>
			<td></td>
			<td>终端状态：</td>
			<td><select class="easyui-combobox" id="termstatus" name="termstatus" style="width: 150px;" editable="true">
					<option value="-1">请选择终端状态</option>
					<option value="1">已绑定</option>
					<option value="0">未绑定</option>
			</select></td>
			<td>终端直属机构查询：</td>
			<td><input id="agencyId" name='agencyId' style="width: 150px;" /></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewAgency()">查询</a></td>
		</tr>
		<tr>
			<td></td>
			<td>交易类型：</td>
			<td><select class="easyui-combobox" id="businesstype" name="businesstype" style="width: 150px;" editable="true">
					<option value="-1">请选择交易类型</option>
					<option value="3001">闪电到账</option>
					<option value="3002">普通到账</option>
					<option value="3003">二维码微信</option>
					<option value="3004">二维码支付宝</option>
					<option value="3008">银联扫码(瑞通宝<=1000)</option>
					<option value="3009">银联扫码(瑞通宝>1000)</option>
			</select></td>
			<td>激活状态：</td>
			<td><select class="easyui-combobox" id="terminalstatus" name="terminalstatus" style="width: 150px;" editable="true">
					<option value="">请选择激活类型</option>
					<option value="1">已激活</option>
					<option value="0">未激活</option>
			</select></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="$.viewAgency()">刷新</a></td>
		</tr>
		<tr>
			<td></td>
			<td>百分比(%)</td>
			<td align="left"><input class="text" id="shopno" name="shopno" style="width: 150px;" /></td>
			<td>单笔(元)</td>
			<td align="left"><input class="text" id="fixFee" name="fixFee" style="width: 150px;" editable="false" /></td>
			<td></td>
		</tr>
	</table>
	<table id="viewAgency">
	</table>

	<div id="setPsmFee" class="easyui-window" title="设置终端费率" closable="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false" resizable="false"
		draggable="false" style="width: 600px; height: 520px; top: 50px; padding: 0px; text-align: center; background: #fafafa; overflow: hidden;">

		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<div align="center">
					<tr>
						终端起始PSAM号：
						<input type="text" name="startPSAMId" id="startPSAMId" maxlength="100" />
					</tr>
					<br /> <br />
					<tr>
						终端截止PSAM号：
						<input type="text" name="endPSAMId" id="endPSAMId" maxlength="100" />
					</tr>
					<div style="width:530px ;height:200px ">
						<ul class="boxUl">
							<li>提示（本级生效中的费率）：</li>
							<c:forEach items="${costList }" var="agencyCost">
								<li>${agencyCost.businessTypeName }百分比底价为：<font color="red">${agencyCost.costRate }</font> 单笔底价：<font color="red">${agencyCost.costFix }</font>
								</li>
							</c:forEach>

							<li><font color="red">闪电到账</font>最大值: <font color="red">${feeMax }</font> + <font color="red">${fixMax }</font> &nbsp; &nbsp; 最小值: <font color="red">${feeMin }</font> + <font color="red">${fixMin }</font></li>
							<li>“收款-云闪付到账”设置的费率设置只针对云闪付单笔交易≤1000元时有效。”</li>
						</ul>

						<table Id="viewPsamFee">
						</table>
						<br /> <a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="save" id="save" href="#" class="easyui-linkbutton"
							data-options="iconCls:'icon-save'" onclick="$.save()">确认设置</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="setPsmFeeUpdate" class="easyui-window" title="设置终端费率" closable="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false" resizable="false"
		draggable="false" style="width: 600px; height: 480px; top: 50px; padding: 0px; text-align: center; background: #fafafa; overflow: hidden;">

		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<div style="width:530px ;height:200px ">
					<input type="hidden" name="psamid" id="psamid" maxlength="100" />
					<ul class="boxUl">
						<li>提示（本级生效中的费率）：</li>
						<c:forEach items="${costList }" var="agencyCost">
							<li>${agencyCost.businessTypeName }百分比底价为：<font color="red">${agencyCost.costRate }</font> 单笔底价：<font color="red">${agencyCost.costFix }</font>
							</li>
						</c:forEach>

						<li><font color="red">闪电到账</font>最大值: <font color="red">${feeMax }</font> + <font color="red">${fixMax }</font> &nbsp; &nbsp; 最小值: <font color="red">${feeMin }</font> + <font color="red">${fixMin }</font></li>
						<li>“收款-云闪付到账”设置的费率设置只针对云闪付单笔交易≤1000元时有效。”</li>
					</ul>

					<table id="viewPsamFeeUpdate"></table>
					<br /> <a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.closeUpdate()">关闭</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="saveUpdate" id="saveUpdate" href="#" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'" onclick="$.saveUpdate()">确认设置</a>
				</div>
			</div>
		</div>
	</div>
</body>
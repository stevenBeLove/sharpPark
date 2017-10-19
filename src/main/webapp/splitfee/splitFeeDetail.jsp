<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
input {
	vertical-align: middle;
	margin: 0;
	padding: 0
}

.file-box {
	position: relative;
	width: 340px
}

.txt {
	height: 30px;
	border: 1px solid #cdcdcd;
	width: 180px;
}

.btn {
	background-color: #FFF;
	border: 1px solid #CDCDCD;
	height: 30px;
	width: 70px;
}

.file {
	position: absolute;
	top: 0;
	right: 80px;
	height: 30px;
	filter: alpha(opacity : 0);
	opacity: 0;
	width: 260px
}
</style>
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
var agencyQueryFlag='<%=session.getAttribute(ConstantUtils.AGENCYQUERYFLAG)%>';

	$(window).resize(function() {
		$('#viewSplitFeeDetail').datagrid('resize', {
			width : cs()
		});
	});
	function cs() {
		return $(window).width() - 6;
	}
	$(function() {
		var myDate = new Date();
		var months = myDate.getMonth() + 1;
		var day = myDate.getDate();

		if (months < 10) {
			months = "0" + months;
		}
		if (day < 10) {
			day = "0" + day;
		}
		var yearmonthdate = myDate.getFullYear() + '' + months + day;

		if ("false" == agencyQueryFlag) {
			$("#queryFlag").css("display", "none");
		}
		//$("#yearmonthdate").val(yearmonthdate);
		$('#dealTypeId').combobox({
			url : "${ctx}/dealtype/dealtype.do?method=getCombDealTypeByAgencyId",
			valueField : "id",
			textField : "text"
		});
		//$.viewSplitFeeDetail();
		$('#agencyId').combotree({    
		    url: '${ctx}/agency/agency.do?method=getAgencyTree',
		    method:'get',
		    readonly:false,
		    onLoadSuccess:function(a,b){
		    },
		    panelWidth:   350
		});  
/* 
 
		$('#agencyId').combotree({
			url : '${ctx}/agency/agency.do?method=getChildList&agencyId=',
			method : 'get',
			readonly : false,
			onLoadSuccess : function(a, b) {
				$('#agencyId').tree({});
			},
			onBeforeExpand : function(node, b) {
				var children = $('#agencyId').tree('getChildren', node.target);
				if (children && children.length == 0) {
					$.ajax({
						type : "post",
						url : '${ctx}/agency/agency.do?method=getChildList&agencyId=' + node.id,
						dataType : "json",
						success : function(data) {
							$('#agencyId').tree('append', {
								parent : node.target,
								data : data
							});
							$("#agencyId").tree("reload");
						}
					});
				}
			},
			panelWidth : 350
		});
 */
		$(".combo-text").bind("input onchange", function(a, b) {
			$('#agencyId').combotree('tree').tree("search", $(this).val());
			if ($(this).val() == "" || null == $(this).val()) {
				$('#agencyId').combotree('tree').tree("collapseAll");
			}
		});
	});
	$.openWin = function() {
		$("#dealImport").window('open').window('refresh');

	};

	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewSplitFeeDetail();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
	};
	$.close = function() {
		$.hideDivShade();
		$("#dealImport").window('close');
	};

	//导出交易明细
	$.exportGetSplitFeeDetail = function(agencyId, dealTypeId, serialnumber, terminalId, startValue, endValue, queryFlag) {

		var datestart = $('#datestart').val();
		var dateend = $('#dateend').val();

		if (datestart.substr(0, 6) != dateend.substr(0, 6)) {
			$.messager.alert("提示", "开始日期与结束日期必须在同一个月");
			return;
		}
		/*
		 if (dateend - datestart > 7) {
		 $.messager.alert("提示", "查询日期的范围在一周之内，并且开始日期与结束日期必须在同一个月");
		 return;
		 }
		 */
		$.getToPost('${ctx}/splitfee/splitfee.do?method=exportGetSplitFeeDetailCVS', {
			agencyId : agencyId,
			datestart : datestart,
			dateend : dateend,
			dealTypeId : dealTypeId,
			serialnumber : serialnumber,
			terminalId : terminalId,
			startValue : startValue,
			endValue : endValue,
			queryFlag : queryFlag
		});

	};

	$.viewSplitFeeDetail = function() {
		var datestart = $('#datestart').val();
		var dateend = $('#dateend').val();
		var agencyId = $("#agencyId").combotree('getValue');
		var dealTypeId = $("#dealTypeId").combobox('getValue');
		var serialnumber = $("#serialnumber").val();
		var terminalId = $("#terminalId").val();
		var startValue = $("#startValue").val();
		var endValue = $("#endValue").val();
		var queryFlag = $("input[name='queryFlag']:checked").val();
		if (datestart.substr(0, 6) != dateend.substr(0, 6)) {
			$.messager.alert("提示", "开始日期与结束日期必须在同一个月");
			return;
		}
		/*
		 if (dateend - datestart > 7) {
		 $.messager.alert("提示", "查询日期的范围在一周之内，并且开始日期与结束日期必须在同一个月");
		 return;
		 }
		 */
		if (startValue != "" & endValue != "") {
			if (parseFloat(startValue) > parseFloat(endValue)) {
				$.messager.alert("提示 ", "起始值不能大于结束值!");
			}
		}
		$('#viewSplitFeeDetail').datagrid({
			title : '分润明细查询',
			width : '100%',
			height : $(window).height() * 0.83,
			nowrap : true,
			fitColumns : false,
			url : "${ctx}/splitfee/splitfee.do?method=getSplitFeeDetail",
			queryParams : {
				agencyId : agencyId,
				datestart : datestart,
				dateend : dateend,
				dealTypeId : dealTypeId,
				serialnumber : serialnumber,
				terminalId : terminalId,
				startValue : startValue,
				endValue : endValue,
				queryFlag : queryFlag
			},
			pageSize : 20,
			pageNumber : 1,
			singleSelect : false,
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "agencyName",
				width : 200,
				title : "机构名称",
				align : "center"
			}, {
				field : "onlyCode",
				width : 200,
				title : "交易唯一号",
				align : "center"
			}, {
				field : "serialNumber",
				width : 100,
				title : "流水号",
				align : "center"
			}, {
				field : "terminalCode",
				width : 100,
				title : "终端号",
				align : "center"
			}, {
				field : "dealTypeStr",
				width : 80,
				title : "交易类型",
				align : "center"
			}, {
				field : "dealData",
				width : 80,
				title : "交易日期",
				align : "center"
			}, {
				field : "transAcount",
				width : 100,
				title : "交易金额",
				align : "center"
			}, {
				field : "feeAmt",
				width : 100,
				title : "手续费金额",
				align : "center"
			}, {
				field : "moneyLevel",
				width : 100,
				title : "分润金额 ",
				align : "center"
			}, {
				field : "moneyLevelNext",
				width : 100,
				title : "下级分润金额 ",
				align : "center"
			}, {
				field : "profit",
				width : 100,
				title : "金额差 ",
				align : "center"
			}
			//	{field : "oneSplitFeeAcount",width : 100,title : "一级分润金额 ",align : "center"},
			//	{field : "twoSplitFeeAcount",width : 100,title : "二级分润金额 ",align : "center"},
			//	{field : "threeSplitFeeAcount",width : 100,title : "三级分润金额 ",align : "center"},
			//	{field : "fourSplitFeeAcount",width : 100,title : "四级分润金额 ",align : "center"},
			//	{field : "fiveSplitFeeAcount",width : 100,title : "五级分润金额 ",align : "center"}
			] ],
			hideColumn : [ [ {
				field : "agencyId"
			}, {
				field : "dealTypeId"
			} ] ],
			toolbar : [ {
				id : 'btnadd',
				text : '导出',
				iconCls : 'icon-undo',
				handler : function() {
					$.exportGetSplitFeeDetail(agencyId, dealTypeId, serialnumber, terminalId, startValue, endValue, queryFlag);
				}
			} ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		var p = $('#viewSplitFeeDetail').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>

			<td>开始日期：</td>
			<td>
				<input id="datestart" name="datestart" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateend\' )}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" />
			</td>
			<td>结束日期：</td>
			<td>
				<input id="dateend" name="dateend" class="Wdate" type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'datestart\' )}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})" "/>
			</td>
			<td>机构编号：</td>
			<td>
				<input class="easyui-combotree" id="agencyId" name="agencyId" editable="true" style="width: 150px;" />
				<span id="queryFlag">
					<input type="radio" id="queryFlag" name="queryFlag" value="1" checked="checked">
						按当前机构 <input type="radio" id="queryFlag" name="queryFlag" value="2" />
						按归属机构 
				</span>
			</td>
		</tr>
		<tr>
			<td>交易类型：</td>
			<td>
				<select id="dealTypeId" class="easyui-combobox" name="dealTypeId" data-options="panelHeight:'auto'" style="width: 155px;" editable="false"></select>
			</td>
			<td>流水号：</td>
			<td>
				<input type="text" name="serialnumber" id="serialnumber" />
			</td>
			<td>终端号：</td>
			<td>
				<input type="text" name="terminalId" id="terminalId" />
			</td>
		</tr>
		<tr>
			<td>交易金额起始值：</td>
			<td>
				<input type="text" name="startValue" id="startValue" data-options="precision:2" class="easyui-numberbox" />
			</td>
			<td>交易金额结束值：</td>
			<td>
				<input type="text" name="endValue" id="endValue" data-options="precision:2" class="easyui-numberbox" />
			</td>
			<td colspan="2">
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewSplitFeeDetail()">查询</a>
			</td>
		</tr>
	</table>
	<table id="viewSplitFeeDetail"></table>

</body>
</html>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
<script type="text/javascript">	
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString().trim()%>';
var parentagencyId='<%=session.getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim()%>';
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
	var type = 1;
	$(function() {
		$.search(type);
	});

	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.search();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#markSave").window('close');
	};
	$.search = function(obj) {
		type = obj;
		var carNumber = $('#carNumber').val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');
		var payType = $("#payType").combobox('getValue');
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if(type==2){
			if ($.trim(startDate) == "") {
				$.messager.alert("提示 ", "起始日期不能为空");
				return false;
			}
			if ($.trim(endDate) == "") {
				$.messager.alert("提示 ", "结束日期不能为空");
				return false;
			}
		}
		if (payType == null || $.trim(payType) == '-1') {
			payType = "";
		}
		$('#search').datagrid({
			title : '流水管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/finance/transactionFlow.do?method=getOrderPay",
			queryParams : {
				carNumber : carNumber,
				startDate : startDate,
				endDate : endDate,
				payType : payType
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{
				field : "carNumber",
				title : "车牌",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "carType",
				title : "车辆类型",
				width : 100,
				align : "center",
			}, {
				field : "payMoney",
				title : "收费金额",
				width : 100,
				align : "center",
				formatter:function(value,row,index){
		          	if (row != null) {
                      return parseFloat(value).toFixed(2);
                    }
		        }
			}, {
				field : "payType",
				title : "收费类型",
				width : 150,
				align : "center",
				sortable : true,
				formatter:function(value,row,index){
		          	if(value == 1){
		          		return '支付宝在线缴费';
		          	}else if(value == 2){
		          		return '支付宝代扣缴费';
		          	}else if(value == 3){
		          		return '当面付';
		          	}else{
		          		return '未知';
		          	}
		        }
			}, {
				field : "inTime",
				title : "进场时间",
				width : 200,
				align : "center",
			}, {
				field : "outTime",
				title : "出场时间",
				width : 200,
				align : "center",
			}] ],
			hideColumn : [ [ {
				field : "timeOut"
			} ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		$('#btnsave').hide();
		var p = $('#search').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	
	function reset(){
		$('#carNumber').val('');
		$("#startDate").datebox('setValue','');	
		$("#endDate").datebox('setValue','');	
		$("#payType").combobox('select', '');
	}
	
	function dateFormat(value){
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' +m + '-' + d;
    }
    
    function exportExcel() {
		var carNumber = $('#carNumber').val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');
		var payType = $("#payType").combobox('getValue');
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if ($.trim(startDate) == "") {
			$.messager.alert("提示 ", "起始日期不能为空");
			return false;
		}
		if ($.trim(endDate) == "") {
			$.messager.alert("提示 ", "结束日期不能为空");
			return false;
		}
		if (payType == null || $.trim(payType) == '-1') {
			payType = "";
		}
		$.getToPost('${ctx}/finance/transactionFlow.do?method=makeTransactionFlow', {
			carNumber : carNumber,
			startDate : startDate,
			endDate : endDate,
			payType : payType
		});
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间设置：</td>
			<td>
				<input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;" />
				——
				<input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;" />
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				车牌：
			</td>
			<td>
			<input type="text" id="carNumber" name="carNumber" style="width:150px"></input>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收费类型：
			</td>
			<td>
				<select class="easyui-combobox" id="payType" name="payType" style="width: 155px;" editable="false">
						<option value="" selected="selected">--请选择--</option>
						<option value="1">支付宝在线缴费</option>
						<option value="2">支付宝代扣缴费</option>
						<option value="3">当面付</option>
				</select>
			</td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.search(2)">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="reset()">重置</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="exportExcel()">导出报表文件</a></td>
		</tr>
	</table>
	<table id="search"></table>
</body>

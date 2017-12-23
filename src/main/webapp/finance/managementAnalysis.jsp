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
		$.search(1);
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
		var dateSet = $("input[name='dateSet']:checked").val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');
		if ('1'!=obj&&$.trim(dateSet) == "") {
			$.messager.alert("提示 ", "请选择对账时间");
			return false;
		}
		if('1'==obj){
			dateSet = 1;
		}
		if(dateSet==4){
			if($.trim(startDate) == ""||$.trim(endDate) == ""){
				$.messager.alert("提示 ", "自定义时间不能为空");
				return false;
			}
		}
		$('#search').datagrid({
			title : '经营分析',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/managementAnalysis/managementAnalysis.do?method=getManagementAnalysis",
			queryParams : {
				dateSet : dateSet,
				startDate : startDate,
				endDate : endDate
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{
				field : "dateStr",
				title : "日期",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "tempTotalAmount",
				title : "临停总金额",
				width : 100,
				align : "center",
			}, {
				field : "tempAmount",
				title : "临停现金",
				width : 100,
				align : "center"
			}, {
				field : "tempAlipayAmount",
				title : "临停支付宝",
				width : 150,
				align : "center",
				sortable : true
			}, {
				field : "tempWeiXinAmount",
				title : "临停微信",
				width : 200,
				align : "center",
			}, {
				field : "inTimeCount",
				title : "入库车次",
				width : 200,
				align : "center",
			}, {
				field : "outTimeCount",
				title : "出库车次",
				width : 200,
				align : "center",
			}, {
				field : "expectedVehicleCount",
				title : "预计库内车辆",
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
		$("#startDate").datebox('setValue','');	
		$("#endDate").datebox('setValue','');	
	}
	
	function dateFormat(value){
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' +m + '-' + d;
    }
    
    function exportExcel() {
		var dateSet = $("input[name='dateSet']:checked").val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');
		if ($.trim(dateSet) == "") {
			$.messager.alert("提示 ", "请选择对账时间");
			return false;
		}
		if(dateSet==4){
			if($.trim(startDate) == ""||$.trim(endDate) == ""){
				$.messager.alert("提示 ", "自定义时间不能为空");
				return false;
			}
		}
		$.getToPost('${ctx}/managementAnalysis/managementAnalysis.do?method=makeManagementAnalysis', {
			dateSet : dateSet,
			startDate : startDate,
			endDate : endDate
		}); 
    }
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label>对账时间设置：</label>
			</td>
			<td>
				<input class="easyui-validatebox" type="radio" name="dateSet" value="1" />昨天
				<input class="easyui-validatebox" type="radio" name="dateSet" value="2"/>上周
				<input class="easyui-validatebox" type="radio" name="dateSet" value="3"/>上月
				<input class="easyui-validatebox" type="radio" name="dateSet" value="4"/>自定义时段
				<input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;" />
				——
				<input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;" />
			</td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.search(2)">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="reset()">重置</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="exportExcel()">经营分析表导出</a></td>
		</tr>
	</table>
	<table id="search"></table>
</body>

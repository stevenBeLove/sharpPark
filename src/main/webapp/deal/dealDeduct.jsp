<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
.button1
{
  color: #444;
   background-repeat: no-repeat;
  background: #f5f5f5;
  background-repeat: repeat-x;
  border: 1px solid #bbb;
  background: -webkit-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: -moz-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: -o-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: linear-gradient(to bottom,#ffffff 0,#e6e6e6 100%);
  background-repeat: repeat-x;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff,endColorstr=#e6e6e6,GradientType=0);
  -moz-border-radius: 5px 5px 5px 5px;
  -webkit-border-radius: 5px 5px 5px 5px;
  border-radius: 5px 5px 5px 5px;
}
</style>
<script type="text/javascript">
var thisAgencyId='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
var thisSystemId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString().trim()%>';
var parentAgencyId='<%=session.getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim()%>';
var staticagecyId='<%=ConstantUtils.CENTERCODE%>';

$(function() {
	$('#agencyId').combotree({
		url:"${ctx}/agency/agency.do?method=getAgencyTree",
		valueField:"id",
		textField:"text"
		/* onLoadSuccess: function(node, data) {
           	var node = $('#agencyId').combotree("tree").tree('getRoot');
	        $('#agencyId').combotree("setValue", node.id);
        } */
	});
	
	$('#dealTypeId').combobox({
		url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypes",
		valueField:"id",
		textField:"text"
	});
	
	$('#deductType').combobox({
		url:"${ctx}/deal/deal.do?method=getComboDeductTypes&flag=1",
		valueField:"id",
		textField:"text"
	});
	
	$("select").combobox({
		editable:false 
	});
	
	$("#agencyId").combotree('setValue', -1);
	
	// 设置起止日期为当日
	var myDate = new Date();
	var month = myDate.getMonth()+1;
	var day = myDate.getDate();
	
	if (month < 10){
		month = "0"+month;
	}
	if (day < 10) {
		day ="0"+day;
	}
	var datestart=myDate.getFullYear()+''+month+day;
	$("#datestart").val(datestart);
	var dateend=myDate.getFullYear()+''+month+day;
	$("#dateend").val(dateend);
	
	// 中心显示交易查询按钮
	if(thisAgencyId == staticagecyId){
		$("#dealQueryBtn").show();
	}
	
	$.viewDealDeduct();
});

$(window).resize(function(){
	 $('#viewDealDeduct').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}

var flag;
$.openWin = function(obj) {
	$('#dealDeductSave').window({title: '添加交易扣款'});
	$.showDivShade('${ctx}');
	
	//$('#dealTypeNameU').removeAttr('readonly');
/*	$('#dealId').val('');
	$('#serialNumber').val('');
	$('#transacount').val('');
	$('#terminalId').val('');
	$('#dealDate').val('');
	$('#dealTime').val('');
	$('#onlyCode').val('');
*/
	$("#deductType").combobox('select', -1);
	$('#deductMoney').val('');
	$("#dealFlag").combobox('select', 0);
	$('#comments').val('');
	
	var rows = $('#viewDealDeduct').datagrid('getSelections');
	if (rows.length > 1) {
		$.hideDivShade();
		$.messager.alert("提示 ", "一次只能添加一条交易扣款记录");
		return;
	}else if(rows.length ==0){
		$.hideDivShade();
		$.messager.alert("提示 ", "请选择要添加的交易记录");
		return;
	}
	
	$('#dealId').attr("disabled","disabled");
	$('#serialNumber').attr("disabled","disabled");
	$('#transacount').attr("disabled","disabled");
	$('#terminalId').attr("disabled","disabled");
	$('#dealDate').attr("disabled","disabled");
	$('#dealTime').attr("disabled","disabled");
	$('#onlyCode').attr("disabled","disabled");
	$('#terminalOnlyCode').attr("disabled","disabled");
	$("#dealTypeId").combobox('disable');
	
	// 将交易记录对应的值填入交易扣款相应字段
	var row = $('#viewDealDeduct').datagrid('getSelected');
	$('#dealId').val(row.dealId);
	$('#serialNumber').val(row.serialNumber);
	$('#transacount').numberbox('setValue',row.transacount);
	$('#terminalId').val(row.terminalId);
	$('#dealDate').val(row.deal_data);
	$('#dealTime').val(row.deal_time);
	$('#onlyCode').val(row.onlyCode);
	$('#terminalOnlyCode').val(row.terminal_OnlyCode);
	$('#bankCardNumber').val(row.bankcardNumber);
	$("#dealTypeId").combobox('select', row.dealtype_id);
	$('#deductMoney').numberbox('setValue',row.transacount);
	
	flag = obj;
	if (flag != "-1") {
		$('#dealDeductSave').window({title: '修改交易扣款'});
		//$('#dealTypeNameU').attr("readonly","readonly");
		var rows = $('#viewDealDeduct').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		
		var row = $('#viewDealDeduct').datagrid('getSelected');
		if (row.dealFlag == 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "已处理记录不能修改");
			return;
		}
		
		$('#dealId').val(row.dealId);
		$('#serialNumber').val(row.serialNumber);
		$('#transacount').numberbox('setValue',row.transacount);
		$('#terminalId').val(row.terminalId);
		$('#dealDate').val(row.dealDate);
		$('#dealTime').val(row.dealTime);
		$('#onlyCode').val(row.onlyCode);
		$('#terminalOnlyCode').val(row.terminalOnlyCode);
		$('#deductMoney').numberbox('setValue',row.deductMoney);
		$('#comments').val(row.comments);
		$("#dealTypeId").combobox('select', row.dealTypeId);
		$("#deductType").combobox('select', row.deductType);
		$("#dealFlag").combobox('select', row.dealFlag);
	}
	
	$("#dealDeductSave").window('open').window('refresh');
};

$.save = function() {
	var dealId = $('#dealId').val();
	var serialNumber = $('#serialNumber').val();
	var transacount = $('#transacount').val();
	var terminalId = $('#terminalId').val();
	var dealDate = $('#dealDate').val();
	var dealTime = $('#dealTime').val();
	var onlyCode = $('#onlyCode').val();
	var terminalOnlyCode = $('#terminalOnlyCode').val();
	var bankCardNumber = $('#bankCardNumber').val();
	var deductMoney = $('#deductMoney').val();
	var comments = $('#comments').val();
	var dealTypeId = $("#dealTypeId").combobox("getValue");
	var deductType = $("#deductType").combobox("getValue");
	var dealFlag = $("#dealFlag").combobox("getValue");
	
	// 输入参数检查
	if ($.trim(dealId) == "") {
		$.messager.alert("提示 ", "请输入交易编号");
		return false;
	}
	if ($.trim(serialNumber) == "") {
		$.messager.alert("提示", "请输入流水号");
		return false;
	}
	if ($.trim(transacount) == "") {
		$.messager.alert("提示", "请输入交易金额");
		return false;
	}
	if ($.trim(terminalId) == "") {
		$.messager.alert("提示", "请输入终端编号");
		return false;
	}
	if ($.trim(onlyCode) == "") {
		$.messager.alert("提示", "请输入唯一号");
		return false;
	}
	if ($.trim(dealDate) == "") {
		$.messager.alert("提示", "请输入交易日期");
		return false;
	}
	if ($.trim(dealTime) == "") {
		$.messager.alert("提示", "请输入交易时间");
		return false;
	}
	if ($.trim(deductMoney) == "") {
		$.messager.alert("提示", "请输入扣款金额");
		return false;
	}
	if ($.trim(dealTypeId) == "-1") {
		$.messager.alert("提示", "请选择交易类型");
		return false;
	}
	if ($.trim(deductType) == "-1") {
		$.messager.alert("提示", "请选择扣款类型");
		return false;
	}

	if (flag == "-1") {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=addDealDeduct", {
			dealId : dealId,
			serialNumber : serialNumber,
			transacount : transacount,
			terminalId : terminalId,
			dealDate : dealDate,
			dealTime : dealTime,
			onlyCode : onlyCode,
			terminalOnlyCode : terminalOnlyCode,
			bankCardNumber : bankCardNumber,
			dealTypeId : dealTypeId,
			deductType : deductType,
			deductMoney : deductMoney,
			dealFlag : dealFlag,
			comments : comments
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=updateDealDeduct", {
			dealId : dealId,
			serialNumber : serialNumber,
			transacount : transacount,
			terminalId : terminalId,
			dealDate : dealDate,
			dealTime : dealTime,
			onlyCode : onlyCode,
			terminalOnlyCode : terminalOnlyCode,
			bankCardNumber : bankCardNumber,
			dealTypeId : dealTypeId,
			deductType : deductType,
			deductMoney : deductMoney,
			dealFlag : dealFlag,
			comments : comments
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}

	$.viewDealDeduct();
};

$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	$.viewDealDeduct();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#dealDeductSave").window('close');
};

$.delDealDeduct = function() {
	var rows = $('#viewDealDeduct').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert("提示 ", "一次只能删除一条记录");
		return;
	} else if(rows.length == 0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	
	var row = $('#viewDealDeduct').datagrid('getSelected');
	if (row.dealFlag == 1) {
		$.messager.alert("提示 ", "已处理记录不能删除");
		return;
	}
	
	var dealDate = row.dealDate;
	var dealTime = row.dealTime;
	var serialNumber = row.serialNumber;
	$.messager.confirm("提示","确定删除？",function(r) {
		if(r){
			$.post("${ctx}/deal/deal.do?method=deleteDealDeduct", {
				dealDate : dealDate,
				dealTime : dealTime,
				serialNumber : serialNumber
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
		}
	});

};

$.viewDealDeduct = function() {
	var datestart = $('#datestart').val();
	var dateend = $('#dateend').val();
	var agencyId = $('#agencyId').combotree('getValue');
	var serialNo = $('#serialNo').val();
	var dealType = $('#dealType').val();
	var terminalCode=$('#terminalCode').val();
	if(agencyId=='-1' || agencyId==null){
		agencyId='';
	}
	
	$('#viewDealDeduct').datagrid(
		{
						title : '交易扣款查询',
						width : $(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns : false,
						url : "${ctx}/deal/deal.do?method=getDealDeduct",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						queryParams:{
							datestart : datestart,
							dateend : dateend,
							agencySelect : agencyId,
							serialNumber : serialNo,
							dealType : dealType,
							terminalCode : terminalCode
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "agencyName",title : "机构名称",width : 150,align : "center",sortable : true},
										{field : "terminalId",title : "终端编号",width : 150,align : "center",sortable : true},
										{field : "dealTypeName",title : "交易类型",width : 150,align : "center",sortable : true},
										{field : "serialNumber",title : "交易流水号",width : 100,align : "center",sortable : true},
										{field : "transacount",title : "交易金额",width : 100,align : "right",sortable : true},
										{field : "dealDate",title : "交易日期",width : 80,align : "center",sortable : true},
										{field : "dealTime",title : "交易时间",width : 80,align : "center",sortable : true},
										{field : "deductTypeName",title : "扣款类型",width : 150,align : "center",sortable : true},
										{field : "deductMoney",title : "扣款金额",width : 100,align : "right",sortable : true},
										{field : "deductDate",title : "扣款日期",width : 80,align : "center",sortable : true},
										{field : "dealFlagStr",title : "处理标志",width : 80,align : "center",sortable : true},
										//{field : "createId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										//{field : "createDt",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										{field : "comments",title : "备注",width : 200,align : "center",sortable : true},
										] ],
						/* 无此属性！
						hideColumn : [ [ 
						                 {field : "dealId"},
						                 {field : "onlyCode"},
						                 {field : "dealTypeId"},
						                 {field : "deductType"},
						                 {field : "dealFlag"},
						                 {field : "bankCardNumber"}
						              ] ],
						*/
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar: [
								 {
									id:'btnedit',
									text:'修改',
									iconCls:'icon-edit',
									handler:function(){
										$.openWin(-2);
									}
								 },'-',
								 {
									id:'btndel',
									text:'删除',
									iconCls:'icon-cut',
									handler:function(){
										$.delDealDeduct();
									}
								}
						]
	});
	// 非中心用户不显示工具栏
	if(thisAgencyId != staticagecyId){
		$('.datagrid-toolbar').hide();
	}
	
	var p = $('#viewDealDeduct').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

	$.viewDealDetail = function() {
		var yearmonthdatestart = $('#datestart').val();
		var yearmonthdateend = $('#dateend').val();
		var agencyId = $('#agencyId').combotree('getValue');
		var serialNumber = $('#serialNo').val();
		var dealType = $('#dealType').val();
		var terminalcode = $('#terminalCode').val();
		var merchantCode =  '';
		if (agencyId == '-1' || agencyId == null) {
			agencyId = '';
		}

		$('#viewDealDeduct').datagrid(
				{
					title : '交易明细查询',
					width : $(window).width() - 6,
					height : $(window).height() * 0.9,
					nowrap : true,
					fitColumns : false,
					url : "${ctx}/deal/deal.do?method=getDealdetail",
					queryParams : {
						yearmonthdatestart : yearmonthdatestart,
						yearmonthdateend : yearmonthdateend,
						agencySelect : agencyId,
						serialNumber : serialNumber,
						dealType : dealType,
						terminalcode : terminalcode,
						merchantCode : merchantCode
					},
					pageSize : 20,
					pageNumber : 1,
					singleSelect : false,
					loadMsg : '数据载入中,请稍等！',
					remoteSort : false,
					columns : [ [
									//{field : "sysSource",width : 100,title : "来源系统",align : "center"},
									{field : "agencyName",width : 150,title : "机构名称",align : "center",sortable : true},
									//{field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
									{field : "terminalId",width : 150,title : "终端编号",align : "center"},
									{field : "dealTypeName",width : 150,title : "交易类型",align : "center"},
									{field : "serialNumber",width : 100,title : "交易流水号",align : "center"},
									{field : "deal_data",width : 80,title : "交易日期",align : "center"},
									{field : "deal_time",width : 80,title : "交易时间",align : "center"},
									{field : "transacount",width : 100,title : "交易金额(元)",align : "right"},
									{field : "feeAmt",width : 100,title : "手续费金额(元) ",align : "right"},
									//{field : "dealrebackcode",width : 100,title : "交易返回码",align : "center"},
									//{field : "splitAmont",width : 100,title : "可分润金额(元) ",align : "center"},
								//	{field : "dealdesc",width : 100,title : "交易描述 ",align : "center"},
									{field : "bankcardNumber",width : 200,title : "银行卡号",align : "center"},
								//	{field : "deal_status",width : 100,title : "交易状态 ",align : "center"},
					] ],
					hideColumn : [ [
									{field : "dealId"},
									{field : "dealtype_id"},
									{field : "deal_statusStr"}
					] ],
					pagination : true,
					rownumbers : true,
					showFooter : true,
					toolbar: [
						         {
						        	 id:'btnadd',text:'添加',iconCls:'icon-add',
						        	 handler:function(){
						        	 	$.openWin(-1);
						        	 }
								 }
						]
				});
		
		var p = $('#viewDealDeduct').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '    第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '   当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});

	};
	
	// 导出交易扣款明细 20141119
	$.dealDeductExport=function(){
		var datestart = $('#datestart').val();
		var dateend = $('#dateend').val();
		var agencyId = $('#agencyId').combotree('getValue');
		var serialNo = $('#serialNo').val();
		var dealType = $('#dealType').val();
		var terminalCode=$('#terminalCode').val();
		if(agencyId=='-1' || agencyId==null){
			agencyId='';
		}
		
		$.getToPost('${ctx}/deal/deal.do?method=dealDeductExport',{
			datestart : datestart,
			dateend : dateend,
			agencySelect : agencyId,
			serialNumber : serialNo,
			dealType : dealType,
			terminalCode : terminalCode
		});
	};
	
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>
			<td align="left">交易开始日期：</td>
			<td align="left">
				 <input id="datestart" name="datestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">交易结束日期：</td>
			<td align="left">
				 <input id="dateend" name="dateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">机构名称：</td>
			<td align="left"><select id="agencyId" editable="false" class="easyui-combotree" name="agencyId" style="width: 160px;" selected="true"></select></td>
			<td id="dealQueryBtn" style="display: none;">
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealDetail()">交易查询</a>
			</td>			
		</tr>
		
		<tr>
			<td align="left">流水号：</td>
			<td align="left">
				 <input id="serialNo"  name="serialNo" style="width:120px;height: 20px" onkeyup="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;&nbsp;  
			</td>
			<td align="left">交易类型：</td>
			<td align="left">
				 <input id="dealType"  name="dealType" style="width:120px;height: 20px" />&nbsp;&nbsp;&nbsp;  
			</td>
			<td align="left">终端编号：</td>
			<td align="left">
			  <input id="terminalCode"  name="terminalCode" style="width:160px;height: 20px"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>&nbsp;&nbsp;&nbsp;  
			</td>
			
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealDeduct()">扣款查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.dealDeductExport()">导出</a></td>
		</tr>
	</table>
	
	<table id="viewDealDeduct"></table>
	
	<div id="dealDeductSave" class="easyui-window" title="交易扣款" draggable="false" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 768px; height: 320px; top: 100px; padding:0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding:10px;background: #fff; overflow: hidden;">
				<input type="hidden" id="bankCardNumber" name="bankCardNumber" />
				<table>
						<tr>
							<td align="left" style="width: 60px;">交易编号：</td>
							<td align="left"><input type="text" id="dealId" name="dealId" maxlength="100" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
							<td align="left" style="width: 60px;">流水号：</td>
							<td align="left"><input type="text" id="serialNumber" name="serialNumber" maxlength="100" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
							<td align="left" style="width: 60px;">交易金额：</td>
							<td align="left">
								<input type="text" id="transacount" name="transacount" maxlength="100" style="width: 150px;"
								data-options="precision:2" class="easyui-numberbox" />
							</td>
						</tr>
						<tr>
							<td align="left" style="width: 60px;">终端编号：</td>
							<td align="left"><input type="text" id="terminalId" name="terminalId" maxlength="100" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
							<td align="left" style="width: 60px;">唯一号：</td>
							<td align="left"><input type="text" id="onlyCode" name="onlyCode" maxlength="100" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
							<td align="left" style="width: 60px;">终端唯一编号：</td>
							<td align="left"><input type="text" id="terminalOnlyCode" name="terminalOnlyCode" maxlength="100" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
						</tr>
						<tr>
							<td align="left" style="width: 60px;">交易类型：</td>
							<td align="left"><select id="dealTypeId" name="dealTypeId" editable="false" class="easyui-combobox" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"></select></td>
							<td align="left" style="width: 60px;">交易日期：</td>
							<td align="left"><input type="text" id="dealDate" name="dealDate" title="yyyyMMdd" maxlength="8" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
							<td align="left" style="width: 60px;">交易时间：</td>
							<td align="left"><input type="text" id="dealTime" name="dealTime" title="hhmmss" maxlength="8" style="width: 150px;" onkeyup="this.value=this.value.replace(/\D/g,'')"/></td>
						</tr>
						<tr>
							<td align="left" style="width: 60px;">扣款类型：</td>
							<td align="left"><select id="deductType" name="deductType" editable="false" class="easyui-combobox" style="width: 150px;"></select></td>
							<td align="left" style="width: 60px;">扣款金额：</td>
							<td align="left">
								<input type="text" id="deductMoney" name="deductMoney" maxlength="100" style="width: 150px;"
								data-options="precision:2" class="easyui-numberbox" />
							</td>
							<td align="left" style="width: 60px;">处理标志：</td>
							<td align="left">
								<select id="dealFlag" class="easyui-combobox" name="dealFlag" data-options="panelHeight:'auto'" style="width: 150px;">
									<option value="0">未处理</option>
									<option value="1">已处理</option>
								</select>
							</td>
						</tr>
							
						<tr><td colspan="4" height="20">备注：</td></tr>
						<tr>
							<td align="left" colspan="4"><textarea rows="4" cols="47" style="width: 100%"
									id="comments" name="comments" ></textarea>
							</td>
						</tr>
						
						<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
							</td>
						</tr>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>
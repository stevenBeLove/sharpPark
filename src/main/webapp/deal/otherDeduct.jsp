<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
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
	});
	
	$('#agencySelect').combotree({
		url:"${ctx}/agency/agency.do?method=getAgencyTree",
		valueField:"id",
		textField:"text"
	});
	
	$('#deductType').combobox({
		url:"${ctx}/deal/deal.do?method=getComboDeductTypes&flag=2",
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
	
	$.viewOtherDeduct();
});

$(window).resize(function(){
	 $('#viewOtherDeduct').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}

var flag;
var id;
$.openWin = function(obj) {
	$('#otherDeductSave').window({title: '添加其他扣款'});
	
	$.showDivShade('${ctx}');
	$('#transacount').numberbox('setValue','');
	$('#deductMoney').numberbox('setValue','');
	$('#comments').val('');
	$("#agencySelect").combotree('setValue', -1);
	$("#deductType").combobox('select', -1);
	$("#dealFlag").combobox('select', 0);
	
	flag = obj;
	if (flag != "-1") {
		$('#dealDeductSave').window({title: '修改其他扣款'});
		var rows = $('#viewOtherDeduct').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		
		var row = $('#viewOtherDeduct').datagrid('getSelected');
		if (row.dealFlag == 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "已处理记录不能修改");
			return;
		}
		
		id = row.id;
		$('#transacount').numberbox('setValue',row.transacount);
		$('#deductMoney').numberbox('setValue',row.deductMoney);
		$('#comments').val(row.comments);
		$("#agencySelect").combotree('setValue', row.agencyId);
		$("#deductType").combobox('select', row.deductType);
		$("#dealFlag").combobox('select', row.dealFlag);
	}
	
	$("#otherDeductSave").window('open').window('refresh');
};

$.save = function() {
	var transacount = $('#transacount').val();
	var deductMoney = $('#deductMoney').val();
	var comments = $('#comments').val();
	var agencySelect = $("#agencySelect").combotree("getValue");
	var deductType = $("#deductType").combobox("getValue");
	var dealFlag = $("#dealFlag").combobox("getValue");
	
	// 输入参数检查
	if ($.trim(transacount) == "") {
		$.messager.alert("提示", "请输入金额");
		return false;
	}
	if ($.trim(deductMoney) == "") {
		$.messager.alert("提示", "请输入扣款金额");
		return false;
	}
	if ($.trim(agencySelect) == "-1") {
		$.messager.alert("提示", "请选择机构");
		return false;
	}
	if ($.trim(deductType) == "-1") {
		$.messager.alert("提示", "请选择扣款类型");
		return false;
	}

	if (flag == "-1") {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=addOtherDeduct", {
			agencyId : agencySelect,
			transacount : transacount,
			deductType : deductType,
			deductMoney : deductMoney,
			dealFlag : dealFlag,
			comments : comments
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=updateOtherDeduct", {
			id : id,
			agencyId : agencySelect,
			transacount : transacount,
			deductType : deductType,
			deductMoney : deductMoney,
			dealFlag : dealFlag,
			comments : comments		
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}
	
};

$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	$.viewOtherDeduct();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#otherDeductSave").window('close');
};

$.delDealDeduct = function() {
	var rows = $('#viewOtherDeduct').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	
	var row = $('#viewOtherDeduct').datagrid('getSelected');
	if (row.dealFlag == 1) {
		$.messager.alert("提示 ", "已处理记录不能删除");
		return;
	}
	
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var dealFlag = rows[i].dealFlag;
		if (dealFlag == 1){
			$.messager.alert("提示 ", "选取记录中有已处理的，不能删除");
			return;
		}

		if (i == rows.length - 1) {
			Ids += "'" + rows[i].id + "'";
		} else {
			Ids += "'" + rows[i].id + "'" + ",";
		}
	}
	
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
			$.post("${ctx}/deal/deal.do?method=deleteOtherDeducts", {
				Ids : Ids
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
		}
	});
	
};

$.viewOtherDeduct = function() {
	var datestart = $('#datestart').val();
	var dateend = $('#dateend').val();
	var agencyId = $('#agencyId').combotree('getValue');
	if(agencyId=='-1' || agencyId==null){
		agencyId='';
	}
	
	$('#viewOtherDeduct').datagrid(
		{
						title : '其他扣款',
						width : $(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/deal/deal.do?method=getOtherDeduct",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						queryParams:{
							datestart : datestart,
							dateend : dateend,
							agencySelect : agencyId
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "agencyName",title : "机构名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "deductTypeName",title : "扣款类型",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "transacount",title : "金额",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "deductMoney",title : "扣款金额",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "deductDate",title : "扣款日期",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "dealFlagStr",title : "处理标志",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "createId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										{field : "createDt",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										{field : "comments",title : "备注",width : $(window).width()*0.1,align : "center",sortable : true},
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
								 },'-',
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
	
	var p = $('#viewOtherDeduct').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

// 导出其他扣款明细 20141119
$.otherDeductExport=function(){
	var datestart = $('#datestart').val();
	var dateend = $('#dateend').val();
	var agencyId = $('#agencyId').combotree('getValue');
	if(agencyId=='-1' || agencyId==null){
		agencyId='';
	}
	
	$.getToPost('${ctx}/deal/deal.do?method=otherDeductExport',{
		datestart : datestart,
		dateend : dateend,
		agencySelect : agencyId
	});
};

</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>
			<td align="left">扣款开始日期：</td>
			<td align="left">
				 <input id="datestart" name="datestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">扣款结束日期：</td>
			<td align="left">
				 <input id="dateend" name="dateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">机构名称：</td>
			<td align="left"><select id="agencyId" editable="false" class="easyui-combotree" name="agencyId" style="width: 160px;" selected="true"></select></td>
			
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewOtherDeduct()">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.otherDeductExport()">导出</a></td>			
		</tr>
	</table>
	
	<table id="viewOtherDeduct"></table>
	
	<div id="otherDeductSave" class="easyui-window" title="其他扣款" draggable="false" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 700px; height: 320px; top: 100px; padding:0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding:10px;background: #fff; overflow: hidden;">
				<table>
						<tr>
							<td align="left">机构名称：</td>
							<td align="left"><select id="agencySelect" editable="false" class="easyui-combotree" name="agencySelect" style="width: 150px;" selected="true"></select></td>
							<td align="left" style="width: 60px;">金额：</td>
							<td align="left">
								<input type="text" id="transacount" name="transacount" maxlength="100" style="width: 150px;"
								data-options="precision:2" class="easyui-numberbox" />
							</td>
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
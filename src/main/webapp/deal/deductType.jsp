<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
</style>
<script type="text/javascript">

$(function() {
	$("select").combobox({
		editable:false 
	});
		
	$.viewDeductType();
});

$(window).resize(function(){
	 $('#viewDeductType').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}

var flag;
var deductType;
$.openWin = function(obj) {
	$('#deductTypeSave').window({title: '添加扣款类型'});
	$.showDivShade('${ctx}');
	
	$('#saveDeductTypeName').val('');
	$('#comments').val('');
	$("#deductClass").combobox('select', 1);
	$("#saveStatus").combobox('select', 0);
	
	flag = obj;
	if (flag != "-1") {
		$('#dealDeductSave').window({title: '修改扣款类型'});
		var rows = $('#viewDeductType').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		
		var row = $('#viewDeductType').datagrid('getSelected');
		deductType = row.deductType;
		$('#saveDeductTypeName').val(row.deductName);
		$('#comments').val(row.comments);
		$("#deductClass").combobox('select', row.deductClass);
		$("#saveStatus").combobox('select', row.status);
	}
	
	$("#deductTypeSave").window('open').window('refresh');
};

$.save = function() {
	var saveDeductTypeName = $('#saveDeductTypeName').val();
	var comments = $('#comments').val();
	var deductClass = $("#deductClass").combobox("getValue");
	var saveStatus = $("#saveStatus").combobox("getValue");
	
	// 输入参数检查
	if ($.trim(saveDeductTypeName) == "") {
		$.messager.alert("提示", "请扣款类型名称");
		return false;
	}

	if (flag == "-1") {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=addDeductType", {
			deductClass : deductClass,
			deductName : saveDeductTypeName,
			status : saveStatus,
			comments : comments
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$('#save').linkbutton('disable');
		$.post("${ctx}/deal/deal.do?method=updateDeductType", {
			deductClass : deductClass,
			deductType : deductType,
			deductName : saveDeductTypeName,
			status : saveStatus,
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
	$.viewDeductType();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#deductTypeSave").window('close');
};

$.delDeductType = function() {
	var rows = $('#viewDeductType').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert("提示 ", "只能对单条记录删除");
		return;
	}else if(rows.length ==0){
		$.messager.alert("提示 ", "请选择要删除的记录");
		return;
	}
	
	var row = $('#viewDeductType').datagrid('getSelected');
	deductType = row.deductType;
	
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
			$.post("${ctx}/deal/deal.do?method=deleteDeductType", {
				deductType : deductType
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
		}
	});
	
};

$.viewDeductType = function() {
	var deductClass = "";
	var deductTypeName = $("#deductTypeName").val();
	var status = $("#status").combobox('getValue');

	$('#viewDeductType').datagrid(
		{
						title : '扣款类型',
						width : $(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/deal/deal.do?method=getDeductType",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						queryParams:{
							deductClass : deductClass,
							deductTypeName : deductTypeName,
							status : status,
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : true,
						columns : [ [
										{field : "deductClassName",title : "扣款类别",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "deductName",title : "扣款类型",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "statusName",title : "状态",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "createId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true},
										{field : "createDt",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true},
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
										$.delDeductType();
									}
								}
						]
	});
	
	var p = $('#viewDeductType').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
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
			<td width="100">	扣款类型名称：</td>
			<td width="160" align="left">
				<input type="text" name="deductTypeName" id="deductTypeName" style="width: 150px;"/>				
			</td>
			<td width="100" align="left">扣款类型状态：	</td>
			<td align="left" width="160">
				<select id="status" data-options="panelHeight:'auto'" class="easyui-combobox" name="status" style="width: 150px;">
					<option value="0">有效</option>
					<option value="1">无效</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDeductType()">查询</a>
			</td>			
		</tr>	
	</table>
	
	<table id="viewDeductType"></table>
	
	<div id="deductTypeSave" class="easyui-window" title="扣款类型" draggable="false" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 600px; height: 280px; top: 100px; padding:0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding:10px;background: #fff; overflow: hidden;">
				<table>
						<tr>
							<td align="left" style="width: 60px;">扣款类别：</td>
							<td align="left">
								<select id="deductClass" class="easyui-combobox" name="deductClass" data-options="panelHeight:'auto'" style="width: 150px;">
									<option value="1">交易扣款(有明细)</option>
									<option value="2">其他扣款(无明细)</option>
								</select>
							</td>
							<td align="left" style="width: 100px;">扣款类型名称：</td>
							<td width="160" align="left">
								<input type="text" name="saveDeductTypeName" id="saveDeductTypeName" style="width: 150px;"/>				
							</td>
						</tr>
						<tr>
							<td align="left" style="width: 60px;">状态：</td>
							<td align="left">
								<select id="saveStatus" class="easyui-combobox" name="saveStatus" data-options="panelHeight:'auto'" style="width: 150px;">
									<option value="0">有效</option>
									<option value="1">无效</option>
								</select>
							</td>
						</tr>
							
						<tr><td colspan="4" height="20">备注：</td></tr>
						<tr>
							<td align="left" colspan="4">
								<textarea rows="4" cols="47" style="width: 100%" id="comments" name="comments" ></textarea>
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
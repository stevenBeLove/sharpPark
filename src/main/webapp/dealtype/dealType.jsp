<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
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
$(window).resize(function(){
	 $('#viewDealType').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
	$('#dealTypeSave').window({title: '添加交易类型'});
	$('#dealTypeNameU').removeAttr('readonly');
	
	$.showDivShade('${ctx}');
	$('#dealTypeId').val('');
	$('#dealTypeNameU').val('');
	$("#statusU").combobox('select', 1);
	$("#dealTypeDesc").val('');
	flag = obj;
	if (flag != "-1") {
		$('#dealTypeSave').window({title: '修改交易类型'});
		$('#dealTypeNameU').attr("readonly","readonly");
		var rows = $('#viewDealType').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		//$('#viewDealType').datagrid('selectRow', flag);
		var row = $('#viewDealType').datagrid('getSelected');
		$("#dealTypeId").val(row.dealTypeId);
		$("#dealTypeNameU").val(row.dealTypeName);
		$("#dealTypeDesc").val(row.dealTypeDesc);
		$("#statusU").combobox('select', row.dealTypeStatus);
	}
	$("#dealTypeSave").window('open').window('refresh');
};
$.save = function() {
	var dealTypeId = $("#dealTypeId").val();
	var dealTypeName = $("#dealTypeNameU").val();
	var status = $("#statusU").combobox("getValue");
	var dealTypeDesc = $("#dealTypeDesc").val();
	if ($.trim(dealTypeName) == "") {
		$.messager.alert("提示 ", "请输入交易类型名称");
		return false;
	}
	if ($.trim(status) == "") {
		$.messager.alert("提示 ", "请选择状态");
		return false;
	}
	if ($.trim(dealTypeDesc) == "") {
		$.messager.alert("提示 ", "备注不能为空");
		return false;
	}
	if($.trim(dealTypeDesc).length>200){
		$.messager.alert("提示","备注字数应小于200字符");
		return false;
	}

	if (flag == "-1") {
		$('#save').linkbutton('disable');
		$.post("${ctx}/dealtype/dealtype.do?method=addDealType", {
			dealTypeName : dealTypeName,
			status : status,
			dealTypeDesc:dealTypeDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		var rows = $('#viewDealType').datagrid('getSelected');
		var dealCount = rows.dealCount;
		var dealTypeName = rows.dealTypeName;
		if(status=='0' && dealCount > 0){
			$.messager.alert("提示 ", dealTypeName+"类型已经产生交易，不能修改");
			return;
		}
		$('#save').linkbutton('disable');
		$.post("${ctx}/dealtype/dealtype.do?method=updateDealType", {
			dealTypeId :dealTypeId,
			dealTypeName : dealTypeName,
			status : status,
			dealTypeDesc : dealTypeDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}

};
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	$.viewDealType();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	
};
$.close = function() {
	$.hideDivShade();
	$("#dealTypeSave").window('close');
};

$.deleteRole = function() {
	var rows = $('#viewDealType').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var status = rows[i].dealTypeStatus;
		var dealCount = rows[i].dealCount;
		var dealTypeName = rows[i].dealTypeName;
		if (i == rows.length - 1) {
			Ids += "'" + rows[i].dealTypeId + "'";
		} else {
			Ids += "'" + rows[i].dealTypeId + "'" + ",";
		}
		if (status == "0") {
			$.messager.alert("提示 ", "该交易类型失效，不能删除!");
			return;
		}
		if(dealCount > 0){
			$.messager.alert("提示 ", dealTypeName+"类型已经产生交易，不能删除");
			return;
		}
	}
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/dealtype/dealtype.do?method=deleteDealType", {
		Ids : Ids
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.viewDealType = function() {
	var status = $("#status").combobox('getValue');
	var dealTypeName = $("#dealTypeName").val();
	$('#viewDealType').datagrid(
					{
						title : '交易类型',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/dealtype/dealtype.do?method=getDealTypes",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							status:status,
							dealTypeName:dealTypeName
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : true,
						columns : [ [
										//{field : "systemName",title : "来源系统名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "dealTypeName",title : "交易类型名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "serverCode",title : "服务编码",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "tradeCode",title : "渠道编码",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "dealTypeDesc",title : "交易类型描述",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "dealTypeStatusStr",title : "状态",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "createrId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										{field : "createDate",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true,hidden:true},
										 ] ],
						hideColumn : [ [ 
						                 {field : "dealTypeId"},
						                 {field : "dealTypeStatus"},
						                 //{field : "systemId"}
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
						/* toolbar:[
						         {id:'btnadd',text:'添加',iconCls:'icon-add',
						        	 handler:function(){
						        		 $.openWin(-1);
						        	 }
								 },'-',
								 {
									id:'btncut',
									text:'修改',
									iconCls:'icon-edit',
									handler:function(){
											$.openWin(-2);
									}
								 },'-',
								 {
									id:'btnsave',
									text:'删除',
									iconCls:'icon-cut',
									handler:function(){
										$.deleteRole();
									}
								}
						] */
						
					});
	var p = $('#viewDealType').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	$(function() {
		$.viewDealType();
		$("select").combobox({
			editable:false 
		});
	});
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table width="100%">
		<tr>
			<td width="100">
				交易类型名称：
			</td>
			<td width="160" align="left">
				<input type="text" name="dealTypeName" id="dealTypeName" style="width: 150px;"/>				
			</td>
			<td width="100" align="left">
				交易类型状态：
			</td>
			<td align="left" width="160">
				<select id="status" data-options="panelHeight:'auto'" class="easyui-combobox" name="status" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealType()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewDealType"></table>
	
	<div id="dealTypeSave" class="easyui-window" title="交易类型" draggable="false" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 510px; height: 280px; top: 100px; padding:0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px;background: #fff; overflow: hidden;">
				<input type="hidden" id="dealTypeId" name="dealTypeId" />
					<table width="100%">
						<tr>
							<td align="left">交易类型名称：</td>
							<td align="left"><input type="text" name="dealTypeNameU"
								id="dealTypeNameU" maxlength="100"/>
							</td>
							<td align="right">交易类型状态：</td>
							<td align="left"><select id="statusU" class="easyui-combobox"
								name="statusU" data-options="panelHeight:'auto'" style="width: 100px;">
									<option value="1">有效</option>
									<option value="0">无效</option>
							</select>
							</td>
						</tr>
						<tr>
							<td colspan="4" height="20">备注：</td>
						</tr>
						<tr>
							<td align="left" colspan="4"><textarea rows="5" cols="47" style="width: 100%"
									id="dealTypeDesc" name="dealTypeDesc" ></textarea>
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
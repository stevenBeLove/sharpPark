<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
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
	 $('#viewSystem').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
	$('#systemSave').window({title: '添加系统信息'});
	$.showDivShade('${ctx}');
	$('#systemId').val('');
	$('#systemNameU').val('');
	$("#systemCode").val('');
	$("#statusU").combobox('select', 1);
	$("#systemDesc").val('');
	flag = obj;
	if (flag != "-1") {
		$('#systemSave').window({title: '修改系统信息'});
		var rows = $('#viewSystem').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		//$('#viewSystem').datagrid('selectRow', flag);
		var row = $('#viewSystem').datagrid('getSelected');
		$("#systemId").val(row.systemId);
		$("#systemNameU").val(row.systemName);
		$("#systemCode").val(row.systemCode);
		$("#systemDesc").val(row.systemDesc);
		$("#statusU").combobox('select', row.systemStatus);
	}
	$("#systemSave").window('open').window('refresh');
};
$.save = function() {
	var systemId = $("#systemId").val();
	var systemName = $("#systemNameU").val();
	var systemCode = $("#systemCode").val();
	var status = $("#statusU").combobox("getValue");
	var systemDesc = $("#systemDesc").val();
	if ($.trim(systemName) == "") {
		$.messager.alert("提示 ", "请输入系统名称");
		return false;
	}
	if ($.trim(systemCode) == "") {
		$.messager.alert("提示 ", "请输入系统编号");
		return false;
	}
	if ($.trim(status) == "") {
		$.messager.alert("提示 ", "请选择状态");
		return false;
	}
	if ($.trim(systemDesc) == "") {
		$.messager.alert("提示 ", "备注不能为空");
		return false;
	}
	if($.trim(systemDesc).length>100){
		$.messager.alert("提示","备注字数应小于100字符");
		return false;
	}
	$('#save').linkbutton('disable');
	if (flag == "-1") {
		$.post("${ctx}/system/system.do?method=addSystem", {
			systemName : systemName,
			systemCode:systemCode,
			status : status,
			systemDesc:systemDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$.post("${ctx}/system/system.do?method=updateSystem", {
			systemId :systemId,
			systemName : systemName,
			systemCode:systemCode,
			status : status,
			systemDesc : systemDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}

};
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	$.viewSystem();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#systemSave").window('close');
};

$.deleteRole = function() {
	var rows = $('#viewSystem').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var status = rows[i].systemStatus;
		if (i == rows.length - 1) {
			Ids += "'" + rows[i].systemId + "'";
		} else {
			Ids += "'" + rows[i].systemId + "'" + ",";
		}
		if (status == "0") {
			$.messager.alert("提示 ", "该系统失效，不能删除!");
			return;
		}
	}
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/system/system.do?method=deleteSystem", {
		Ids : Ids
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.viewSystem = function() {
	var status = $("#status").combobox('getValue');
	var systemName = $("#systemName").val();
	$('#viewSystem').datagrid(
					{
						title : '系统管理',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/system/system.do?method=getSystems",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						queryParams:{
							status:status,
							systemName:systemName
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "ck",checkbox : true},
										{field : "systemName",title : "系统名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "systemCode",title : "系统编号",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "systemDesc",title : "系统描述",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "systemStatusStr",title : "状态",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "createrId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true},
										{field : "createDate",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true}
										
										 ] ],
						hideColumn : [ [ 
						                 {field : "systemId"},
						                 {field : "systemStatus"}
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:[
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
						]
						
					});
	var p = $('#viewSystem').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	$(function() {
		$.viewSystem();
		$("select").combobox({
			editable:false 
		});
	});
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>
			<td style="height: 30px">
				系统名称：
			</td>
			<td style="padding-left: 5px">
				<input type="text" name="systemName" id="systemName" style="width: 150px;"/>				
			</td>
			<td style="padding-left: 30px">
				系统状态：
			</td>
			<td style="padding-left: 5px">
				<select id="status" data-options="panelHeight:'auto'" editable="false" class="easyui-combobox" name="status" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewSystem()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewSystem"></table>
	
	<div id="systemSave" class="easyui-window" title="系统" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="false"
		style="width: 550px; height: 320px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px;background: #fff; overflow: hidden;">
				<input type="hidden" id="systemId" name="systemId" />
					<table width="100%">
						<tr>
							<td align="left">系统名称：</td>
							<td align="left"><input type="text" name="systemNameU"
								id="systemNameU" maxlength="100"/>
							</td>
							<td align="left">系统编号：</td>
							<td align="left"><input type="text" name="systemCode"
								id="systemCode" maxlength="100"/>
							</td>
						</tr>
						<tr style="height: 40px">	
							<td align="left">系统状态：</td>
							<td align="left"><select id="statusU" class="easyui-combobox"
								name="statusU" data-options="panelHeight:'auto',editable:false" style="width: 156px;">
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
									id="systemDesc" name="systemDesc" ></textarea>
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
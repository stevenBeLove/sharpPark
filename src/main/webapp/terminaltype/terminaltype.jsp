<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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
	 $('#viewTerminalType').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
//	$('#systemId').combobox({
//		url:"${ctx}/system/system.do?method=getCombSystems",
//		valueField:"id",
//		textField:"text",
//		onLoadSuccess:function(){
			$('#TerminalTypeSave').window({title: '添加终端类型'});
			$.showDivShade('${ctx}');
			$('#terminalTypeId').val('');
			$('#terminalTypeNameT').val('');
			$("#statusT").combobox('select', 1);
			$("#terminalTypeDesc").val('');
			flag = obj;
			if (flag != "-1") {
				$('#TerminalTypeSave').window({title: '修改终端类型'});
				var rows = $('#viewTerminalType').datagrid('getSelections');
				if (rows.length > 1) {
					$.hideDivShade();
					$.messager.alert("提示 ", "只能对单条记录修改");
					return;
				}else if(rows.length ==0){
					$.hideDivShade();
					$.messager.alert("提示 ", "请选择要修改的记录");
					return;
				}
				//$('#viewTerminalType').datagrid('selectRow', flag);
				var row = $('#viewTerminalType').datagrid('getSelected');
//				$("#systemId").combobox('setValue',row.systemId);
				$("#terminalTypeId").val(row.terminalTypeId);
				$("#terminalTypeNameT").val(row.terminalTypeName);
				$("#terminalTypeDesc").val(row.terminalTypeDesc);
				$("#statusT").combobox('select', row.terminalTypeStatus);
//				$("#systemId").combobox('readonly',true);
			}
			$("#TerminalTypeSave").window('open').window('refresh');
//		}
//	});

};
$.save = function() {
//	var systemId = $("#systemId").combobox('getValue');  
	var terminalTypeId = $("#terminalTypeId").val();
	var terminalTypeName = $("#terminalTypeNameT").val();
	var status = $("#statusT").combobox("getValue");
	var terminalTypeDesc = $("#terminalTypeDesc").val();
/*		
	if ($.trim(systemId) == "-1") {
		$.messager.alert("提示 ", "请输入来源系统");
		return false;
	}
*/
	if ($.trim(terminalTypeName) == "") {
		$.messager.alert("提示 ", "请输入终端类型名称");
		return false;
	}
	if ($.trim(status) == "") {
		$.messager.alert("提示 ", "请选择状态");
		return false;
	}
	if($.trim(terminalTypeDesc).length == 0){
		$.messager.alert("提示","请输入备注");
		return false;
	}
	if($.trim(terminalTypeDesc).length>200){
		$.messager.alert("提示","备注字数应小于200字符");
		return false;
	}
	
	var systemId = '';		// 已不用
	if (flag == "-1") {
		$('#save').linkbutton('disable');
		$.post("${ctx}/terminaltype/terminaltype.do?method=addTerminalType", {
			systemId : systemId,
			terminalTypeName : terminalTypeName,
			status : status,
			terminalTypeDesc:terminalTypeDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$.post("${ctx}/terminaltype/terminaltype.do?method=updateTerminalType", {
			systemId : systemId,
			terminalTypeId :terminalTypeId,
			terminalTypeName : terminalTypeName,
			status : status,
			terminalTypeDesc : terminalTypeDesc
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}
};
$.success = function(message, data) {
	$.messager.alert("提示", message);
	$.viewTerminalType();
	$.close();
	
};
$.failed = function(message, data) {
	console.info(message+"  "+data);
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$('#save').linkbutton('enable');
	$("#TerminalTypeSave").window('close');
};

$.deleteTerminal = function() {
	var rows = $('#viewTerminalType').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var status = rows[i].terminalTypeStatus;
		var terminalCount = rows[i].terminalCount;
		var terminalTypeName = rows[i].terminalTypeName;
		if (i == rows.length - 1) {
			Ids += "'" + rows[i].terminalTypeId + "'";
		} else {
			Ids += "'" + rows[i].terminalTypeId + "'" + ",";
		}
		
		if (status == "0") {
			$.messager.alert("提示 ", "该终端类型失效，不能删除!");
			return;
		}
		
		if(terminalCount>0){
			$.messager.alert("提示 ",terminalTypeName+"类型已经被占用，不能删除!");
			return;
		}
	}
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/terminaltype/terminaltype.do?method=deleteTerminalType", {
		Ids : Ids
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.viewTerminalType = function() {
	var status = $("#status").combobox('getValue');
	var terminalTypeName = $("#terminalTypeName").val();
	$('#viewTerminalType').datagrid(
					{
						title : '终端类型',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/terminaltype/terminaltype.do?method=getTerminalTypes",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							status:status,
							terminalTypeName:terminalTypeName
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "ck",checkbox : true},
										//{field : "systemName",title : "来源系统",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "terminalTypeName",title : "终端类型名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "terminalTypeDesc",title : "终端类型描述",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "terminalTypeStatusStr",title : "状态",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "createrId",title : "操作人",width : $(window).width()*0.15,align : "center",sortable : true},
										{field : "createDate",title : "操作时间",width : $(window).width()*0.15,align : "center",sortable : true}
										 ] ],
						hideColumn : [ [ 
						                 {field : "terminalTypeId"},
						                 {field : "terminalTypeStatus"},
						                 {field : "terminalCount"},
						                 //{field : "systemId"}
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
										$.deleteTerminal();
									}
								}
						]
						
					});
	var p = $('#viewTerminalType').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	$(function() {
		$.viewTerminalType();
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
				终端类型名称：
			</td>
			<td width="160" align="left">
				<input type="text" name="terminalTypeName" id="terminalTypeName" style="width: 150px;"/>				
			</td>
			<td width="100" align="left">
				终端类型状态：
			</td>
			<td align="left" width="160">
				<select id="status" data-options="panelHeight:'auto'" class="easyui-combobox" name="status" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalType()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewTerminalType"></table>
	
	<div id="TerminalTypeSave" class="easyui-window" title="终端类型" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 550px; height: 350px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px;background: #fff; overflow: hidden;">
				<input type="hidden" id="terminalTypeId" name="terminalTypeId" />
					<table width="100%">
					<!-- 
						<tr>
							<td align="left">来源系统：</td>
							<td align="left">
								 <select id="systemId" class="easyui-combobox" name="systemId data-options="panelHeight:'auto'" style="width: 155px;height: 30px"  editable="false"></select>
							</td>
						</tr>
					 -->
						<tr>
							<td align="left">终端类型名称：</td>
							<td align="left"><input type="text" name="terminalTypeNameT"
								id="terminalTypeNameT" maxlength="100"/>
							</td>
							<td align="right">终端类型状态：</td>
							<td align="left"><select id="statusT" class="easyui-combobox"
								name="statusT" data-options="panelHeight:'auto'" style="width: 100px;">
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
									id="terminalTypeDesc" name="terminalTypeDesc" ></textarea>
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
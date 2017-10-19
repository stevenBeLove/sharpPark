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
	 $('#viewRole').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
var userCount;
$.openWin = function(obj) {
	$('#roleSave').window({title: '添加角色'});
	$.showDivShade('${ctx}');
	$('#roleId').val('');
	$('#RoleName').val('');
	$("#Status").combobox('select', 1);
	$("#roletypeId").combobox('select', 2);
	$("#roleDesc").val('');
	flag = obj;
	if (flag != "-1") {
		$('#roleSave').window({title: '修改角色'});
		var rows = $('#viewRole').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		//$('#viewRole').datagrid('selectRow', flag);
		var row = $('#viewRole').datagrid('getSelected');
		userCount=row.userCount;
		$("#roleId").val(row.roleId);
		$("#RoleName").val(row.roleName);
		$("#roleDesc").val(row.roleDesc);
		$("#Status").combobox('select', row.status);
		$("#roletypeId").combobox('select', row.roletypeId);
	}
	$("#roleSave").window('open').window('refresh');
};
$.save = function() {
	var roleId = $("#roleId").val();
	var roleName = $("#RoleName").val();
	var Status = $("#Status").combobox("getValue");
	var roleDesc = $("#roleDesc").val();
	var roletypeId=$("#roletypeId").combobox("getValue");
	if ($.trim(roleName) == "") {
		$.messager.alert("提示 ", "请输入角色名称");
		return false;
	}
	if ($.trim(Status) == "") {
		$.messager.alert("提示 ", "请选择状态");
		return false;
	}
	if($.trim(roleDesc)== ""){
		$.messager.alert("提示","备注不能为空");
		return false;
	}
	if($.trim(roleDesc).length>200){
		$.messager.alert("提示","备注字数应小于200字符");
		return false;
	}
	$('#save').linkbutton('disable');
	if (flag == "-1") {
		$.post("${ctx}/role/role.do?method=addRole", {
			roleName : roleName,
			Status : Status,
			roleDesc : roleDesc,
			roletypeId:roletypeId
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		if(Status=='0' && userCount>0){
			$.messager.alert("提示","该角色已经分配了用户，不能修改为无效");
			return false;
		}
		if(Status=='0' && userCount>0){
			$.messager.alert("提示","该角色已经分配了权限，不能修改为无效");
			return false;
		}
		$.post("${ctx}/role/role.do?method=updateRole", {
			roleId : roleId,
			roleName : roleName,
			Status : Status,
			roleDesc : roleDesc,
			roletypeId:roletypeId
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}

};
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	$.viewRole();
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#roleSave").window('close');
};

$.deleteRole = function() {
	var rows = $('#viewRole').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var ucount=rows[0].userCount;
	var mcount=rows[0].menuCount;
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var status = rows[i].status;
		if (i == rows.length - 1) {
			Ids += "'" + rows[i].roleId + "'";
		} else {
			Ids += "'" + rows[i].roleId + "'" + ",";
		}
		if (status == "0") {
			$.messager.alert("提示 ", "该角色已失效，不能删除!");
			return;
		}
		if(ucount>0){
			$.messager.alert("提示 ", "该角色已分配了用户，不能删除!");
			return;
		}if(mcount>0){
			$.messager.alert("提示 ", "该角色已分配了权限，不能删除!");
			return;
		}
	}
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/role/role.do?method=deleteRole", {
		Ids : Ids
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.viewRole = function() {
	var status = $("#status").combobox('getValue');
	var roleName = $("#roleName").val();
	$('#viewRole').datagrid(
					{
						title : '角色管理',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/role/role.do?method=getRole",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams : {
							status : status,
							roleName : roleName
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "ck",checkbox : true,width : "50"},
										{field : "roleName",title : "角色名称",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "roletypeName",title : "角色类型",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "roleDesc",title : "角色描述",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "statusStr",title : "状态",width : $(window).width()*0.1,align : "center",sortable : true},
										{field : "comment",title : "备注",width : $(window).width()*0.15,align : "center",sortable : true,hidden : true},
										{field : "createId",width : $(window).width()*0.15,align : "center",sortable : true,hidden : true},
										{field : "createDt",title : "操作时间",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "modifyid",title : "修改人",width : $(window).width()*0.15,align : "center",sortable : true},
										{field : "modifydt",title : "修改时间",width : $(window).width()*0.2,align : "center",sortable : true}
										 ] ],
						hideColumn : [ [ 
						    {field : "roleId"},
						    {field : "status"}, 
						    {field :"roletypeId"},
						    {field :"userCount"},
						    {field :"menuCount"}
						    
						    ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:[{
							id:'btnadd',
							text:'添加',
							iconCls:'icon-add',
							handler:function(){
								$.openWin(-1);
							}
						},'-',{
							id:'btncut',
							text:'修改',
							iconCls:'icon-edit',
							handler:function(){
								$.openWin(-2);
							}
						},'-',{
							id:'btnsave',
							text:'删除',
							iconCls:'icon-cut',
							handler:function(){
								$.deleteRole();
							}
						}]
					});
	var p = $('#viewRole').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	$(function() {
		$.viewRole();
		$("select").combobox({
			editable:false 
		});
	});
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table width="100%">
		<tr>
			<td width="60">
				角色名称：
			</td>
			<td width="160" align="left">
				<input type="text" name="roleName" id="roleName" style="width: 150px;"/>				
			</td>
			<td width="60" align="left">
				角色状态：
			</td>
			<td align="left" width="160">
				<select id="status" data-options="panelHeight:'auto'" class="easyui-combobox" name="status" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewRole()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewRole"></table>
	
	<div id="roleSave" class="easyui-window" title="角色更新"  draggable="true"  closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true"
		style="width: 720px; height: 300px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px;background: #fff; overflow: hidden;">
				<input type="hidden" id="roleId" name="roleId" />
					<table width="680">
						<tr style="height: 50px">
							<td align="right">角色名称：</td>
							<td align="left"><input type="text" name="RoleName"
								id="RoleName" maxlength="100"/>
							</td>
							<td align="right">角色状态：</td>
							<td align="left"><select id="Status" class="easyui-combobox"
								name="Status" data-options="panelHeight:'auto'" style="width: 150px;">
									<option value="1">有效</option>
									<option value="0">无效</option>
							</select>
							</td>
							<td width="60" align="left">
								角色类型：
							</td>
							<td align="left" width="160">
								<select id="roletypeId" data-options="panelHeight:'auto'" class="easyui-combobox" name="status" style="width: 150px;">
									<option value="2">管理员</option>
									<option value="3">操作员</option>
								</select>
							</td>
						</tr>
						<tr>
							<td align="left">备注：</td>
							<td align="left" colspan="5"><textarea rows="5" cols="70"
									id="roleDesc" name="roleDesc" ></textarea>
							</td>
						</tr>
						<tr style="height: 50px">
							<td align="center" colspan="6">
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
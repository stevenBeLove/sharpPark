<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>瑞通宝综合管理系统</title>
<style type="text/css">
table#border {
	border-top: #d4d4d4 1px solid;
	border-left: #d4d4d4 1px solid;
}

table#border td {
	border-bottom: #d4d4d4 1px solid;
	border-right: #d4d4d4 1px solid;
}
</style>

</head>
<body style="padding-top: 10px" onload="$.openPage()">
	<table id="border" width="100%" border="0" align="center"
		cellpadding="0" cellspacing="0">
		<tr>
			<td class="SmallPartHeader" colspan="2" style="background: #F2F2F2;" align="center"><font size="5"><b>角色菜单管理</b></font></td>
		</tr>
		<tr><td colspan="2"><b>请选择一个角色,勾选菜单列表后点击保存进行更新菜单 :&nbsp;&nbsp;</b><select id="rolelist"
						 class="easyui-combobox" name="rolelist" data-options="panelHeight:'auto'" style="width: 110px;">
					</select></td></tr>
		<tr>
		
			<td>
				<div id="divTree"
					style="background: #fafafa;position: relative;  visibility: visible; left: 0 px; top: 0 px; z-index: 1; width: 100%; height: 400px;  overflow-y: auto;">
					<ul id="detailTree" name="detailTree" data-options="checkbox:true,animate:true,lines:true"></ul>
					
				</div>
				<div align="center">
				<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()" >保存</a>
				</div><br/>
			</td>
			
		</tr>
	</table>
	
	<script type="text/javascript">
	$.openPage = function(){
	$("#rolelist").combobox({
		url:"${ctx}/role/role.do?method=getRoles&flag=1",
		valueField:"id",
		textField:"text",
		editable:false,
		onSelect:function(){
			var roleId = $("#rolelist").combobox("getValue");
			var rows=$("#detailTree").tree("getChecked");
			if(roleId != "-1"){	
			$("#detailTree").tree({
				onlyLeafCheck:true,
				url:"${ctx}/role_auth/role_auth.do?method=getRoleAuthsTree&roleId="+roleId,
				checkbox:true
			});
			}else{
				for(var i=0;i<rows.length;i++){
				$("#detailTree").tree("uncheck",rows[i].target);
				}
			}
			//alert(roleId);
		}
	});
	$("#detailTree").tree({
		onlyLeafCheck:true,
		url:"${ctx}/authority/authority.do?method=getAuthTree"
		});
	};
	
	$.save = function() {
		var powerIds = "";
		var roleId = $("#rolelist").combobox("getValue");
		if ($.trim(roleId) == "-1") {
			$.messager.alert("提示 ", "请选择一个角色");
			return;
		}
		
		var rows = $("#detailTree").tree("getChecked");
		var authId = $("#authId").val;
		for ( var i = 0; i < rows.length; i++) {
			
			powerIds += rows[i].id + ",";
			
		}
		$('#save').linkbutton('disable');
		$.post("${ctx}/role_auth/role_auth.do?method=saveAuths", {
			roleId:roleId,
			powerIds:powerIds	
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	};
	
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		
	};
	
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
</script>
</body>
</html>
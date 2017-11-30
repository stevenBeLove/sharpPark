<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
<script type="text/javascript">
	var flag;
	$.openWin = function(obj) {
		$.showDivShade('${ctx}');
		$('#AuthoritySave').window({title: '添加菜单'});
		$('#powerId').val('');
		$('#PowerName').val('');
		$("#Status").combobox('setValue',1);
		
		$("#menuparentId").combobox({
				url:"${ctx}/authority/authority.do?method=getParentMenu",
				valueField:"id",
				textField:"text",
				editable:false,
				onLoadSuccess:function(){
					if (flag != "-1") {
						$("#menuparentId").combobox('select', row.parentNodeId);
					}
				},
				onSelect:function(){
					var menuPid=$("#menuparentId").combobox("getValue");
					if(menuPid=="0"){
						$("#menuDesc").val('');
						$("#menuDesc").attr("readonly","readonly");
					}else{
						$("#menuDesc").removeAttr("readonly");
					}
				}
			}
		);
		
		$("#comment").val('');
		$("#menuDesc").val('');
		flag = obj;
		if (flag != "-1") {
			$('#AuthoritySave').window({title: '修改菜单'});
			var rows = $('#viewAuthority').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			}else if(rows.length==0){
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录");
				return;
			}
			//$('#viewAuthority').datagrid('selectRow', flag);
			var row = $('#viewAuthority').datagrid('getSelected');
			if (row.parentId == '-1') {
				$.messager.alert("提示 ", "根节点无法修改");
				return;
			}
			$("#powerId").val(row.menuId);
			$("#PowerName").val(row.menuName);
			$("#comment").val(row.comment);
			$("#menuDesc").val(row.menuDesc);
			$("#Status").combobox('select', row.status);
		}
		$("#AuthoritySave").window('open').window('refresh');
	};
	
	$.save = function() {
		var powerId = $("#powerId").val();
		var powerName = $("#PowerName").val();
		var Status = $("#Status").combobox("getValue");
		var menuPid=$("#menuparentId").combobox("getValue");
		var comment = $("#comment").val();
		var menuDesc =$("#menuDesc").val();
		var parentId=$("#menuparentId").combobox("getValue");
		/* $('#viewAuthority').datagrid('selectRow', flag);
		var row = $('#viewAuthority').datagrid('getSelected');
		var parentId = row.menuId; */
		if($.trim(parentId) == "-1"){
			$.messager.alert("提示 ", "请选择上级菜单");
			return false;
		}
		if ($.trim(powerName) == "") {
			$.messager.alert("提示 ", "请输入菜单名称");
			return false;
		}
		if ($.trim(Status) == "") {
			$.messager.alert("提示 ", "请选择菜单状态");
			return false;
		}
		if(menuPid!="0"){
			if ($.trim(menuDesc) == "") {
				$.messager.alert("提示 ", "请输入菜单URL");
				return false;
			}
		}
		if($.trim(comment) == ""){
			$.messager.alert("提示 ", "备注不能为空");
			return false;
		}
		if($.trim(comment).length>200){
			$.messager.alert("提示","备注字数应小于200字符");
			return false;
		}
		$('#save').linkbutton('disable');
		if (flag == "-1") {
			$.post('${ctx}/authority/authority.do?method=addAuthority', {

				powerName : powerName,
				Status : Status,
				comment : comment,
				parentId : parentId,
				menuDesc:menuDesc
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			$.post("${ctx}/authority/authority.do?method=updateAuthority", {
				powerId : powerId,
				powerName : powerName,
				Status : Status,
				comment : comment,
				menuDesc:menuDesc,
				parentId : parentId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}
	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewAuthority();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		
	};
	$.close = function() {
		$.hideDivShade();
		$("#AuthoritySave").window('close');
	};

	$.deleteAuthority = function() {
		var rows = $('#viewAuthority').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要删除的记录");
			return;
		}
		for ( var i = 0; i < rows.length; i++) {
			if (rows[i].childCode == '1') {
				$.messager.alert("提示 ", "存在子节点无法删除");
				return;
			}
			if (rows[i].parentId == '-1') {
				$.messager.alert("提示 ", "根节点无法删除");
				return;
			}
		}
		var Ids = "";
		for ( var i = 0; i < rows.length; i++) {
			var status = rows[i].status;
			if (i == rows.length - 1) {
				Ids += "'" + rows[i].menuId + "'";
			} else {
				Ids += "'" + rows[i].menuId + "'" + ",";
			}
			if (status == "0") {
				$.messager.alert("提示 ", "该菜单已失效，不能删除!");
				return;
			}
		}

		$.messager.confirm("提示", "确定删除？", function(r) {
			if (r) {
				$.post("${ctx}/authority/authority.do?method=deleteAuthority",
						{
							Ids : Ids
						}, function(data) {
							$.parseAjaxReturnInfo(data, $.success, $.failed);
						}, "json");
			}
		});
	};
	$.viewAuthority = function() {
		var status = $("#status").combobox('getValue');
		var powerName = $("#powerName").val();
		$('#viewAuthority')
				.datagrid(
						{
							width : '100%',
							title : '菜单管理',
							height : $(window).height()*0.9,
							nowrap : false,
							fitColumns : true,
							url : "${ctx}/authority/authority.do?method=getAuthority",
							pageSize : 20,
							pageNumber : 1,
							queryParams : {
								status : status,
								powerName : powerName
							},
							loadMsg : '数据载入中,请稍等！',
							remoteSort : false,
							columns : [ [
									{field : "ck",checkbox : true,width : "150"},
									{field : "menuName",title : "菜单名称",width : "200",align : "center",sortable : true},
									{field : "menuDesc",title : "URL",width : "150",align : "center",sortable : true},
									{field : "menuStatus",title : "状态",width : "150",align : "center",sortable : true},
									{field : "comment",title : "备注",width : "200",align : "center",sortable : true},
									{field : "createId",title : "操作人",width : "150",align : "center",sortable : true},
									{field : "createDt",title : "操作时间",width : "150",align : "center",sortable : true}
									] ],
							hideColumn : [ [ 
							         {field : "menuId"}, 
									 {field : "status"},
									 {field : "parentNodeId"},
									 {field : "childCode"} 
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
									$.deleteAuthority();
									
								}
							}]
						});
		var p = $('#viewAuthority').datagrid('getPager');
    	$(p).pagination({
    		pageList : [20],
    		beforePageText : '第',
    		afterPageText : '页    共 {pages} 页',
    		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    	});
	};
	$(function() {
		$.viewAuthority();
		$("#del").bind("click", function() {
			var rows = $('#viewAuthority').datagrid('getSelections');
			if (rows.length == 0) {
				$.messager.alert("提示 ", "请选择要删除的记录");
				return;
			}
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].childCode == '1') {
					$.messager.alert("提示 ", "存在子节点无法删除");
					return;
				}
				if (rows[i].parentId == '-1') {
					$.messager.alert("提示 ", "根节点无法删除");
					return;
				}
			}
			$.deleteAuthority();
		});
	});
</script>
</head>
<body id="indexd">
	<table width="100%">
		<tr>
			<td width="60">菜单名称：</td>
			<td width="160" align="left">
				<input type="text" name="powerName" id="powerName" style="width: 150px;"/>
			</td>
			<td width="60" align="left">菜单状态：</td>
			<td width="160" align="left">
				<select id="status" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" name="status" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			<td>
				<a id="btn" href="#" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="$.viewAuthority()">查询</a>
			</td>
		</tr>
	</table>
	<table id="viewAuthority"></table>

<div id="AuthoritySave" class="easyui-window" title="菜单更新" false="false" closed=true
	cache="false" collapsible="false" zIndex="20px" minimizable="false"
	maximizable="false" resizable="true" draggable="true"
	style="width: 580px; height: 330px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
<div class="easyui-layout" fit="true">
<div region="center" border="true"
	style="padding: 10px; background: #fff; overflow: hidden;">
<input type="hidden" id="powerId" name="powerId"/>
<table width="550">
	<tr height="30px;">
		<td align="right">父菜单：</td>
		<td align="left">
		
		<input class="easyui-combobox" id="menuparentId"  name="menuparentId" style="width:150px;" />
		</td>
		<td align="right"></td>
		<td align="left"></td>
	</tr>
	<tr height="30px;">
		<td align="right">菜单名称：</td>
		<td align="left"><input type="text" name="PowerName"
			id="PowerName" /></td>
		<td align="right">菜单状态：</td>
		<td align="left"><select id="Status" class="easyui-combobox"
			name="Status" style="width: 156px;" data-options="panelHeight:'auto',editable:false">
			<option value="1">有效</option>
			<option value="0">无效</option>
		</select></td>
	</tr>
	<tr height="30px;">
		<td align="right">URL：</td>
		<td align="left" colspan="3"><textarea rows="2" cols="48"
			id="menuDesc" name="menuDesc"></textarea></td>
	</tr>
	<tr height="30px;">
		<td align="right">备注：</td>
		<td align="left" colspan="3"><textarea rows="2" cols="48"
			id="comment" name="comment"></textarea></td>
	</tr>
	<tr height="40px;">
		<td align="left" colspan="4">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a name="save" id="save" href="#"
			class="easyui-linkbutton" data-options="iconCls:'icon-save'"
			onclick="$.save()">保存</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a name="close" id="close"
			href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
	</tr>
</table></div>
</div>
</div>
<script type="text/javascript">
	
</script>
</body>
</html>
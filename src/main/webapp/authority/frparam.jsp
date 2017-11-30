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
		$('#ParamSave').window({
			title : '添加菜单'
		});
		$('#paramName').val('');
		$('#paramValue').val('');
		$("#paramDes").val('');
		$("#paramId").val('');
		$("#paramId").attr("disabled",false);
		flag = obj;
		if (flag != "-1") {
			$('#ParamSave').window({
				title : '修改菜单'
			});
			var rows = $('#viewParam').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			} else if (rows.length == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录");
				return;
			}
			var row=rows[0];
			$("#paramValue").val(row.paramValue);
			$("#paramName").val(row.paramName);
			$("#paramDes").val(row.paramDes);
			$("#paramId").val(row.paramId);
			$("#paramId").attr("disabled",true);
		}
		$("#ParamSave").window('open').window('refresh');
	};

	$.save = function() {
		var paramName = $("#paramName").val();
		var paramValue = $("#paramValue").val();
		var paramDes = $("#paramDes").val();
		var paramId=$("#paramId").val();
		if ($.trim(paramId) == "") {
            $.messager.alert("提示 ", "请输入参数ID");
            return false;
        }
		if ($.trim(paramName) == "") {
			$.messager.alert("提示 ", "请输入参数名称");
			return false;
		}
		if ($.trim(paramValue) == "") {
			$.messager.alert("提示 ", "请输入参数值");
			return false;
		}
		$('#save').linkbutton('disable');
		if (flag == "-1") {
			$.post('${ctx}/authority/authority.do?method=addParam', {

				paramName : paramName,
				paramValue : paramValue,
				paramDes : paramDes,
				paramId : paramId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			$.post("${ctx}/authority/authority.do?method=updateParam", {
				paramName : paramName,
                paramValue : paramValue,
                paramDes : paramDes,
                paramId : paramId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}
	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewParam();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#ParamSave").window('close');
	};

	$.deleteAuthority = function() {
		var rows = $('#viewParam').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要删除的记录");
			return;
		}
		var ids="";
		for(var i=0;i<rows.length;i++){
			ids=ids+":"+rows[i].paramId;
		}
		ids=ids.substring(1, ids.length);
		$.messager.confirm("提示", "确定删除？", function(r) {
			if (r) {
				$.post("${ctx}/authority/authority.do?method=deleteParam", {
					ids : ids
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
			}
		});
	};
	$.viewParam = function() {
		var paramId = $("#queryParamId").val();
		var paramName = $("#queryParamName").val();
		$('#viewParam').datagrid({
			width : '100%',
			title : '参数管理',
			height : $(window).height() * 0.9,
			nowrap : false,
			fitColumns : true,
			url : "${ctx}/authority/authority.do?method=getFrParam",
			pageSize : 20,
			pageNumber : 1,
			queryParams : {
				paramId : paramId,
				paramName : paramName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "ck",
				checkbox : true,
				width : "150"
			}, {
				field : "paramId",
				title : "参数ID",
				width : "200",
				align : "center",
				sortable : true
			}, {
				field : "paramName",
				title : "参数名称",
				width : "200",
				align : "center",
				sortable : true
			}, {
				field : "paramValue",
				title : "参数值",
				width : "600",
				align : "center",
				sortable : true
			}, {
				field : "paramDes",
				title : "参数描述",
				width : "200",
				align : "center",
				sortable : true
			}, ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar : [ {
				id : 'btnadd',
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {

					$.openWin(-1);
				}
			}, '-', {
				id : 'btncut',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {

					$.openWin(-2);
				}
			}, '-', {
				id : 'btnsave',
				text : '删除',
				iconCls : 'icon-cut',
				handler : function() {
					$.deleteAuthority();

				}
			} ]
		});
		var p = $('#viewParam').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	$(function() {
		$.viewParam();
		$('#ParamSave').window({
			onBeforeClose : function(){
				$.hideDivShade();
			}
        });
	});
</script>
</head>
<body id="indexd">
	<table width="100%">
		<tr>
			<td width="60">参数ID：</td>
			<td width="160" align="left">
				<input type="text"  id="queryParamId" style="width: 150px;" />
			</td>
			<td width="60" align="left">参数名称:</td>
			<td width="160" align="left">
				<input type="text"  id="queryParamName" style="width: 150px;" />
			</td>
			<td>
				<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewParam()">查询</a>
			</td>
		</tr>
	</table>
	<table id="viewParam"></table>

	<div id="ParamSave" class="easyui-window" title="参数修改" false="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="true" draggable="true"
		style="width: 580px; height: 330px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;"
	>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="powerId" name="powerId" />
				<table width="550">
				<tr height="30px;">
                        <td align="right">参数ID：</td>
                        <td align="left">
                            <input type="text" name="paramId" id="paramId" />
                        </td>
                    </tr>
					<tr height="30px;">
						<td align="right">参数名称：</td>
						<td align="left">
							<input type="text" name="paramName" id="paramName" />
						</td>
					</tr>
					<tr height="30px;">
						<td align="right">参数值：</td>
						<td align="left" >
							<textarea rows="2" cols="48" id="paramValue" name="paramValue"></textarea>
						</td>
					</tr>
					<tr height="30px;">
						<td align="right">参数描述：</td>
						<td align="left" colspan="3">
							<textarea rows="2" cols="48" id="paramDes" name="paramDes"></textarea>
						</td>
					</tr>
					<tr height="40px;">
						<td align="left" colspan="4">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		
	</script>
</body>
</html>
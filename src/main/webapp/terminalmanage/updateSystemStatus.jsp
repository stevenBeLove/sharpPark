<%@page import="com.compass.utils.ConstantUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>瑞通宝综合管理系统</title>
<style>
.datagrid-cell-rownumber{
	width:50px;
	text-align:center;
	margin:0px;
	padding:3px 0px;
	color:#000;
}
.datagrid-header-rownumber{
	width:50px;
	text-align:center;
	margin:0px;
	padding:3px 0px;
}
</style>
<script type="text/javascript">
$(window).resize(function(){
	 $('#viewTerminalstatus').datagrid('resize', {
	 	width:cs()
	 });
}); 
var num=0;
function cs(){	
	return $(window).width()-8;
}
$(function(){
	$.viewTerminalstatus();
	$('#updateSystemStatus').linkbutton('disable'); 
 });
		//加载数据表格
	$.viewTerminalstatus = function() {
		var startCode = $("#terminalStartCode").val();
		var endCode = $("#terminalEndCode").val();
		var systemId = $('#systemId').combobox('getValue');
		if(startCode != ""){
			if(startCode.length < 15 || startCode.length > 16){
				$.messager.alert("提示","起始编号不能小于15位大于16位!");
				return;
			}
		}
		if(endCode != ""){
			if(endCode.length < 15 || endCode.length > 16){
				$.messager.alert("提示","结束编号不能小于15位大于16位!");
				return;
			}
		}
		if(endCode != "" && startCode != ""){
			if(endCode < startCode){
				$.messager.alert("提示","结束编号必须大于起始编号！");
				return;
			}
		}
		
		$('#viewTerminalstatus').datagrid({
			title : '来源系统变更',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=queryTerminalSystem",
			queryParams:{
				startCode : startCode,
				endCode : endCode,
				systemId : systemId
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess: function(data){
	            num=data.total;
	            if(num > 0){
					$('#updateSystemStatus').linkbutton('enable'); 
				}else if(num == 0){
					$('#updateSystemStatus').linkbutton('disable'); 
				}
		      },
		      columns : [ [
				             {field : "systemSource",title : "来源系统",width : 130,align : "center",sortable : true},
				             {field : "terminalCode",title : "终端识别码",width : 180,align : "center",sortable : true},
				             {field : "terminaltypeName",title : "终端类型",width : 110,align : "center",sortable : true},
				             {field : "agencyName",title : "所属机构",width : 270,align : "center",sortable : true},
				             {field : "terminalStatusStr",title : "终端状态",width : 100,align : "center",sortable : true},
				             {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true,hidden:true},
				             {field : "createId",title : "创建人编号",width : 100,align : "center",sortable : true},
				             {field : "createDt",title : "创建时间",width : 200,align : "center",sortable : true}
				] ],
		         hideColumn : 
		        	   [ [ 
						 {field : "systemId"},
						 {field : "onlyCode"},
						 {field : "agencyId"},
						 {field : "terminaltypeId"},
						 {field : "terminalStatus"}
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		//分页
		var p = $('#viewTerminalstatus').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		
	};
	$(function(){
		$('#batchIssuedView').window({
	        onBeforeClose: function () { //当面板关闭之前触发的事件
	        	$.hideDivShade();
	        }
	    	});
		$('#systemId').combobox({
			url:"${ctx}/system/system.do?method=getCombSystemsSigle",
			valueField:"id",
			textField:"text"
			});
		$('#systemSource').combobox({
			url:"${ctx}/system/system.do?method=getCombSystemsSigle",
			valueField:"id",
			textField:"text"
			});
		
	});	
	
	//终端状态变更
	$.updateSystemStatus = function(){
		var systemId=$('#systemId').combobox('getValue'); 
		var startCode = $("#terminalStartCode").val();
		var endCode = $("#terminalEndCode").val();
	  	if(startCode ==""){
	    	$.messager.alert("提示","请输入起始编号！");
	      	return;
	    }
	  	if(endCode ==""){
	    	$.messager.alert("提示","请输入结束编号！");
	      	return;
	    }
	  	if(startCode.length < 15 || startCode.length > 16){
			$.messager.alert("提示","起始编号不能小于15位大于16位!");
			return;
		}
		if(endCode.length < 15 || endCode.length > 16){
			$.messager.alert("提示","结束编号不能小于15位大于16位!");
			return;
		}
		if(endCode < startCode){
			$.messager.alert("提示","结束编号必须大于起始编号！");
			return;
		}
		if(systemId == -1){
			$.messager.confirm('确认','您还没有选择来源系统，您确定想要变更该号段所有来源系统吗？',function(r){    
			    if (r){    
			    	$("#terManageIssuedView").window('open'); 
			    	$.showDivShade('${ctx}');
			    }    
			});
		} else {
			$("#terManageIssuedView").window('open'); 
    		$.showDivShade('${ctx}');
		}
	};
	$.cancle = function(){
		$("#terManageIssuedView").window('close');
		$.hideDivShade();
	};
	
	$.confirm = function(){
		var startCode = $("#terminalStartCode").val();
		var endCode = $("#terminalEndCode").val();
		var systemId=$('#systemId').combobox('getValue');
		var systemSource = $('#systemSource').combobox('getValue'); 
		if (systemSource == '-1'){
			$.messager.alert('提示','请选择来源系统!');
			return;
		}
		$('#confirm').linkbutton('disable');
		$.post("${ctx}/terminalmanage/terminalmanage.do?method=updateSystemStatus", 
	  			{
				startCode :  startCode,
				endCode : endCode,
			    systemId : systemId,
			    systemSource : systemSource
	  			}, function(data) {
	  				$('#confirm').linkbutton('enable');
	  				$.cancle();
	  				$.messager.alert("提示",data.message);
	  				$.viewTerminalstatus();
	  			});
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td id="systr1" align="left">来源系统：</td>
			<td id="systr2" align="left">
			 	<select id="systemId" class="easyui-combobox" name="systemId" data-options="panelHeight:'auto'" style="width: 150px;" editable="false"></select>
			</td>
			<td align="left">开始编号：</td>
			<td align="left"><input type="text" name="terminalStartCode"
				id="terminalStartCode" maxlength="100"/>
			</td>
			<td align="left">结束编号：</td>
			<td align="left"><input type="text" name="terminalEndCode"
				id="terminalEndCode" maxlength="100"/>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalstatus()">查询</a>
			</td>
			<td>
				<a id="updateSystemStatus" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.updateSystemStatus()">变更</a>
			</td>
		</tr>
	</table>
	<table id="viewTerminalstatus"></table>
	
	<div id="terManageIssuedView" class="easyui-window" title="来源系统变更" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 510px; height: 280px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:30px; background: #fff; overflow: hidden;">
				<input type="hidden" id="terminalTypeId" name="terminalTypeId" />
					<table width="100%">
						<tr  style="height: 30px">
							<td align="left">来源系统：</td>
							<td align="left"><select style="width: 200px;" name="systemSource" id="systemSource" class="easyui-combobox" data-options="panelHeight:'auto'"   editable="true"></select> 
							</td>
						</tr>
						<tr style="height: 80px">
							<td align="center" colspan="4">
								<a name="confirm" id="confirm"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.confirm()">确定</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.cancle()">关闭</a>
							</td>
						</tr>
					</table>
			</div>
		</div>
	</div>
</body>
</html>
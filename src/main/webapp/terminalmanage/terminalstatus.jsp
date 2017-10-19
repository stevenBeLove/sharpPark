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
 });
	
	function checkTerminal(){
		 var rows = $('#viewTerminalstatus').datagrid('getSelections');
		if(rows.length==0){
			 $.messager.alert("提示 ","请选择要更改状态的状态");
			   return ;
		}
		/* 
		var terminalCode='';
		for ( var i = 0; i < rows.length; i++) {
			if(rows[i].terminalStatus=='1'){
				terminalCode+= rows[i].terminalCode ;
			}
		} */
		
		$('#btnCheckSuc').linkbutton('disable');
		
		if(num<1){
			return  ;
		}
		var terminalCode = $("#terminalCode").val();
		terminalCode=terminalCode.substring(0,15)
		$.post('${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalstatus', 
				{
					terminalCode : terminalCode
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
	 
	}
	
	 
	$.success = function (message, data) {
		$.messager.alert("提示 ",message);
		 $.close(); 
		 $('#btnCheckSuc').linkbutton('enable');
		 $('#btnCheckFail').linkbutton('enable');
		 $.viewTerminalexamine();
	};
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		$('#btnCheckSuc').linkbutton('enable');
		$('#btnCheckFail').linkbutton('enable');
		 $.close(); 
	};
	$.close = function(){
		$.hideDivShade();
	};
	
	
		//加载数据表格
	$.viewTerminalstatus = function() {
		var terminalCode = $("#terminalCode").val();
		 
		$('#viewTerminalstatus').datagrid({
			title : '终端状态变更',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageStatus",
			queryParams:{
				terminalCode:terminalCode
			},		
			toolbar : [ {
				id : 'btnCheckSuc',
				text : '更改',
				iconCls : 'icon-add',
				handler : function() {
					checkTerminal();
				}
			} ],
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess: function(data){
	            num=data.total;
		      },
			columns : [ [  
			             {field : "ck",checkbox : true,width : "50"},
			             {field : "systemSource",title : "来源系统",width : 100,align : "center",sortable : true},
			          //   {field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
			             {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : 100,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : 300,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : 100,align : "center",sortable : true},
			          //  {field : "openDate",title : "开通时间",width : 200,align : "center",sortable : true}, 
			          //   {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true},
			           //  {field : "scrapDate",title : "报废时间",width : 200,align : "center",sortable : true}, 
			             {field : "createId",title : "创建人编号",width : 200,align : "center",sortable : true},
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
	
	 
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="left">终端编号：</td>
			<td align="left"><input type="text" name="terminalCode"
				id="terminalCode" maxlength="100"/>
			</td>
			<!--
			<td align="left">终端类型：</td>
			<td align="left"><select id="terminalTypeF" class="easyui-combobox" editable="false" name="terminalTypeF" style="width: 150px;height: 30px"></select>
			</td>
			
			--><!-- <td align="left">终端状态：</td> 
			<td align="left" style="display: none;">
				<select id="status" class="easyui-combobox" editable="false" name="status" style="width: 150px;">
					<option selected="selected" value="3">回拨</option>
				</select>
			</td>-->
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalstatus()">查询</a>
			</td>
			
		</tr>
	</table>
	<table id="viewTerminalstatus"></table>

</body>
</html>
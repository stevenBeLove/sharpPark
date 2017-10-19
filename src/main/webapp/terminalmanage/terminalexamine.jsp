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
	 $('#viewTerminalexamine').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-8;
}
$(function(){
	$.viewTerminalexamine();
	$('#terminalTypeF').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});
	});
	
	function checkTerminal(){
		 var rows = $('#viewTerminalexamine').datagrid('getSelections');
		if(rows.length==0){
			 $.messager.alert("提示 ","请选择要审核的记录");
			   return ;
		}
		var agencyId='';
		var terminalCodes='';
		for ( var i = 0; i < rows.length; i++) {
			if(rows[i].terminalStatus=='3'){
				terminalCodes+="'"+rows[i].onlyCode+"',";
				agencyId=rows[i].agencyId;
			}
		}
		
		$('#btnCheckSuc').linkbutton('disable');
		$.post('${ctx}/terminalmanage/terminalmanage.do?method=checkTerminal', 
		{
			agencyId:agencyId,
			terminalCodes : terminalCodes
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		 
	}
	
	function checkTerminalfail(){
		 var rows = $('#viewTerminalexamine').datagrid('getSelections');
		if(rows.length==0){
			 $.messager.alert("提示 ","请选择要审核的记录");
			   return ;
		}
		var agencyId='';
		var terminalCodes='';
		for ( var i = 0; i < rows.length; i++) {
			if(rows[i].terminalStatus=='3'){
				terminalCodes+="'"+rows[i].onlyCode+"',";
				agencyId=rows[i].agencyId;	
			}
		}
		
		$('#btnCheckFail').linkbutton('disable');
		$.post('${ctx}/terminalmanage/terminalmanage.do?method=checkTerminalfail', 
		{
			agencyId:agencyId,
			terminalCodes : terminalCodes
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
	$.viewTerminalexamine = function() {
		var status = $("#status").combobox('getValue');
		var terminalCode = $("#terminalCode").val();
		var terminalTypeId =  $('#terminalTypeF').combobox('getValue');
		$('#viewTerminalexamine').datagrid({
			title : '终端审核',
			width : $(window).width()-8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			fitColumns : false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManagecheck",
			queryParams:{
				terminalCode:terminalCode,
				terminalTypeId:terminalTypeId,
				status:status
			},	
			toolbar : [ {
				id : 'btnCheckSuc',
				text : '审核通过',
				iconCls : 'icon-add',
				handler : function() {
					checkTerminal();
				}
			}, '-', {
				id : 'btnCheckFail',
				text : '审核不通过',
				iconCls : 'icon-edit',
				handler : function() {
					checkTerminalfail();
					
				}
			} ],
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [  
					     {field : "ck",checkbox : true,width : "50"},
					     {field : "systemSource",title : "来源系统",width : 100,align : "center",sortable : true},
					  //  {field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
					     {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : 100,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : 300,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : 100,align : "center",sortable : true},
			             {field : "terminalDesc",title : "终端描述",width : $(window).width()*0.15,align : "center",sortable : true},
			          //   {field : "openDate",title : "开通时间",width : 200,align : "center",sortable : true}, 
			          //   {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true},
			          //   {field : "scrapDate",title : "报废时间",width : 200,align : "center",sortable : true}, 
			             {field : "createId",title : "创建人编号",width : 200,align : "center",sortable : true},
			             {field : "createDt",title : "创建时间",width : 200,align : "center",sortable : true}
			           ] ],
		         hideColumn : 
		        	   [ [ 
						 {field : "onlyCode"},
						 {field : "agencyId"},
						 {field : "terminaltypeId"},
						 {field:"terminalStatus"},
						 {field : "systemId"}
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		//分页
		var p = $('#viewTerminalexamine').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		
	};
	$.save=function(){
		var rows = $('#viewTerminalexamine').datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示 ", "请选择要审核的记录!");
			return;
		}
		
		var Ids = "";
		for ( var i = 0; i < rows.length; i++) {
			if (i == rows.length - 1) {
				Ids += "'" + rows[i].onlyCode + "'";
			} else {
				Ids += "'" + rows[i].onlyCode + "'" + ",";
			}
			
		}
	  			
	  	$.post('${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalIssued', 
  			{
	  			agencyName : agencyName,
  				Ids : Ids
  			}, function(data) {
  				$.parseAjaxReturnInfo(data, $.success, $.failed);
  			}, "json");
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
			<td align="left">终端类型：</td>
			<td align="left"><select id="terminalTypeF" class="easyui-combobox" editable="false" name="terminalTypeF" style="width: 150px;height: 30px"></select>
			</td>
			
			<!-- <td align="left">终端状态：</td> -->
			<td align="left" style="display: none;">
				<select id="status" class="easyui-combobox" editable="false" name="status" style="width: 150px;">
					<option selected="selected" value="3">回拨</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalexamine()">查询</a>
			</td>
			
		</tr>
	</table>
	<table id="viewTerminalexamine"></table>

</body>
</html>
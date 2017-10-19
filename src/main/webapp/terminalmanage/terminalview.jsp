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
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString().trim()%>';
var staticagecyId='<%=ConstantUtils.CENTERCODE%>';

$(window).resize(function(){
	 $('#viewTerminalView').datagrid('resize', {
	 	width:cs()
	 });
});

function cs(){	
	return $(window).width()-8;
}

$(function(){
 
		$('#agencytreeId').combotree({
				  url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',    
		   		 method:'get',
		  		  readonly:false,
		       onLoadSuccess:function(a,b){
		    	$('#agencytreeId').combotree('setValue', '-1');
		      },
	    	  panelWidth:   350
		});
	
		$('#agencytreeIdOld').combotree({
				  url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',    
		   		 method:'get',
		  		  readonly:false,
		       onLoadSuccess:function(a,b){
		    	$('#agencytreeIdOld').combotree('setValue', '-1');
		      },
	    	  panelWidth:   350
		});
		$.viewTerminalView(3);
		
	 
});
	
	//加载数据表格
	$.viewTerminalView = function(opt) {
	    var agencyId= $("#sagencyId").val();
		var selAgencyId = $("#agencytreeId").combotree('getValue');
	    var agencyIdOld= $("#sagencyIdOld").val();
		var selAgencyIdOld = $("#agencytreeIdOld").combotree('getValue');
		var startCode = $("#startCode").val();
		var endCode = $("#endCode").val();
		var  datestart = $('#yearmonthdatestart').val();
		var  dateend = $('#yearmonthdateend').val();
		if(selAgencyId=="-1"){
			selAgencyId="";
		}
		 if(selAgencyIdOld=="-1"){
			selAgencyIdOld="";
		}
		$('#viewTerminalView').datagrid({
			title : '终端记录查询',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			nowrap : true,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalView",
			queryParams:{
				agencyId:agencyId,
				selAgencyId:selAgencyId,
			    agencyIdOld:agencyIdOld,
				selAgencyIdOld:selAgencyIdOld,
				startCode:startCode,
				endCode:endCode,
				datestart:datestart,
				dateend:dateend,
				opt:opt
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [  
			             {field : "terminalId",title : "终端编号",width : 150,align : "center",sortable : true},
			             {field : "agencyid",title : "当时机构",width : 150,align : "center",sortable : true},
			             {field : "oldagencyid",title : "先前机构",width : 150,align : "center",sortable : true},
			             {field : "xfdate",title : "下发时间",width : 150,align : "center",sortable : true},
			             {field : "hbdate",title : "回拔时间",width :150,align : "center",sortable : true},
			             {field : "username",title : "操作人",width : 200,align : "center",sortable : true}
			           ] ],
			hideColumn : [ [ {
								field : "username" 
							} ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		});
		//分页
		var p = $('#viewTerminalView').datagrid('getPager');
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
			<td align="left">起始编号：</td>
			<td align="left"><input type="text" name="startCode" id="startCode" maxlength="100" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/></td>
			<td align="left">结束编号：</td>
			<td align="left"><input type="text" name="endCode" id="endCode" maxlength="100" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/></td>
		</tr>
		<tr>
			<td align="left">当时机构名称：</td>
			<td align="left"><input type="text" name="sagencyId" id="sagencyId" maxlength="100" style="width: 150px;"/></td>
			<td align="left">当时机构名称：</td>
			<td align="left"><select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true"></select></td>	
		</tr>
		<tr>
			<td align="left">先前机构名称：</td>
			<td align="left"><input type="text" name="sagencyIdOld" id="sagencyIdOld" maxlength="100" style="width: 150px;"/></td>
			<td align="left">先前机构名称：</td>
			<td align="left"><select id="agencytreeIdOld" editable="true" class="easyui-combotree" name="agencytreeIdOld" style="width: 150px;" selected="true"></select></td>	
		</tr>
		<tr>
		<td align="left">注册日期：</td>
			<td align="left" >
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			 -
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalView(2)">下发查询</a></td>
			<td align="center"><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.viewTerminalView(1)">回拔查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.viewTerminalView(3)">查询所有</a></td>
		</tr>
	</table>
	
	<table id="viewTerminalView"></table>
	
	 
	 
</body>
</html>
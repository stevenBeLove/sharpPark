<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
input{ vertical-align:middle; margin:0; padding:0}
.file-box{ position:relative;width:340px}
.txt{ height:30px; border:1px solid #cdcdcd; width:180px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:30px; width:70px;}
.file{ position:absolute; top:0; right:80px; height:30px; filter:alpha(opacity:0);opacity: 0;width:260px }
</style>
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
	 $('#viewLogDetail').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
$(function(){
		$('#agencyId').combotree({    
		    url: '${ctx}/agency/agency.do?method=getAgencyTree',    
		    method:'get',
		    readonly:false,
		    onLoadSuccess:function(a,b){
		    	$('#agencyId').combotree('setValue', '-1');
		    }
		});
	
	$('#operateType').combobox({
		url:"${ctx}/systemlog/systemlog.do?method=getOperateType",
		valueField:"id",
		textField:"text",
		});

	var myDate = new Date();
	var months=myDate.getMonth()+1;
	var day=myDate.getDate();
	
	if(months<10){
		months="0"+months;
	}
	if(day<10){
		day="0"+day;
	}
	var yearmonthdatestart=myDate.getFullYear()+''+months+day;
	$("#yearmonthdatestart").val(yearmonthdatestart);
	var yearmonthdateend=myDate.getFullYear()+''+months+day;
	$("#yearmonthdateend").val(yearmonthdateend);
	$.viewLogDetail();
});
	

	/* $.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewLogDetail();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	
	}; */

	$.viewLogDetail = function() {
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var agencyId=$('#agencyId').combotree('getValue');
		var operateMan = $('#operateMan').val();
		var operateName =$('#operateName').val();
		var operateType=$('#operateType').combobox('getValue');
			
		if(agencyId=='-1' || agencyId==null){
			agencyId='';
		}
 
		$('#viewLogDetail').datagrid(
						{
						title : '明细查询',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/systemlog/systemlog.do?method=getLogDetail",
						queryParams : {		
							yearmonthdatestart:yearmonthdatestart,						
							yearmonthdateend:yearmonthdateend,
							agencySelect:agencyId,
							operateMan : operateMan,
							operateName:operateName,
							operateType:operateType
						},
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						
						columns : [ [
										{field : "ipAddress",width : 100,title : "IP地址",align : "center",sortable : true},
										{field : "operateAgency",width : 100,title : "操作机构",align : "center",sortable : true},
										{field : "operateMan",width : 100,title : "操作人",align : "center",sortable : true},
										{field : "operateTime",width : 150,title : "操作时间",align : "center",sortable : true},
										{field : "operateName",width : 100,title : "操作名称",align : "center",sortable : true},
										{field : "operateType",width : 100,title : "操作类型",align : "center",sortable : true},
										{field : "operateDetail",width : 400,title : "操作详情",align : "center",sortable : true},
										 ] ],
						
						pagination : true,
						rownumbers : true,
						showFooter : true,
					});
	var p = $('#viewLogDetail').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '    第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '   当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
	
};
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr style="height: 30px">
		<td align="left">操作开始日期：</td>
		<td align="left">
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
		</td>
		<td align="left">操作结束日期：</td>
		<td align="left">
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		</td>
		<td align="left">机构名称：</td>
		<td align="left">
			 <input class="easyui-combotree" id="agencyId"  name="agencyId" style="width:160px;height: 20px" />&nbsp;&nbsp;&nbsp;  
		</td>
		<td>
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewLogDetail()">查询</a>
		</td>
</tr>
<tr>
		<td align="left">操作人：</td>
		<td align="left">
			 <input id="operateMan"  name="operateMan" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  
		</td>
		
		<td align="left">操作名称：</td>
		<!-- <td align="left"><input id="dealType"  name="dealType" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  </td> -->
		<td><input id="operateName"  name="operateName" style="width:110px;height: 20px"/></td>
		
		<td align="left">操作类型： </td>
		<td align="left">
		  <select id="operateType" class="easyui-combobox" name="operateType data-options="panelHeight:'auto'" style="width: 150px;" ></select>
		</td>
		
		</tr>
	</table>		



<table id="viewLogDetail"> </table>
	
</body>
</html>
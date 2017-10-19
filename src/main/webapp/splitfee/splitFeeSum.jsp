<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%@ page import="com.compass.utils.ConstantUtils;" %>

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
var agency_id='<%=session.getAttribute(ConstantUtils.AGENCYID)%>';

$(function(){
	$('#agencyId').combotree({    
	    url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',
	    method:'get',
	    readonly:false,
	    onLoadSuccess:function(a,b){
	    },
	    panelWidth:   350
	});  
	
	$(".combo-text").bind("input onchange",function(a,b){
		$('#agencyId').combotree('tree').tree("search",$(this).val());
		if($(this).val()==""||null==$(this).val()){
			$('#agencyId').combotree('tree').tree("collapseAll");
		} 
});
	
});

$.spliteOne=function(){
		var datestart = $('#datestart').val();
		var dateend = $('#dateend').val();
		var agencyId = $("#agencyId").combotree('getValue');
		var parentAgencyId="";
		if(""==datestart){
			$.messager.alert("提示 ", "请选择开始日期 ");
			return ;
		}
		if(""==dateend){
			$.messager.alert("提示 ", "请选择结束日期 ");
			return ;
		}
		if(""==agencyId){
			$.messager.alert("提示 ", "请选择下级机构 ");
			return ;
		}
		if("-1"==agencyId){
			$.messager.alert("提示 ", "请选择下级机构 ");
			return ;
		}
		if(agency_id==agencyId){
			$.messager.alert("提示 ", "请选择下级机构 ");
			return ;
		}
		
		if(datestart==dateend){
			$.messager.alert("提示 ", "请选择日期 ");
			return ;
		}
		$.post("${ctx}/splitfee/splitfee.do?method=splitFeeSum",
			 {
				datestart:datestart,
				dateend:dateend,
				agencyId:agencyId,
				parentAgencyId:parentAgencyId
			 }
			, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
}
$.spliteAll=function(){
		var datestart = $('#datestart').val();
		var dateend = $('#dateend').val();
		var agencyId = "";
		var parentAgencyId=agency_id;
		if(""==datestart){
			$.messager.alert("提示 ", "请选择开始日期 ");
			return ;
		}
		if(""==dateend){
			$.messager.alert("提示 ", "请选择结束日期 ");
			return ;
		}
		if(datestart==dateend){
			$.messager.alert("提示 ", "请选择日期 ");
			return ;
		}
		 
		$.post("${ctx}/splitfee/splitfee.do?method=splitFeeSum",
			 {
				datestart:datestart,
				dateend:dateend,
				agencyId:agencyId,
				parentAgencyId:parentAgencyId
			 }
			, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
}
 
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
};

$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
};
</script>
  </head>
  
  <body>
  
  <body id="indexd" bgcolor="#ff0000">
	<table >
	<tr>
		
		<td>开始日期：</td>
		<td>
			<input id="datestart" name="datestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateend\' )}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		</td>
		<td>结束日期：</td>
		<td>
			<input id="dateend" name="dateend" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'datestart\' )}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})""/>
		</td>
		<td>机构编号：</td>
		<td >
			<input class="easyui-combotree" id="agencyId"  name="agencyId"
						editable="true"	 style="width:150px;" />
		</td>	
		
		<td >
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.spliteOne()">申请计算 </a>
		</td>
		<td >
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.spliteAll()">申请计算所有下级 </a>
		</td>
	</tr>
	<tr>
	   <td colspan="4"> 
	   <span>   申请的分润计算将在第二天凌晨进行重新计算 ,请第二天上午进行查询 。  </span>
	   </td>
	</tr>
	 
	
	</table>
  </body>
</html>

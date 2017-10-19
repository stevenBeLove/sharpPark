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
	 
}); 
function cs(){	
	return $(window).width()-6;
}



$(function(){
	 
	$('#prepbranchid').combobox({
				url:'${ctx}/data/data.do?method=getPrepBranchid',
				valueField:'id',
				textField:'text',
				editable:false,
				onLoadSuccess:function(){
					$("#prepbranchid").combobox('select',-1); 
					   
				},
				onSelect:function(row){
						$('#branchid').combobox({
							url:'${ctx}/data/data.do?method=getBranchid&prepBranchid='+row.id,
							valueField:'id',
							textField:'text',
							editable:false,
							onLoadSuccess:function(){
								$("#branchid").combobox('select',-1); 
							}
						});
				  	}
				});
	
	 
});

// 交易记录导入
$.dealDataSave=function(){
	var dealData_date= $('#dealData_date').val();
	alert(dealData_date);
	 $.post("${ctx}/data/data.do?method=dealImp" , {
			execDate : dealData_date 
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	

};

//一级代理商分润计算
$.fristFrSave=function(){
	var fristFr_startDate= $('#fristFr_startDate').val();
	var fristFr_endDate= $('#fristFr_endDate').val();
	var  prepbranchid = $("#prepbranchid").combobox('getValue');   ;
	var branchid= $("#branchid").combobox('getValue');   ;
	 $.post("${ctx}/data/data.do?method=fristCal" , {
			startDate : fristFr_startDate,
			endDate : fristFr_endDate,
			prepbranchid: prepbranchid,
			branchid: branchid,
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
};

//代理商分润计算
$.agencyFrSave=function(){
	var agencyFr_startDate= $('#agencyFr_startDate').val();
	var agencyFr_endDate= $('#agencyFr_endDate').val();
	
	 $.post("${ctx}/data/data.do?method=agencyFrSave" , {
			startDate : agencyFr_startDate,
			endDate : agencyFr_endDate
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	
};


//分润重算申请计算
$.againCalFr=function(){
	 $.post("${ctx}/data/data.do?method=againCalFrSave" , {

		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
};

//终端激活
$.terminalActive=function(){
	$.post("${ctx}/data/data.do?method=terminalActive",{
		
			},function(data){
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			},"json");
};

$.success = function (message, data) {
	$.messager.alert("提示 ",message);
	 $('#saveButton').linkbutton('enable');
	 $.close(); 
	 $.viewAgency();
};
$.failed = function (message, data) {
	$.messager.alert("提示 ",message);
	 $('#saveButton').linkbutton('enable');
};
 
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<br/>
	<fieldset>
		<legend> 交易记录导入</legend>
			<table>
			  <tr>  
			  		<td> 导入日期 : </td>
			  		<td width="250px">
			  			<input id="dealData_date" name="dealData_date" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'%y-%M-%d', dateFmt:'yyyyMMdd'})"/>
			  		 </td>
			  		<td> <input  type="button" id="dealData" value="确定" name="dealData"  onclick="$.dealDataSave()"  /> </td>
			  </tr>
			</table>
	</fieldset>
	<br/>
 	<fieldset>
		<legend>一级代理商分润计算</legend>
			<table>
			  <tr>  
			  		<td>
			  			顶级机构：
			  			<input class="easyui-combobox" id="prepbranchid"  name="prepbranchid" style="width:155px;"/>
			  		</td>
			  		<td>
			  			品牌机构：
			  			<input class="easyui-combobox" id="branchid"  name="branchid" style="width:155px;"/>
			  		</td>
			  		<td> 计算日期:  </td>
			  		<td width="250px"> 
			  			 <input id="fristFr_startDate" name="fristFr_startDate" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'fristFr_endDate\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
						 -
						 <input id="fristFr_endDate" name="fristFr_endDate" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'fristFr_startDate\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		
			  		</td>
			  		<td> <input type="button" id="fristFr" value="确定" name="fristFr"   onclick="$.fristFrSave()" /> </td>
			  </tr>
			</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>代理商分润计算</legend>
			<table>
			  <tr>  
			  		<td>  计算日期: </td>
			  		<td width="250px">
			  			 <input id="agencyFr_startDate" name="agencyFr_startDate" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'agencyFr_endDate\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
						 -
						 <input id="agencyFr_endDate" name="agencyFr_endDate" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'agencyFr_startDate\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
					 </td>
			  		<td> <input  type="button" id="agencyFr" value="确定" name="agencyFr"  onclick="$.agencyFrSave()" /> </td>
			  </tr>
			</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>分润重算申请计算 </legend>
			<table>
			  <tr>  
			  		 
			  		<td> <input type="button" id="againCalFr" value="确定" name="againCalFr"  onclick="$.againCalFr()"/> </td>
			  </tr>
			</table>
	</fieldset>
		<fieldset>
		<legend>终端激活 </legend>
			<table>
			  <tr>  
			  		 
			  		<td> <input type="button" id="terminalActive" value="确定" name="terminalActive"  onclick="$.terminalActive()"/> </td>
			  </tr>
			</table>
	</fieldset>
	 
</body>
</html>
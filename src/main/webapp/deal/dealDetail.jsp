<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
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
var agencyQueryFlag='<%=session.getAttribute(ConstantUtils.AGENCYQUERYFLAG)%>';


$(window).resize(function(){
	 $('#viewDealDetail').datagrid('resize', {
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
	    },
	    panelWidth:   350
	});
		/* $('#agencyId').combotree({
			url : '${ctx}/agency/agency.do?method=getChildList&agencyId=',
			method : 'get',
			readonly : false,
			onLoadSuccess : function(a, b) {
				$('#agencyId').tree({}); 
			},
			onBeforeExpand : function(node, b) {
				var children=$('#agencyId').tree('getChildren',node.target);
				if(children&&children.length==0){
					$.ajax({
	                    type : "post",
	                    url : '${ctx}/agency/agency.do?method=getChildList&agencyId='+node.id,
	                    dataType : "json",
	                    success : function(data) {
	                        $('#agencyId').tree('append', {
	                            parent : node.target,
	                            data : data
	                        });
	                        $("#agencyId").tree("reload");
	                    }
	                });	
				}
			},
			panelWidth : 350
		}); */
		
		
		$('#dealTypeId').combobox({
			url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypeByAgencyId",
			valueField:"id",
			textField:"text"
		});
		
		$(".combo-text").bind("input onchange",function(a,b){
			$('#agencyId').combotree('tree').tree("search",$(this).val());
			if($(this).val()==""||null==$(this).val()){
				$('#agencyId').combotree('tree').tree("collapseAll");
			} 
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
	if("false"==agencyQueryFlag){
     	$("#queryFlag").css("display","none");
	}
	//$.viewDealDetail();
});
	$.openWin= function(){
		 $("#dealImport").window('open').window('refresh');
		 
	}; 

	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewDealDetail();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	
	};
	$.close = function() {
		$.hideDivShade();
		$("#dealImport").window('close');
	};

	//导出交易明细
	$.exportDealDetail=function(yearmonthdatestart,yearmonthdateend){
		
		var agencyId=$('#agencyId').combotree('getValue');
		if(agencyId=='-1' || agencyId==null){
			agencyId='';
		}
		var merchantCode =  '';
		var serialNumber = $('#serialNumber').val();
		var dealTypeId = $('#dealTypeId').combobox('getValue');
		var terminalcode=$('#terminalcode').val();
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var queryFlag=$("input[name='queryFlag']:checked").val();
		var moblieNo=$('#moblieNo').val();
		var customerName=$('#customerName').val();
		$.getToPost('${ctx}/deal/deal.do?method=exportDealDetailCSV',{
				 yearmonthdatestart:yearmonthdatestart,
				 yearmonthdateend:yearmonthdateend,
				 agencySelect:agencyId,
				 dealType : dealTypeId,
				 serialNumber : serialNumber,
				 terminalcode : terminalcode,
				 merchantCode : merchantCode,
				 queryFlag:queryFlag,
				 moblieNo:moblieNo,
				 customerName:customerName
			   });
	};
	
	$.viewDealDetail = function() {
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var agencyId=$('#agencyId').combotree('getValue');
		var serialNumber = $('#serialNumber').val();
		//var dealType = $('#dealType').val();
		var dealTypeId = $('#dealTypeId').combobox('getValue');
		var terminalcode=$('#terminalcode').val();
		var queryFlag=$("input[name='queryFlag']:checked").val();
		var moblieNo=$('#moblieNo').val();
		var customerName=$('#customerName').val();
		//var merchantCode = $('#merchantCode').val();
			var merchantCode =  '';
		if(agencyId=='-1' || agencyId==null){
			agencyId='';
		}
 
		$('#viewDealDetail').datagrid(
						{
						title : '交易明细查询',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/deal/deal.do?method=getDealdetail",
						queryParams : {		
							yearmonthdatestart:yearmonthdatestart,						
							yearmonthdateend:yearmonthdateend,
							agencySelect:agencyId,
							serialNumber:serialNumber,
							dealType:dealTypeId,
							terminalcode:terminalcode,
							merchantCode:merchantCode,
							queryFlag:queryFlag,
							 moblieNo:moblieNo,
							 customerName:customerName
						},
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						onLoadSuccess: function(data){
							var val;
							if(dealTypeId==''||dealTypeId=='-1'){
								 val = "收款总金额："+data.accountCount  +"元        收款总手续费： "+ data.feeAmtcount+"元    T+0总金额："  +  data.t0Count +"元 ";
							}else{ 
								if(dealTypeId=="7"){
									 val = $('#dealTypeId').combobox('getText')+"总金额："+data.accountCount  +"元  ";
								}else{
									 val = $('#dealTypeId').combobox('getText')+"总金额："+data.accountCount  +"元        收款总手续费： "+ data.feeAmtcount+"元  ";
								}
							}
						 
						   $("#counts").text(val);
						},
						columns : [ [
										//{field : "sysSource",width : 100,title : "来源系统",align : "center"},
										{field : "agencyName", title : "机构名称",align : "center",sortable : true},
										//{field : "merchantCode"title : "商户号",width : 200,align : "center",sortable : true},
										{field : "terminal_OnlyCode",sortable : true,title : "终端编号",align : "center"},
										{field : "dealTypeName",sortable : true,title : "交易类型",align : "center"},
										{field : "serialNumber",sortable : true,title : "交易流水号",align : "center"},
										{field : "deal_data",sortable : true,title : "交易日期",align : "center"},
										{field : "deal_time",sortable : true,title : "交易时间",align : "center"},
										{field : "transacount",sortable : true,title : "交易金额(元)",align : "center"},
										{field : "feeAmt",sortable : true,title : "手续费金额(元) ",align : "center"},
										//{field : "dealrebackcode",width : 100,title : "交易返回码",align : "center"},
										//{field : "splitAmont",width : 100,title : "可分润金额(元) ",align : "center"},
									//	{field : "dealdesc",width : 100,title : "交易描述 ",align : "center"},
										{field : "bankcardNumber",sortable : true,title : "银行卡号",align : "center"},
										{field : "moblieNo",sortable : true,title : "手机号码",align : "center"},
										{field : "customerName",sortable : true,title : "客户姓名 ",align : "center"},
									//	{field : "deal_status",width : 100,title : "交易状态 ",align : "center"},
										 ] ],
						hideColumn : [ [ 
						                 {field : "dealId"},
						                 {field : "dealtype_id"},
						                 {field : "deal_statusStr"}
						              ] ], 
						pagination : true,
						rownumbers : true,
						showFooter : true,
					});
	var p = $('#viewDealDetail').datagrid('getPager');
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
		<td align="left">交易开始日期：</td>
		<td align="left">
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
		</td>
		<td align="left">交易结束日期：</td>
		<td align="left">
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		</td>
		
		
</tr>
<tr>
		<td align="left">流水号：</td>
		<td align="left">
			 <input id="serialNumber"  name="serialNumber" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  
		</td>
		
		<td align="left">交易类型：</td>
		<!-- <td align="left"><input id="dealType"  name="dealType" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  </td> -->
		<td><select id="dealTypeId" class="easyui-combobox" name="dealTypeId" data-options="panelHeight:'auto'" style="width: 110px;"  editable="false"></select></td>
		
		
		</tr>
	  <tr>
	  	<td align="left">终端编号：   <!--商户号：--></td>
		<td align="left">
		  <input id="terminalcode"  name="terminalcode" style="width:160px;height: 20px" />&nbsp;&nbsp;&nbsp;  
			  <!--<input id="merchantCode"  name="merchantCode" style="width:120px;height: 20px" />&nbsp;&nbsp;&nbsp;  
	 
		--></td>
		<td align="left">机构名称：</td>
		<td align="left">
			 <input class="easyui-combotree" id="agencyId"  name="agencyId" style="width:160px;height: 20px"  editable="true" />&nbsp;&nbsp;&nbsp;  
		</td>
		<td align="left">
					<span id="queryFlag">		 <input type="radio" id="queryFlag" name="queryFlag" value="1" checked="checked">按当前机构
				  <input type="radio" id="queryFlag" name="queryFlag" value="2"/>按归属机构 
				  		<span id="queryFlag">	
			</td>
		
	  </tr>
	  <tr>
		<td align="left">手机号码：</td>
		<td align="left">
			 <input id="moblieNo"  name="moblieNo" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  
		</td>
		
		<td align="left">客户姓名：</td>
			<td>
		 		<input id="customerName"  name="customerName" style="width:110px;height: 20px" />&nbsp;&nbsp;&nbsp;  
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealDetail()">查询</a>
			</td>
			<td>
				<a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.exportDealDetail(yearmonthdatestart,yearmonthdateend)" >导出</a>
			</td>
		</tr>
	</table>		

	<table>
	<tr>
	<td>
	  <div id="counts"></div>
	</td>
	</tr>
	</table>

<table id="viewDealDetail"> </table>
	
</body>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>交易及分润明细查询</title>
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
		$('#business_name').combobox({
			url:"${ctx}/fee/queryTradeProfit.do?method=getBusiness",
			valueField:"id",
			textField:"text"
		});
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

	
	//清空查询条件
	$.cleanDetail=function(){
		$("#yearmonthdatestart").val(""); 
		$("#yearmonthdateend").val("");
		$("#logNo").val("");
		$("#psamId").val("");
		$("#mobileNo").val(""); 
		$("#business_name").combobox({});
		$("#agency_Name").combogrid({});
	}
	
	//导出交易明细
	$.exportDetail=function(yearmonthdatestart,yearmonthdateend,agency_Name,queryFlag,business_name,logNo,psamId,mobileNo){
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var queryFlag = $("input[name='queryFlag']:checked").val();
		var business_name = $('#business_name').combobox('getValue');
		var logNo = $('#logNo').val();
		var psamId = $('#psamId').val();
		var mobileNo = $('#mobileNo').val();
		var agency_Id = $('#agency_Name').combogrid('getValue');
    	$.getToPost("${ctx}/fee/queryTradeProfit.do?method=exportDetail",{
		    		yearmonthdatestart:yearmonthdatestart,						
					yearmonthdateend:yearmonthdateend,
					agency_Id:agency_Id,
					queryFlag:queryFlag,
					business_name:business_name,
					logNo:logNo,
					psamId:psamId,
					mobileNo:mobileNo
		});
	};
	//查询交易及分润明细查询
$.viewDealDetail = function() {
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var queryFlag = $("input[name='queryFlag']:checked").val();
		var business_name = $('#business_name').combobox('getValue');
		var logNo = $('#logNo').val();
		var psamId = $('#psamId').val();
		var mobileNo = $('#mobileNo').val();
		var agency_Id = $('#agency_Name').combogrid('getValue');
		$('#viewDealDetail').datagrid(
						{
						title : '交易及分润明细查询',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/fee/queryTradeProfit.do?method=queryTradeProfit",
						queryParams : {		
							yearmonthdatestart:yearmonthdatestart,						
							yearmonthdateend:yearmonthdateend,
							agency_Id:agency_Id,
							queryFlag:queryFlag,
							business_name:business_name,
							logNo:logNo,
							psamId:psamId,
							mobileNo:mobileNo
						},
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						onLoadSuccess: function(data){
							$("#dealAmount").text(data.dealAmountSum);
							$("#poundageAmount").text(data.poundageAmountSum);
						    $("#allAmount").text(data.fee_AmountSum);
						    $("#childAmount").text(data.next_feeAmountSum);
						    $("#differAmount").text(data.difference_feeAmountSum);
						},
						columns : [ [
										{field : "agencyName", sortable : true, title : "机构名称", align : "center"},
										{field : "mobileNo", sortable : true, title : "手机号", align : "center"},
										{field : "locallogno", sortable : true, title : "交易流水号", align : "center"},
										{field : "psamid",sortable : true, title : "终端号", align : "center"},
										{field : "businessName", sortable : true, title : "交易类型", align : "center"},
										{field : "localdate", sortable : true, title : "交易日期", align : "center"},
										{field : "localtime", sortable : true, title : "交易时间", align : "center"},
										{field : "amount", sortable : true, title : "交易金额(元)", align : "center"},
										{field : "rtbfee", sortable : true, title : "手续费(元) ", align : "center"},
										{field : "feerate", sortable : true, title : "终端费率百分比(%)", align : "center",
											formatter: function(value,row,index){
												if (value.length >= 5 && value != null){
													 result = value.substr(0,value.indexOf(".")+3);
													return result;
												} else {
													return value;
												}
											}
										},
										{field : "fixFee", sortable : true, title : "终端消费单笔(元)", align : "center"},
										{field : "costRate", sortable : true, title : "本级分润成本百分比(%) ", align : "center"},
										{field : "costFix", sortable : true, title : "本级分润成本单笔(元)", align : "center"},
										{field : "childCostRate", sortable : true, title : "直属下级分润成本百分比(%)", align : "center"},
										{field : "childCostFix", sortable : true, title : "直属下级分润成本单笔(元) ", align : "center"}, 
										{field : "childAmount", sortable : true, title : "直属下级分润金额(元)", align : "center"}, 
										/* {field : "differRate", sortable : true, title : "与上级相差分润费率比(%) ", align : "center"},
										{field : "differCostFix", sortable : true, title : "与上级相差固定值(元) ", align : "center"}, */
										{field : "sumAmount", sortable : true, title : "分润总金额(元)", align : "center"},
										{field : "frAmount", sortable : true, title : "分润差值(元)", align : "center"}
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
<script type="text/javascript">
        $(function(){
            $('#agency_Name').combogrid({
                panelWidth:380,
                url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
    			queryParams : {
    				rows : 40
    			},
                idField:'agency_id',
                textField:'companyName',
                mode:'remote',
                fitColumns:true,
                columns:[[
            {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			}
			]]
            });
        });
        </script>
</head>
<body id="indexd" bgcolor="#ff0000">
<table>
	<tr style="height: 30px">
		<td align="left">开始日期：</td>
		<td align="left">
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
		</td>
		
		<td align="left">结束日期：</td>
		<td align="left">
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		</td>
		
		<td align="left">机构查询：</td>
		<td align="left">
			  <input id="agency_Name" style="width:150px"></input>
		</td>
		
		<td align="left">
			<span id="queryFlag">		 
				<input type="radio" id="queryFlag" name="queryFlag" value="1" checked="checked">本级自营(当前机构)
				<input type="radio" id="queryFlag" name="queryFlag" value="2"/>本级及直属下级（归属机构）
			<span id="queryFlag">	
		</td>
	</tr>

	<tr>
		<td align="left">交易类型：</td>
		<td align="left">
			<input id="business_name"  name="business_name" style="width:120px;height: 20px" />
		</td>
			
		<td align="left">交易流水号：</td>
		<td align="left">
			 <input id="logNo"  name="logNo" style="width:110px;height: 20px" />  
		</td>
		
		<td align="left">终端号：</td>
		<td align="left">
			 <input id="psamId"  name="psamId" style="width:110px;height: 20px" />  
		</td>
		<td align="left">
			 手机号：<input id="mobileNo"  name="mobileNo" style="width:110px;height: 20px" />  
		</td>
		<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealDetail()">查询</a>
		</td>
		<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.cleanDetail()">清空</a>
		</td>
	  </tr>
	  <tr>
		<td>
			<a href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.exportDetail(yearmonthdatestart,yearmonthdateend,agency_Name,queryFlag,business_name,logNo,psamId)" >导出excel</a>
		</td>
	  </tr>
</table>		
	
	<div>
	   
		本次查询数据汇总 &nbsp; &nbsp; &nbsp;交易总金额:<p id="dealAmount" style="display:inline; font-size:15px;color:red;" >0.00</p>元  
		&nbsp; &nbsp; &nbsp;手续费总金额:<p id="poundageAmount" style="display:inline; font-size:15px;color:red;" >0.00</p>元
		  &nbsp; &nbsp; &nbsp;分润总金额:<p id="allAmount" style="display:inline; font-size:15px;color:red;" >0.00</p>元 
		   &nbsp; &nbsp; &nbsp;直属下级分润总金额:<p id="childAmount" style="display:inline;font-size:15px;color:red;">0.00</p>元 
		   &nbsp; &nbsp; &nbsp;总分润差:<p id="differAmount" style="display:inline;font-size:15px;color:red;">0.00</p>元
		   
	</div>

<table id="viewDealDetail"> </table>
	
</body>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>

<script type="text/javascript">
//判断文件类型
String.prototype.endWith=function(oString){  
	var reg=new RegExp(oString+"$");  
	return reg.test(this);     
};
	
$(window).resize(function(){
	 $('#viewDeal').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
$(function(){
	$.viewDeal();
	});
	
	//上传导入功能
	$.upload = function() {
		$('#sysSource').combobox({
			url:"${ctx}/system/system.do?method=getCombSystems",
			valueField:"id",
			textField:"text"
		});
		var dealrule = $("#dealrule").combobox('getText');
		var dealruleId = $("#dealrule").combobox('getValue');
		
	  	$("#uploadBtn").linkbutton('disable');
	  	var name = $("#upload_file").val();
	  	
	    if(name ==""){
	    	$.messager.alert("提示","请选择要上传的文件！");
	    	$("#uploadBtn").linkbutton('enable');
	      	return;
	    }
	   
	    if(name.endWith(".txt")){
	    	if(dealruleId =="-1"){
		    	$.messager.alert("提示","请选择分隔符！");
		    	$("#uploadBtn").linkbutton('enable');
		      	return;
		    } 
	    } 
	    if(!(name.endWith(".txt") || name.endWith(".xls"))){
	    	$.messager.alert("提示","文件类型不正确，请重新选择！");
	    	$("#uploadBtn").linkbutton('enable');
	    	return;
	    }
	    
    $.ajaxFileUpload({
           url:"${ctx}/deal/deal.do?method=upload&dealrule="+dealrule,
           secureuri:false,
           fileElementId:'upload_file',
           dataType: 'json',
           
           success:function(data,textStatus) {
         	 	var str = eval("(" +data+ ")");
         	 	
         	 	$("#uploadBtn").linkbutton('enable');
         	 	$.openWin();
         	 	$('#dealId').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#serialNumber').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#transacount').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#terminalId').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#deal_data').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#deal_time').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#deal_status').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#dealtype_id').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#dealdesc').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#dealrebackcode').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#charge').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#transcost').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#bankcardNumber').combobox({valueField:"id",textField:"text",data:str});
         	 	$('#merchantCode').combobox({valueField:"id",textField:"text",data:str});
           		}
        	 }
 		); 
  };

	$.openWin= function(){
		 $("#dealImport").window('open').window('refresh');
		 
	}; 

	$.saveExcelData = function (){
		
	
		var dealrule = $("#dealrule").combobox('getText');
	    var dealId=$('#dealId').combobox('getValue');
	    var serialNumber=$('#serialNumber').combobox('getValue');
	    var transacount=$('#transacount').combobox('getValue');
	    var terminalId=$('#terminalId').combobox('getValue');
	    var deal_data=$('#deal_data').combobox('getValue');
	    var deal_time=$('#deal_time').combobox('getValue');
	    var deal_status=$('#deal_status').combobox('getValue');
	    var dealtype_id=$('#dealtype_id').combobox('getValue');
	    var dealdesc=$('#dealdesc').combobox('getValue');
	    var dealrebackcode=$('#dealrebackcode').combobox('getValue');
	    var charge=$('#charge').combobox('getValue');
 	 	var transcost=$('#transcost').combobox('getValue');
 	 	var sysSource =$('#sysSource').combobox('getValue');
 	 	var bankcardNumber =$('#bankcardNumber').combobox('getValue');
 	 	var merchantCode =$('#merchantCode').combobox('getValue');
 	 	
 	 	
 	 	if(dealId=="-1"){
 	 		$.messager.alert("提示","请选择交易编号！");
	      	return;
	      	$("#save").linkbutton('enable');
 	 	}
 		if(sysSource=="-1"){
 	 		$.messager.alert("提示","请选择来源系统！");
	      	return;
 	 	}
 	 	
 	 	if(serialNumber=="-1"){
 	 		$.messager.alert("提示","请选择流水号！");
	      	return;
 	 	}
 	 	
 	 	if(transacount=="-1"){
 	 		$.messager.alert("提示","请选择交易金额！");
	      	return;
 	 	}
 	 	
 	 	if(terminalId=="-1"){
 	 		$.messager.alert("提示","请选择终端编号！");
	      	return;
 	 	}
 	 	
 	 	if(deal_data=="-1"){
 	 		$.messager.alert("提示","请选择交易日期！");
	      	return;
 	 	}
 	 	
 	 	if(deal_time=="-1"){
 	 		$.messager.alert("提示","请选择交易时间！");
	      	return;
 	 	}
 	 	
 	 	if(deal_status=="-1"){
 	 		$.messager.alert("提示","请选择交易状态！");
	      	return;
 	 	}
 	 	
 	 	if(dealtype_id=="-1"){
 	 		$.messager.alert("提示","请选择交易类型编号！");
	      	return;
 	 	}
 	 	
 	 	if(dealdesc=="-1"){
 	 		$.messager.alert("提示","请选择交易描述！");
	      	return;
 	 	}
 	 	
 	 	if(dealrebackcode=="-1"){
 	 		$.messager.alert("提示","请选择交易返回码！");
	      	return;
 	 	}
 	 	
 	 	if(charge=="-1"){
 	 		$.messager.alert("提示","请选择交易成本！");
	      	return;
 	 	}
 	 	
 	 	if(transcost=="-1"){
 	 		$.messager.alert("提示","请选择手续费！");
	      	return;
 	 	}
 	 	
 	 	if(bankcardNumber=="-1"){
 	 		$.messager.alert("提示","请选择银行卡号！");
	      	return;
 	 	}
 	 	
 	 	if(merchantCode=="-1"){
 	 		$.messager.alert("提示","请选择商户号！");
	      	return;
 	 	}
 		$("#dealImport").window('close');
 		$.showDivShade('${ctx}');
 		$.ajax({
            type: "post",
            url: "${ctx}/deal/deal.do?method=saveExcelData",
            contentType:"application/x-www-form-urlencoded; charset=utf-8",
            data: {
            	sysSource:sysSource,
				merchantCode:merchantCode,
				dealrule:dealrule,
				dealId:dealId,
				serialNumber:serialNumber,
				transacount:transacount,
				terminalId:terminalId,
				deal_data:deal_data,
				deal_time:deal_time,
				deal_status:deal_status,
				dealtype_id:dealtype_id,
				dealdesc:dealdesc,
				dealrebackcode:dealrebackcode,
				charge:charge,
				transcost:transcost,
				bankcardNumber:bankcardNumber
				},
            dataType: "json",
            success: function(data){
            	alert(data);
            	$.hideDivShade();
        		$.viewDeal();
            }
				
        });
 		
 		
		/* $.post('${ctx}/deal/deal.do?method=saveExcelData', 
				{
					sysSource:sysSource,
					merchantCode:merchantCode,
					dealrule:dealrule,
					dealId:dealId,
					serialNumber:serialNumber,
					transacount:transacount,
					terminalId:terminalId,
					deal_data:deal_data,
					deal_time:deal_time,
					deal_status:deal_status,
					dealtype_id:dealtype_id,
					dealdesc:dealdesc,
					dealrebackcode:dealrebackcode,
					charge:charge,
					transcost:transcost,
					bankcardNumber:bankcardNumber
					
				},
				function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json"); */
	};
	
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$.hideDivShade();
		$('#uploadBtn').linkbutton('enable');
		$.close();
		$.viewDeal();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#uploadBtn').linkbutton('enable');
	};
	$.close = function() {
		$("#dealImport").window('close');
	};


	$.viewDeal = function() {
		$('#viewDeal').datagrid(
						{
						title : '交易管理',
						width:$(window).width()-6,
						height : $(window).height()*0.97,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/deal/deal.do?method=getDealList",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										//{field : "sysSource",title : "来源系统",width : 100,align : "center",sortable : true},
										//{field : "merchantCode",title : "商户号",width : 150,align : "center",sortable : true},
										{field : "dealId",title : "交易编号",width : 100,align : "center",sortable : true},
										{field : "serialNumber",title : "流水号",width : 100,align : "center",sortable : true},
										{field : "transacount",title : "交易金额(元)",width : 100,align : "center",sortable : true},
										{field : "charge",title : "交易成本(元)",width : 100,align : "center",sortable : true},
										{field : "dealTypeName",title : "交易类型",width : 100,align : "center",sortable : true},
										{field : "deal_status",title : "交易状态",width : 100,align : "center",sortable : true},
										{field : "terminalId",title : "终端编号",width : 150,align : "center",sortable : true},
										{field : "transcost",title : "手续费",width : 100,align : "center",sortable : true,hidden:true},
										{field : "deal_data",title : "交易日期",width : 150,align : "center",sortable : true},
										{field : "deal_time",title : "交易时间",width : 100,align : "center",sortable : true},
										{field : "dealdesc",title : "交易描述",width : 100,align : "center",sortable : true},
										{field : "dealrebackcode",title : "交易返回码",width : 100,align : "center",sortable : true},
										{field : "createId",title : "操作人",width : 100,align : "center",sortable : true,hidden:true},
										{field : "createDate",title : "操作时间",width : 100,align : "center",sortable : true,hidden:true}
										 ] ],
						hideColumn : [ [ 
						                 {field : "dealId"},
						                 {field : "dealtype_id"},
						                 {field : "deal_statusStr"}
						              ] ], 
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:"#toolbarDiv"
						
					});
	var p = $('#viewDeal').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

</script>
</head>
<body id="indexd" bgcolor="#ff0000">

 <div id="toolbarDiv" style="padding:5px;height:auto">
	 <table>
 	<tr>
 		<td>分隔符：</td>
 		<td  nowrap="nowrap">
 			<select id="dealrule" class="easyui-combobox" name="dealrule" style="width: 150px;height: 30px" editable="false">
 					<option selected="selected" value="-1">请选择分隔符</option>
					<option value="0">|</option>
					<option value="1">^</option>
					<option value="2">,</option>
 			</select>
 		</td>
		<td  nowrap="nowrap">
			<div class="file-box">
					 <input type='text' name='textfield' id='textfield' class='txt' />  
					 <input type='button' class='btn' value='浏览...' />
					 <input type="file" name="upload_file" class="file" id="upload_file" size="28" onchange="document.getElementById('textfield').value=this.value" />
					 <a href="#" class="easyui-linkbutton" id="uploadBtn" name="uploadBtn" class="btn" iconCls="icon-add" plain="true" onclick="$.upload()">上传</a>
			</div>
		</td>
		<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDeal()">查询</a></td>
	</tr>
 </table>
</div>

<table id="viewDeal"></table>
	
	<div id="dealImport" class="easyui-window" title="交易管理" draggable="false" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 650px; height: 480px; top: 40px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px; background: #fff;  overflow: hidden;">
					<table>
					<tr>
						<td>来源系统：</td>
					 		<td  nowrap="nowrap">
					 			<select id="sysSource" class="easyui-combobox" name="sysSource" style="width: 150px;height: 30px" editable="false">
					 			</select>
					 		</td>
					</tr>

					<tr><td>请选择字段:</td></tr>							
						<tr>
							<td style="padding: 0px 30px 0px 30px">交易编号:</td>
							<td>
								<select id="dealId" class="easyui-combobox" name="dealId" style="width: 150px;height: 30px" editable="false"></select>				
							</td>
							<td  style="padding: 0px 30px 0px 30px">流水号:</td>
							<td>
								<select id="serialNumber" class="easyui-combobox" name="serialNumber" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>	
						
						<tr>
							<td style="padding: 0px 30px 0px 30px">交易金额:</td>
							<td>
								<select id="transacount" class="easyui-combobox" name="transacount" style="width: 150px;height: 30px" editable="false"></select>
							</td>
					
							<td style="padding: 0px 30px 0px 30px">终端编号:</td>
							<td>
								<select id="terminalId" class="easyui-combobox" name="terminalId" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>
						
						<tr>	
							<td style="padding: 0px 30px 0px 30px">交易日期:</td>
							<td>
								<select id="deal_data" class="easyui-combobox" name="deal_data" style="width: 150px;height: 30px" editable="false"></select>
							</td>
							<td  style="padding: 0px 30px 0px 30px">交易时间:</td>
							<td>
								<select id="deal_time" class="easyui-combobox" name="deal_time" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>
						
						<tr>
							<td style="padding: 0px 30px 0px 30px">交易状态:</td>
							<td>
								<select id="deal_status" class="easyui-combobox" name="deal_status" style="width: 150px;height: 30px" editable="false"></select>
							</td>
							<td  style="padding: 0px 30px 0px 30px">交易类型编号:</td>
							<td>
								<select id="dealtype_id" class="easyui-combobox" name="dealtype_id" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>
						
						<tr>	
							<td style="padding: 0px 30px 0px 30px">交易描述:</td>
							<td>
								<select id="dealdesc" class="easyui-combobox" name="dealdesc" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						
							<td  style="padding: 0px 30px 0px 30px">交易返回码:</td>
							<td>
								<select id="dealrebackcode" class="easyui-combobox" name="dealrebackcode" style="width: 150px;height: 30px" editable="false"></select>
							</td>
							
						</tr>
						<tr>	
							<td style="padding: 0px 30px 0px 30px">交易成本:</td>
							<td>
								<select id="charge" class="easyui-combobox" name="charge" style="width: 150px;height: 30px" editable="false"></select>
							</td>
							<td  style="padding: 0px 30px 0px 30px">商户号:</td>
							<td>
								<select id="merchantCode" class="easyui-combobox" name="merchantCode" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>
						
						<tr>	
							<td  style="padding: 0px 30px 0px 30px">手续费:</td>
							<td>
								<select id="transcost" class="easyui-combobox" name="transcost" style="width: 150px;height: 30px" editable="false"></select>
							</td>
							<td  style="padding: 0px 30px 0px 30px">银行卡号:</td>
							<td>
								<select id="bankcardNumber" class="easyui-combobox" name="bankcardNumber" style="width: 150px;height: 30px" editable="false"></select>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="4" style="height: 100px">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.saveExcelData()">导入</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
							</td>
						</tr>
						
					</table>
			</div>
		</div>
	</div>
	
</body>
</html>
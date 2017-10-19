<%@page import="com.compass.utils.ConstantUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>终端下发</title>
<style>
.datagrid-cell-rownumber {
	width: 50px;
	text-align: center;
	margin: 0px;
	padding: 3px 0px;
	color: #000;
}

.datagrid-header-rownumber {
	width: 50px;
	text-align: center;
	margin: 0px;
	padding: 3px 0px;
}
</style>
<script type="text/javascript">	
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString()
					.trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString()
					.trim()%>';
var staticagecyId='<%=ConstantUtils.CENTERCODE%>';
var agencyQueryFlag='<%=session.getAttribute(ConstantUtils.AGENCYQUERYFLAG)%>';

var agencyId_value='';
var agencyIds_value='';

$(window).resize(function(){
	 $('#viewTerminalManage').datagrid('resize', {
	 	width:cs()
	 });
});

function cs(){	
	return $(window).width()-8;
}

$(function(){
	if(agid==staticagecyId){
		$("#systr1").show();
		$("#systr2").show();
		$("#systr3").show();
		$("#systr4").show();
	}
	
	$('#batchIssuedView').window({
        onBeforeClose: function () { //当面板关闭之前触发的事件
        	$.hideDivShade();
        }
    	});
	$('#systemId').combobox({
		url:"${ctx}/system/system.do?method=getCombSystemsSigle",
		valueField:"id",
		textField:"text",
		onLoadSuccess:function(){
			$.viewTerminalManage();
		}
		});
	$('#systemIdp').combobox({
		url:"${ctx}/system/system.do?method=getCombSystemsSigle",
		valueField:"id",
		textField:"text"
		});
	
	$('#terminalTypeF').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});

	$('#agencytreeId').combotree({
		url:"${ctx}/agency/agency.do?method=getAgencyTree",
		valueField:"id",
		textField:"text",
		panelWidth:   350
	});
	 
	
	$(".combo-text").bind("input onchange",function(a,b){
		$('#agencytreeId').combotree('tree').tree("search",$(this).val());
		if($(this).val()==""||null==$(this).val()){
			$('#agencytreeId').combotree('tree').tree("collapseAll");
		} 
	});
		
	if("false"==agencyQueryFlag){
     	$("#queryFlag").css("display","none");
	}
	
});

	$.success = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#save').linkbutton('enable');
		 $('#confirm').linkbutton('enable');
		 $.close(); 
		 $.viewTerminalManage();
		 $.batchIssuedView();
	};
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#save').linkbutton('enable');
		 $('#confirm').linkbutton('enable');
	};
	$.close = function(){
		$.hideDivShade();
		$("#terminalManageIssued").window('close');  
		$("#terManageIssuedView").window('close'); 
	};
	$.cancle = function(){
		$("#terManageIssuedView").window('close'); 
	};
	
	//加载数据表格
	$.viewTerminalManage = function() {
		var status = $("#status").combobox('getValue');
		var terminalCode = $("#terminalCode").val();
		var agencyCode = $("#sagencyId").val();
		var terminalTypeId =  $('#terminalTypeF').combobox('getValue');
		var systemId=$('#systemId').combobox('getValue');
		var agencyId = $("#agencytreeId").combotree('getValue');
		var startCode = $("#startCode1").val();
		var endCode = $("#endCode1").val();
		var queryFlag=$("input[name='queryFlag']:checked").val();
		var  datestart = $('#yearmonthdatestart').val();
		var  dateend = $('#yearmonthdateend').val();
	 	var bindDtstart =  $('#bindDtyearmonthdatestart').val();
		var bindDtend = $('#bindDtyearmonthdateend').val(); 
		var isJoincash =  $('#isJoincash').combobox('getValue');
		var isPay =  $('#isPay').combobox('getValue');
		var havepsam =  $('#havepsam').combobox('getValue');
		if(systemId=="-1"){
		  systemId="";
		}
		$('#viewTerminalManage').datagrid({
			title : '终端下发',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageAllTb",
			queryParams:{
				terminalCode:terminalCode,
				terminalTypeId:terminalTypeId,
				status:status,
				agencyId:agencyId,
				systemId:systemId,
				agencyCode:agencyCode,
				startCode:startCode,
				endCode:endCode,
				queryFlag:queryFlag,
				datestart:datestart,
				dateend:dateend,
			 	bindDtstart:bindDtstart,
				bindDtend:bindDtend, 
				isJoincash:isJoincash,
				isPay:isPay,
				havepsam:havepsam
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [  
			             {field : "ck",checkbox : true,width : "50"},
			             {field : "systemSource",title : "来源系统",width : 100,align : "center",sortable : true,hidden : true},
			          // {field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
			             {field : "terminalCode",title : "终端识别码",width : 150,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : 100,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : 250,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : 100,align : "center",sortable : true},
			             {field : "havePsam",title : "绑定状态",width : 100,align : "center",sortable : true},
			             {field : "isJoincash",title : "参于押金模式",width : 100,align : "center"},
			             {field : "isPay",title : "押金支付类别",width : 100,align : "center"},
			          //  {field : "openDate",title : "开通时间",width : 200,align : "center",sortable : true}, 
			          //   {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true},
			           //  {field : "scrapDate",title : "报废时间",width : 200,align : "center",sortable : true}, 
			            // {field : "createId",title : "创建人编号",width : 200,align : "center",sortable : true},
			            {field : "bindDt",title : "绑定时间 ",width : 100,align : "center",sortable : true},
			             {field : "activeDt",title : "激活时间 ",width : 100,align : "center",sortable : true},
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
			showFooter : true,
			toolbar:[{
				id:'terminalBatchIssued',text:'批量下发',iconCls:'icon-redo',
				handler:function(){
					$.terminalBatchIssued();
				}
			}]
		});
		
		if(agid==staticagecyId){
			$('#viewTerminalManage').datagrid("showColumn","systemSource");
		}
		
		//分页
		var p = $('#viewTerminalManage').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		
	};
	
	//加载数据表格(终端批量下发)
	$.batchIssuedView = function() {
	
	//	var prefix = $("#prefix").val();
		var startCode = $("#startCode").val();
		var endCode = $("#endCode").val();
	//	var lengthNumber = prefix.length;
		var systemId=$('#systemIdp').combobox('getValue');
		
		if(startCode !="" && endCode !=""){
			if(endCode-startCode<0){
				$.messager.alert("提示","结束编号必须大于起始编号！");
				return;
			}
		}
		$('#batchIssuedViews').datagrid({
			title : '终端批量下发',
			width : '100%',
			height : $(window).height()*0.8,
			pageSize : 20,
			pageNumber : 1,
			url : '${ctx}/terminalmanage/terminalmanage.do?method=getTerminalBatchIssued',
			queryParams : {								
				//prefix:prefix,
				startCode:startCode,
				endCode:endCode,
				//lengthNumber:lengthNumber,
				systemId:systemId
			},
			onLoadSuccess:function(data){
				if(data.total > 0){
					$('#issued').linkbutton('enable'); 
				}else if(data.total == 0){
					$('#issued').linkbutton('disable'); 
				}
			},
			nowrap : false,
			pageSize : 20,
			fitColumns : true,
			columns : [ [
			             {field : "systemSource",title : "来源系统",width : 130,align : "center",sortable : true,hidden : true},
			             //{field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
			             {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : 130,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : 300,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : 130,align : "center",sortable : true},
			            // {field : "openDate",title : "开通时间",width : 200,align : "center",sortable : true,hidden:true}, 
			             {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true,hidden:true},
			             {field : "scrapDate",title : "报废时间",width : 200,align : "center",sortable : true,hidden:true}, 
			             {field : "createId",title : "创建人编号",width : 200,align : "center",sortable : true},
			             {field : "createDt",title : "创建时间",width : 200,align : "center",sortable : true}
			] ],
			hideColumn : [ [ 
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
		
		if(agid==staticagecyId){
			$('#batchIssuedViews').datagrid("showColumn","systemSource");
		}
		
		var p = $('#batchIssuedViews').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		
	};
	
	//下发
	$.terminalIssued = function() {
		//var systemId=$('#systemId').combotree('getValue');
		//var agencyid=$('#agencytreeId').combotree('getValue');
		var rows = $('#viewTerminalManage').datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示 ", "请选择要下发的记录!");
			return;
		}
		for ( var i = 0; i < rows.length; i++) {
			var status = rows[i].havePsam;
			var merchantCode = rows[i].merchantCode;
			var agencyid=rows[i].agencyId;
			if(status == "已绑定"){
				$.messager.alert("提示","只有状态为未绑定的才能下发");
		      	return;
			}
			
			if(merchantCode!=null && merchantCode != "" && merchantCode.length>0){
				$.messager.alert("提示","商户号已存在,不能下发");
		      	return;
			}
			if(agid!=agencyid){
				$.messager.alert("提示 ", "机构只能下发属于自己的终端!");
				return;
			}
		}
		
		
		
		$("#terminalManageIssued").window('open').window('refresh');
		$.showDivShade('${ctx}');
		//获取子机构名称
		var systemId=rows[0].systemId;
		$('#agencyId').combobox({
			url:"${ctx}/agency/agency.do?method=getChildAgencyListSin&systemId="+systemId,
			valueField:"id",
			textField:"text",
			onSelect: function(opt){
				agencyId_value=opt.id;
			}
			});
		$("#merchantCodes").val('');
	};
	
	//批量下发
	$.terminalBatchIssued = function(){
		$.showDivShade('${ctx}');
		$('#issued').linkbutton('disable'); 
	//	$("#prefix").val('');
		$("#startCode").val('');
		$("#endCode").val('');
	
		$.batchIssuedView();
		$("#batchIssuedView").window('open').window('refresh');
	};
	
	$.save=function(){
		var rows = $('#viewTerminalManage').datagrid('getSelections');
		if(null==agencyId_value||''==agencyId_value){
			alert("请选择机构 !");
			return;
		}
		
		//var merchantCodes = $("#merchantCodes").val();
		var merchantCodes ='';
		var agencyId = agencyId_value;
	  	if(agencyId =="-1"){
	    	$.messager.alert("提示","请选择机构！");
	    	$("#upload").linkbutton('enable');
	      	return;
	    }
	  	
 
		
		var Ids = "";
		for ( var i = 0; i < rows.length; i++) {
			if (i == rows.length - 1) {
				Ids += "'" + rows[i].terminalCode + "',";
			} else {
				Ids += "'" + rows[i].terminalCode + "'" + ",";
			}
			
			
		}
		$('#save').linkbutton('disable');
		$.post("${ctx}/terminalmanage/terminalmanage.do?method=terminalDeal", 
	  			{
				  Ids : Ids,
				  flag: "down"
				}, function(data) {
					var flagTT=data.datas.flag;
					if("2"==flagTT){
						var terminals=data.datas.terminals;
						$.messager.confirm("提示", "终端当月已存在交易，您是否将交易同时进行分配?",function(r){
			    		  $.post("${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalIssued", 
			    	  			{
			    		  			agencyId : agencyId,
			    		  			merchantCodes : merchantCodes,
			    	  				Ids : Ids,
			    	  				flag: r
			    	  			}, function(data) {
			    	  				alert(data.message);
			    	  				 $('#save').linkbutton('enable');
			    	  				 $('#confirm').linkbutton('enable');
			    	  				 $.close(); 
			    	  				 $.viewTerminalManage();
			    	  				 $.batchIssuedView();
			    	  			}, "json");
					       });
					}else{
						$.post("${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalIssued", 
					  			{
						  			agencyId : agencyId,
						  			merchantCodes : merchantCodes,
					  				Ids : Ids,
					  				flag: false
					  			}, function(data) {
					  				$.parseAjaxReturnInfo(data, $.success, $.failed);
					  			}, "json");
					}
				}, "json");
		
	  	
	};
	
	$.issued=function(){
		//var prefix = $("#prefix").val();
		var startCode = $("#startCode").val();
		var endCode = $("#endCode").val();
		
	 /** 	if(prefix ==""){
	    	$.messager.alert("提示","请输入前缀！");
	      	return;
	    } */
	  	
	  	if(startCode ==""){
	    	$.messager.alert("提示","请输入起始编号！");
	      	return;
	    }
	  	
	  	if(endCode ==""){
	    	$.messager.alert("提示","请输入结束编号！");
	      	return;
	    }
	  
	  	$("#terManageIssuedView").window('open'); 
		var rows = $('#batchIssuedViews').datagrid('getRows');
		var systemId=rows[0].systemId;
		$('#agencyIds').combobox({
			url:"${ctx}/agency/agency.do?method=getChildAgencyListSin&systemId="+systemId,
			valueField:"id",
			textField:"text",
			onSelect: function(opt){
					agencyIds_value=opt.id;
				}
			});
	  	//$("#agencyIds").combobox('select',-1);
	  	$("#merchantCode").val('');
	  
	};
	
	//批量下发
	$.confirm=function(){
		var rows = $('#batchIssuedViews').datagrid('getRows');
		if(null==agencyIds_value||''==agencyIds_value){
			alert("请选择机构！");
			return;
		}
		//var prefix = $("#prefix").val();
		var startCode = $("#startCode").val();
		var endCode = $("#endCode").val();
		//var lengthNumber = prefix.length;
		//var merchantCode = $("#merchantCode").val();
		var merchantCode = '';
		var agencyId = agencyIds_value ;
	  	if(agencyId =="-1"){
	    	$.messager.alert("提示","请选择机构！");
	      	return;
	    }
	  	
	  	//判断是否有分润费率设置
	  	/**
	  	 $.ajax({
	         type: "POST",
	     	 url : "${ctx}/cost/agencyCost.do?method=isEffectCost",
	         data:{
	        	 agencyId : agencyId
	         },
	         success: function(msg) {
	        	if(msg.success =='false'){
	        		$.messager.alert("提示",msg.message, 'error');
	        		return;
	        	}
	         }
	     });
	  	**/
		 
	  	var systemId = rows[0].systemId;
		$('#confirm').linkbutton('disable');
		$.post("${ctx}/terminalmanage/terminalmanage.do?method=terminalsDeal", 
	  			{
				   startTerminal :  startCode,
			       endTerminal:endCode,
			       systemId:systemId,
				   flag:     "down"
	  			}, function(data) {
	  				var flagTT=data.datas.flag;
					if("2"==flagTT){
						var terminals=data.datas.terminals;
						$.messager.confirm("提示", "终端当月已存在交易，您是否将交易同时进行分配?",function(r){
							$.post("${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalBatchIssued", 
						  			{
								  		agencyId:agencyId,
										startCode:startCode,
										endCode:endCode,
										merchantCode:merchantCode,
										systemId:systemId,
										flag: r
						  			}, function(data) {
						  				 $('#save').linkbutton('enable');
				    	  				 $('#confirm').linkbutton('enable');
				    	  				 $.close(); 
				    	  				 $.viewTerminalManage();
				    	  				 $.batchIssuedView();
						  			}, "json");
						});
					}else{
						$.post("${ctx}/terminalmanage/terminalmanage.do?method=updateTerminalBatchIssued", 
					  			{
							  		agencyId:agencyId,
									startCode:startCode,
									endCode:endCode,
									merchantCode:merchantCode,
									systemId:systemId,
									flag: false
					  			}, function(data) {
					  				$.parseAjaxReturnInfo(data, $.success, $.failed);
					  			}, "json");
					}
	  				
	  			}, "json");
		
	  	
	};
	
	// 导出终端
	$.terminalExport=function(){
		var status = $("#status").combobox('getValue');
		var terminalCode = $("#terminalCode").val();
		var agencyCode = $("#sagencyId").val();
		var terminalTypeId =  $('#terminalTypeF').combobox('getValue');
		var systemId=$('#systemId').combobox('getValue');
		var agencyId = $("#agencytreeId").combotree('getValue');
		var startCode = $("#startCode1").val();
		var endCode = $("#endCode1").val();
		var queryFlag=$("input[name='queryFlag']:checked").val();
		var  datestart = $('#yearmonthdatestart').val();
		var  dateend = $('#yearmonthdateend').val();
		var bindDtstart =  $('#bindDtyearmonthdatestart').val();
		var bindDtend = $('#bindDtyearmonthdateend').val(); 
		var isPay =  $('#isPay').combobox('getValue');
		var isJoincash =  $('#isJoincash').combobox('getValue');
		var havepsam =  $('#havepsam').combobox('getValue');
		$.getToPost('${ctx}/terminalmanage/terminalmanage.do?method=terminalExport',{
			terminalCode:terminalCode,
			terminalTypeId:terminalTypeId,
			status:status,
			agencyId:agencyId,
			systemId:systemId,
			agencyCode:agencyCode,
			startCode:startCode,
			endCode:endCode,
			queryFlag:queryFlag,
			datestart:datestart,
			dateend:dateend,
			bindDtstart:bindDtstart,
			bindDtend:bindDtend, 
			isPay:isPay,
			isJoincash:isJoincash,
			havepsam:havepsam
		});
	};
	$.queryPayterminalCount = function() {
			
			//	var prefix = $("#prefix").val();
				var startCode = $("#startCode").val();
				var endCode = $("#endCode").val();
			//	var lengthNumber = prefix.length;
				var systemId=$('#systemIdp').combobox('getValue');
				if(startCode !="" && endCode !=""){
					if(startCode.length < 15 || startCode.length > 16){
						$.messager.alert("提示","起始编号不能小于15位大于16位!");
						return;
					}
					if(endCode.length < 15 || endCode.length > 16){
						$.messager.alert("提示","结束编号不能小于15位大于16位!");
						return;
					}
					if(endCode-startCode<0){
						$.messager.alert("提示","结束编号必须大于起始编号！");
						return;
					}
				}
				$.post('${ctx}/terminalmanage/terminalmanage.do?method=queryPayterminalCount',
			            {
				            startCode:startCode,
							endCode:endCode,
							systemId:systemId
						}, function(data) {
							if(data.success == "false"){
								$.messager.alert("提示",data.message+"<br/>可能终端已绑定或终端费率低于下级分润模板，请核对输入连续可下发的终端。");
							} else {
								$.batchIssuedView();
							}		
				});
			}
</script>
</head>
<body id="indexd">

	<table>
		<tr>
			<td id="systr1" style="display: none;" align="left">来源系统：</td>
			<td id="systr2" style="display: none;" align="left"><select id="systemId" class="easyui-combobox" name="systemId" data-options="panelHeight:'auto'" style="width: 150px;" editable="false"></select>
			</td>

			<td align="left">终端编号：</td>
			<td align="left"><input type="text" name="terminalCode" id="terminalCode" maxlength="100" style="width: 150px;" /></td>
			<td align="left">终端类型：</td>
			<td align="left"><select id="terminalTypeF" editable="false" class="easyui-combobox" name="terminalTypeF" style="width: 150px;"></select></td>
		</tr>
		<tr>
			<td align="left">起始编号：</td>
			<td align="left"><input type="text" name="startCode1" id="startCode1" maxlength="100" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
			<td align="left">结束编号：</td>
			<td align="left"><input type="text" name="endCode1" id="endCode1" maxlength="100" style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
			<td align="left">终端状态：</td>
			<td align="left"><select id="status" class="easyui-combobox" editable="false" name="status" style="width: 150px;">
					<option selected="selected" value="-1">请选择终端状态</option>
					<option value="0">未激活</option>
					<option value="1">已激活</option>
					<option value="2">冻结</option>
					<option value="3">回拨</option>
					<option value="4">作废</option>
			</select></td>
		</tr>
		<tr>
			<td align="left">机构名称：</td>
			<td align="left"><input type="text" name="sagencyId" id="sagencyId" maxlength="100" /></td>
			<td align="left">机构名称：</td>
			<td align="left"><select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true"></select></td>


			<td align="left" colspan="2"><span id="queryFlag"> <input type="radio" id="queryFlag" name="queryFlag" value="1" checked="checked">按当前机构 <input type="radio" id="queryFlag"
						name="queryFlag" value="2" />按归属机构 </span></td>
		</tr>
		<tr>
			<td align="left">参与押金模式：</td>
			<td align="left"><select class="easyui-combobox" id="isJoincash" name="isJoincash">
					<option value="">请选择押金模式</option>
					<option value="1">是</option>
					<option value="0">否</option>
			</select></td>
			<td align="left">押金支付类别：</td>
			<td align="left"><select class="easyui-combobox" id="isPay" name="isPay">
					<option value="">请选择押金支付类别</option>
					<option value="1">是</option>
					<option value="0">否</option>
			</select></td>
			<td align="left">终端绑定状态：</td>
			<td align="left"><select class="easyui-combobox" id="havepsam" name="havepsam">
					<option value="">请选择终端绑定状态</option>
					<option value="0">未绑定</option>
					<option value="1">已绑定</option>
			</select></td>
		</tr>
		<tr>

			<td align="left">激活日期：</td>
			<td align="left" >
				 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
				 -
				 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalManage()">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.terminalIssued()">下发</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.terminalExport()">导出</a></td>
			<td align="right">绑定日期：</td>
			<td align="right" >
				 <input id="bindDtyearmonthdatestart" name="bindDtyearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'bindDtyearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
				 -
				 <input id="bindDtyearmonthdateend" name="bindDtyearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'bindDtyearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
		</tr>
	</table>

	<table id="viewTerminalManage"></table>

	<div id="terminalManageIssued" class="easyui-window" title="终端下发" closable="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false"
		resizable="false" draggable="false" style="width: 510px; height: 280px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 30px; background: #fff; overflow: hidden;">
				<input type="hidden" id="terminalTypeId" name="terminalTypeId" />
				<table width="100%">
					<tr>
						<td align="left">机构名称：</td>
						<td align="left"><select style="width: 200px;" name="agencyId" id="agencyId" class="easyui-combobox" editable="true"></select></td>
					</tr>
					<!--
						 <tr style="height: 30px">
						<td align="left">商户编号：</td>
							<td align="left"><input style="width: 200px;" name="merchantCodes" id="merchantCodes"></input> 
							</td>
						</tr>	
						 -->
					<tr style="height: 80px">
						<td align="center" colspan="4"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close" id="close" href="#"
							class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="batchIssuedView" class="easyui-window" title="批量下发" closable="true" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false" resizable="true"
		draggable="true" style="width: 1000px; height: 500px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="background: #fff; overflow: hidden;">
				<table style="padding: 5px 0 5px 5px">
					<tr>
						<td id="systr3" style="display: none;" align="left">系统：</td>
						<td id="systr4" style="display: none;" align="left"><select id="systemIdp" class="easyui-combobox" name="systemIdp" data-options="panelHeight:'auto'" style="width: 130px;" editable="true"></select>
						</td>
						<td align="left">起始编号：</td>
						<td align="left"><input style="width: 130px;" name="startCode" id="startCode" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></input></td>
						<td align="left">结束编号：</td>
						<td align="left"><input style="width: 130px;" name="endCode" id="endCode" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></input></td>
						<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.queryPayterminalCount()">查询</a></td>
						<td align="center" colspan="4"><a name="issued" id="issued" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.issued()">下发</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
				</table>
				<table id="batchIssuedViews"></table>
			</div>
		</div>
	</div>

	<div id="terManageIssuedView" class="easyui-window" title="终端批量下发" closable="false" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false"
		resizable="true" draggable="true" style="width: 510px; height: 280px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 30px; background: #fff; overflow: hidden;">
				<input type="hidden" id="terminalTypeId" name="terminalTypeId" />
				<table width="100%">
					<tr style="height: 30px">
						<td align="left">机构名称：</td>
						<td align="left"><select style="width: 200px;" name="agencyIds" id="agencyIds" class="easyui-combobox" data-options="panelHeight:'auto'" editable="true"></select></td>
					</tr>
					<!--<tr style="height: 30px">
						<td align="left">商户编号：</td>
							<td align="left"><input style="width: 200px;" name="merchantCode" id="merchantCode"></input> 
							</td>
						</tr>	
						-->
					<tr style="height: 80px">
						<td align="center" colspan="4"><a name="confirm" id="confirm" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.confirm()">确定</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close" id="close" href="#"
							class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.cancle()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>

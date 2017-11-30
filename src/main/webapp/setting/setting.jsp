<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
<style type="text/css">
.button1 {
	color: #444;
	background-repeat: no-repeat;
	background: #f5f5f5;
	background-repeat: repeat-x;
	border: 1px solid #bbb;
	background: -webkit-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -moz-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -o-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: linear-gradient(to bottom, #ffffff 0, #e6e6e6 100%);
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff, endColorstr=#e6e6e6, GradientType=0);
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
}

input {
	vertical-align: middle;
	margin: 0;
	padding: 0
}

.file-box {
	position: relative;
	width: 340px
}

.txt {
	height: 30px;
	border: 1px solid #cdcdcd;
	width: 180px;
}

.btn {
	background-color: #FFF;
	border: 1px solid #CDCDCD;
	height: 30px;
	width: 70px;
}

.file {
	position: absolute;
	top: 0;
	right: 80px;
	height: 30px;
	filter: alpha(opacity : 0);
	opacity: 0;
	width: 260px
}
</style>
<script type="text/javascript">
//判断文件类型
String.prototype.endWith=function(oString){  
	var reg=new RegExp(oString+"$");  
	return reg.test(this);     
};
$(window).resize(function(){
	 $('#viewSetting').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
	
	$('#settingSave').window({title: '添加导入数据信息'});
	$.showDivShade('${ctx}');
	$('#settingId').val('');
	$("#systemCode").combobox("setValue",-1);
	$("#path").val('');
	$("#fileName").val('');
	$("#startFlag").combobox('setValue',1);
	$("#splitStr").combobox("setText",'');
	$("#dealId").numberbox("setValue",'');
	$("#serialNumber").numberbox("setValue",'');
	$("#transaCount").numberbox("setValue",'');
	$("#terminalCode").numberbox("setValue",'');
	$("#dealData").numberbox("setValue",'');
	$("#dealTime").numberbox("setValue",'');
	$("#dealStatus").numberbox("setValue",'');
	$("#dealtypeId").numberbox("setValue",'');
	$("#dealDesc").numberbox("setValue","");
	$("#dealRebackCode").numberbox("setValue",'');
	$("#bankcardnumber").numberbox("setValue",'');
	$("#cost").numberbox("setValue",'');
	$("#merchantCode").numberbox("setValue",'');
	$("#status").combobox('select', 1);
	$("#settingDesc").val('');
	flag = obj;
	if (flag != "-1") {
		$('#settingSave').window({title: '修改导入数据信息'});
		var rows = $('#viewSetting').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		var row = $('#viewSetting').datagrid('getSelected');
		$("#settingId").val(row.settingId);
		$("#systemCode").combobox("setValue",row.systemCode);
		$("#path").val(row.path);
		$("#fileName").val(row.fileName);
		$("#startFlag").combobox('setValue',row.startFlag);
		$("#splitStr").combobox("setValue",row.splitStr);
		$("#dealId").numberbox("setValue",row.dealId);
		$("#serialNumber").numberbox("setValue",row.serialNumber);
		$("#transaCount").numberbox("setValue",row.transaCount);
		$("#terminalCode").numberbox("setValue",row.terminalCode);
		$("#dealData").numberbox("setValue",row.dealData);
		$("#dealTime").numberbox("setValue",row.dealTime);
		$("#dealStatus").numberbox("setValue",row.dealStatus);
		$("#dealtypeId").numberbox("setValue",row.dealtypeId);
		$("#dealDesc").numberbox("setValue",row.dealDesc);
		$("#dealRebackCode").numberbox("setValue",row.dealRebackCode);
		$("#bankcardnumber").numberbox("setValue",row.bankCardnumber);
		$("#cost").numberbox("setValue",row.cost);
		$("#settingDesc").val(row.settingDesc);
		$("#status").combobox('select', row.settingStatus);
		$("#merchantCode").numberbox("setValue",row.merchantCode);
	}else{
		$('#systemCode').combobox({
			url:"${ctx}/system/system.do?method=getCombSystems",
			valueField:"id",
			textField:"text"
			});
	}
	$("#settingSave").window('open').window('refresh');
};
$.save = function() {
	var settingId = $("#settingId").val();
	var splitStr = $("#splitStr").combobox("getValue");
	var systemCode = $("#systemCode").combobox("getValue");
	var status = $("#status").combobox("getValue");
	var path= $("#path").val();
	var fileName= $("#fileName").val();
	var startFlag= $("#startFlag").combobox("getValue");
	var dealId= $("#dealId").numberbox('getValue');
	var serialNumber= $("#serialNumber").numberbox('getValue');
	var transaCount= $("#transaCount").numberbox('getValue');
	var terminalCode= $("#terminalCode").numberbox('getValue');
	var dealData= $("#dealData").numberbox('getValue');
	var dealTime= $("#dealTime").numberbox('getValue');
	var dealStatus= $("#dealStatus").numberbox('getValue');
	var dealtypeId= $("#dealtypeId").numberbox('getValue');
	var dealDesc= $("#dealDesc").numberbox('getValue');
	var dealRebackCode= $("#dealRebackCode").numberbox('getValue');
	var bankcardNumber= $("#bankcardnumber").numberbox('getValue');
	var cost= $("#cost").numberbox('getValue');
	var settingDesc = $("#settingDesc").val();
	var merchantCode = $("#merchantCode").val();
	
	if (systemCode == "-1") {
		$.messager.alert("提示 ", "请选择系统名称");
		return false;
	}
	
	if (splitStr == "-1") {
		$.messager.alert("提示 ", "请选择分隔符");
		return false;
	}
	
	if (path == "") {
		$.messager.alert("提示 ", "请输入文件路径");
		return false;
	}
	
	if (fileName == "") {
		$.messager.alert("提示 ", "请输入文件名称");
		return false;
	}
	
	if (terminalCode == "") {
		$.messager.alert("提示 ", "请输入终端编号列序号");
		return false;
	}
	
	if (dealId == "") {
		$.messager.alert("提示 ", "请输入交易编号列序号");
		return false;
	}
	
	if (serialNumber == "") {
		$.messager.alert("提示 ", "请输入流水号列序号");
		return false;
	}
	
	if (transaCount == "") {
		$.messager.alert("提示 ", "请输入交易金额列序号");
		return false;
	}
	
	if (cost == "") {
		$.messager.alert("提示 ", "请输入成本金额列序号");
		return false;
	}
	
	if (dealData == "") {
		$.messager.alert("提示 ", "请输入交易日期列序号");
		return false;
	}
	
	if (dealTime == "") {
		$.messager.alert("提示 ", "请输入交易时间列序号");
		return false;
	}
	
	if (dealStatus == "") {
		$.messager.alert("提示 ", "请输入交易状态列序号");
		return false;
	}
	
	if (dealtypeId == "") {
		$.messager.alert("提示 ", "请输入交易类型编号列序号");
		return false;
	}
	
	if (dealDesc == "") {
		$.messager.alert("提示 ", "请输入交易描述列序号");
		return false;
	}
	
	if (dealRebackCode == "") {
		$.messager.alert("提示 ", "请输入交易返回码列序号");
		return false;
	}
	if (bankcardNumber == "") {
		$.messager.alert("提示 ", "请输入银行卡号列序号");
		return false;
	}
	if (settingDesc == "") {
		$.messager.alert("提示 ", "备注不能为空");
		return false;
	}
	if (merchantCode == "") {
		$.messager.alert("提示 ", "请输入商户号序列号");
		return false;
	}
	if($.trim(settingDesc).length>200){
		$.messager.alert("提示","备注字数应小于200字符");
		return false;
	}
	
	$('#save').linkbutton('disable');
	if (flag == "-1") {
		$.post("${ctx}/setting/setting.do?method=addSetting", {
			systemCode:systemCode,
			path:path,
			fileName:fileName,
			startFlag:startFlag,
			splitStr:splitStr,
			dealId:dealId,
			serialNumber:serialNumber,
			transaCount:transaCount,
			terminalCode:terminalCode,
			dealData:dealData,
			dealTime:dealTime,
			dealStatus:dealStatus,
			dealtypeId:dealtypeId,
			dealDesc:dealDesc,
			dealRebackCode:dealRebackCode,
			bankcardNumber:bankcardNumber,
			cost:cost,
			status : status,
			settingDesc:settingDesc,
			merchantCode:merchantCode
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	} else {
		$.post("${ctx}/setting/setting.do?method=updateSetting", {
			settingId :settingId,
			systemCode:systemCode,
			path:path,
			fileName:fileName,
			startFlag:startFlag,
			splitStr:splitStr,
			dealId:dealId,
			serialNumber:serialNumber,
			transaCount:transaCount,
			terminalCode:terminalCode,
			dealData:dealData,
			dealTime:dealTime,
			dealStatus:dealStatus,
			dealtypeId:dealtypeId,
			dealDesc:dealDesc,
			dealRebackCode:dealRebackCode,
			bankcardNumber:bankcardNumber,
			cost:cost,
			status : status,
			settingDesc : settingDesc,
			merchantCode:merchantCode
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}

};
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
	var systemCode = $("#systemCodeF").combobox('setValue',"");
	$.viewSetting(systemCode);
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.close = function() {
	$.hideDivShade();
	$("#settingSave").window('close');
	$("#settingData").window('close');
};

$.cancle = function() {
	$.hideDivShade();
	$("#settingData").window('close');
};
$.deleteRole = function() {
	var rows = $('#viewSetting').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var Ids = "";
	for ( var i = 0; i < rows.length; i++) {
		var status = rows[i].settingStatus;
		if (i == rows.length - 1) {
			Ids += "'" + rows[i].settingId + "'";
		} else {
			Ids += "'" + rows[i].settingId + "'" + ",";
		}
		if (status == "0") {
			$.messager.alert("提示 ", "该设置失效，不能删除!");
			return;
		}
	}
	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/setting/setting.do?method=deleteSetting", {
		Ids : Ids
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.viewSetting = function(systemCode) {
	var systemCode = $("#systemCodeF").combobox('getValue');
	//var systemName = $("#systemCodeF").combobox('getName');
	var status = $("#statusF").combobox('getValue'); 	
	var setId = $("#filepath").combobox('getValue');
	if(systemCode=="-1"){
		systemCode="";
	}
	
	$('#viewSetting').datagrid(
					{
						title : '设置管理',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns : false,
						url : "${ctx}/setting/setting.do?method=getSettings",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						queryParams:{
							systemCode:systemCode,
							status:status,
							setId:setId
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "ck",checkbox : true},
										{field : "path",title : " 文件路径",width : 100,align : "center",sortable : true},
										{field : "fileName",title : " 文件名称",width : 100,align : "center",sortable : true},
										{field : "settingStatusStr",title : "状态",width : 100,align : "center",sortable : true},
										{field : "dealId",title : " 交易编号",width : 80,align : "center",sortable : true},
										{field : "serialNumber",title : " 流水号",width : 80,align : "center",sortable : true},
										{field : "transaCount",title : " 交易金额",width : 80,align : "center",sortable : true},
										{field : "terminalCode",title : " 终端编号",width : 80,align : "center",sortable : true},
										{field : "dealData",title : " 交易日期",width : 80,align : "center",sortable : true},
										{field : "dealTime",title : " 交易时间",width : 80,align : "center",sortable : true},
										{field : "dealStatus",title : " 交易状态",width : 80,align : "center",sortable : true},
										{field : "dealtypeId",title : " 交易类型编号",width : 100,align : "center",sortable : true},
										{field : "dealDesc",title : " 交易描述",width : 80,align : "center",sortable : true},
										{field : "dealRebackCode",title : " 交易返回码",width : 80,align : "center",sortable : true},
										{field : "bankCardnumber",title : " 银行卡号",width : 80,align : "center",sortable : true},
										{field : "merchantCode",title : "商户号",width : 80,align : "center",sortable : true},
										{field : "cost",title : " 成本金额",width : 80,align : "center",sortable : true},
										{field : "settingDesc",title : "设置描述",width : 100,align : "center",sortable : true},
										{field : "createrId",title : "操作人",width : 100,align : "center",sortable : true},
										{field : "createDate",title : "操作时间",width : 150,align : "center",sortable : true}
										 ] ],
						hideColumn : [ [ 
						                 {field : "settingId"},
						                 {field : "startFlag"},
						                 {field : "settingStatus"},
						                 {field : "splitStr"}
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:[
						         {id:'btnadd',text:'添加',iconCls:'icon-add',
						        	 handler:function(){
						        		 $.openWin(-1);
						        	 }
								 },'-',
								 {
									id:'btncut',
									text:'修改',
									iconCls:'icon-edit',
									handler:function(){
											$.openWin(-2);
									}
								 },'-',
								 {
									id:'btnsave',
									text:'删除',
									iconCls:'icon-cut',
									handler:function(){
										$.deleteRole();
									}
								},'-',
								 {
									id:'btnsave',
									text:'文件上传',
									iconCls:'icon-redo',handler:function(){
										$.upload();
									}
								}
						]
						
					});
	var p = $('#viewSetting').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	//上传导入功能
	$.upload = function() {
		$.showDivShade('${ctx}');
		$("#settingData").window('open').window('refresh');
		 $('#filepath').combotree({   
			 url:"${ctx}/setting/setting.do?method=getFilePath",
			 valueField:"id",
			 textField:"text",
			 onSelect:function(node){
				 if(node.id!='-1'){
						var systemCode = node.attributes.systemCode;
						var systemName = node.attributes.systemName;
					  	var splitStr = node.attributes.splitStr;
					  	var fpath = node.attributes.path;
					  	var fileName = node.attributes.fileName;
					  	var startFlag = node.attributes.startFlagStr;
					  	var settingStatus = node.attributes.settingStatusStr;
					  	var terminalCode = node.attributes.terminalCode;
					  	var dealId = node.attributes.dealId;
					  	var serialNumber = node.attributes.serialNumber;
					  	var transaCount = node.attributes.transaCount;
					  	var cost = node.attributes.cost;
					  	var dealData = node.attributes.dealData;
					  	var dealTime = node.attributes.dealTime;
					  	var dealStatus = node.attributes.dealStatus;
					  	var dealtypeId = node.attributes.dealtypeId;
					  	var dealDesc = node.attributes.dealDesc;
					  	var dealRebackCode = node.attributes.dealRebackCode;
					  	var settingDesc = node.attributes.settingDesc;
					  	var merchantCode =node.attributes.merchantCode;
					  	var bankCardNumber=node.attributes.bankCardNumber;
						$("#systemCodes").val(systemName);
						$("#splitStrs").val(splitStr);
						$("#paths").val(fpath);
						$("#fileNames").val(fileName);
						$("#startFlags").val(startFlag);
						$("#statuss").val(settingStatus);
						$("#terminalCodes").numberbox("setValue",terminalCode);
						$("#dealIds").numberbox("setValue",dealId);
						$("#serialNumbers").numberbox("setValue",serialNumber);
						$("#transaCounts").numberbox("setValue",transaCount);
						$("#costs").numberbox("setValue",cost);
						$("#dealDatas").numberbox("setValue",dealData);
						$("#dealTimes").numberbox("setValue",dealTime);
						$("#dealStatuss").numberbox("setValue",dealStatus);
						$("#dealtypeIds").numberbox("setValue",dealtypeId);
						$("#dealDescs").numberbox("setValue",dealDesc);
						$("#dealRebackCodes").numberbox("setValue",dealRebackCode);
						$("#bankcardnumbers").numberbox("setValue",bankCardNumber);
						$("#settingDescs").val(settingDesc); 
						$("#merchantCodes").val(merchantCode); 
						
				 }else{
					 $("#systemCodes").val('');
						$("#splitStrs").val('');
						$("#paths").val('');
						$("#fileNames").val('');
						$("#startFlags").val('');
						$("#statuss").val('');
						$("#terminalCodes").numberbox("setValue",'');
						$("#dealIds").numberbox("setValue",'');
						$("#serialNumbers").numberbox("setValue",'');
						$("#transaCounts").numberbox("setValue",'');
						$("#costs").numberbox("setValue",'');
						$("#dealDatas").numberbox("setValue",'');
						$("#dealTimes").numberbox("setValue",'');
						$("#dealStatuss").numberbox("setValue",'');
						$("#dealtypeIds").numberbox("setValue",'');
						$("#dealDescs").numberbox("setValue",'');
						$("#dealRebackCodes").numberbox("setValue",'');
						$("#bankcardnumbers").numberbox("setValue",'');
						$("#settingDescs").val(''); 
						$("#merchantCodes").val(''); 
				 }
				 
			 }
			 
			}); 
		 $('#filepath').combotree('setValue','-1');
	};
	$.confirm = function(){
		var path = $("#filepath").combobox("getText");
		var name = $("#upload_file").val();
	    
		if(path=="请选择文件路径"){
			 $.messager.alert("提示","请选择文件路径！");
	    	   return;
		}
		
		if(name==""){
			 $.messager.alert("提示","请选择文件！");
	    	   return;
		}
		
		if(!(name.endWith(".txt"))){
	    	$.messager.alert("提示","文件类型不正确，请重新选择！");
	    	return;
	    }
		
		$.ajaxFileUpload({
		       url:"${ctx}/setting/setting.do?method=upload&path="+path,
		       secureuri:false,
		       fileElementId:'upload_file',
		       dataType: 'json',
		       
		       success:function(data,textStatus) {
		    	   $.messager.alert("提示","上传成功！");
		    	   $.close();
		       		}
		    	 }
			);
		
	};
	$(function() {
		$.viewSetting();
		$('#systemCodeF').combobox({
			url:"${ctx}/system/system.do?method=getCombSystems",
			valueField:"id",
			textField:"text"
			}); 
	});
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr style="height: 30px">
			<td>系统名称：</td>
			<td style="padding-left: 5px">
				<select id="systemCodeF" class="easyui-combobox" name="systemCodeF" data-options=" panelHeight:'auto'" style="width: 150px;" editable="false"></select>
			</td>
			<td style="padding-left: 30px">设置状态：</td>
			<td style="padding-left: 5px">
				<select id="statusF" data-options="panelHeight:'auto'" editable="false" class="easyui-combobox" name="statusF" style="width: 150px;">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewSetting()">查询</a>
			</td>
		</tr>
	</table>




	<table id="viewSetting"></table>

	<div id="settingSave" class="easyui-window" title="添加导入数据信息" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="false"
		style="width: 600px; height: 450px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 0px 35px 0px 35px; background: #fff; overflow: hidden;">
				<input type="hidden" id="settingId" name="settingId" />
				<p>
					备注：在对应的表单中输入导入文件的列序号
					<hr>
				</p>
				<table width="100%;" style="padding-top: 2px">
					<tr>
						<td width="100" align="left">系统名称：</td>
						<td align="left">
							<select id="systemCode" class="easyui-combobox" name="systemCode" data-options="panelHeight:'auto'" style="width: 100px;" editable="false"></select>
						</td>
						<td width="100" align="left">分隔符：</td>
						<td align="left">
							<select id=splitStr class="easyui-combobox" name="splitStr" data-options="panelHeight:'auto'" style="width: 100px;" editable="false">
								<option selected="selected" value="-1">请选择分隔符</option>
								<option value="|">|</option>
								<option value="^">^</option>
								<option value=",">,</option>
							</select>
						</td>
					</tr>

					<tr>
						<td width="100" align="left">文件路径：</td>
						<td align="left">
							<input id="path" name="path" type="text" style="width: 100px;" />
						</td>

						<td width="100" align="left">文件名称：</td>
						<td align="left" width="160">
							<input id="fileName" name="fileName" type="text" style="width: 100px;" />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">从第一行读取：</td>
						<td align="left" width="160">
							<select id="startFlag" class="easyui-combobox" name="startFlag" data-options="panelHeight:'auto'" style="width: 100px;" editable="false">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</td>

						<td>设置状态：</td>
						<td align="left">
							<select id="status" class="easyui-combobox" name="status" data-options="panelHeight:'auto'" style="width: 100px;" editable="false">
								<option value="1">有效</option>
								<option value="0">无效</option>
							</select>
						</td>
					</tr>

					<tr>
						<td width="100" align="left">终端编号：</td>
						<td align="left" width="160">
							<input id="terminalCode" name="terminalCode" class="easyui-numberbox" style="width: 100px;" />
						</td>

						<td width="100" align="left">交易编号：</td>
						<td align="left" width="160">
							<input id="dealId" name="dealId" class="easyui-numberbox" style="width: 100px;" />
						</td>

					</tr>

					<tr>
						<td width="100" align="left">流水号：</td>
						<td align="left" width="160">
							<input id="serialNumber" name="serialNumber" class="easyui-numberbox" style="width: 100px;" />
						</td>

						<td width="100" align="left">交易金额：</td>
						<td align="left" width="160">
							<input id="transaCount" name="transaCount" class="easyui-numberbox" style="width: 100px;" />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易成本：</td>
						<td align="left" width="160">
							<input id="cost" name="cost" class="easyui-numberbox" style="width: 100px;" />
						</td>
						<td width="100" align="left">交易日期：</td>
						<td align="left" width="160">
							<input id="dealData" name="dealData" class="easyui-numberbox" style="width: 100px;" />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易时间：</td>
						<td align="left" width="160">
							<input id="dealTime" name="dealTime" class="easyui-numberbox" style="width: 100px;" />
						</td>
						<td width="100" align="left">交易状态：</td>
						<td align="left" width="160">
							<input id="dealStatus" name="dealStatus" class="easyui-numberbox" style="width: 100px;" />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易类型编号：</td>
						<td align="left" width="160">
							<input id="dealtypeId" name="dealtypeId" class="easyui-numberbox" style="width: 100px;" />
						</td>
						<td width="100" align="left">交易描述：</td>
						<td align="left" width="160">
							<input id="dealDesc" name="dealDesc" class="easyui-numberbox" style="width: 100px;" />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易返回码：</td>
						<td align="left" width="160">
							<input id="dealRebackCode" name="dealRebackCode" class="easyui-numberbox" style="width: 100px;" />
						</td>
						<td width="100" align="left">商户号:</td>
						<td>
							<input id="merchantCode" name="merchantCode" class="easyui-numberbox" style="width: 100px;" />
						</td>
					</tr>
					<tr>
						<td width="100" align="left">银行卡号：</td>
						<td align="left" width="160">
							<input id="bankcardnumber" name="bankcardnumber" class="easyui-numberbox" style="width: 100px;" />
						</td>
						<td width="100" align="left"></td>
						<td></td>
					</tr>

					<tr>
						<td colspan="4">备注：</td>
					</tr>
					<tr>
						<td align="left" colspan="4">
							<textarea rows="2" cols="47" style="width: 100%" id="settingDesc" name="settingDesc" maxlength="200"></textarea>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="4">
							<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="settingData" class="easyui-window" title="导入数据信息" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 600px; height: 450px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div id="toolbarDiv" style="padding: 5px; height: auto">
			<table>
				<tr>
					<td>文件路径：</td>
					<td nowrap="nowrap">
						<select id="filepath" class="easyui-combobox" name="filepath" style="width: 150px; height: 30px" editable="false">
						</select>
					</td>
					<td nowrap="nowrap">
						<div class="file-box">
							<input type='text' name='textfield' id='textfield' class='txt' />
							<input type='button' class='btn' value='浏览...' />
							<input type="file" name="upload_file" class="file" id="upload_file" size="28" onchange="document.getElementById('textfield').value=this.value" />

						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px 35px 0px 35px; background: #fff; overflow: hidden;">
				<input type="hidden" id="settingId" name="settingId" />
				<table width="100%;" style="padding-top: 2px">
					<tr>
						<td width="100" align="left">系统名称：</td>
						<td align="left">
							<input id="systemCodes" name="systemCodes" type="text" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">分隔符：</td>
						<td align="left">
							<input id="splitStrs" name="splitStrs" type="text" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">文件路径：</td>
						<td align="left">
							<input id="paths" name="paths" type="text" style="width: 100px;" readonly />
						</td>

						<td width="100" align="left">文件名称：</td>
						<td align="left" width="160">
							<input id="fileNames" name="fileNames" type="text" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">从第一行读取：</td>
						<td align="left" width="160">
							<input id="startFlags" name="startFlags" type="text" style="width: 100px;" readonly />
						</td>

						<td>设置状态：</td>
						<td align="left">
							<input id="statuss" name="statuss" type="text" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">终端编号：</td>
						<td align="left" width="160">
							<input id="terminalCodes" name="terminalCodes" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>

						<td width="100" align="left">交易编号：</td>
						<td align="left" width="160">
							<input id="dealIds" name="dealIds" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>

					</tr>

					<tr>
						<td width="100" align="left">流水号：</td>
						<td align="left" width="160">
							<input id="serialNumbers" name="serialNumbers" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">交易金额：</td>
						<td align="left" width="160">
							<input id="transaCounts" name="transaCounts" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>	</tr>

					<tr>
						<td width="100" align="left">交易成本：</td>
						<td align="left" width="160">
							<input id="costs" name="costs" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">交易日期：</td>
						<td align="left" width="160">
							<input id="dealDatas" name="dealDatas" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易时间：</td>
						<td align="left" width="160">
							<input id="dealTimes" name="dealTimes" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">交易状态：</td>
						<td align="left" width="160">
							<input id="dealStatuss" name="dealStatuss" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易类型编号：</td>
						<td align="left" width="160">
							<input id="dealtypeIds" name="dealtypeIds" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">交易描述：</td>
						<td align="left" width="160">
							<input id="dealDescs" name="dealDescs" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
					</tr>

					<tr>
						<td width="100" align="left">交易返回码：</td>
						<td align="left" width="160">
							<input id="dealRebackCodes" name="dealRebackCodes" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left">商户号:</td>
						<td>
							<input id="merchantCodes" name="merchantCodes" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
					</tr>
					<tr>
						<td width="100" align="left">银行卡号：</td>
						<td align="left" width="160">
							<input id="bankcardnumbers" name="bankcardnumbers" class="easyui-numberbox" style="width: 100px;" readonly />
						</td>
						<td width="100" align="left"></td>
						<td></td>
					</tr>
					<tr>
						<td colspan="4">备注：</td>
					</tr>
					<tr>
						<td align="left" colspan="4">
							<textarea rows="2" cols="47" style="width: 100%" id="settingDescs" name="settingDescs" maxlength="200" readonly></textarea>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="4" height="50px">
							<a name="confirm" id="confirm" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.confirm()">确定</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.cancle()">取消</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
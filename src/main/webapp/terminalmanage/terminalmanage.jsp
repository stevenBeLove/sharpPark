<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>瑞通宝综合管理系统</title>
<style type="text/css">
input{ vertical-align:middle; margin:0; padding:0}
.file-box{ position:relative;width:340px}
.txt{ height:30px; border:1px solid #cdcdcd; width:180px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:30px; width:70px;}
.file{ position:absolute; top:0; right:80px; height:30px; filter:alpha(opacity:0);opacity: 0;width:260px }
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
//判断文件类型
String.prototype.endWith=function(oString){  
	var reg=new RegExp(oString+"$");  
	return reg.test(this);     
};
$(window).resize(function(){
	 $('#viewTerminalManage').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-8;
}
$(function(){
	$.viewTerminalManage();
	});
	
	//上传导入功能
	$.upload = function() {
		$('#systemSource').combobox({
			url:"${ctx}/system/system.do?method=getCombSystems",
			valueField:"id",
			textField:"text"
			});
		$('#terminalType').combobox({
			url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
			valueField:"id",
			textField:"text"
			});
	  	$("#uploadBtn").linkbutton('disable');
	  	var name = $("#upload_file").val();
	    
		var teminalmanageRule = $("#teminalmanageRule").combobox('getText');
		var teminalmanageRuleId = $("#teminalmanageRule").combobox('getValue');
		
	    if(name ==""){
	    	$.messager.alert("提示","请选择要上传的文件！");
	    	$("#uploadBtn").linkbutton('enable');
	      	return;
	    }
	    
	    if(name.endWith(".txt")){
	    	if(teminalmanageRuleId =="-1"){
		    	$.messager.alert("提示","请选择分隔符！");
		    	$("#uploadBtn").linkbutton('enable');
		      	return;
		    } 
	    }
	    
	    if(!(name.endWith(".txt")||name.endWith(".xls"))){
	    	$.messager.alert("提示","文件类型不正确，请重新选择！");
	    	$("#uploadBtn").linkbutton('enable');
	    	return;
	    }
	   
	    $.ajaxFileUpload({
	           url:"${ctx}/terminalmanage/terminalmanage.do?method=upload&teminalmanageRule="+teminalmanageRule,
	           secureuri:false,
	           fileElementId:'upload_file',
	           contentType: "application/x-www-form-urlencoded; charset=utf-8", 
	           dataType: 'json',
	           success:function(data,textStatus) {
	         	 	var str = eval("(" +data+ ")");
	         	 	$("#uploadBtn").linkbutton('enable');
	         	 	$.openWin();
	         	 	$('#terminalCode').combobox({
	         			valueField:"id",
	         			textField:"text",
	         			data:str
	         			});
	         	 	$('#merchantCode').combobox({
	         			valueField:"id",
	         			textField:"text",
	         			data:str
	         			});
	         	 	$('#terminalDevCode').combobox({
	         			valueField:"id",
	         			textField:"text",
	         			data:str
	         			});
	           		}
	        	 }
	 		); 
	  };

	$.openWin= function(){
		 $("#terminalManageImport").window('open').window('refresh');  
	}; 


	$.success = function (message, data) {
		$.hideDivShade();
		$.messager.alert("提示 ",message);
		$('#uploadBtn').linkbutton('enable');
		 $.close(); 
		 $.viewTerminalManage();
	};
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		$('#uploadBtn').linkbutton('enable');
	};
	$.close = function(){
	
		$("#terminalManageImport").window('close');  
	};
	
	
	$.saveExcelData = function (){
		
		var systemSource =$('#systemSource').combobox('getValue');
		var merchantCode = $("#merchantCode").combobox('getValue');
		var teminalmanageRule = $("#teminalmanageRule").combobox('getText');
		var terminalTypeId = $("#terminalType").combobox('getValue');
		var terminalCode = $("#terminalCode").combobox('getValue');
		
		if(systemSource=="-1"){
 	 		$.messager.alert("提示","请选择来源系统！");
	      	return;
 	 	}
		
		if(merchantCode=="-1"){
 	 		$.messager.alert("提示","请选择商户号！");
	      	return;
 	 	}
		
		if(terminalTypeId=="-1"){
			$.messager.alert("提示 ","请选择终端类型！");
			return;
		}
		
		if(terminalCode=="-1"){
			$.messager.alert("提示 ","请选择终端编号！");
			return;
		}
		$("#terminalManageImport").window('close');  
		$.showDivShade('${ctx}');
		$.ajax({
            type: "post",
            url: "${ctx}/terminalmanage/terminalmanage.do?method=saveExcelData",
            contentType:"application/x-www-form-urlencoded; charset=utf-8",
            data: {
            	systemSource:systemSource,
				merchantCode:merchantCode,
				teminalmanageRule:teminalmanageRule,
				terminalTypeId:terminalTypeId,
				terminalCode:terminalCode
				},
            dataType: "json",
            success: function(data){
            	alert(data);
            	$.hideDivShade();
        		$.viewTerminalManage();
            }
        });
		/* $.post('${ctx}/terminalmanage/terminalmanage.do?method=saveExcelData', 
				
				{
					systemSource:systemSource,
					merchantCode:merchantCode,
					teminalmanageRule:teminalmanageRule,
					terminalTypeId:terminalTypeId,
					terminalCode:terminalCode
				},
				contentType:"application/x-www-form-urlencoded; charset=utf-8",
				function(data) {
					//$.parseAjaxReturnInfo(data, $.success, $.failed);//
					alert(data);
				}, "json"); */
	};
	//加载数据表格
	$.viewTerminalManage = function() {
		$('#viewTerminalManage').datagrid({
			title : '终端管理',
			width : $(window).width()-8,
			height : $(window).height() * 0.95,
			pageSize : 20,
			pageNumber : 1,
			fitColumns : false,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageTb",
			queryParams:{
				terminalCode:'',
				terminalTypeId:'',
				status:''
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ 
			             {field : "systemSource",title : "来源系统",width : 100,align : "center",sortable : true},
			           //  {field : "merchantCode",title : "商户号",width : 200,align : "center",sortable : true},
			             {field : "terminalCode",title : "终端编号",width : 150,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : 100,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : 200,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : 100,align : "center",sortable : true},
			           //  {field : "openDate",title : "开通时间",width : 200,align : "center",sortable : true}, 
			           //   {field : "clientCode",title : "所属客户编码",width : 200,align : "center",sortable : true},
			           //  {field : "scrapDate",title : "报废时间",width : 200,align : "center",sortable : true}, 
			            {field : "createId",title : "创建人编号",width : 200,align : "center",sortable : true},
			            {field : "createDt",title : "创建时间",width : 200,align : "center",sortable : true}
			           ] ],
		         hideColumn : 
		        	   [ [ 
						 {field : "onlyCode"},
						 {field : "agencyId"},
						 {field : "terminaltypeId"},
						 {field : "terminalStatus"},
						 {field : "systemId"}
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar:'#toolbarDiv'
		});
		//分页
		var p = $('#viewTerminalManage').datagrid('getPager');
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
<div id="toolbarDiv" style="padding:5px;height:auto">

	<div style="margin-bottom:5px">
		<table>
		<tr>
			<td>分隔符：</td>
			<td nowrap="nowrap">
				<select id="teminalmanageRule" class="easyui-combobox" name="teminalmanageRule" style="width: 150px;height: 30px" editable="false">
 					<option selected="selected" value="-1">请选择分隔符</option>
					<option value="0">|</option>
					<option value="1">^</option>
					<option value="2">,</option>
 				</select>
 			</td>
 			<td nowrap="nowrap">
			    <div class="file-box">
					 <input type='text' name='textfield' id='textfield' class='txt' />  
					 <input type='button' class='btn' value='浏览...' />
					 <input type="file" name="upload_file" class="file" id="upload_file" size="28" onchange="document.getElementById('textfield').value=this.value" />
					 	<a href="#" id="uploadBtn" name="uploadBtn" class="easyui-linkbutton" class="btn" iconCls="icon-add" plain="true" onclick="$.upload()">上传</a>
				</div>
			</td>
		</tr>
		</table>
	</div>
	
</div>
	<table id="viewTerminalManage"></table>

	<div id="terminalManageImport" class="easyui-window" title="终端管理" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="false"
		style="width: 600px; height: 300px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:10px;background: #fff; overflow: hidden;">
					<table>
						<tr style="height: 50px">
							<td>来源系统：</td>
					 		<td  nowrap="nowrap">
					 			<select id="systemSource" class="easyui-combobox" name="systemSource" style="width: 150px;height: 30px" editable="false">
					 			</select>
					 		</td>
							<td>商户号：</td>
					 		<td  nowrap="nowrap">
					 			<select id="merchantCode" class="easyui-combobox" name="merchantCode" style="width: 150px;height: 30px" editable="false">
					 			</select>
					 		</td>
						</tr>
						<tr style="height: 50px">
							<td>终端类型名称：</td>
							<td>
								<select id="terminalType" class="easyui-combobox" editable="false" name="terminalType" style="width: 150px;height: 30px"></select>				
							</td>
							
							<td>终端编号</td>
							<td>
								<select id="terminalCode" class="easyui-combobox" name="terminalCode" style="width: 150px;height: 30px"></select>				
							</td>
							
						</tr>
						
						<tr>
							<td align="center" colspan="4" style="height: 100px">
								<a name="saveExcelData" id="saveExcelData"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.saveExcelData()">导入</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
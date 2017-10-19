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
 
var terminalFlag=1;
var num=1, num1=1, num2=1;
var startCode;
var endCode ;
$(window).resize(function(){
	 $('#viewTerminalManage').datagrid('resize', {
	 	width:cs()
	 });
});

function cs(){	
	return $(window).width()-8;
}

$(function(){

    $("#down").attr('disabled',"false");
	
	    $("#isPsamOrTa").click(function() {               
         if($("#isPsamOrTa").attr("checked")){
                    $("#isPsamOrTa").prop("checked",false);
                    $("#isPsamOrTa").removeAttr("checked");              
              }else{         
                 $("#isPsamOrTa").prop("checked",true);
                 $("#isPsamOrTa").attr("checked",true);
               
              }
         });
              

	
	
	
});

$.openWin= function(){
		 $("#terminalImport").window('open').window('refresh');  
	}; 
	
$.close = function(){
	
		$("#terminalImport").window('close');  
	};
	
	  var isTaFlag ="";
	//加载数据表格
	$.viewTerminalManage = function(obj) {
	       isTaFlag ="";
		  startCode = $("#startCode").val();
		  endCode = $("#endCode").val();
		  
		    if($("#isPsamOrTa").attr("checked")=="checked" && 1==obj){
	        $.messager.alert("提示 ","请输入Ta终端号段,选ta卡核心查询！");
	          return;
	       }
		   if(!($("#isPsamOrTa").attr("checked")=="checked") && 3==obj){
	         $.messager.alert("提示 ","请勾选TA终端");
	           return;
	        }
	        
        if($("#isPsamOrTa").attr("checked")=="checked"){
	          isTaFlag="1";
	     }else{
	           isTaFlag="0";
	     }	
		  
		  
		if(1==obj){
		   $('#viewTerminalManage').datagrid({
			title : '终端入库',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : '${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageCore',
			queryParams:{
				startCode:startCode,
				endCode:endCode,
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess: function(data){
			            num1=data.total;
				      },
			columns : [ [  
			             {field : "ck",checkbox : true,width : "50"},
			             {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "agencyId",title : "所属机构",width : 300,align : "center",sortable : true},
			              {field : "terminalStatusStr",title : "终端状态",width : 300,align : "center",sortable : true},
			           ] ],
		         hideColumn : 
		        	   [ [ 
						  
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		 });
		
		}
		
		
		if(3==obj){
		   $('#viewTerminalManage').datagrid({
			title : '终端入库',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : '${ctx}/terminalmanage/terminalmanage.do?method=getTaCardTerminalCore',
			queryParams:{
				startCode:startCode,
				endCode:endCode,
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess: function(data){
			            num=data.total;
				      },
			columns : [ [  
			             {field : "ck",checkbox : true,width : "50"},
			             {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "agencyId",title : "所属机构",width : 300,align : "center",sortable : true},
			              {field : "terminalStatusStr",title : "终端状态",width : 300,align : "center",sortable : true},
			           ] ],
		         hideColumn : 
		        	   [ [ 
						  
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		 });
		
		}
		
		
		
		 if(2==obj){
		     $('#viewTerminalManage').datagrid({
			title : '终端入库',
			width : $(window).width()-8,
			height : $(window).height() * 0.85,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:false,
			url : '${ctx}/terminalmanage/terminalmanage.do?method=getAgencyFrun',
			queryParams:{
				startCode:startCode,
				endCode:endCode,
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			onLoadSuccess: function(data){
			               num2=data.total;
				      },
			columns : [ [  
			             {field : "ck",checkbox : true,width : "50"},
			             {field : "terminalCode",title : "终端识别码",width : 200,align : "center",sortable : true},
			             {field : "agencyId",title : "所属机构",width : 300,align : "center",sortable : true},
			              {field : "terminalStatusStr",title : "终端状态",width : 300,align : "center",sortable : true},
			           ] ],
		         hideColumn : 
		        	   [ [ 
						  
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true
		  });
		
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
 
	 $.terminalInsert=function(){
	     if(num1 < 0 || num2 > 0){
	     	return false;
	     }else{
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
			$.openWin();
	
	     }
	   
	 };
	 $.importTerminal=function(){
	 
							
			var systemSource =$('#systemSource').combobox('getValue');
			var terminalTypeId = $("#terminalType").combobox('getValue');
		//  alert(systemSource);
		//    alert(terminalTypeId);
			if(systemSource=="-1"){
 	 			$.messager.alert("提示","请选择来源系统！");
	    		 return;
 	 		}
		
			if(terminalTypeId=="-1"){
				$.messager.alert("提示 ","请选择终端类型！");
				return;
			}
			$.close();
			$.ajax({
          	  type: "post",
          	  url: "${ctx}/terminalmanage/terminalmanage.do?method=terminalImport",
          	  data: {
            	startCode:startCode,
				endCode:endCode,
				isTaFlag :isTaFlag,
				systemSource:systemSource,
				terminalTypeId:terminalTypeId 
				},
             dataType: "json",
             success: function(data){
            	alert(data);
            }
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
			<td align="left"><input type="text" name="endCode" id="endCode" maxlength="100" style="width: 150px;"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/></td>
	     	<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalManage(1)">psam核心查询</a></td>
	       <!--  <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTerminalManage(3)">TA卡核心查询</a></td>  --> 
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.viewTerminalManage(2)">分润查询</a></td>
			<td><a href="#" id="down" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.terminalInsert()">导入</a></td>
			<!-- <td><label for="isPsamOrTa">TA终端</label></td>  
			<td><input type="checkbox"  id="isPsamOrTa"  name="isPsamOrTa" value="0" /></td>   --> 
		</tr>
	</table>
	
	<table id="viewTerminalManage"></table>
	
 		<div id="terminalImport" class="easyui-window" title="终端入库" closable="false"
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
					 			<select id="systemSource" class="easyui-combobox" name="systemSource" style="width: 150px;height: 30px" editable="true">
					 			</select>
					 		</td>
					 			<td>终端类型名称：</td>
							<td>
								<select id="terminalType" class="easyui-combobox" editable="false" name="terminalType" style="width: 150px;height: 30px"></select>				
							</td>
						</tr>
						<tr>
							<td align="center" colspan="4" style="height: 100px">
								<a name="saveExcelData" id="saveExcelData"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.importTerminal()">导入</a>
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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">	
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
		$('#agencytreeId').combotree({
				  url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',    
		   		 method:'get',
		  		  readonly:false,
		       onLoadSuccess:function(a,b){
		      },
		      panelWidth:   350
		});
	
	$(".combo-text").bind("input onchange",function(a,b){
			$('#agencytreeId').combotree('tree').tree("search",$(this).val());
			if($(this).val()==""||null==$(this).val()){
				$('#agencytreeId').combotree('tree').tree("collapseAll");
			} 
	});
		
	$('#terminalType').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});
	$('#terminalTypeF').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});
	});
	

	$.success = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#terminalBack').linkbutton('enable');
		 $.close(); 
		 $.viewTerminalManage();
	};
	
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#terminalBack').linkbutton('enable');
		 $.close(); 
	};
	$.close = function(){
		$.hideDivShade();
	};
	
	//加载数据表格
	$.viewTerminalManage = function() {
		var terminalCode = $("#terminalCode").val();
		var terminalCodeEnd=$("#terminalCodeEnd").val();
		
		$('#viewTerminalManage').datagrid({
			title : '终端回拨',
			width : $(window).width()-8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:true,
			url : "",
			queryParams:{
				terminalCode:terminalCode,
				terminalCodeEnd:terminalCodeEnd
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ 
			             {field : "terminalCode",title : "终端识别码",width : $(window).width()*0.25,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : $(window).width()*0.15,align : "center",sortable : true},
			           //  {field : "openDate",title : "开通时间",width : $(window).width()*0.2,align : "center",sortable : true}, 
			           //  {field : "clientCode",title : "所属客户编码",width : $(window).width()*0.2,align : "center",sortable : true},
			           //  {field : "scrapDate",title : "报废时间",width : $(window).width()*0.2,align : "center",sortable : true}, 
			             {field : "createId",title : "下发人",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "createDt",title : "下发时间",width : $(window).width()*0.2,align : "center",sortable : true}
			           ] ],
		         hideColumn : 
		        	   [ [ 
						 {field : "onlyCode"},
						 {field : "agencyId"},
						 {field : "terminaltypeId"},
						 {field:"terminalStatus"}
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
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
	
	function viewTerminalManageLoad(){
		var status = "0";
		var terminalCode = $("#terminalCode").val();
		var terminalCodeEnd=$("#terminalCodeEnd").val();
		if(terminalCode == ''  && terminalCodeEnd =='' ){
			$.messager.alert("提示 ", "输入筛选号段不能为空！");
			return;
		}
		var opts = $("#viewTerminalManage").datagrid("options");
	    opts.url = "${ctx}/terminalmanage/terminalmanage.do?method=getNewTerminalManageback";
	    opts.queryParams = {
	    		terminalCode:terminalCode,
				terminalCodeEnd:terminalCodeEnd
	    };
	    $("#viewTerminalManage").datagrid("load");
		
	}
	
	//回拨
	 $.terminalBack = function(){
		var length = $("#viewTerminalManage").datagrid("getRows").length;
		if(length == 0){
			$.messager.alert("提示 ", "查询可回拨的记录为空，请输入筛选条件！");
			return;
		}

		var terminalCode = $("#terminalCode").val();
		var terminalCodeEnd=$("#terminalCodeEnd").val();
		
		if(terminalCode == ''&&  terminalCodeEnd ==''){
			$.messager.alert("提示 ", "输入筛选条件不能为空！");
		}
		
		$.messager.confirm("确认", "依照搜索条件创建 回拨 批次请求？", function (r) {  
		        if (r) {  
			        	 $.post('${ctx}/terminalmanage/terminalmanage.do?method=saveTerminalChangeBatch',{
			        		 terminalCode:terminalCode,
			        		 terminalCodeEnd:terminalCodeEnd
			     		}, function(data) {
			     			
			     		}, "json");
		        }  
		});  
		/* */
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="left">终端编号开始：</td>
			<td align="left"><input type="text" name="terminalCode" style="width: 150px;"
			 onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"   id="terminalCode" maxlength="100"/>
			</td>
			<td align="left">终端编号结束：</td>
			<td align="left"><input type="text" name="terminalCodeEnd" style="width: 150px;"
			onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"	  id="terminalCodeEnd" maxlength="100"/>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-search'" onclick="viewTerminalManageLoad()">查询</a>
			</td>	
			<td>
				<a id="terminalBack"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.terminalBack()">创建申请回拨批次</a>
			</td>			
		</tr>
	</table>
	<table id="viewTerminalManage"></table>

	
</body>
</html>
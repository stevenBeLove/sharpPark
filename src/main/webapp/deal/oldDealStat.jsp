<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
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
		    url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=spec',
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
		});
		 */
		
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
	
	$.viewDealStat();
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
	
	$.viewDealStat = function() {
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var agencyId = $('#agencyId').combotree('getValue');

		if(agencyId=='-1' || agencyId==null){
			agencyId = '';
		}
 
		$('#viewDealStat').datagrid(
						{
						title : '交易统计报表',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/deal/deal.do?method=oldDealStat",
						queryParams : {
							yearmonthdatestart:yearmonthdatestart,						
							yearmonthdateend:yearmonthdateend,
							agencySelect:agencyId,
						},
						pageSize : 20,
						pageNumber : 1,
						singleSelect : false,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						onLoadSuccess: function(data){
							var val="总笔数："+data.feeAmtcount  +", 总金额： "+ data.accountCount+"元";
							$("#counts").text(val);
						},
						columns : [ [
										{field : "dealTypeName",width : 300,title : "交易类型",align : "center"},
										{field : "transcost",width : 150,title : "总交易笔数",align : "center",sortable : true},
										{field : "transacount",width : 150,title : "总交易金额(元)",align : "center",sortable : true},
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
		
		var p = $('#viewDealStat').datagrid('getPager');
		$(p).pagination({
			pageList : [20],
			beforePageText : '    第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '   当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
		
	// 导出交易统计报表
	$.dealStatExport = function() {
		var yearmonthdatestart = $('#yearmonthdatestart').val();
		var yearmonthdateend = $('#yearmonthdateend').val();
		var agencyId = $('#agencyId').combotree('getValue');

		if(agencyId=='-1' || agencyId==null){
			agencyId = '';
		}
		
		$.getToPost("${ctx}/deal/deal.do?method=dealOldStatExport",{
			yearmonthdatestart:yearmonthdatestart,
			yearmonthdateend:yearmonthdateend,
			agencySelect:agencyId
		});
	};
};
</script>
</head>

<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr style="height: 30px">
			<td align="left">交易开始日期：</td>
			<td align="left">
				 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">交易结束日期：</td>
			<td align="left">
				 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 100px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">机构名称：</td>
			<td align="left">
				 <input id="agencyId"  name="agencyId" editable="true"  class="easyui-combotree" style="width:160px;height: 20px" />&nbsp;&nbsp;&nbsp;  
			</td>
	
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDealStat()">查询</a>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.dealStatExport()">导出</a>
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

	<table id="viewDealStat"> </table>

</body>
</html>
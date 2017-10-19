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
	 $('#viewSplitLog').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}

$.viewSplitLog = function() {
	$('#viewSplitLog').datagrid(
					{
						title : '分润日志',
						width:$(window).width()-6,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/splitfee/splitfee.do?method=getSplitFeeLog",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
						
										{field : "executeCode",title : "执行编号",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "executeDesc",title : "执行描述",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "startTime",title : "开始时间",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "endTime",title : "结束时间",width : $(window).width()*0.2,align : "center",sortable : true},
										{field : "executeTime",title : "执行时间",width : $(window).width()*0.15,align : "center",sortable : true,hidden : true},
										{field : "executeReult",title : "执行结果",width : $(window).width()*0.2,align : "center",sortable : true}
										
										 ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:[{
							id:'btnadd',
							text:'刷新',
							iconCls:'icon-add',
							handler:function(){
								$.viewSplitLog();
							}
						}]
					});
	var p = $('#viewSplitLog').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};
	$(function() {
		$.viewSplitLog();
	});
</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table id="viewSplitLog"></table>
</body>
</html>
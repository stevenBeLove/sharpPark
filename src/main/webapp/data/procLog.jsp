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
	 $('#viewDealType').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}

$(function(){
		$('#agencyTreeId').combotree({    
		    url: '${ctx}/agency/agency.do?method=getAgencyTree',    
		    method:'get',
		    readonly:false,
		    onLoadSuccess:function(a,b){
		    	$('#agencyTreeId').combotree('setValue', '-1');
		    }
		});
		$.viewProcLog();
});
    
$.viewProcLog = function() {
	var agencyTreeId = $("#agencyTreeId").combobox('getValue');
	var execDate = $("#execDate").val();
	if("-1"==agencyTreeId){
			agencyTreeId="";	
	}
	$('#viewProcLog').datagrid(
					{
						title : '交易类型',
						width : $(window).width()*1.1,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:true,
						url : "${ctx}/data/data.do?method=getProcLog",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							agencyId:agencyTreeId,
							execDate:execDate
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : true,
						columns : [ [
										{field : "procName",title : "存储过程名", align : "center",sortable : true},
										{field : "procRemark",title : "存储过程备注", align : "center",sortable : true},
										{field : "execDate",title : "执行日期", align : "center",sortable : true},
										{field : "runFlag",title : "执行状态 ", align : "center",sortable : true},
										{field : "stime",title : "开始时间", align : "center",sortable : true},
										{field : "etime",title : "结束时间 ", align : "center",sortable : true},
										{field : "retCode",title : "处理状态码 ", align : "center",sortable : true },
										{field : "retMsg",title : "处理消息 ", align : "center",sortable : true },
										{field : "note1",title : "备注1 ", align : "center",sortable : true },
									    {field : "note2" ,title : "备注2 ", align : "center",sortable : true },
										{field : "procTime",title : "执行时间 ", align : "center",sortable : true },
										{field : "qty",title : "处理数据量 ", align : "center",sortable : true },
										
								 ] ],
						hideColumn : [ [ 
						                
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true 
						});
	var p = $('#viewDealType').datagrid('getPager');
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
	<table width="100%">
		<tr>
			<td width="100">
				机构名称：
			</td>
				<td align="left" width="160">
		     	<select id="agencyTreeId" class="easyui-combobox" name="agencyTreeId" style="width: 150px;height: 30px" editable="false"></select>
			</td>
			<td width="100" align="left">
				执行时间：
			</td>
			<td>
				 <input id="execDate" name="execDate" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			</td>
		
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewProcLog()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewProcLog"></table>
	
	 
</body>
</html>
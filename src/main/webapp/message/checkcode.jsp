<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>验证码查询</title>
<script type="text/javascript">
$(function(){
	//初始化页面
	viewApplyList();
});

function resetQuery()
{
	$("#userName").val('');
	$("#telephone").val('');
}

function viewApplyList()
{
	var userName = $("#userName").val();
	var telephone = $("#telephone").val();
	var startDate = $("#startDate").datebox('getValue');
	var endDate = $("#endDate").datebox('getValue');
	$("#messageList").datagrid({
		title : '验证码查询',
		height : $(window).height() * 0.9,
		loadMsg : '数据正在加载,请耐心等待...' ,
		rownumbers:true ,
		remoteSort : false,
		url : "${ctx}/message/message.do?method=getCheckCode",
		queryParams : {
			userName: userName,
			telephone: telephone,
			startDate: startDate,
			endDate:endDate
		},
		columns : [[    
		          	{field:'userName',title:'用户名称',width:100,align:'left',sortable:true,halign:'center'},    
		          	{field:'mobileNo',title:'手机号码',width:100,align:'center',sortable:true},    
		          	{field:'checkCode',title:'短信验证码',width:100,align:'center',sortable:true},
		          	{field:'effTime',title:'有效时间',width:150,align:'center',sortable:true,
		          		formatter:function(value, row, index){
		          			return value.substring(0,8) + " " + value.substring(8);
		          		}	
		          	},
		          	{field:'checkFlag',title:'验证标识',width:100,align:'center',sortable:true,
		          		formatter:function(value,row,index){
		          			if(value == 0){
		          				return "未验证";
		          			}else{
		          				return "已验证";
		          			}
		          		}
		          	}
		]],
		pagination : true,
		pageSize : 20 ,
		pageList : [10, 20, 50, 100],
		showFooter :  true
	});
}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td>用户名称:</td>
			<td><input class="easyui-validatebox" id="userName" data-options="required:false" /></td>
			<td>手机号码:</td>
			<td><input class="easyui-validatebox" id="telephone" data-options="required:false" /></td>
			<td>开始日期:</td>
			<td><input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;" /></td>
			<td>结束日期:</td>
			<td><input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;" /></td>
			<td><a id="queryBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="viewApplyList()">查询</a> <!-- <a id="resetBtn" class="easyui-linkbutton" data-options="iconCls:'icon-reset'" onclick="resetQuery()">重置</a> -->
			</td>
		</tr>
	</table>
	<table id="messageList"></table>
</body>
</html>
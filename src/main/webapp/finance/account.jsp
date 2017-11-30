<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;"%>
<%
    String agencyId = session.getAttribute(ConstantUtils.AGENCYID)
					.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
<script type="text/javascript">	
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString().trim()%>';
var parentagencyId='<%=session.getAttribute(ConstantUtils.PARENTAGENCYID).toString().trim()%>';
var staticagecyId='<%=ConstantUtils.CENTERCODE%>';
var roleid=${sessionScope.roletypeId};
var onlineControl='<%=session.getAttribute(ConstantUtils.ONLINEFLAG)%>';
var agencyControl='<%=session.getAttribute(ConstantUtils.AGENCYFLAG)%>';
	function makeAccount() {
		var dateSet = $("input[name='dateSet']:checked").val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');
		var payType = $("input[name='payType']:checked").val();
		if ($.trim(dateSet) == "") {
			$.messager.alert("提示 ", "请选择对账时间");
			return false;
		}
		if(dateSet==4){
			if($.trim(startDate) == ""||$.trim(endDate) == ""){
				$.messager.alert("提示 ", "自定义时间不能为空");
				return false;
			}
		}
		if ($.trim(payType) == "") {
			$.messager.alert("提示 ", "收费类型不能为空");
			return false;
		}
		
		$.getToPost('${ctx}/finance/account.do?method=makeAccount', {
			dateSet : dateSet,
			startDate : startDate,
			endDate : endDate,
			payType : payType
		});
		}
</script>
</head>
<body id="indexd">
<div style="padding-top: 20px;">
	<table>
		<tr>
			<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label>对账时间设置：</label>
			</td>
			<td>
				<input class="easyui-validatebox" type="radio" name="dateSet" value="1" />昨天
				<input class="easyui-validatebox" type="radio" name="dateSet" value="2"/>上周
				<input class="easyui-validatebox" type="radio" name="dateSet" value="3"/>上月
				<input class="easyui-validatebox" type="radio" name="dateSet" value="4"/>自定义时段
				<input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;" />
				——
				<input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;" />
			</td>
			<td colspan="2"><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="makeAccount()">生成对账单</a></td>
		</tr>
		<tr>
			<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label>收费类型：</label>
			</td>
			<td>
				<input class="easyui-validatebox" type="radio" name="payType" value="0"/>全部
				<input class="easyui-validatebox" type="radio" name="payType" value="1"/>应收现金
				<input class="easyui-validatebox" type="radio" name="payType" value="2"/>支付宝
				<input class="easyui-validatebox" type="radio" name="payType" value="3"/>微信
			</td>
		</tr>
	</table>
	</div>
</body>

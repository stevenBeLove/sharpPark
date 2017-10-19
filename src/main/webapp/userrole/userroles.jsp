<%@page import="com.compass.utils.ConstantUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>瑞通宝综合管理系统</title>
<style type="text/css">
		#fm {
			margin: 0;
			padding: 10px 30px;
		}
		.ftitle {
			font-size: 14px;
			font-weight: bold;
			padding: 5px 0;
			margin-bottom: 10px;
			border-bottom: 1px solid #ccc;
		}
		
		.fitem {
			margin-bottom: 5px; 
		}
		
		.fitem label {
			display: inline-block;
			width: 80px;
}
</style>
<script type="text/javascript">
var thisAgencyId='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
var centercode='<%=ConstantUtils.CENTERCODE%>';
var companyBriefName='<%=session.getAttribute(ConstantUtils.COMPANYBRIEFNAME).toString().trim()%>';
var roleid=${sessionScope.roletypeId};
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString().trim()%>';

//判断是否为中文
function checkChinese(str){
	var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
	return reg.test(str);
}
$(window).resize(function(){
	 $('#userList').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-6;
}
	var url;
	var agencyId;
	var loginNamePrefix;
	
	//修改
	function editUser(obj) {
		
		var rows = $('#userList').datagrid('getSelections');
		var len = rows.length;	
		var row = $('#userList').datagrid('getSelected');
		if (len > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条记录修改");
			return;
		}else if(len==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要修改的记录");
			return;
		}
		$('#roleidsel').combobox('setValue', row.roleId); 
		// 原始登录名拆开显示 20141102
		var logname = row.loginName;
		$('#loginName').val(row.loginName);
		$("#roleids").val(row.roleId);
		$("#userid").val(row.userId);
	 
		agencyId=row.agencyId;
		$("#status").combobox('select', row.status);
		url = "${ctx}/userrole/userrole.do?method=updateUserRole";
		$('#dlg').dialog('open').dialog('setTitle', '修改用户角色');
		
	}
	 
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		userView();
	
	};

	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
	$.close=function(){
		$('#dlg').dialog('close');
		$.hideDivShade();
	};
	
 
	//添加
	function saveUser() {
		debugger;
		var userId=$("#userid").val();
		var roleid=$("#roleids").val();
		var roleidsel=$("#roleidsel").combobox('getValue');
		if(""==roleidsel||null==roleidsel){
			alert("请选择角色!");
			return ;
		}
		if(roleidsel==roleid){
			alert("请选择的角色与原角色一样!");
			return ;
		}
		$('#save').linkbutton('disable');
		$.post("${ctx}/userrole/userrole.do?method=updateUserRole", {
			userId : userId,
		    roleId : roleidsel,

		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	}
	
	
	function userView() {
		var userName = $("#userNameAndId").val();
		var agencyid = $("#agencyheadId").combobox('getValue');
		var roleid=  $("#roleid").combobox('getValue');
		debugger;
		$('#userList').datagrid(
						{
							title : '用户角色管理',
							url : '${ctx}/userrole/userrole.do?method=getUserRole',
							queryParams : {								
								userName : userName,
								agencyid : agencyid,
								roleid : roleid,
							},
							pageSize:20,
							width : $(window).width()-6,
							height : $(window).height() * 0.90,
							fitColumns:true,
							frozenColumns : [ [ {
								field : 'ck',
								checkbox : true,
								width : "50"
							} ] ],
							columns : [ [
									{field : "userId",width : $(window).width()*0.1,title : "用户ID",align : "center",hidden : true},
									{field : "roleId",width : $(window).width()*0.1,title : "用户ID",align : "center",hidden : true},
									{field : "userName",width : $(window).width()*0.1,title : "用户名称",align : "center"},
									{field : "loginName",width : $(window).width()*0.1,title : "登录名称",align : "center"},
									{field : "roleName",width : $(window).width()*0.1,title : "用户类型",align : "center"},
									{field : "agencyName",width : $(window).width()*0.2,title : "所属机构",align : "center"},
								 
							] ],
							hideColumn : [ [ {
							} ] ],
							pagination : true,
							rownumbers : true,
							showFooter : true,
							toolbar:[ {
								id:'btncut',
								text:'修改',
								iconCls:'icon-edit',
								handler:function(){
									editUser(-2);
									
								}
							} ]
						});
		var p = $('#userList').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	}

	$(function() {
		if(thisAgencyId == centercode){
			$("#agencyStr1").show();
			$("#agencyStr2").show();
		}
		
		userView();
		$("select").combobox({
			editable:false 
		});
		
		$('#agencyheadId').combotree({
			  url: '${ctx}/agency/agency.do?method=getAgencyTree',    
	 		  method:'get',
	          onLoadSuccess:function(a,b){
	    	  },
	    	  panelWidth:   350
		});
		/* $('#agencyheadId').combotree({
			url : '${ctx}/agency/agency.do?method=getChildList&agencyId=',
			method : 'get',
			readonly : false,
			onLoadSuccess : function(a, b) {
				$('#agencyheadId').tree({}); 
			},
			onBeforeExpand : function(node, b) {
				var children=$('#agencyheadId').tree('getChildren',node.target);
				if(children&&children.length==0){
					$.ajax({
	                    type : "post",
	                    url : '${ctx}/agency/agency.do?method=getChildList&agencyId='+node.id,
	                    dataType : "json",
	                    success : function(data) {
	                        $('#agencyheadId').tree('append', {
	                            parent : node.target,
	                            data : data
	                        });
	                        $("#agencyheadId").tree("reload");
	                    }
	                });	
				}
			},
			panelWidth : 350
		});
		 */
		$(".combo-text").bind("input onchange",function(a,b){
			$('#agencyheadId').combotree('tree').tree("search",$(this).val());
			if($(this).val()==""||null==$(this).val()){
				$('#agencyheadId').combotree('tree').tree("collapseAll");
			} 
		});
		
		
		$("#roleid").combobox({
			url:"${ctx}/role/role.do?method=getRoles&flag=1",
			valueField:"id",
			textField:"text",
			editable:false 
		});
		
		$("#roleidsel").combobox({
			url:"${ctx}/role/role.do?method=getRoles&flag=1",
			valueField:"id",
			textField:"text",
			editable:false,
			 
		});
	});
	
</script>
</head>
<body id="indexd">
	<table >
		<tr>
			<td align="left">机&nbsp;&nbsp;&nbsp;&nbsp;构：</td>
			<td  align="left"><input class="easyui-combotree" id="agencyheadId"  name="vestagencyId"
							 editable="true"  style="width:140px;" />&nbsp;&nbsp;&nbsp;
			</td>
			<!-- 隐藏机构编号 20141114 -->
			<td >用户角色：</td>
			<td  align="left"  >
				<input class="easyui-combobox"  name="roleid" id="roleid" style="width: 140px;" />
			</td>
			<td align="left">用户名称：</td>
			<td   align="left">
				<input type="text" name="userNameAndId" id="userNameAndId" style="width: 140px;" />
			</td>
		 
		 
			 
			<td>
				<a href="#" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="userView()">查询</a>
			</td>
		</tr>
	</table>
	<table id="userList"></table>
	
	
	<div id="dlg" class="easyui-window" title="用户更新" closed=true cache ="false" collapsible="false" zIndex="20px"  
       minimizable="false" maximizable="false" resizable="false" draggable="true"  closable="false"
       style="width:330px;height:150px;top:100px;padding:0px;background: #fafafa; overflow: hidden;">
	    <div class="easyui-layout" fit="true">	    
		<div region="center" border="true" style="padding:10px;background:#fff;overflow: hidden;">
		      <input type="hidden" id="userid" />
		      <input type="hidden" id="roleids" />
	    		<table>
	    			<tr>	    				
	    				 
	    				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;登录名称：</td>
	    				<td>
	    					<input type="text" name="loginName" id="loginName" maxlength="100" style="width: 155px" readOnly />
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>用户角色</td>
		    				<td  align="left"  >
								<input class="easyui-combobox"  name="roleidsel" id="roleidsel" style="width: 140px;" />
							</td>
	    				 
	    			</tr>
	    		 
	    			<tr style="height: 50px">
	    				<td align="center" colspan="4">
	    				    <a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveUser()">保存</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    					<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
	    				</td>
	    			</tr>
	    		</table>
	     	</div>
	    	</div>
	    </div>
</body>
</html>
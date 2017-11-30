<%@page import="com.compass.utils.ConstantUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>云停风驰管理系统</title>
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
	
	function check(){
	
		if(roleid!="1"){
			return true;
		}
		return false;
	}
	
	
	function addUsers() {
	 
		agencyId='';
	//	loginNamePrefix = companyBriefName + '_';
		$('#userId').val('');
		$('#userName').val('');
		//$('#loginNamePrefix').val(loginNamePrefix);
		$('#loginName').val('');
		$('#password').val('');
		$('#email').val('');
		//$('#phone').val('');
		$('#address').val('');
		$("#status").combobox('select', 1);
		$.showDivShade('${ctx}');
		$('#dlg').dialog('open').dialog('setTitle', '新增用户信息');	
		$("#status").combobox('select', 1);
		url = '${ctx}/userrole/userrole.do?method=add';
	}

	//修改
	function editUser(obj) {
			
		$.showDivShade('${ctx}');
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
		var parentagencyId=	row.parentagencyId;
		
		// 非本机构用户不可修改 20141111
		
			if(parentagencyId==thisAgencyId&&sysId==row.systemsource){
				
			}else if (row.agencyid != thisAgencyId) {
					$.hideDivShade();
					$.messager.alert("提示 ", "非本机构用户不可修改");
					return;
				}
		// 原始登录名拆开显示 20141102
		var logname = row.loginName;
		$('#userId').val(row.userId);
		$('#userName').val(row.userName);
		$('#loginName').val(row.loginName);
		$('#password').val('');
		$('#email').val(row.email);
		//$('#phone').val(row.phone);
		$('#address').val(row.address);
		agencyId=row.agencyId;
		$("#status").combobox('select', row.status);
		url = "${ctx}/userrole/userrole.do?method=update";
		$('#dlg').dialog('open').dialog('setTitle', '修改用户信息');
		
	}
	//添加
	function saveUser() {
		var userId = $('#userId').val();
		var userName = $('#userName').val();
		var loginName = $('#loginName').val();
		var password = $('#password').val();
		var email = $('#email').val();
		var phone = $('#phone').val();
		var status = $("#status").combobox("getValue");
		//var roleId=$('#roleId').combobox('getValue');

		if ($.trim(userName) == "") {
			$.messager.alert("提示 ", "请输入用户名称");
			return false;
		}
		if ($.trim(loginName) == "") {
			$.messager.alert("提示 ", "请输入登录名称");
			return false;
		}
		if ($.trim(password) == "") {
			$.messager.alert("提示 ", "请输入用户密码");
			return false;
		}
		if ($.trim(email) == "") {
			$.messager.alert("提示 ", "请输入用户邮箱");
			return false;
		}
		/* if (!(/^1[3578][01379]\d{8}|1[34578][01256]\d{8}|134[012345678]\d{7}|1[34578][012356789]\d{8}$/g).test($.trim(phone))) {
			$.messager.alert("提示 ", "请输入正确的手机号码");
			return false;
		} */

		if ($.trim(status) == "") {
			$.messager.alert("提示 ", "请选择用户状态");
			return false;
		}
		if (!(/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
				.test($.trim(email))) {
			$.messager.alert("提示 ", "邮箱格式不正确");
			return false;
		}
		if(checkChinese(userName)){
			$.messager.alert("提示 ", "用户名称不能为中文");
			return false;
		}
		if(checkChinese(loginName)){
			$.messager.alert("提示 ", "登录名称不能为中文");
			return false;
		}
		if(checkChinese(password)){
			$.messager.alert("提示 ", "用户密码不能为中文");
			return false;
		}
		/* if(checkChinese(phone)){
			$.messager.alert("提示 ", "电话号码不能为中文");
			return false;
		} */
		password=hex_md5(password);
		$('#save').linkbutton('disable');
		$.post(url, {
			userId : userId,
			userName : userName,
			loginName : loginName,
			password : password,
			email : email,
			phone : phone,
			status : status,
			agencyId:agencyId

		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
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
	//删除
	function destroyEmail() {
		var userid='<%=session.getAttribute(ConstantUtils.USERID)%>';
		var row = $('#userList').datagrid('getSelections');
		var userIds = "";

		if (row.length < 1) {
			$.messager.alert("提示 ", "请先选中要删除的数据");
		} else {
			$.messager.confirm('提示', '您确定要删除该数据？', function(r) {
				if (r) {
					for ( var i = 0; i < row.length; i++) {
						var status = row[i].status;
						userIds = userIds + "'" + row[i].userId + "',";
						if (status == '0') {
							$.messager.alert("提示 ", "对不起!该角色已无效,不能删除!");
							return;
						}
						if($.trim(row[i].userId)==$.trim(userid)){
							$.messager.alert("提示 ", "当前登录的用户不能删除");
							return;
						}
						// 非本机构用户不可删除 20141111
						if ($.trim(row[i].agencyid) != thisAgencyId) {
							$.messager.alert("提示 ", "非本机构用户不可删除");
							return;
						}
					}
					$.post('${ctx}/userrole/userrole.do?method=delete', {
						userIds : userIds
					}, function(data) {
						$.parseAjaxReturnInfo(data, $.success, $.failed);
					}, "json");
				}
			});
		}
	}

	function resetPwd(){
		$.showDivShade('${ctx}');
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
		var userId = row.userId;
		var userPwd = row.loginName;
		userPwd=hex_md5(userPwd);
		$.messager.confirm('提示', '您确定要重置？', function(r) {
			if(r){
				$.post('${ctx}/userrole/userrole.do?method=resetPwd', 
		            	{
							userId : userId,
							userPwd : userPwd
						}, 
						function(data) {
						$.parseAjaxReturnInfo(data, $.success, $.failed);
					}, "json");
			}
			$.hideDivShade();
		});
		//$('#dlg').dialog('open').dialog('setTitle', '修改用户信息');
	}
	function clearPhone(){
		$.showDivShade('${ctx}');
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
		var userId = row.userId;
		$.messager.confirm('提示', '您确定要清空？', function(r) {
			if(r){
				$.post('${ctx}/userrole/userrole.do?method=clearPhone', 
		            	{
							userId : userId
						}, 
						function(data) {
						$.parseAjaxReturnInfo(data, $.success, $.failed);
					}, "json");
			}
			$.hideDivShade();
		});
		//$('#dlg').dialog('open').dialog('setTitle', '修改用户信息');
	}
	
	function userView() {
		var status = $("#statusStr").combobox('getValue');
		var userName = $("#userNameAndId").val();
		var  agencyid=$("#agencyid").val();
		var selAgencyId=$('#agencyheadId').combogrid("getValue");
		var  datestart = $('#yearmonthdatestart').val();
		var  dateend = $('#yearmonthdateend').val();
		$('#userList').datagrid(
						{
							title : '用户管理',
							url : '${ctx}/userrole/userrole.do?method=inquire',
							queryParams : {								
								status : status,
								userName : userName,
								agencyid:agencyid,
								selAgencyId:selAgencyId,
								datestart:datestart,
								dateend:dateend
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
									{field : "userName",width : $(window).width()*0.1,title : "用户名称",align : "center"},
									{field : "loginName",width : $(window).width()*0.1,title : "登录名称",align : "center"},
									{field : "roleId",width : $(window).width()*0.3,title : "用户类型",align : "center"},
									{field : "email",width : $(window).width()*0.2,title : "邮箱",align : "center"},
									{field : "phone",width : $(window).width()*0.15,title : "电话",align : "center"},
									{field : "agencyName",width : $(window).width()*0.3,title : "所属机构",align : "center"},
									{field : "systemsourceName",width : $(window).width()*0.1,title : "来源系统",align : "center",hidden:check()},
									{field : "statusStr",width : $(window).width()*0.1,title : "状态",align : "center"},
									{field : "createId",width : $(window).width()*0.1,title : "操作人 ",align : "center",hidden : false},
									{field : "comments",width : $(window).width()*0.1,title : "备注 ",align : "center",hidden : true},
									{field : "createDt",width : $(window).width()*0.15,title : "操作时间 ",align : "center",hidden : false},
									{field : "parentagencyId",width : $(window).width()*0.15,title : "上线机构 信息 ",align : "center",hidden : true},
									{field : "systemsource",width : $(window).width()*0.15,title : "上线机构 信息 ",align : "center",hidden : true},
									{field : "isDtbUser",width : $(window).width()*0.1,title : "是否 ",align : "center",formatter:function(val){
											if(val == "1"){
												return "是";
											}else{
												return "否";
											}													
									}}
							] ],
							hideColumn : [ [ {
								field : "status",
								field : "agencyId"
							} ] ],
							pagination : true,
							rownumbers : true,
							showFooter : true,
							toolbar:[{
								id:'btnadd',
								text:'添加',
								iconCls:'icon-add',
								handler:function(){									
									addUsers();
								}
							},'-',{
								id:'btncut',
								text:'修改',
								iconCls:'icon-edit',
								handler:function(){
									editUser(-2);
									
								}
							},'-',{
								id:'btndelete',
								text:'删除',
								iconCls:'icon-cut',
								handler:function(){
									destroyEmail();
									
								}
							},'-',{
								id:'btnReset',
								text:'密码重置',
								iconCls:'icon-reload',
								handler:function(){
									resetPwd();
									
								}
							}//,'-',{
							//	id:'btndclear',
							//	text:'手机清空',
							//	iconCls:'icon-reload',
							//	handler:function(){
							//		clearPhone();
									
							//	}
							//}
							]
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
		 $('#agencyheadId').combogrid({
             panelWidth:380,
             url : "${ctx}/agency/agency.do?method=getAgencyComb",
 			queryParams : {
 				rows : 40
 			},
             idField:'agency_id',
             textField:'companyName',
             mode:'remote',
             fitColumns:true,
             columns:[[
        	 {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			}
			]]
         });
		
		if(thisAgencyId == centercode){
			$("#agencyStr1").show();
			$("#agencyStr2").show();
		}
		
		userView();
		$("select").combobox({
			editable:false 
		});
		
		
		
		 $('#cg').combogrid({
             panelWidth:380,
             url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
 			queryParams : {
 				rows : 40
 			},
             idField:'agency_id',
             textField:'companyName',
             mode:'remote',
             fitColumns:true,
             columns:[[
         {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			}
			]]
         });
		
		/**
		$('#agencyheadId').combotree({
			  url: '${ctx}/agency/agency.do?method=getAgencyTree',    
	 		  method:'get',
	          onLoadSuccess:function(a,b){
	    	  },
	    	  panelWidth:   350
		});
		**/
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
		
		/* $(".combo-text").bind("input onchange",function(a,b){
			$('#agencyheadId').combotree('tree').tree("search",$(this).val());
			if($(this).val()==""||null==$(this).val()){
				$('#agencyheadId').combotree('tree').tree("collapseAll");
			} 
		}); */
	});
	
</script>
</head>
<body id="indexd">
	<table >
		<tr>
			<td align="left">机&nbsp;&nbsp;&nbsp;&nbsp;构：</td>
			<td  align="left"> <!--  <input class="easyui-combotree" id="agencyheadId"  name="vestagencyId"
							 editable="true"  style="width:140px;" />  -->
						<input id="agencyheadId" name="vestagencyId" style="width:150px"></input>	 
							 &nbsp;&nbsp;&nbsp;
			</td>
			<!-- 隐藏机构编号 20141114 -->
			<td id="agencyStr1" align="left" style="display: none">机构编号：</td>
			<td id="agencyStr2"   align="left" style="display: none">
				<input type="text" name="agencyid" id="agencyid" style="width: 140px;" />
			</td>
			<td align="left">用户名称：</td>
			<td   align="left">
				<input type="text" name="userNameAndId" id="userNameAndId" style="width: 140px;" />
			</td>
		</tr>
		<tr >
			<td   align="left">用户状态：</td>
			<td   align="left">
				<select class="easyui-combobox" data-options="panelHeight:'auto'" 
				id="statusStr" name="statusStr" style="width: 150px;">
					<option value="1" selected="selected">有效</option>
					<option value="0">无效</option>
				</select>
			</td>
			
			<td align="left">注册日期：</td>
			<td align="left" >
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			 -
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
		
				</td>
					<td align="left"> </td>
			<td>
			<a href="#" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="userView()">查询</a>
			</td>
		</tr>
	</table>
	<table id="userList"></table>
	
	
	<div id="dlg" class="easyui-window" title="用户更新" closed=true cache ="false" collapsible="false" zIndex="20px"  
       minimizable="false" maximizable="false" resizable="false" draggable="true"  closable="false"
       style="width:630px;height:230px;top:100px;padding:0px;background: #fafafa; overflow: hidden;">
	    <div class="easyui-layout" fit="true">	    
		<div region="center" border="true" style="padding:10px;background:#fff;overflow: hidden;">
	    		<table>
	    			<tr>	    				
	    				<td>用户名称：</td>
	    				<td>
		    				<input type="text" name="userName" id="userName" maxlength="100" style="width: 155px" onkeyup="value=value.replace(/[\W]/g,'') "/>
   							 <input type="text" name="userId" id="userId" style="display: none;" />
	    				</td>
	    				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;登录名称：</td>
	    				<td>
	    					<input type="text" name="loginName" id="loginName" maxlength="16" style="width: 155px" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>登录密码：</td>
	    				<td>
		    				<input type="text" name="password" id="password" maxlength="20" style="width: 155px" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
	    				</td>	   
	    				<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮箱地址：</td>
	    				<td>
	    					<input type="text" name="email" id="email" maxlength="50"/>   
	    				</td>	 			
	    			</tr>
	    			<tr>
	    				<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;标记状态：</td>
	    				<td>
		    					<select class="easyui-combobox" id="status"
							name="status" data-options="panelHeight:'auto'" style="width: 155px;">
								<option value="1" selected="selected">有效</option>
								<option value="0">无效</option>
						</select>   
	    				</td>	
	    				    				
	    				<td><!-- 电话号码： --></td>
	    				<td>	    				 
	    					<!-- <input type="text" name="phone" id="phone" maxlength="11" style="width: 155px" onkeyup="value=this.value.replace(/\D+/g,'')"
   											 onkeydown="fncKeyStop(event)" onpaste="return false"
    										 oncontextmenu="return false"
    										 /> -->
	    				</td>	   
	    				 			
	    			</tr>
	    			
	    			<tr><td>&nbsp;</td></tr>
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
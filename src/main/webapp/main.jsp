<%@page import="java.util.ArrayList"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
.colors {
	font-size: 12px;
	color: #ffffff;
	font-weight: bold;
	line-height: 25px;
	padding: 2px;
	background-image: -moz-linear-gradient(top, #ffffff, #F2F2F2); /* Firefox */
	background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #ffffff), color-stop(1, #F2F2F2)); /* Saf4+, Chrome */
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#84909c', endColorstr='#c7ccd1', GradientType='0');
} /* IE*/
</style>
<script type="text/javascript" src="${ctx}/commons/js/tab.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>云停风驰管理系统</title>
<script type="text/javascript">
	var flag = "${initpwd}";
	var userPhone = "${userPhone}";
	var pwdRegularity = "${pwdRegularity}";
	var systemLogIpAddress = "${systemLogIpAddress}";
	var operateTime = "${perateTime}";
	var unCheckCount = '';
	var InterValObj; //timer变量，控制时间  
	var count = 60; //间隔函数，1秒执行  
	var curCount;//当前剩余秒数  

	$(function() {

		$("#detailTree").tree({
			onlyLeafCheck : true,
			checkbox : false,
			url : "${ctx}/users/users.do?method=getMenuTree",
			onSelect : function() {

				var row = $("#detailTree").tree('getSelected');
				var url = row.attributes.url;
				if (url != '' && url != null) {
					addTab(row.text, url, '123');
				}

			}
		});

		$
				.post(
						'${ctx}/terminalmanage/terminalmanage.do?method=getNoCheckTerminalBackCount',
						{}, function(data) {
							if (data.success == "true") {
								unCheckCount = data.datas.unCheckCount;
								if (unCheckCount != 0) {
									$("#unCheckCount").text(unCheckCount);
									$('#checkDesc').window('open');
								}
							} else {
								$.messager.alert("提示", data.message);
							}
						}, "json");

		$('#agencyApprove').click(function() {
			$('#sendMessageWin').window({
				title : '激活我的机构',
				width : 365,
				modal : true,
				shadow : true,
				closable : false,
				height : 270,
				resizable : false
			});
			$('#sendMessageWin').window('open');
		});
		$('#btnIgnore').click(function() {
			$('#sendMessageWin').window('close');
		});

		//实名认证窗口
<%Boolean flag = (Boolean) request.getAttribute("flag");
			if (!flag) {%>
	$('#certification').window({
			title : '代理商实名认证',
			width : 365,
			modal : true,
			shadow : true,
			closable : false,
			height : 350,
			resizable : false
		});
		$('#certification').window('open');
<%}%>
	
<%Boolean approve = (Boolean) request.getAttribute("approve");
			//true为已认证通过
			if (!approve) {%>
	$('#sendMessageWin').window({
			title : '激活我的机构',
			width : 365,
			modal : true,
			shadow : true,
			closable : false,
			height : 270,
			resizable : false
		});
		$('#sendMessageWin').window('open');
<%}%>
	//实名认证
		$("#btnCertification")
				.click(
						function() {
							var contactsName = $("#contactsName").val();
							var userpId = $("#userpId").val();
							var companyEmail = $("#someoneEmail").val();
							var companyName = $("#agencyName").val();
							var companyPhone = $("#companyPhone").val();
							if (contactsName == "") {
								$.messager.alert("提示 ", "请输入本人姓名!");
								return false;
							}

							if (userpId == "") {
								$.messager.alert("提示 ", "请输入身份证号!");
								return false;
							}

							if (companyEmail != "") {
								if (!(/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
										.test(companyEmail)) {
									$.messager.alert("提示 ", "邮箱格式不正确");
									return false;
								}
							}
							$
									.post(
											'${ctx}/agency/agency.do?method=updateCertification',
											{
												companyName : companyName,
												contactsName : contactsName,
												userpId : userpId,
												companyEmail : companyEmail,
												companyPhone : companyPhone
											}, function(data) {
												if (data.success == "true") {
													$.messager.alert("提示",
															data.message);
													$('#certification').window(
															'close');
												} else {
													$.messager.alert("提示",
															data.message);
												}
											}, "json");

						});
<%String username = (String) request.getSession().getAttribute(
					"username");
			String agencyId = (String) request.getSession().getAttribute(
					"agencyId");
			if (username.equals(agencyId)) {%>
	$.post("${ctx}/agency/agency.do?method=getAgencyObj", {},
				function(data) {
					getAgencyDate(data);
				}, "json");

		$.post('${ctx}/agency/agency.do?method=getAgencyObject',
				function(data) {
					if (data.success == "true") {
						$('#resert').window('close');
					} else {
						$('#resert').window('open');
					}
				}, "json");
<%}%>
	});

	function getAgencyDate(data) {
		if (data) {
			$("#companyName").val(data.companyName);
			$("#renameCompanyName").val(data.renameCompanyName);
			$("#contactsName").val(data.contactsName);
			$("#companyPhone").val(data.companyPhone);
			$("#companyEmail").val(data.companyEmail);
		}
	}

	//设置登录窗口
	function openPwd() {
		$('#w').window({
			title : '修改密码',
			width : 300,
			modal : true,
			shadow : true,
			closable : false,
			height : 210,
			resizable : false
		});
		/* $('#sendMessage').window({
		    title: '验证码校验',
		    width: 320,
		    modal: true,
		    shadow: true,
		    closable: true,
		    height: 210,
		    resizable:false,
		    onBeforeClose:function(){ 
		    	$.post('${ctx}/users/users.do?method=destroySession',{       
				}, function(data) {
					location.href = '${ctx}/login/login.jsp';	
				}, "json");
		        }
		}); */
		$('#txtNewPass').val('');
		$('#txtRePass').val('');
		$('#txtOldPass').val('');
	}
	//关闭登录窗口
	function closePwd() {
		if ("1" == flag || "2" == flag) {
			$.messager.alert("提示 ", "请修改密码!");
			return;
		}

		$('#w').window('close');
		$('#txtNewPass').val('');
		$('#txtRePass').val('');
		$('#txtOldPass').val('');
	};

	$("#showTime").hide();
	//发送验证码
	function sendMessage() {

		var phone = $("#messageText").val();
		if (phone == '') {
			$.messager.alert("提示 ", "请输入手机号码!");
			return;
		}

		var regMobile = /^1\d{10}$/g;//手机号码有效判断电信|联通|移动|移动
		if (!regMobile.test(phone)) {
			$.messager.alert("提示 ", "请输入有效的手机号码!");
			return;
		}

		$.messager.progress({
			title : '请稍等',
			msg : '发送验证码',
			text : '发送中.......'
		});

		$.post('${ctx}/message/message.do?method=sendMessage', {
			phone : phone
		}, function(data) {
			$.messager.progress('close');
			if (data.success == "true") {
				$("#getCheckCode").hide();
				$("#showTime").show();
				$.messager.alert("提示", data.message);
				curCount = count;
				$("#showTime").val("请在" + curCount + "秒内输入验证码");
				InterValObj = window.setInterval(SetRemainTime, 1000);
			} else {
				$.messager.alert("提示", data.message);
			}
		}, "json");

	}

	//timer处理函数  
	function SetRemainTime() {
		if (curCount == 0) {
			window.clearInterval(InterValObj);//停止计时器  
			$("#getCheckCode").removeAttr("disabled");//启用按钮  
			$("#getCheckCode").show();
			$("#showTime").hide();
			$("#getCheckCode").val("重新发送验证码");
		} else {
			curCount--;
			$("#showTime").val("请在" + curCount + "秒内输入验证码");
		}
	}
	//修改密码
	function serverLogin() {
		var newPwd = $('#txtNewPass').val();
		var rePass = $('#txtRePass').val();
		var oldPwd = $('#txtOldPass').val();
		if (newPwd.length < 6) {
			msgShow('系统提示', '密码长度必须大于6！', 'warning');
			return false;
		}

		var regg = eval(pwdRegularity);
		if (!regg.test($('#txtNewPass').val().trim())) {
			msgShow('系统提示', '您的密码强度较低，请设置长度8~16位并且数字、字母、字符至少包含两种的密码', 'warning');
			$("#txtNewPass").focus();
			return false;
		}

		newPwd = hex_md5(newPwd);
		rePass = hex_md5(rePass);
		oldPwd = hex_md5(oldPwd);

		if (oldPwd == '') {
			msgShow('系统提示', '请输入原始密码！', 'warning');
			return false;
		}

		if (newPwd == '') {
			msgShow('系统提示', '请输入新密码！', 'warning');
			return false;
		}
		if (rePass == '') {
			msgShow('系统提示', '请再一次输入密码！', 'warning');
			return false;
		}

		if (newPwd != rePass) {
			msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
			return false;
		}

		if (newPwd == oldPwd) {
			msgShow('系统提示', '不可以为初始密码！', 'warning');
			return false;
		}
		$.post('${ctx}/users/users.do?method=updateUserPwd', {
			newPwd : newPwd,
			oldPwd : oldPwd
		}, function(data) {
			if (data.success == "true") {
				$.messager.alert("提示", data.message
						+ "!请使用新密码。");
				$('#w').window('close');
			} else {
				$.messager.alert("提示", data.message);
			}

		}, "json");

	}

	$(function() {
		$.post('${ctx}/systemlog/systemlog.do?method=getOpenLoginShowFlag',
				function(data) {
					if (data == "1") {
						$('#hint').window('open');
					} else if (data == "0") {
						$('#hint').window('close');
					}
				}, "json");
		openPwd();
		$('#editpass').click(function() {
			$('#w').window('open');
		});

		$('#btnEp').click(function() {
			serverLogin();
		});

		$('#btnCancel').click(function() {
			closePwd();
		});

		$('#btnCancelhint').click(function() {
			closeHint();
		});

		$('#hint')
				.window(
						{
							onBeforeClose : function() {
								$
										.post(
												'${ctx}/systemlog/systemlog.do?method=getCloseLoginShowFlag',
												function(data) {
													if (data.success == "true") {
													} else {
													}
												}, "json");
							}
						});


		$("#btnMsg").click(function() {
			var phoneVal = $("#messageText").val();
			var checkCode = $("#checkCode").val();
			$.post('${ctx}/message/message.do?method=checkCode', {
				checkCode : checkCode,
				phone : phoneVal
			}, function(data) {
				if (data.success == "true") {
					updateAgencyMobile(phoneVal);
					$('#sendMessageWin').window('close');
				} else {
					$.messager.alert("提示", data.message);
				}
			}, "json");

		});

		$('#loginOut')
				.click(
						function() {
							$.messager
									.confirm(
											'系统提示',
											'您确定要退出本次登录吗?',
											function(r) {
												if (r) {
													$
															.post(
																	'${ctx}/users/users.do?method=destroySession',
																	{},
																	function(
																			data) {
																		location.href = '${ctx}/login/login.jsp';
																	}, "json");
												}
											});
						});
		if ("1" == flag) {
			$("#warnID").text("您的密码的初始密码,请修改修改！");
			$('#w').window('open');
		} else {
			$("#warnID").text("");
			closePwd();
		}

		if ("2" == flag) {
			$("#warnID").text("您的密码超出有效期,请修改！");
			$('#w').window('open');
		} else {
			$("#warnID").text("");
			closePwd();
		}

		$('#btnEpResert').click(function() {
			btnEpResert();
		});

		$('#editAgency').click(
				function() {
					$('#resert').window('open');
					$.post("${ctx}/agency/agency.do?method=getAgencyObj", {},
							function(data) {
								getAgencyDate(data);
							}, "json");
				});

		$('#loginRemind').click(function() {
			$('#hint').window('open');
		});

		//完善机构信息
		$('#agencyUpdate')
				.click(
						function() {
							$('#provinceId2')
									.combobox(
											{
												url : '${ctx}/agency/agency.do?method=getProvince',
												valueField : 'id',
												textField : 'text',
												editable : false,
												onLoadSuccess : function() {
													$("#provinceId2").combobox(
															'select', -1);
													$("#provinceId2")
															.combobox('select',
																	'${agencyBean.provinceId}'); //省
												},
												onSelect : function(row) {
													$('#cityId2')
															.combobox(
																	{
																		url : '${ctx}/agency/agency.do?method=getCity&provinceId='
																				+ row.id,
																		valueField : 'id',
																		textField : 'text',
																		editable : false,
																		onLoadSuccess : function() {
																			var provinceId = $(
																					"#provinceId2")
																					.combobox(
																							'getValue'); //省
																			var cityId = $(
																					"#cityId2")
																					.combobox(
																							'getValue'); //市
																			if ('${agencyBean.provinceId}' == provinceId) {
																				$(
																						"#cityId2")
																						.combobox(
																								'select',
																								'${agencyBean.cityId}'); //市
																			}
																		}
																	});
												}
											});

							$('#sendMessageWin').window({
								title : '完善机构信息',
							});
							$('#markSave').window('open');
						});

		$.AgencySave = function() {
			var contactsName = $("#contactsName2").val(); //联系人
			var companyPhone = $("#companyPhone2").val(); //企业电话
			var companyEmail = $("#companyEmail2").val(); //企业邮箱
			var companyAddr = $("#companyAddr2").val(); //企业地址
			var certificate = $("#certificate2").val(); //企业证书信息
			var legalinfo = $("#legalinfo2").val(); //法人信息
			var organizationCode = $("#organizationCode2").val(); //组织机构代码
			var agreementcode = $("#agreementcode2").val(); //合同编号
			var provinceId = $("#provinceId2").combobox('getValue'); //省
			var cityId = $("#cityId2").combobox('getValue'); //市
			var accountBank = $("#accountBank2").val(); //开户银行
			var bankId = $("#bankId2").val(); //开户银行编码
			var accountName = $("#accountName2").val(); //户名
			var bankcode = $("#bankcode2").val(); //账号
			var someoneName = $("#someoneName2").val();//联系人姓名
			var someonePhone = $("#someonePhone2").val();//联系人电话

			$.post("${ctx}/agency/agency.do?method=modifyAgencyBean", {
				contactsName : contactsName,
				companyPhone : companyPhone,
				companyEmail : companyEmail,
				companyAddr : companyAddr,
				certificate : certificate,
				legalinfo : legalinfo,
				organizationCode : organizationCode,
				agreementcode : agreementcode,
				provinceId : provinceId,
				cityId : cityId,
				accountBank : accountBank,
				bankId : bankId,
				accountName : accountName,
				bankcode : bankcode,
				someoneName : someoneName,
				someonePhone : someonePhone
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}
		$.success = function(message, data) {
			$.messager.alert("提示 ", message);
			$('#save').linkbutton('enable');
			$.close();
		};
		$.failed = function(message, data) {
			$.messager.alert("提示 ", message);
			$('#save').linkbutton('enable');

		};

	});

	$.success = function(message, data) {
		flag = "0";
		closePwd();
	};

	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
	};

	$.checkLength = function(a) {
		alert(a);
		alert(a.length());
	}

	$.close = function() {
		$("#markSave").window('close');
	};

	//设置登录提示窗口
	function openhint() {
		$('#hint').window({
			title : '  ',
			width : 750,
			modal : true,
			shadow : true,
			closable : false,
			height : 120,
			resizable : false
		});
		$('#checkDesc').window({
			title : '  ',
			width : 750,
			modal : true,
			shadow : true,
			closable : false,
			height : 120,
			resizable : false
		});
	}

	//更新手机号码
	function updateAgencyMobile(phoneVal) {
		var textAlert = '登录分润移动端&nbsp;<br/>登录账号：' + phoneVal
				+ '<br/>机构名称：${agencyBean.companyName}&nbsp;<br/>'
				+ '登录密码为您的平台登录密码，是否确认使用以上信息作为分润移动端的登录信息？';
		$.messager.confirm('激活确认', textAlert, function(r) {
			if (r) {
				$.post('${ctx}/agency/agency.do?method=modifyCompanyPhone', {
					phone : phoneVal
				}, function(data) {
					if (data.success == "true") {
						$.messager.alert("提示", data.message);
						window.location.reload();
					} else {
						$.messager.alert("提示", data.message);
					}
				}, "json");
			}
		});
	}

	//设置用户登录修改资料窗口
	function openResert() {
		$('#resert').window({
			title : '修改个人信息',
			width : 750,
			modal : true,
			shadow : true,
			closable : false,
			height : 120,
			resizable : false
		});
	}

	//用户登录修改资料
	function btnEpResert() {
		var contactsName = $("#contactsName").val(); //联系人
		var renameCompanyName = $("#renameCompanyName").val(); //企业名称
		var companyPhone = $("#companyPhone").val(); //公司电话
		var companyEmail = $("#companyEmail").val(); //企业邮箱

		if ($.trim(contactsName) == "") {
			$.messager.alert("提示 ", "请输入联系人");
			return false;
		}
		if ($.trim(renameCompanyName) == "") {
			$.messager.alert("提示 ", "请输入公司名称");
			return false;
		}

		if ($.trim(companyEmail) == "") {
			$.messager.alert("提示 ", "请输入电子邮箱");
			return false;
		}

		if (!(/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
				.test($.trim(companyEmail))) {
			$.messager.alert("提示 ", "邮箱格式不正确");
			return false;
		}
		$.post('${ctx}/agency/agency.do?method=updateAgency', {
			renameCompanyName : renameCompanyName,
			companyPhone : companyPhone,
			companyEmail : companyEmail,
			contactsName : contactsName
		}, function(data) {
			if (data.success == "true") {
				$.messager.alert("提示", data.message);
				$('#resert').window('close');
			} else {
				$.messager.alert("提示", data.message);
			}
		}, "json");

	}
</script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" fit="true" scroll="no">
	<div data-options="region:'north',title:'',split:false" style="overflow: true; height: 65%; background: url('') repeat-x center 50%; line-height: 20px; font-family: Verdana, 微软雅黑, 黑体" class='colors'>
		<table width="100%">
			<tr>
				<td><img height="50px" width="463px" src="${ctx}/commons/images/logo.png" alt="" /></td>
				<td align="right" style="font-size: 12px; font-weight: bold;"><span style="float: right; padding-right: 20px; padding-top: 15px"> 欢迎${username} &nbsp;&nbsp;&nbsp;&nbsp; <a href="#"
						id="loginRemind">登录提示</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="editpass">修改密码</a>&nbsp;&nbsp; <%
     Boolean isLevelOne = (Boolean) request.getAttribute("levelOne");
     //是一级机构
     if (isLevelOne) {
 %> <a href="#" id="agencyUpdate">完善机构信息</a>&nbsp;&nbsp; <%
     }
 %> <%-- <%
				Boolean isWhiteListUser = (Boolean)request.getAttribute("isWhiteListUser");
				//是白名单
				if(isWhiteListUser && !approve){
				%>
					<a href="#" id="agencyApprove">代理商认证</a>&nbsp;&nbsp; 
				<%	    
				}
				%> --%> <a href="#" id="loginOut">安全退出</a></span></td>
			</tr>
		</table>
	</div>

	<div data-options="region:'west',title:'导航菜单',split:true" style="width: 180px;">
		<div id="nav">
			<!--  导航内容 -->
			<ul id="detailTree" name="detailTree" data-options="checkbox:true,animate:true,lines:true"></ul>
		</div>
	</div>
	<div id="mainPanle" data-options="region:'center',title:''" style="padding: 0px; background: #eee; overflow-y: hidden">
		<div id="tabs" class="easyui-tabs" fit="true" border="false"></div>
	</div>

	<!--修改密码窗口-->
	<div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false" maximizable="false" icon="icon-save" style="width: 350px; height: 250px; padding: 5px; background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<span id="warnID"></span>
				<table cellpadding=3>
					<tr>
						<td>原始密码：</td>
						<td><input id="txtOldPass" class="txt01" type="password" maxlength="16" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input id="txtNewPass" class="txt01" type="password" maxlength="16" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" data-errormessage="密码长度最少6位" minlength="6" placeholder="密码长度最少6位" /></td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" class="txt01" type="password" maxlength="16" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" data-errormessage="密码长度最少6位" minlength="6" placeholder="密码长度最少6位" /></td>
					</tr>
				</table>
			</div>
			<div region="south" border="false" style="text-align: center; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);">取消</a>

			</div>
		</div>
	</div>


	<!--登录提示窗口-->
	<div id="hint" class="easyui-window" title="登录提示" closed="true" style="width: 500px; height: 180px; padding: 5px; background: #fafafa;" collapsible="false" minimizable="false" maximizable="false"
		icon="icon-save">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>尊敬的用户，您上次登录的时间为：<a>${perateTime}<a />（IP地址为<a> ${systemLogIpAddress}<a />）</td>
					</tr>
					<tr>
						<td>如上次登录时间与您实际操作时间不符，请警惕平台安全。</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!--终端回拨审核提示窗口-->
	<div id="checkDesc" class="easyui-window" title="审核提示" closed="true" style="width: 400px; height: 150px; padding: 5px; background: #fafafa;" collapsible="false" minimizable="false"
		maximizable="false" icon="icon-save">
		<div class="easyui-layout" fit="true">
			<div region="right" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
<%-- 						<td>尊敬的用户，您有<span id="unCheckCount"></span>个终端回拨请求未审核：<a href="${ctx}/terminalmanage/terminalmanage.do?method=getListTerminalBack">查看<a /></td> --%>
						<td>尊敬的用户，您好！您的上级向您发起了回拨请求，点击：<a href="${ctx}/terminalmanage/terminalmanage.do?method=getListTerminalBack">查看<a /></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<%-- <%
if(username.equals(agencyId)){
%>
   <!--可登陆用户修改资料窗口-->
	<div id="resert" class="easyui-window" closed="true" title="修改代理商信息" collapsible="false" minimizable="false" maximizable="false" icon="icon-save"
		style="width: 350px; height: 350px; padding: 5px; background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<span id="warnID"></span>
				<table cellpadding=3 style="padding-left: 30px; padding-top: 10px;">
					<tr>
						<td>历史公司名称：</td>
						<td><input type="text" name="companyName" id="companyName" maxlength="50"  disabled="disabled"/></td>
					</tr>
					<tr>
						<td>公司名称：</td>
						<td><input type="text" name="renameCompanyName" id="renameCompanyName" maxlength="50" /></td>
					</tr>
					<tr>
						<td>联系人：</td>
						<td><input type="text" id="contactsName" name="contactsName" maxlength="50" /></td>
					</tr>
					<tr>
						<td>手机号码：</td>
						<td><input type="text" name="companyPhone" id="companyPhone" maxlength="11" onkeyup="value=this.value.replace(/\D+/g,'')" /></td>
					</tr>
					<tr>
						<td>电子邮箱：</td>
						<td><input type="text" name="companyEmail" id="companyEmail" maxlength="50" /></td>
					</tr>
				</table>
				<p><font color="red">为不影响您本人的平台正常使用，请于2017年3月15日前完成以上资料的更改，谢谢。</font></p>
			</div>
			<div region="south" border="false" style="text-align: center; height: 30px; line-height: 30px;">
				<a id="btnEpResert" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">修改</a>
			</div>
		</div>
	</div>
   
<%
}
%> --%>

	<!--可登陆用户修改资料窗口-->



	<!-- 发送验证码   -->
	<div id="sendMessageWin" class="easyui-window" title="激活我的机构" collapsible="false" minimizable="false" maximizable="false" icon="icon-save"
		style="width: 380px; height: 250px; padding: 5px; background: #fafafa;" closed="true">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<span id="warnID1">激活机构后即可登录分润移动端，为确保您的账户安全，请确保填写的信息真实有效。</span><br /> <br> 登录手机号码：<input id="messageText" type="text" style="width: 223px;" maxlength="11" placeholder="本人手机号作为移动端登录账号" /> <br />
					<br /> 验证码 ：<input id="checkCode" placeholder="请输入验证码" style="width: 90px;" onkeyup="value=this.value.replace(/\D+/g,'')" type="text" maxlength="6"><input id="showTime"
						style="display: none;" type="button"></input><input id="getCheckCode" type="button" onclick="sendMessage()" value="发送验证码"></input><br /> <br /> <span>验证码将发送到您绑定的手机号码上</span>
			</div>
			<div region="south" border="false" style="text-align: center; height: 30px; line-height: 30px;">
				<a id="btnMsg" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确认</a>
				<c:if test="${isWhiteListUser == 'true'}">
					<a id="btnIgnore" style="display: none;" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">忽略，暂不认证</a>
				</c:if>
			</div>
		</div>
	</div>
	<!-- 实名认证   -->
	<div id="certification" class="easyui-window" title="代理商实名认证" collapsible="false" minimizable="false" maximizable="false" icon="icon-save"
		style="width: 380px; height: 250px; padding: 5px; background: #fafafa;" closed="true">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<span id="warnID1">**为了确保您经营数据的私密性，重置密码的安全性，分润反馈的真实性，请您填写本人真实信息，一经提交，不可修改！</span><br /> <br /> 机构名称：<input id="agencyName" type="text" value="${agencyBean.companyName}" readonly="readonly"
					style="width: 223px;" /><br /> <br /> 手机号码：<input id="companyPhone" type="text" value="${agencyBean.companyPhone}" readonly="readonly" style="width: 223px;" maxlength="11" /><br /> <br />
				法人姓名：<input id="contactsName" placeholder="请输入本人姓名" type="text" style="width: 223px;" /><br /> <br /> 身份证号：<input id="userpId" placeholder="请输入本人身份证号" type="text" style="width: 223px;"
					maxlength="18" /><br /> <br /> 联系邮箱：<input id="someoneEmail" value="${agencyBean.companyEmail}" placeholder="请输入本人联系邮箱(选填)" type="text" style="width: 223px;" /><br /> <br />
			</div>
			<div region="south" border="false" style="text-align: center; height: 30px; line-height: 30px;">
				<a id="btnCertification" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确认提交</a>

			</div>
		</div>
	</div>



	<!-- 完善机构信息 -->
	<div id="markSave" class="easyui-window" title="完善机构信息" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false" maximizable="false" resizable="false" draggable="true"
		closable="false" style="width: 730px; height: 455px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="optFlag" name="optFlag" />
				<table style="width: 100%;">

					<tr id="someoneNameTr">
						<td align="right">联系人姓名：</td>
						<td align="left"><input type="text" name="someoneName2" id="someoneName2" maxlength="50" value="${ agencyBean.someoneName}" /></td>
						<td align="right">联系人电话：</td>
						<td align="left"><input type="text" name="someonePhone2" id="someonePhone2" maxlength="11" value="${ agencyBean.someonePhone}" onkeyup="value=this.value.replace(/\D+/g,'')" /></td>
					</tr>

					<tr id="provinceTr">
						<td align="right"><span style="color: red"></span>省：</td>
						<td align="left"><input class="easyui-combobox" id="provinceId2" name="provinceId2" style="width: 155px;" /></td>
						<td align="right"><span style="color: red"></span>市：</td>
						<td align="left"><input class="easyui-combobox" id="cityId2" name="cityId2" style="width: 155px;" editable="false" /></td>

					</tr>

					<tr id="companyAddrTr">
						<td align="right"><span style="color: red"></span>企业地址</td>
						<td align="left"><input type="text" name="companyAddr2" id="companyAddr2" maxlength="50" value="${ agencyBean.companyAddr}" /></td>

						<td align="right"></td>
						<td align="left"></td>
					</tr>

					<tr id="companyEmailTr">
						<td align="right"><span style="color: red"></span>企业邮箱：</td>
						<td align="left"><input type="text" name="companyEmail2" id="companyEmail2" maxlength="50" value="${ agencyBean.companyEmail}" /></td>

						<td align="right"><span style="color: red"></span>企业证书信息：</td>
						<td align="left"><input type="text" name="certificate2" id="certificate2" maxlength="50" value="${ agencyBean.certificate}" /></td>
					</tr>

					<tr id="legalinfoTr">
						<td align="right"><span style="color: red"></span>法人信息：</td>
						<td align="left"><input type="text" name="legalinfo2" id="legalinfo2" maxlength="50" value="${ agencyBean.legal_info}" /></td>
						<td align="right"><span style="color: red"></span>组织机构代码：</td>
						<td align="left"><input type="text" name="organizationCode2" id="organizationCode2" maxlength="50" value="${ agencyBean.organizationCode}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
					</tr>

					<tr id="agreementcodeTr">

						<td align="right"><span style="color: red"></span>合同编号：</td>
						<td align="left"><input type="text" name="agreementcode2" id="agreementcode2" value="${ agencyBean.agreementcode}" maxlength="50" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
						<td align="right"><span style="color: red"></span>开户银行：</td>
						<td align="left"><input type="text" name="accountBank2" id="accountBank2" maxlength="50" value="${ agencyBean.accountBank}" /></td>
					</tr>

					<tr id="accountNameTr">

						<td align="right"><span style="color: red"></span>户名：</td>
						<td align="left"><input type="text" name="accountName2" id="accountName2" maxlength="50" value="${ agencyBean.accountName}" /></td>

						<td align="right"><span style="color: red"></span>账号：</td>
						<td align="left"><input type="text" name="bankcode2" id="bankcode2" maxlength="50" value="${ agencyBean.bankcode}" onkeyup="value=this.value.replace(/\D+/g,'')" /></td>

					</tr>
					<tr id="agencystatusTr">
						<td align="right"><span style="color: red"></span>开户银行编码：</td>
						<td align="left"><input type="text" name="bankId2" id="bankId2" maxlength="50" value="${ agencyBean.bankId}" /></td>


						<td align="right"></td>
						<td></td>
					</tr>



					<tr height="54px">
						<td align="center" colspan="6"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.AgencySave()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close" id="close" href="#" class="easyui-linkbutton"
							data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>




	<div align="center" data-options="region:'south',title:'',split:true" style="overflow: true; height: 30%; line-height: 20px; font-family: Verdana, 微软雅黑, 黑体" class='colors'>
		<img align="middle" src="${ctx}/commons/images/bottom.png" alt="" />
	</div>

	<div id="mm" class="easyui-menu" style="width: 150px;">
		<div id="tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="close">关闭</div>
		<div id="closeall">全部关闭</div>
		<div id="closeother">除此之外全部关闭</div>
	</div>

</body>
</html>

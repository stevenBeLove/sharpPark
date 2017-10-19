<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>瑞通宝综合管理系统</title>
   <style type="text/css">  
        .code  
        {  
            background-image:url(${ctx}/commons/images/code.gif);  
            no-repeat;
            font-family:Arial;  
            font-style:italic;  
            position:absolute;
            color:Red;  
            border:0;  
            padding:2px 3px;  
            letter-spacing:3px;  
            font-weight:bolder;  
        }  
        .unchanged  
        {  
            border:0;  
        }  
    </style>  
<script type="text/javascript">
		//判断是否为中文
		function checkChinese(str){
			var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
			return reg.test(str);
		}
		
		var code =""; //在全局 定义验证码  
		function createCode()  
		{   
		  code = "";  
		  var codeLength = 4;//验证码的长度  
		  var checkCode = document.getElementById("checkCode");  
		  var selectChar = new Array(1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z');//所有候选组成验证码的字符，当然也可以用中文的  
		     
		  for(var i=0;i<codeLength;i++)  
		  {  
		  var charIndex = Math.floor(Math.random()*33);  
		 	 code += selectChar[charIndex];  
		  }  
		  if(checkCode)  
		  {  
		    checkCode.className="code";  
		    checkCode.value = code;  
		  }  
		    
		}
	$(document).ready(function() {
		createCode();
		if (top.location != location) {
			top.location = location;
		}


	});
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			$.login();
		}
	});

	$.login = function() {
		var loginname = $("#loginname").val();
		var password = $("#password").val();
		var validateCode = $("#validateCode").val();
		password=hex_md5(password);
		var agencyId = $("#agencyId").val();
		var systemId=$("#systemId").combobox('getValue');
		if (agencyId == null || $.trim(agencyId) == "") {
			$.messager.alert("提示", "请输入机构编码");
			return;
		}
		if (loginname == null || $.trim(loginname) == "") {
			$.messager.alert("提示", "请输入用户名");
			return;
		}
		
		if (password == null || $.trim(password) == "") {
			$.messager.alert("提示", "请输入密码");
			return;
		}
		
		if(validateCode.length <=0){
			$.messager.alert("提示", "请输入验证码");
			return;
		} else if(validateCode.toUpperCase()!= code )  {  
			$.messager.alert("提示", "验证码输入错误！");  
	          createCode();//刷新验证码  
	          return;
	      }  
		
		if(checkChinese(agencyId)){
			$.messager.alert("提示 ", "机构编码输入错误，请重新输入");
			return false;
		}
		if(checkChinese(loginname)){
			$.messager.alert("提示 ", "用户名输入错误，请重新输入");
			return false;
		}
		if(checkChinese(password)){
			$.messager.alert("提示 ", "密码输入错误，请重新输入");
			return false;
		}
		
		$.post("${ctx}/users/users.do?method=checkLogin", {
			loginname : loginname,
			password : password,
			agencyId : agencyId,
			systemId:systemId
		}, function(data) {
			if (data.success == "true") {
				location.href = "${ctx}/main.jsp";
			} else {
				$.messager.alert("提示", data.message);
				$("#loginname").val("");
				$("#password").val("");
			}
		}, "json");
	};
	
	
</script>
</head>
<body style="overflow: hidden;">

	<DIV id=UpdatePanel1>
		<DIV id=div1
			style="LEFT: 0px; POSITION: absolute; TOP: 0px; BACKGROUND-COLOR: #006600"></DIV>
		<DIV id=div2
			style="LEFT: 0px; POSITION: absolute; TOP: 0px; BACKGROUND-COLOR: #006600"></DIV>

		<DIV style="BACKGROUND-COLOR: #fafafa">&nbsp;&nbsp;</DIV>
		<DIV>
			<TABLE cellSpacing=0 cellPadding=0 width=900 align=center border=0>
				<TBODY>
					<TR>
						<TD style="HEIGHT: 105px"><IMG
							src="${ctx}/commons/images/login/login_1.png" border=0>
						</TD>
					</TR>
					<TR>
						<TD background="${ctx}/commons/images/login/login_2.jpg"
							height=300>
							<TABLE height=300 cellPadding=0 width=900 border=0>
								<TBODY>
									<TR>
										<TD colSpan=2 height=35></TD>
									</TR>
									<TR>
										<TD width=360></TD>
										<TD>
											<TABLE cellSpacing=0 cellPadding=2 border=0>
												<TBODY>
												<TR>
														<TD style="HEIGHT: 28px">来源系统：</TD>
														<TD style="HEIGHT: 28px">
															 <select id="systemId" class="easyui-combobox" name="systemId"   style="width: 130px;" editable="false">
															 <option  value="QTCX"> 创新收单</option>
															 
															 </select>
														</TD>
														
													</TR>
													<TR>
														<TD style="HEIGHT: 28px">机构编号：</TD>
														<TD style="HEIGHT: 28px"><input id="agencyId"
															name="agencyId" type="text" style="WIDTH: 130px" maxlength="8"/>
														</TD>
														<TD style="HEIGHT: 28px"><SPAN
															id=RequiredFieldValidator5
															style="FONT-WEIGHT: bold; VISIBILITY: hidden; COLOR: white">请输入机构编号</SPAN>&nbsp;</TD>
													</TR>
													<TR>
														<TD style="HEIGHT: 28px" width=80>用 户 名：</TD>
														<TD style="HEIGHT: 28px" width=150><INPUT
															id="loginname" name="loginname" style="WIDTH: 130px" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')">
														</TD>
														<TD style="HEIGHT: 28px" width=370><SPAN
															id=RequiredFieldValidator3
															style="FONT-WEIGHT: bold; VISIBILITY: hidden; COLOR: white">请输入用户名</SPAN>
														</TD>
													</TR>
													<TR>
														<TD style="HEIGHT: 28px">登录密码：</TD>
														<TD style="HEIGHT: 28px"><INPUT type="password"
															 id="password" name="password" style="WIDTH: 130px" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')">
														</TD>
														<TD style="HEIGHT: 28px"><SPAN
															id=RequiredFieldValidator4
															style="FONT-WEIGHT: bold; VISIBILITY: hidden; COLOR: white">请输入登录密码</SPAN>
														</TD>
													</TR>
														<TR>
														<TD style="HEIGHT: 28px">验证码：</TD>
														<TD style="HEIGHT: 28px"><input  type="text"   id="validateCode" style="WIDTH: 50px" />  
   														   <input type="button" onclick="createCode()" readonly="readonly" id="checkCode" class="unchanged" style="width: 80px"  />
														</TD>
														<TD style="HEIGHT: 28px">
														</TD>
													</TR>
													<TR>
														<TD></TD>
														<TD><INPUT id=btn
															style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
															onclick='$.login()' type=image
															src="${ctx}/commons/images/login/login_button.gif"
															name=btn></TD>
													</TR>
												</TBODY>
											</TABLE>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD><IMG src="${ctx}/commons/images/login/login_3.jpg"
							border=0>
						</TD>
					</TR>
					<TR height="100px" align="center">
						<TD><IMG src="${ctx}/commons/images/copyright.png"
							border=0>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</DIV>
	</DIV>
</body>
</html>
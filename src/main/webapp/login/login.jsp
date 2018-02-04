<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>云停风驰管理系统</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

.code {
	font-family: Arial;
	font-style: italic;
	position: absolute;
	background-repeat: no-repeat;
	color: Red;
	border: 0;
	letter-spacing: 5px;
	font-weight: bolder;
}

.unchanged {
	border: 0;
}

.STYLE2 {
	font-family: "微软雅黑"
}

#Layer1 {
	position: absolute;
	width: 1094px;
	height: 61px;
	z-index: 1;
}

#checkCode {
 	font-size: 20px;  
  	color: #666;  
}
</style>
<script type="text/javascript">
    //判断是否为中文
    function checkChinese(str) {
        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        return reg.test(str);
    }

    var code = ""; //在全局 定义验证码  
    function createCode() {
        code = "";
        var codeLength = 4;//验证码的长度  
        var checkCode = document.getElementById("checkCode");
        var selectChar = new Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');//所有候选组成验证码的字符，当然也可以用中文的  

        for (var i = 0; i < codeLength; i++) {
            var charIndex = Math.floor(Math.random() * 33);
            code += selectChar[charIndex];
        }
        if (checkCode) {
            checkCode.className = "code";
            checkCode.value = code;
        }

    }
    $(document).ready(function() {
        createCode();
        if (top.location != location) {
            top.location = location;
        }

        /* $('#systemId').combobox({
        	url:"${ctx}/system/system.do?method=getCombSystems",
        	valueField:"id",
        textField:"text"
        	});
         */
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
        password = hex_md5(password);

        /*
        var agencyId = $("#agencyId").val();
        var systemId=$("#systemId").val();
        if (agencyId == null || $.trim(agencyId) == "") {
        	$.messager.alert("提示", "请输入机构编码");
        	return;
        }
         */

        if (loginname == null || $.trim(loginname) == "") {
            $.messager.alert("提示", "请输入用户名");
            return;
        }

        if (password == null || $.trim(password) == "") {
            $.messager.alert("提示", "请输入密码");
            return;
        }

        if (validateCode.length <= 0) {
            $.messager.alert("提示", "请输入验证码");
            return;
        } else if (validateCode.toUpperCase() != code) {
            $.messager.alert("提示", "验证码输入错误！");
            createCode();//刷新验证码  
            return;
        }

        /*
        if(checkChinese(agencyId)){
        	$.messager.alert("提示 ", "机构编码输入错误，请重新输入");
        	return false;
        }
         */

        if (checkChinese(loginname)) {
            $.messager.alert("提示 ", "用户名输入错误，请重新输入");
            return false;
        }
        if (checkChinese(password)) {
            $.messager.alert("提示 ", "密码输入错误，请重新输入");
            return false;
        }

        /*
        if(null==systemId&&systemId==""){
        	systemId=-1;
        }
         */

        $.messager.progress({
            title : '请稍等',
            msg : '登录',
            text : '验证中.......'
        });

        $.post("${ctx}/users/users.do?method=checkLogin", {
            loginname : loginname,
            password : password
        //	agencyId : agencyId,
        //	systemId:systemId
        }, function(data) {
            $.messager.progress('close');
            if (data.success == "true") {
                location.href = "${ctx}/main.do";
            } else {
                $.messager.alert("提示", data.message);
                $("#loginname").val("");
                $("#password").val("");
            }
        }, "json");
    };
</script>
</head>
<body style="line-height: 0; color: black; background: white; margin: auto;">
	<div style="MARGIN-RIGHT: auto; MARGIN-LEFT: auto; margin-top: 0px; width: 100%; min-width: 1200px;">
		<div style="width: 1200px; margin: 0 auto; overflow: hidden;">
			<div style="float: left; padding: 18px 0 16px;">
				<img src="${ctx}/commons/images/majorization/profit-management-system.png" style="height: 44px;">
			</div>
			<p style="float: left; font-size: 20px; color: #333; margin-left: 42px; height: 80px; line-height: 80px;">欢迎登录</p>
		</div>

		<div style="width:100%;min-width:1200px;height:684px;background:url(${ctx}/commons/images/majorization/JX-24.png)center top 100%;background-repeat: no-repeat;">
			<div style="width: 1200px; margin: 0 auto; overflow: hidden; position: relative;">
				<div style="float: left; width: 1200px; height: 684px; position: relative;">
					<img src="${ctx}/commons/images/majorization/Z-7.png" style="position: absolute; top: 95px; left: -77px;">
					<p style="position: absolute; top: 135px; left: 410px; font-size: 18px; color: #F0F0F0;"></p>
					<p style="position: absolute; top: 165px; left: 410px; font-size: 18px; color: #F0F0F0;"></p>
					<p style="position: absolute; top: 195px; left: 410px; font-size: 18px; color: #F0F0F0;"></p>
					<p style="position: absolute; top: 435px; left: 80px; font-size: 20px; color: #f5ff52;"></p>
				</div>

				
			<div style="float:right;position:absolute;top:103px;right:80px;height:417px;width: 344px;background: #FFF">
				<div class="loginWrap">
				<div class="changeLogin" style="position:absolute;top:0px;right:0px;" width="310px" height="230px">
				</div>
				<table width="344px" height="360" border="0 cellspacing="0" style="position:absolute;top:60px;left:0px;">
					<tr height="40px"">
						<td colspan="2">
						  <p style="font-size:16px;position:absolute;top:0px;left:30px;">账户登录</p>
						 <p style="border-top: 1px solid #E6E6E6;width: 220px;position:absolute;top:10px;left:100px;"></p>
						</td>
					</tr>
					<tr height="40px">
					    <TD align="center" valign="middle" background="#BFDFFF">
				          <div id="loginname1" style="position: absolute;left:30px;top:50px;width:285px;height:40px;border:1px solid #dfdfdf;">
					    		<img style="position: absolute; left: 10px; top: 10px;border-right:1px solid #dfdfdf;padding-right: 8px;" width="20px"  height="20px"  src="${ctx}/commons/images/majorization/mty-user-icon02.png">
					    		<input id="loginname"  style="position: absolute;left:40px;top:0px;width:245px;height:40px;outline:none;border:none;font-size:14px;color:#000000;" 
									type="loginname" placeholder="  账户"  autocomplete="off" name="loginname"  onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')"></input>
				    	    </div>
				    	    <img style="position: absolute; left: 30px; top: 100px;" src="${ctx}/commons/images/majorization/!.png">
									<p style="position: absolute; left: 55px; top: 100px;">
										<font size='1' style="color:#ff6d00; font-size: 13px;"></font>
									</p>
				       </TD>
					</tr>
					<TR height="15px">
					    <TD align="center" background="#BFDFFF" >
				    		<div id="password1" style="position: absolute;left:30px;top:145px;width:285px;height:40px;border:1px solid #dfdfdf;">
					    		<img style="position: absolute; left: 10px; top: 10px;border-right:1px solid #dfdfdf;padding-right: 8px;" width="20px"  height="20px"  src="${ctx}/commons/images/majorization/mty-password-icon.png">
					    		<input id="password"  style="position: absolute;left:40px;top:0px;outline:none;width:245px;height:40px;border:none;font-size:14px;color:#000000;" 
									type="password" placeholder="  密码" name="password"  autocomplete="off"  onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')"></input>
				    	    </div>
				    	</TD>
					</TR>
					<TR height="19px">
						<TD background="#BFDFFF">
								<div id="yzm_div" style="position:relative;top:115px;left:30px;width:238px;height:40px;border:1px solid #dfdfdf;">
									<input id="validateCode" style="position: absolute;float:left;width:135px;height:100%;font-size:14px;color:#000000;border:none;outline:none;" placeholder="  验证码"  autocomplete="off"></input>
									<input type="button"  readonly="readonly"  id="checkCode" style="position:absolute;top:0px;left:135px;width:103px;height:40px;" onClick="createCode()"></input>
									<img style="position: absolute;left:250px;top:8px;" src="${ctx}/commons/images/majorization/SX.png" onClick="createCode()">
								</div>
				       </TD>
				      
				       
					</TR>
					<tr>
						<td colspan="2"  align="center" >
						<input id="submit" style="position:absolute;top:290px;left:30px;
						color:white;
						font-size:16px;
						width:285px;
						height:40px;
						border:1px solid #FF6600;
						border-radius:5px;
						background: #ff6d42;
						cursor:pointer;" type="button" value="登录" onClick="$.login()">
					    </td>
				    </tr>
				</table>
				
		</div>
			
		</div>

			</div>
		</div>
		<TR>
			<TD align="center">
				<div style="line-height: 20px; font-size: 50px;margin-top:-20px;">
					<p align="center" style="color: #666666;">
						<b style="color: #333333;"></b>
					</p>
					<p align="center" style="color: #666666;"></p>
				</div>
			</TD>
		</TR>
		<!-- <p style="border-top: 1px solid #E6E6E6;margin:25 auto 0;width: 1000px;"></p> -->
		<TD align="center" valign="middle">
			<div align="center" style="margin-top: 0px;margin-bottom: 26px;">
				<p style="font-size: 14px;color: #777777;">Copyright 云停风驰 版权所有 沪ICP备17047021号-1</p>
			</div>
		</TD>
	</div>
	</div>

	<script type="text/javascript">
        $('.changeLogin').click(function() {
            $('.loginWrap').css('display', 'none');
            $('.download').css('display', 'block');
        })
        $('.changeLogin2').click(function() {
            $('.loginWrap').css('display', 'block');
            $('.download').css('display', 'none');
        })
        $('.changeLogin3').click(function() {
            $('.loginWrap').css('display', 'block');
            $('.download').css('display', 'none');
        })
        $("#password").click(function() {
            $("#password1").css("border", "1px solid rgb(255, 140, 60)");
        });
        $("#password").mouseout(function() {
            $("#password1").css("border", "1px solid #dfdfdf");
        });
        $("#loginname").click(function() {
            $("#loginname1").css("border", "1px solid rgb(255, 140, 60)");
        });
        $("#loginname").mouseout(function() {
            $("#loginname1").css("border", "1px solid #dfdfdf");
        });
        $("#validateCode").click(function() {
            $("#yzm_div").css("border", "1px solid rgb(255, 140, 60)");
        });
        $("#validateCode").mouseout(function() {
            $("#yzm_div").css("border", "1px solid #dfdfdf");
        });
    </script>
	</script>
</body>
</html>

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


	//判断是否为中文
	function checkChinese(str) {
		var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
		return reg.test(str);
	}

	function cs() {
		return $(window).width() - 8;
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
		
		$('#systemId').combobox({
			url : "${ctx}/system/system.do?method=getCombSystems",
			valueField : "id",
			textField : "text"
		});
		$('#provinceIds')
				.combobox(
						{
							url : '${ctx}/agency/agency.do?method=getProvince',
							valueField : 'id',
							textField : 'text',
							editable : false,
							onLoadSuccess : function() {
								$("#provinceIds").combobox('select', -1);
								if (len == 1 && flag != "-1") {
									$("#provinceIds").combobox('select',
											rows[0].provinceId); //省
								}
							},
							onSelect : function(row) {
								$('#cityIds')
										.combobox(
												{
													url : '${ctx}/agency/agency.do?method=getCity&provinceId='
															+ row.id,
													valueField : 'id',
													textField : 'text',
													editable : false,
													onLoadSuccess : function() {
														var provinceId = $(
																"#provinceIds")
																.combobox(
																		'getValue'); //省
														var cityId = $(
																"#cityIds")
																.combobox(
																		'getValue'); //市
														if (len == 1
																&& flag != "-1"
																&& rows[0].provinceId == provinceId) {
															$("#cityIds")
																	.combobox(
																			'select',
																			rows[0].cityId); //市
														}
													}
												});
							}
						});
		$.viewAgency();

	});

	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}

	function onlineFlag() {
		if ("0" == onlineControl) {
			return true;
		}
		return false;
	}

	$.openWin = function(obj) {
		
		
		var rows = $('#viewAgency').datagrid('getSelections');
		var len = rows.length;
		$("#agencyid").removeAttr("disabled");
		$("#agencyid").removeAttr("readonly");
		$("#agencyname").removeAttr("readonly");
		$("#agencyname").removeAttr("disabled");
		$("#companyPhone").removeAttr("disabled");
		$("#contactsName").removeAttr("disabled");
		$("#userpId").removeAttr("disabled");
		//$("#agencybriefname").removeAttr("disabled");
		$('#markSave').window({
			title : '添加机构'
		});
		var flag = obj;
		$("#optFlag").val(flag);
		$("#agencyid").val(''); 
		//机构编码
		if (agid == staticagecyId) {
			$("#systr").show();
			//顶级机构登录
			$("#contactsNameTr").show();
			$("#companyPhoneTr").show();
			$("#someoneNameTr").show();
			$("#provinceTr").show();
			$("#companyAddrTr").show();
			$("#companyEmailTr").show();
			$("#legalinfoTr").show();
			$("#agreementcodeTr").show();
			$("#accountNameTr").show();
			$("#agencystatusTr").show();
			$("#dtbProfitFlag1Tr").show();
			$("#agencyTypeDis").show();
			
		} else {
			$("#agencyid").val(agid);
			$("#someoneNameTr").show();
			$("#provinceTr").show();
			$("#companyAddrTr").show();
			$("#dtbProfitFlag1Tr").show();
			
		}

		// 默认先隐藏机构类型下拉框
		$("#agencyTypeDis").hide();
		// 添加机构时一级代理商显示机构类型
		if (flag == "-1" && parentagencyId == staticagecyId && sysId == "TKMY") {
			$("#agencyTypeDis").show();
		}
		/* 	if("1"==agencyControl){
				$("#agencyTypeDis").show();
			} */

		$.showDivShade('${ctx}');
		$("#systemId").combobox('select', -1);
		$("#agencyType").combobox('select', 1); // 机构类型：1-代理商；2-定制商
		$("#agencyname").val(''); //机构名称
		$('#vestagencyId').combotree('clear');
		//$("#agencybriefname").val('');  //机构简称
		$("#contactsName").val(''); //联系人
		$("#companyPhone").val(''); //企业电话
		$("#companyEmail").val(''); //企业邮箱
		$("#companyAddr").val(''); //企业地址
		$("#certificate").val(''); //企业证书信息
		$("#legalinfo").val(''); //法人信息
		$("#organizationCode").val(''); //组织机构代码
		$("#agreementcode").val(''); //合同编号
		//$("#provinceId").combobox('select',-1);  //省
		$("#cityId").combobox('clear'); //市
		$("#accountBank").val(''); //开户银行
		$("#bankId").val(''); //开户银行编码
		$("#accountName").val(''); //户名
		$("#bankcode").val(''); //账号
		$("#someoneName").val('');//联系人姓名
		$("#someonePhone").val('');//联系人电话
		$("#userpId").val('');
		
		
		//$("#isDtbUser").attr("checked","checked");//默认选中
		$('#provinceId')
				.combobox(
						{
							url : '${ctx}/agency/agency.do?method=getProvince',
							valueField : 'id',
							textField : 'text',
							editable : false,
							onLoadSuccess : function() {
								$("#provinceId").combobox('select', -1);
								if (len == 1 && flag != "-1") {
									$("#provinceId").combobox('select',
											rows[0].provinceId); //省
								}
							},
							onSelect : function(row) {
								$('#cityId')
										.combobox(
												{
													url : '${ctx}/agency/agency.do?method=getCity&provinceId='
															+ row.id,
													valueField : 'id',
													textField : 'text',
													editable : false,
													onLoadSuccess : function() {
														var provinceId = $(
																"#provinceId")
																.combobox(
																		'getValue'); //省
														var cityId = $(
																"#cityId")
																.combobox(
																		'getValue'); //市
														if (len == 1
																&& flag != "-1"
																&& rows[0].provinceId == provinceId) {
															$("#cityId")
																	.combobox(
																			'select',
																			rows[0].cityId); //市
														}
													}
												});
							}
						});

		if (flag != "-1") {
			if (roleid == "3") {
				$.hideDivShade();
				$.messager.alert("提示 ", "操作员不可修改的机构！");
				return;
			}
			$('#markSave').window({
				title : '修改机构'
			});
			$("#agencyid").attr("disabled", true);
			$("#companyPhone").attr("disabled", true);
			$("#contactsName").attr("disabled", true);
			$("#userpId").attr("disabled", true);
			//$("#agencybriefname").attr("disabled",true);
			if (len == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录！");
				return;
			}
			if (len > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			}
			var row = $('#viewAgency').datagrid('getSelected');
			// 修改机构时一级代理商并且修改机构为二级时显示机构类型
			if (parentagencyId == staticagecyId && row.upperAgencyid == agid
					&& sysId == "TKMY") {
				$("#agencyTypeDis").show();
			}
			$("#agencyid").val(row.agency_id); //机构编码

			if (staticagecyId == row.agency_id) {
				$("#agencyname").attr({
					readonly : 'true'
				});
			}
			if (agid == row.agency_id) {
				$("#agencyname").attr({
					readonly : 'true'
				});
			}
			$("#agencyid").attr("readonly", "readonly");
			$("#systemId").combobox('select', row.systemId); //机构编码
			$("#agencyname").val(row.companyName); //机构名称
			//	$("#agencybriefname").val(row.companyBriefName);  //机构简称
			$("#contactsName").val(row.contactsName); //联系人
			$('#vestagencyId').combotree('setValue', row.vestagencyId);
			$("#companyPhone").val(row.companyPhone); //企业电话
			$("#companyEmail").val(row.companyEmail); //企业邮箱
			$("#companyAddr").val(row.companyAddr); //企业地址
			$("#certificate").val(row.certificate); //企业证书信息
			$("#legalinfo").val(row.legal_info); //法人信息
			$("#organizationCode").val(row.organizationCode); //组织机构代码
			$("#agreementcode").val(row.agreementcode); //合同编号
			$("#accountBank").val(row.accountBank); //开户银行
			$("#bankId").val(row.bankId); //开户银行编码
			$("#accountName").val(row.accountName); //户名
			$("#bankcode").val(row.bankcode); //账号
			$("#agencyType").combobox('select', row.agencyType); //机构类型
			$("#agencystatus").combobox('select', row.agency_status);
			$("#someoneName").val(row.someoneName);//联系人姓名
			$("#someonePhone").val(row.someonePhone);//联系人电话
			$("#userpId").val(row.userpId);
			
			$.post("${ctx}/agency/agency.do?method=getIsDtbUser", {
				'agencyId':row.agency_id
			}, function(data) {
				if(data){
					$("#isDtbUser").attr("checked","checked");
				}
			}, "json");
			
			$.post("${ctx}/agency/agency.do?method=getDtbProfitFlag", {
				'agencyId':row.agency_id
			}, function(data) {
				if(data){
					$("#dtbProfitFlag").attr("checked","checked");
				}
			}, "json");
			

		}
		$("#markSave").window('open').window('refresh');
	};

	$.AgencySave = function() {
		var flag = $("#optFlag").val();
		var agencyid = $("#agencyid").val();
		var agencyname = $("#agencyname").val(); //机构名称
		//var agencybriefname=$("#agencybriefname").val();  //机构简称
		var uppercompanyId = agid;
		var vestagencyId = $('#vestagencyId').combotree('getValue'); // 获取树对象
		var contactsName = $("#contactsName").val(); //联系人
		var companyPhone = $("#companyPhone").val(); //企业电话
		if ($("#companyEmail").val().indexOf("*") == -1) {
			var companyEmail = $("#companyEmail").val(); //企业邮箱
		} else {
			var companyEmail = "";
		}
		
		var companyAddr = $("#companyAddr").val(); //企业地址
		var certificate = $("#certificate").val(); //企业证书信息
		var legalinfo = $("#legalinfo").val(); //法人信息
		var organizationCode = $("#organizationCode").val(); //组织机构代码
		var agreementcode = $("#agreementcode").val(); //合同编号
		var provinceId = $("#provinceId").combobox('getValue'); //省
		var cityId = $("#cityId").combobox('getValue'); //市
		var accountBank = $("#accountBank").val(); //开户银行
		var bankId = $("#bankId").val(); //开户银行编码
		var accountName = $("#accountName").val(); //户名
		var bankcode = $("#bankcode").val(); //账号
		var agencyType = $("#agencyType").combobox('getValue'); //机构类型
		var agency_status = "";
		var someoneName = $("#someoneName").val();//联系人姓名
		var someonePhone = $("#someonePhone").val();//联系人电话
		var userpId = $("#userpId").val();
		
		var isDtbUser = $("input:[name='isDtbUser']:checked").val();
		if(!isDtbUser){
			isDtbUser = '';
		}
		var dtbProfitFlag = $("input:[name='dtbProfitFlag']:checked").val();
		if(!dtbProfitFlag){
			dtbProfitFlag = '';
		}
		
		//debugger;
		//var reg = /^[u4E00-u9FA5]+$/;
		if ($.trim(agencyname) == "") {
			$.messager.alert("提示 ", "请输入机构名称");
			return false;
		}
		
		if ($.trim(provinceId) == "-1") {
			$.messager.alert("提示 ", "请选择省");
			return false;
		}
		if ($.trim(cityId) == "-1") {
			$.messager.alert("提示 ", "请选择市");
			return false;
		}
		
		if ($.trim(companyAddr) == "") {
			$.messager.alert("提示 ", "请输入企业地址");
			return false;
		}
		if ($.trim(someoneName) == "") {
			$.messager.alert("提示 ", "请输入联系人姓名");
			return false;
		}
		
		var regMobile = /^1\d{10}$/g;//手机号码有效判断电信|联通|移动|移动
		
		 if($.trim(companyPhone) != ""){
			 if (companyPhone.indexOf("*") == -1){
				 if (!regMobile.test(companyPhone)) {
						$.messager.alert("提示 ", "请输入有效的手机号码!");
						return;
					}
			 }
		} 
		
		if ($.trim(someonePhone) == "") {
					$.messager.alert("提示 ", "请输入联系人电话");
					return false;
		}
		if ($("#someonePhone").val().indexOf("*") == -1) {
			var someonePhone = $("#someonePhone").val();//联系人电话
		} else {
			var someonePhone = "";
		}
		if ($("#someoneName").val().indexOf("*") == -1) {
			var someoneName = $("#someoneName").val();//联系人电话
		} else {
			var someoneName = "";
		}
		//if ($.trim(agencybriefname) == "") {
		//	$.messager.alert("提示 ", "请输入企业简码 ");
		//	return false;
		//}
	/* 	if ($.trim(contactsName) == "") {
			$.messager.alert("提示 ", "请输入法人姓名");
			return false;
		}
		if ($.trim(companyPhone) == "") {
			$.messager.alert("提示 ", "请输入手机号码");
			return false;
		} */
		
		
	/* 	if ($.trim(companyEmail) == "") {
			$.messager.alert("提示 ", "请输入企业邮箱");
			return false;
		}
		if ($.trim(companyAddr) == "") {
			$.messager.alert("提示 ", "请输入企业地址");
			return false;
		}
		if ($.trim(certificate) == "") {
			$.messager.alert("提示 ", "请输入企业证书编号");
			return false;
		}
		if ($.trim(legalinfo) == "") {
			$.messager.alert("提示 ", "请输入法人信息");
			return false;
		}
		if ($.trim(organizationCode) == "") {
			$.messager.alert("提示 ", "请输入组织机构代码");
			return false;
		}
		if ($.trim(agreementcode) == "") {
			$.messager.alert("提示 ", "请输入合同编号");
			return false;
		}
		
		if ($.trim(accountBank) == "") {
			$.messager.alert("提示 ", "请输入开户银行");
			return false;
		}
		if ($.trim(bankId) == "") {
			$.messager.alert("提示 ", "请输入开户银行编码");
			return false;
		}
		if ($.trim(accountName) == "") {
			$.messager.alert("提示 ", "请输入户名");
			return false;
		}
		if ($.trim(bankcode) == "") {
			$.messager.alert("提示 ", "请输入账号");
			return false;
		}
		if (!(/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
				.test($.trim(companyEmail))) {
			$.messager.alert("提示 ", "邮箱格式不正确");
			return false;
		} */

		/**	if(checkChinese(agencybriefname)){
				$.messager.alert("提示 ", "企业简码不能为中文，请重新输入");
				return false;
			} */
		
		if (flag == "-1") {
			var systemId = "";

			if (agid == staticagecyId) {
				systemId = $("#systemId").combobox('getValue');
				if (systemId == "-1") {
					$.messager.alert("提示 ", "请输入来源系统");
					return false;
				}
				if (!agencyid.length == 0) {
					if (agencyid.length < 4) {
						$.messager.alert("提示 ", "机构编码长度为4位！");
						return;
					}
				} else {
					$.messager.alert("提示 ", "机构编码不能为空！");
					return;
				}
			}
			$('#save').linkbutton('disable');
			var postActionUrl;
			if (agid == staticagecyId) {
				postActionUrl = "${ctx}/agency/agency.do?method=addAgency";
			}else{
				postActionUrl = "${ctx}/agency/agency.do?method=addAgencyWithDubbo";
			}
			
			$.post(postActionUrl, {
				systemId : systemId,
				agencyid : agencyid,
				agencyname : agencyname,
				//agencybriefname:agencybriefname,
				uppercompanyId : uppercompanyId,
				vestagencyId : vestagencyId,
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
				agencyType : agencyType,
				isDtbUser :isDtbUser,
				dtbProfitFlag : dtbProfitFlag,
			    someoneName : someoneName,
				someonePhone : someonePhone,
				userpId : userpId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			var systemId = "";
			if (agid == staticagecyId) {
				systemId = $("#systemId").combobox('getValue');
				if (systemId == "-1") {
					$.messager.alert("提示 ", "请输入来源系统");
					return false;
				}
				if (!agencyid.length == 0) {
					if (agencyid.length < 4) {
						$.messager.alert("提示 ", "机构编码长度为4位！");
						return;
					}
				} else {
					$.messager.alert("提示 ", "机构编码不能为空！");
					return;
				}

			}
			$('#save').linkbutton('disable');
			$.post("${ctx}/agency/agency.do?method=editAgency", {
				systemId : systemId,
				agencyid : agencyid,
				agencyname : agencyname,
				//agencybriefname:agencybriefname,
				vestagencyId : vestagencyId,
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
				agencyType : agencyType,
				agency_status : agency_status,
				isDtbUser :isDtbUser,
				dtbProfitFlag : dtbProfitFlag,
				someoneName : someoneName,
			    someonePhone : someonePhone,
			    userpId : userpId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}

	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewAgency();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#markSave").window('close');
	};
	//导出机构
	function agencyExport() {
		var agencyId = $('#agencyheadId').combogrid("getValue");
		var companyName = $('#agencyheadName').val();
		var agencyStatus = $("#agencyheadstatus").combobox('getValue');
		var provinceId = $("#provinceIds").combobox('getValue'); //省
		var cityId = $("#cityIds").combobox('getValue'); //市
		var certificationStatus = $("#certificationStatus").combobox('getValue'); //实名状态
		if (certificationStatus == null || $.trim(certificationStatus) == '-1') {
			certificationStatus = "";
		}
		if (agencyStatus == null || $.trim(agencyStatus) == '-1') {
			agencyStatus = "";
		}
		
		if (agencyId == null || $.trim(agencyId) == '-1') {
			agencyId = "";
		}
		if (provinceId == null || $.trim(provinceId) == '-1') {
			provinceId = "";
		}
		if (cityId == null || $.trim(cityId) == '-1') {
			cityId = "";
		}
		$.getToPost('${ctx}/agency/agency.do?method=agencyExport', {
			agencyId : agencyId,
			companyName : companyName,
			agencyStatus : agencyStatus,
			provinceId : provinceId,
			cityId : cityId,
			certificationStatus : certificationStatus
		});

	}
	$.deleteAgency = function() {

		if (roleid == "3") {
			alert("操作员不可删除的机构！");
			return;
		}
		var rows = $('#viewAgency').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert("提示 ", "请选择要删除的机构！");
			return;
		}
		$.messager.confirm('提示', '您确定要删除该机构？', function(r) {
			if (r) {
				var rows = $('#viewAgency').datagrid('getSelections');
				var agencyIds = [];
				for (var i = 0; i < rows.length; i++) {
					agencyIds.push("" + rows[i].agency_id + "");
				}
				var agides = agencyIds.join(",");
				$.post("${ctx}/agency/agency.do?method=delAgency", {
					agencyIds : agides
				}, function(data) {
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
			}
		});
	};

	$.viewAgency = function() {
		var agencyId = $('#agencyheadId').combogrid("getValue");
		var companyName = $('#agencyheadName').val();
		var agencyStatus = $("#agencyheadstatus").combobox('getValue');
		var provinceId = $("#provinceId").combobox('getValue'); //省
		var cityId = $("#cityIds").combobox('getValue'); //市
		var certificationStatus = $("#certificationStatus").combobox('getValue'); //实名状态
		if (agencyStatus == null || $.trim(agencyStatus) == '-1') {
			agencyStatus = "";
		}
		if (certificationStatus == null || $.trim(certificationStatus) == '-1') {
			certificationStatus = "";
		}
		if (agencyId == null || $.trim(agencyId) == '-1') {
			agencyId = "";
		}
		if (provinceId == null || $.trim(provinceId) == '-1') {
			provinceId = "";
		}
		if (cityId == null || $.trim(cityId) == '-1') {
			cityId = "";
		}
		$('#viewAgency').datagrid({
			title : '机构管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/agency/agency.do?method=getAgency",
			queryParams : {
				agencyId : agencyId,
				companyName : companyName,
				agencyStatus : agencyStatus,
				provinceId : provinceId,
				cityId : cityId,
				certificationStatus : certificationStatus
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ {
				field : "systemName",
				title : "来源系统",
				width : 100,
				align : "center",
				hidden : check()
			}, {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "vestagencyName",
				title : "归属机构",
				width : 100,
				align : "center",
				hidden : onlineFlag()
			}, {
				field : "uppercompanyName",
				title : "上级机构",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "status",
				title : "激活状态",
				width : 100,
				align : "center",
				formatter: function(value,row,index){
					if (value == null || value == ""){
						return "—";						
					} else {
						return value;
					}
					
				},
				sortable : true
			}, {
				field : "registerUrl",
				title : "注册URL",
				width : 100,
				align : "center",
				formatter: function(value,row,index){
					if (value != null && value != '已失效'){
						return '<input type="Button" value="查看链接" onclick="showRegisterUrl(\''+value+'\')">';						
					} else if (value == null || value == ""){
						return "—";
					} else {
						return value;
					}
					
				},

				sortable : true
			}, {
				field : "actTime",
				title : "激活时间",
				width : 100,
				align : "center",
				formatter: function(value,row,index){
					if (value == null || value == ""){
						return "—";						
					} else {
						return value;
					}
					
				},
				sortable : true
			}, {
				field : "someoneName",
				title : "联系人",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "someonePhone",
				title : "企业电话",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "agencystatusSrc",
				title : "实名状态",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "contactsName",
				title : "法人姓名",
				width : 100,
				align : "center",
				sortable : true
			} ,{
				field : "userpId",
				title : "身份证号",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyPhone",
				title : "手机号码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyEmail",
				title : "企业邮箱",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "provinceName",
				title : "省",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "cityName",
				title : "市",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyAddr",
				title : "企业地址",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "certificate",
				title : "企业证书信息",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "legal_info",
				title : "法人信息",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "organizationCode",
				title : "组织机构代码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "agreementcode",
				title : "合同编号",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "accountBank",
				title : "开户银行",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "bankId",
				title : "开户银行编码",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "accountName",
				title : "户名",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "bankcode",
				title : "银行账号",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "createId",
				title : "创建人",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "createDt",
				title : "创建时间",
				width : 200,
				align : "center",
				sortable : true
			} ] ],
			hideColumn : [ [ {
				field : "upperAgencyid", //上级机构编码vestagencyName
				field : "vestagencyId", //归属机构编码agency_status
				field : "agency_status", //状态编号
				field : "cityId", //市编码
				field : "provinceId", //省编码
				field : "systemId", //来源系统编号
				field : "companyBriefName" //公司简称	
			} ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar : [ {
				id : 'btnadd',
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					$.openWin(-1);
				}
			},
<% 
if(ConstantUtils.CENTERCODE.equals(agencyId.trim())){
%>			
			'-', {
				id : 'btncut',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					$.openWin();
				}
			}, 
<%
}
%>
			
			'-', {
				id : 'btnsave',
				text : '删除',
				iconCls : 'icon-cut',
				handler : function() {
					$.deleteAgency();

				}
			} ]
		});
		if (agid != 'RTB00000000') {
			$('#viewAgency').datagrid('hideColumn','companyEmail');
			$('#viewAgency').datagrid('hideColumn','certificate');
			$('#viewAgency').datagrid('hideColumn','uppercompanyName');
			$('#viewAgency').datagrid('hideColumn','userpId');
			$('#viewAgency').datagrid('hideColumn','vestagencyName');
			$('#viewAgency').datagrid('hideColumn','agencystatusSrc');
			$('#viewAgency').datagrid('hideColumn','certificate');
			$('#viewAgency').datagrid('hideColumn','legal_info');
			$('#viewAgency').datagrid('hideColumn','organizationCode');
			$('#viewAgency').datagrid('hideColumn','agreementcode');
			$('#viewAgency').datagrid('hideColumn','accountBank');
			$('#viewAgency').datagrid('hideColumn','bankId');
			$('#viewAgency').datagrid('hideColumn','accountName');
			$('#viewAgency').datagrid('hideColumn','bankcode');
			$('#viewAgency').datagrid('hideColumn','companyPhone');
			$('#viewAgency').datagrid('hideColumn','contactsName');
			//$("#certificationStatus").combobox('destroy');
			$("#certificationStatusTd,#certificationStatusSelectTd").hide();
			/* //隐藏第二个修改按钮
			$('div.datagrid-toolbar a').eq(1).hide();
			//隐藏第一条分隔线
			$('div.datagrid-toolbar div').eq(1).hide(); */
		}
		$('#btnsave').hide();
		var p = $('#viewSystem').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	function cls() {
		var agencyid = $("#agencyid").val();
		if (!agencyid.length == 0) {
			if (agencyid.length < 4) {
				$.messager.alert("提示 ", "机构编码长度为4位！");
				return;
			}

		} else {
			$.messager.alert("提示 ", "机构编码不能为空！");
		}
	}
		function showRegisterUrl(value){
		$.messager.alert('注册URL',"该下级机构已开设成功！系统已自动生成该下级机构信息，请将下方链接复制给下级进行激活，激活成功后即可登录分润系统（三击链接即可全选）<br/>"+'<textarea class="easyui-combobox" readonly="readonly" style="width: 280px; height:200px;">'+value+'</textarea>'); 
	}
		
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td>
				<!-- <input class="easyui-combotree" id="agencytest"  name="agencytest" style="width:250px;" /> -->&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				机构编码：

			</td>
			<td><!-- <input class="easyui-combotree" id="agencyheadId" name="vestagencyId" editable="true" style="width: 150px;" />  --><%-- data-options="url:'${ctx}/agency/agency.do?method=getAgencyTree',method:'get'"  --%>
			<input id="agencyheadId" style="width:150px"></input>
			
			</td>
			<td>企业名称：</td>
			<td><input type="text" name="agencyheadName" id="agencyheadName" style="width: 150px;" /></td>
			<td>激活状态：</td>
			<td><select class="easyui-combobox" id="agencyheadstatus" name="agencyheadstatus" style="width: 155px;" editable="false">
					<option value="-1" selected="selected">--请选择--</option>
					<option value="0">未激活</option>
					<option value="1">已激活</option>
			</select></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewAgency()">查询</a></td>


		</tr>
		<tr>
			<td align="right">省：</td>
			<td align="left"><input class="easyui-combobox" id="provinceIds" name="provinceIds" style="width: 155px;" /></td>
			<td align="right">市：</td>
			<td align="left"><input class="easyui-combobox" id="cityIds" name="cityIds" style="width: 155px;" editable="false" /></td>
			<td id = "certificationStatusTd">实名状态：</td>
			<td id = "certificationStatusSelectTd"><select class="easyui-combobox" id="certificationStatus" name="agencyheadstatus" style="width: 155px;" editable="false">
					<option value="-1" selected="selected">--请选择--</option>
					<option value="1">未激活未实名</option>
					<option value="2">未实名</option>
					<option value="3">已实名</option>
			</select></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="agencyExport()">导出</a></td>

		</tr>
		<tr>
			<td colspan="4">*请将激活链接发送给下级使用，机构一旦被激活链接将失效，请妥善保管链接；</td>
		</tr>
	</table>
	<table id="viewAgency"></table>
	<div id="markSave" class="easyui-window" title="添加机构" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true" closable="false"
		style="width: 730px; height: 455px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="optFlag" name="optFlag" />
				<table style="width: 100%;">

					<tr id="systr" style="display: none;">
						<td align="right"><span style="color: red">*</span>来源系统：</td>
						<td align="left"><select id="systemId" class="easyui-combobox" name="systemId" data-options="panelHeight:'auto'"
							style="width: 155px; height: 30px;" editable="false"></select></td>

						<td align="right"><span style="color: red">*</span>机构编码：</td>
						<td align="left"><input type="text" name="agencyid" id="agencyid" maxlength="4" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								onblur="cls()" style="text-transform: uppercase;" /></td>
						<td align="right"></td>
						<td align="left"></td>

					</tr>
					

					<tr style="display: none;">
						<td align="right"><span style="color: red">*</span>归属机构：</td>
						<td align="left"><input class="easyui-combotree" id="vestagencyId" name="vestagencyId"
								data-options="url:'${ctx}/agency/agency.do?method=getAgencyTree&flag=1',method:'get'" style="width: 155px;" disabled="disabled" /></td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>机构名称：</td>
						<td align="left"><input type="text" name="agencyname" id="agencyname" maxlength="50" /></td>
						
						<td align="right"></td>
						<td align="left"></td>
						
					</tr>

					<tr id="contactsNameTr" style="display: none;">
						<td align="right"><span style="color: red">*</span>法人姓名：</td>
						<td align="left"><input type="text" name="contactsName" id="contactsName" maxlength="50" /></td>
						
						<td align="right"><span style="color: red">*</span>身份证号：</td>
						<td align="left"><input type="text" name="userpId" id="userpId" maxlength="50" /></td>
					</tr>
					
					
					<tr id="companyPhoneTr" style="display: none;">

						<td align="right"><span style="color: red">*</span>手机号码：</td>
						<td align="left"><input type="text" name="companyPhone" id="companyPhone" maxlength="11" onkeyup="value=this.value.replace(/\D+/g,'')" />
						</td>
						
						<td align="right"></td>
						<td align="left">
						</td>
						
						
					</tr>
					
					
					
					
					<tr id="provinceTr" style="display: none;">
						<td align="right"><span style="color: red">*</span>省：</td>
						<td align="left"><input class="easyui-combobox" id="provinceId" name="provinceId" style="width: 155px;" /></td>
						<td align="right"><span style="color: red">*</span>市：</td>
						<td align="left"><input class="easyui-combobox" id="cityId" name="cityId" style="width: 155px;" editable="false" /></td>

					</tr>
					
					<tr id="companyAddrTr" style="display: none;">
						<td align="right"><span style="color: red">*</span>企业地址</td>
						<td align="left"><input type="text" name="companyAddr" id="companyAddr" maxlength="50" /></td>
						
						<td align="right"></td>
						<td align="left"></td>
					</tr>
					
					<tr id="someoneNameTr" style="display: none;">
						<td align="right"><span style="color: red">*</span>联系人姓名：</td>
						<td align="left"><input type="text" name="someoneName" id="someoneName" maxlength="50" /></td>
						<td align="right"><span style="color: red">*</span>联系人电话：</td>
						<td align="left"><input type="text" name="someonePhone" id="someonePhone" maxlength="11" onkeyup="value=this.value.replace(/\D+/g,'')" /></td>
					</tr>

					<tr id="companyEmailTr" style="display: none;">
						<td align="right"><span style="color: red"></span>企业邮箱：</td>
						<td align="left"><input type="text" name="companyEmail" id="companyEmail" maxlength="50" /></td>

						<td align="right"><span style="color: red"></span>企业证书信息：</td>
						<td align="left"><input type="text" name="certificate" id="certificate" maxlength="50" /></td>
					</tr>

					<tr id="legalinfoTr" style="display: none;">
						<td align="right"><span style="color: red"></span>法人信息：</td>
						<td align="left"><input type="text" name="legalinfo" id="legalinfo" maxlength="50" /></td>
						<td align="right"><span style="color: red"></span>组织机构代码：</td>
						<td align="left"><input type="text" name="organizationCode" id="organizationCode" maxlength="50"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
					</tr>

					<tr id="agreementcodeTr" style="display: none;">

						<td align="right"><span style="color: red"></span>合同编号：</td>
						<td align="left"><input type="text" name="agreementcode" id="agreementcode" maxlength="50" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" /></td>
						<td align="right"><span style="color: red"></span>开户银行：</td>
						<td align="left"><input type="text" name="accountBank" id="accountBank" maxlength="50" /></td>
					</tr>

				

				

					<tr id="accountNameTr" style="display: none;">

						<td align="right"><span style="color: red"></span>户名：</td>
						<td align="left"><input type="text" name="accountName" id="accountName" maxlength="50" /></td>

						<td align="right"><span style="color: red"></span>银行账号：</td>
						<td align="left"><input type="text" name="bankcode" id="bankcode" maxlength="50" onkeyup="value=this.value.replace(/\D+/g,'')" /></td>

					</tr>
					<tr id="sTr" style="display: none;">
						<td align="right"><span style="color: red"></span>开户银行编码：</td>
						<td align="left"><input type="text" name="bankId" id="bankId" maxlength="50" /></td>
						
						
						<td align="right" style="display: none;">状态：</td>
						<td><select class="easyui-combobox" id="agencystatus" name="agencystatus" style="width: 155px; display: none;" editable="false">
								<option value="1" selected="selected">未激活</option>
								<option value="2" selected="selected">审核通过未实名认证</option>
								<option value="3">审核通过且实名</option>
						</select>
						</td>
				
						
					</tr>
					
					<tr id="dtbProfitFlag1Tr" style="display: none;">
							<%
						   boolean tinyBussiness = (Boolean)session.getAttribute(ConstantUtils.TINYBUSSINESS);
							if(tinyBussiness){
						%>
								<td align="right" >开通：</td>
								<td align="left" >
									<input type="checkbox" name="isDtbUser" id ="isDtbUser" value="1"/>
								</td>
						
						<%
							}
						%>
						
						<td id="dtbProfitFlag1" align="right" >开通提现：</td>
						<td id="dtbProfitFlag2" align="left" >
							<input type="checkbox" name="dtbProfitFlag" id ="dtbProfitFlag" value="1" />
						</td>	
					</tr> 
					
					
					<tr id="agencyTypeDis" style="display: none;">
						<td align="right"><span style="color: red"></span>机构类型：</td>
						<td><select class="easyui-combobox" id="agencyType" name="agencyType" style="width: 155px;" editable="false">
								<option value="1" selected="selected">代理商</option>
								<option value="2">定制商</option>
						</select></td>
					</tr>


					<tr height="54px">
						<td align="center" colspan="6"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
							onclick="$.AgencySave()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close"
							id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
<script type="text/javascript">
$.post("${ctx}/agency/agency.do?method=isDtbUserAuthority", {
}, function(data) {
	if(data.success == 'false'){
		$("#dtbProfitFlag1").remove();
		$("#dtbProfitFlag2").remove();
	}
}, "json");
</script>	
	
</body>

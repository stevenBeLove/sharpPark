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
<title>瑞通宝综合管理系统</title>
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
		$('#systemId').combobox({
			url : "${ctx}/system/system.do?method=getCombSystems",
			valueField : "id",
			textField : "text"
		});
		$.viewAgency();

	});

	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}
	var flag;
	$.openWin = function(obj) {
		$("#merchantNameU").val('');
		var rows = $('#viewAgency').datagrid('getSelections');
		var len = rows.length;
		$('#markSave').window({
			title : '添加停车场基础信息'
		});
		flag = obj;
		if (flag != "-1") {
			$('#markSave').window({title: '修改停车场基础信息'});
			var rows = $('#viewAgency').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			}else if(rows.length ==0){
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录");
				return;
			}
			var row = $('#viewAgency').datagrid('getSelected');
			$("#merchantNameU").val(row.merchantName);
		}
		$("#markSave").window('open').window('refresh');
	};

	$.AgencySave = function() {
		var merchantName = $("#merchantName").val();
		var merchantServicePhone = $("#merchantServicePhone").val();
		var accountNo = $("#accountNo").val();
		var cityId = $("#cityId").val();
		var equipmentName = $("#equipmentName").val();
		var parkingAddress = $("#parkingAddress").val();
		var longitude = $("#longitude").val();
		var latitude = $("#latitude").val();
		var parkingStartTime = $("#parkingStartTime").val();
		var parkingEndTime = $("#parkingEndTime").val();
		var parkingNumber = $("#parkingNumber").val();
		var parkingLotType = $("#parkingLotType").combobox('getValue');
		var parkingType = $("#parkingType").combobox('getValue');
		var paymentMode = $("#paymentMode").combobox('getValue');
		var payType = $("#payType").combobox('getValue');
		var shopingmallId = $("#shopingmallId").val();
		var parkingFeeDescription = $("#parkingFeeDescription").val();
		var contactName = $("#contactName").val();
		var contactMobile = $("#contactMobile").val();
		var contactTel = $("#contactTel").val();
		var contactEmali = $("#contactEmali").val();
		var contactWeixin = $("#contactWeixin").val();
		var contactAlipay = $("#contactAlipay").val();
		var parkingName = $("#parkingName").val();
		var timeOut = $("#timeOut").val();
		var status = $("#status").combobox('getValue');
		if (flag == "-1") {
			$('#save').linkbutton('disable');
			$.post("${ctx}/park/park.do?method=addPark", {
				merchantName:merchantName,
				merchantServicePhone:merchantServicePhone,
				accountNo:accountNo,
				cityId:cityId,
				equipmentName:equipmentName,
				parkingAddress:parkingAddress,
				longitude:longitude,
				latitude:latitude,
				parkingStartTime:parkingStartTime,
				parkingEndTime:parkingEndTime,
				parkingNumber:parkingNumber,
				parkingLotType:parkingLotType,
				parkingType:parkingType,
				paymentMode:paymentMode,
				payType:payType,
				shopingmallId:shopingmallId,
				parkingFeeDescription:parkingFeeDescription,
				contactName:contactName,
				contactMobile:contactMobile,
				contactTel:contactTel,
				contactEmali:contactEmali,
				contactWeixin:contactWeixin,
				contactAlipay:contactAlipay,
				parkingName:parkingName,
				timeOut:timeOut,
				status:status
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
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
		var outParkingId = $('#outParkingId').val();
		var merchantName = $('#merchantName').val();
		if (outParkingId == null || $.trim(outParkingId) == '-1') {
			outParkingId = "";
		}
		if (merchantName == null || $.trim(merchantName) == '-1') {
			merchantName = "";
		}
		$('#viewAgency').datagrid({
			title : '停车场管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/park/park.do?method=getPark",
			queryParams : {
				outParkingId : outParkingId,
				merchantName : merchantName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{field : "ck",checkbox : true
			}, {
				field : "outParkingId",
				title : "ISV停车场ID",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "merchantName",
				title : "商户简称",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "merchantServicePhone",
				title : "服务商电话",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "accountNo",
				title : "帐号",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "equipmentName",
				title : "设备商名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "parkingAddress",
				title : "停车场地址",
				width : 100,
				align : "center",
			}, {
				field : "status",
				title : "是否有效",
				width : 100,
				align : "center",
			} ] ],
			hideColumn : [ [ {
				field : "longitude",
				field : "latitude",
				field : "parkingStartTime",
				field : "parkingEndTime",
				field : "parkingNumber",
				field : "parkingLotType",
				field : "parkingType",
				field : "paymentMode",
				field : "payType",
				field : "shopingmallId",
				field : "parkingFeeDescription",
				field : "contactName",
				field : "contactMobile",
				field : "contactTel",
				field : "contactEmali",
				field : "contactWeixin",
				field : "contactAlipay",
				field : "parkingName",
				field : "timeOut"
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
			'-', {
				id : 'btncut',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					$.openWin();
				}
			} ]
		});
		$('#btnsave').hide();
		var p = $('#viewAgency').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
		function showRegisterUrl(value){
		$.messager.alert('注册URL',"该下级机构已开设成功！系统已自动生成该下级机构信息，请将下方链接复制给下级进行激活，激活成功后即可登录分润系统（三击链接即可全选）<br/>"+'<textarea class="easyui-combobox" readonly="readonly" style="width: 280px; height:200px;">'+value+'</textarea>'); 
	}
	
	function resetPark(){
		$('#outParkingId').val('');
		$('#merchantName').val('');
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				停车场ID：
			</td>
			<td>
			<input type="text" id="outParkingId" name="outParkingId" style="width:150px"></input>
			
			</td>
			<td>商户简称：</td>
			<td><input type="text" name="merchantName" id="merchantName" style="width: 150px;" /></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewAgency()">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="resetPark()">重置</a></td>
		</tr>
	</table>
	<table id="viewAgency"></table>
	<div id="markSave" class="easyui-window" title="添加停车场基础信息" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true" closable="false"
		style="width: 730px; height: 555px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="optFlag" name="optFlag" />
				<table style="width: 100%;">
					<tr>
						<td align="right"><span style="color: red">*</span>商户简称：</td>
						<td align="left"><input type="text" name="merchantNameU" id="merchantNameU" /></td>

						<td align="right"><span style="color: red">*</span>服务电话：</td>
						<td align="left"><input type="text" name="merchantServicePhone" id="merchantServicePhone" onkeyup="value=this.value.replace(/\D+/g,'')"/></td>
						<td align="right"></td>
						<td align="left"></td>
					</tr>

					<tr>
						<td align="right"><span style="color: red">*</span>帐号：</td>
						<td align="left"><input type="text" name="accountNo" id="accountNo"/></td>
						
						<td align="right"><span style="color: red">*</span>城市编号：</td>
						<td align="left"><input type="text" name="cityId" id="cityId" maxlength="50" /></td>
					</tr>

					<tr>
						<td align="right"><span style="color: red">*</span>设备商名称：</td>
						<td align="left"><input type="text" name="equipmentName" id="equipmentName" maxlength="100" /></td>
						
						<td align="right"><span style="color: red">*</span>停车场地址：</td>
						<td align="left"><input type="text" name="parkingAddress" id="parkingAddress" maxlength="300" /></td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>经度，最长15位字符(包括小数点)，注：高德坐标系：</td>
						<td align="left"><input type="text" name="longitude" id="longitude" />
						</td>
						<td align="right"><span style="color: red">*</span>纬度，最长15位字符(包括小数点)，注：高德坐标系</td>
						<td align="left">
							<input type="text" name="latitude" id="latitude" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场开始营业时间：</td>
						<td align="left"><input type="text" name="parkingStartTime" id="parkingStartTime" />
						</td>
						<td align="right">停车场结束营业时间:</td>
						<td align="left">
							<input type="text" name="parkingEndTime" id="parkingEndTime" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车位数目：</td>
						<td align="left"><input type="text" name="parkingNumber" id="parkingNumber" />
						</td>
						<td align="right">停车场类型:</td>
						<td align="left">
							<select class="easyui-combobox" id="parkingLotType" name="parkingLotType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">小区停车场</option>
									<option value="2">商圈停车场</option>
									<option value="3">路面停车场</option>
									<option value="4">园区停车场</option>
									<option value="5">写字楼停车场</option>
									<option value="6">私人停车场</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场类型：</td>
						<td align="left">
							<select class="easyui-combobox" id="parkingType" name="parkingType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">地面</option>
									<option value="2">地下</option>
									<option value="3">路边</option>
							</select>
						</td>
						<td align="right">缴费模式:</td>
						<td align="left">
							<select class="easyui-combobox" id="paymentMode" name="paymentMode" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">停车卡缴费</option>
									<option value="2">物料缴费</option>
									<option value="3">中央缴费机</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right">支付方式：</td>
						<td align="left">
							<select class="easyui-combobox" id="payType" name="payType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">支付宝在线缴费</option>
									<option value="2">支付宝代扣缴费</option>
									<option value="3">当面付</option>
							</select>
						</td>
						<td align="right">商圈id:</td>
						<td align="left">
							<input type="text" name="shopingmallId" id="shopingmallId" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场联系人：</td>
						<td align="left">
							<input type="text" name="contactName" id="contactName" />
						</td>
						<td align="right">停车场联系人手机:</td>
						<td align="left">
							<input type="text" name="contactMobile" id="contactMobile" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场联系人座机：</td>
						<td align="left">
							<input type="text" name="contactTel" id="contactTel" />
						</td>
						<td align="right">停车场联系人邮箱:</td>
						<td align="left">
							<input type="text" name="contactEmali" id="contactEmali" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场联系人微信：</td>
						<td align="left">
							<input type="text" name="contactWeixin" id="contactWeixin" />
						</td>
						<td align="right">停车场联系人支付宝账户:</td>
						<td align="left">
							<input type="text" name="contactAlipay" id="contactAlipay" />
						</td>
					</tr>
					
					<tr>
						<td align="right">停车场名称：</td>
						<td align="left">
							<input type="text" name="parkingName" id="parkingName" />
						</td>
						<td align="right">用户支付未离场的超时时间(以分钟为单位):</td>
						<td align="left">
							<input type="text" name="timeOut" id="timeOut" />
						</td>
					</tr>
					
					<tr>
						<td align="right">状态：</td>
						<td align="left">
							<select class="easyui-combobox" id="status" name="status" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">可用</option>
									<option value="0">不可用</option>
							</select>
						</td>
						<td align="right"></td>
						<td align="left">
						</td>
					</tr>
					
					<tr>
						<td align="right">收费说明</td>
						<td align="left">
							<textarea rows="5" cols="20" name="parkingFeeDescription" id="parkingFeeDescription" ></textarea>
						</td>
						<td align="right"></td>
						<td align="left">
						</td>
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

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
		$.search();

	});

	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}
	var flag;
	$.openWin = function(obj) {
		$("#freeVehicleBrandId").val('');
		$("#carNumber").val('');
		$("#vehicleBrandType").combobox('select', '');
		$("#vehiclePlace").val('');
		$("#carOwnerName").val('');
		$("#carOwnerAddres").val('');
		$("#carOwnerPhone").val('');
		$("#carOwnerEmail").val('');
		$("#remark").val('');
		$("#status").combobox('select', '');
		$("#vehicleBrand").val('');
		var rows = $('#search').datagrid('getSelections');
		var len = rows.length;
		$('#markSave').window({
			title : '免费车录入'
		});
		flag = obj;
		if (flag != "-1") {
			$('#markSave').window({title: '免费车编辑'});
			var rows = $('#search').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			}else if(rows.length ==0){
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录");
				return;
			}
			var row = $('#search').datagrid('getSelected');
			$("#freeVehicleBrandId").val(row.freeVehicleBrandId);
			$("#carNumber").val(row.carNumber);
			$("#vehicleBrandType").combobox('select', row.vehicleBrandType);
			$("#vehiclePlace").val(row.vehiclePlace);
			$("#carOwnerName").val(row.carOwnerName);
			$("#carOwnerAddres").val(row.carOwnerAddres);
			$("#carOwnerPhone").val(row.carOwnerPhone);
			$("#carOwnerEmail").val(row.carOwnerEmail);
			$("#remark").val(row.remark);
			$("#vehicleBrand").val(row.vehicleBrand);
			$("#status").combobox('select', row.status);
		}
		$("#markSave").window('open').window('refresh');
	};

	$.save = function() {
		var freeVehicleBrandId =$("#freeVehicleBrandId").val();
		var carNumber = $("#carNumber").val();
		var vehicleBrandType = $("#vehicleBrandType").combobox('getValue');
		var vehiclePlace = $("#vehiclePlace").val();
		var carOwnerName = $("#carOwnerName").val();
		var carOwnerAddres = $("#carOwnerAddres").val();
		var carOwnerPhone =$("#carOwnerPhone").val();
		var carOwnerEmail =$("#carOwnerEmail").val();
		var remark = $("#remark").val();
		var status = $("#status").combobox('getValue');
		var vehicleBrand = $("#vehicleBrand").val();
		if ($.trim(carNumber) == "") {
			$.messager.alert("提示 ", "请输入车牌号");
			return false;
		}
		if (flag == "-1") {
			$('#save').linkbutton('disable');
			$.post("${ctx}/freeVehicleBrand/freeVehicleBrand.do?method=addFreeVehicleBrand", {
				carNumber : carNumber,
				vehicleBrandType : vehicleBrandType,
				vehiclePlace : vehiclePlace,
				carOwnerName : carOwnerName,
				carOwnerAddres : carOwnerAddres,
				carOwnerPhone : carOwnerPhone,
				carOwnerEmail : carOwnerEmail,
				carOwnerAddres : carOwnerAddres,
				remark : remark,
				vehicleBrand : vehicleBrand,
				status:status
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			$.post("${ctx}/freeVehicleBrand/freeVehicleBrand.do?method=editFreeVehicleBrand", {
				carNumber : carNumber,
				vehicleBrandType : vehicleBrandType,
				vehiclePlace : vehiclePlace,
				carOwnerName : carOwnerName,
				carOwnerAddres : carOwnerAddres,
				carOwnerPhone : carOwnerPhone,
				carOwnerEmail : carOwnerEmail,
				carOwnerAddres : carOwnerAddres,
				remark : remark,
				vehicleBrand : vehicleBrand,
				status:status,
				freeVehicleBrandId:freeVehicleBrandId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}

	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.search();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#markSave").window('close');
	};
	
	function freeVehicleBrandExport() {
		var carNumber = $('#s_carNumber').val();
		var carOwnerName = $('#s_carOwnerName').val();
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if (carOwnerName == null || $.trim(carOwnerName) == '-1') {
			carOwnerName = "";
		}
		$.getToPost('${ctx}/freeVehicleBrand/freeVehicleBrand.do?method=freeVehicleBrandExport', {
			carNumber : carNumber,
			carOwnerName : carOwnerName
		});
	}
	
	$.search = function() {
		var carNumber = $('#s_carNumber').val();
		var carOwnerName = $('#s_carOwnerName').val();
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if (carOwnerName == null || $.trim(carOwnerName) == '-1') {
			carOwnerName = "";
		}
		$('#search').datagrid({
			title : '免费车牌管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/freeVehicleBrand/freeVehicleBrand.do?method=getFreeVehicleBrand",
			queryParams : {
				carNumber : carNumber,
				carOwnerName : carOwnerName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{field : "ck",checkbox : true
			}, {
				field : "carNumber",
				title : "车牌号",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "vehicleBrandType",
				title : "车牌类型",
				width : 100,
				align : "center",
				sortable : true,
				formatter:function(value,row,index){
		          	if(value == 1){
		          		return '公安';
		          	}else if(value==2){
		          		return '武警';
		          	}else if(value==3){
		          		return '军队';
		          	}else if(value==4){
		          		return '其他';
		          	}else{
		          		return '其他';
		          	}
		        }
			}, {
				field : "vehicleBrand",
				title : "车辆品牌",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "carOwnerName",
				title : "车主姓名",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "carOwnerPhone",
				title : "联系方式",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "remark",
				title : "备注",
				width : 100,
				align : "center",
			}, {
				field : "status",
				title : "是否有效",
				width : 100,
				align : "center",
				formatter:function(value,row,index){
		          	if(value == 1){
		          		return '有效';
		          	}else{
		          		return '无效';
		          	}
		        }
			} ] ],
			hideColumn : [ [ {
				field : "vehiclePlace",
				field : "carOwnerAddres",
				field : "carOwnerEmail"
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
		var p = $('#search').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	
	function reset(){
		$('#s_carNumber').val('');
		$('#s_carOwnerName').val('');
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				车牌号码：
			</td>
			<td>
			<input type="text" id="s_carNumber" name="s_carNumber" style="width:150px"></input>
			
			</td>
			<td>车主姓名：</td>
			<td><input type="text" name="s_carOwnerName" id="s_carOwnerName" style="width: 150px;" /></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.search()">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="reset()">重置</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="freeVehicleBrandExport()">导出</a></td>
		</tr>
	</table>
	<table id="search"></table>
	<div id="markSave" class="easyui-window" title="免费车录入" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true" closable="false"
		style="width: 730px; height: 555px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="freeVehicleBrandId" name="freeVehicleBrandId"/>
				<table style="width: 100%;">
					<tr>
						<td align="right"><span style="color: red">*</span>车牌号码：</td>
						<td align="left"><input type="text" name="carNumber" id="carNumber" /></td>
						
						<td align="right"><span style="color: red">*</span>车牌类型：</td>
						<td align="left">
							<select class="easyui-combobox" id="vehicleBrandType" name="vehicleBrandType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">公安</option>
									<option value="2">武警</option>
									<option value="3">军队</option>
									<option value="4">其他</option>
							</select>
						</td>
					</tr>

					<tr>
						<td align="right"><span style="color: red">*</span>车辆品牌：</td>
						<td align="left">
							<input type="text" name="vehicleBrand" id="vehicleBrand" />
						</td>
						<td align="right"><span style="color: red">*</span>停车位置：</td>
						<td align="left"><input type="text" name="vehiclePlace" id="vehiclePlace" maxlength="300" /></td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>车主姓名：</td>
						<td align="left"><input type="text" name="carOwnerName" id="carOwnerName" />
						</td>
						<td align="right">车主地址：</td>
						<td align="left"><input type="text" name="carOwnerAddres" id="carOwnerAddres" />
						</td>
					</tr>
					
					<tr>
						<td align="right">联系电话：</td>
						<td align="left">
							<input type="text" name="carOwnerPhone" id="carOwnerPhone" onkeyup="value=this.value.replace(/\D+/g,'')"/>
						</td>
						<td align="right">电子邮件:</td>
						<td align="left">
							<input type="text" name="carOwnerEmail" id="carOwnerEmail" />
						</td>
					</tr>
					
					<tr>
						<td align="right">备注：</td>
						<td align="left">
							<textarea rows="5" cols="20" name="remark" id="remark" ></textarea>
						</td>
						<td align="right">状态：</td>
						<td align="left">
							<select class="easyui-combobox" id="status" name="status" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">可用</option>
									<option value="0">不可用</option>
							</select>
						</td>
					</tr>

					<tr height="54px">
						<td align="center" colspan="6"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
							onclick="$.save()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close"
							id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>

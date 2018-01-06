<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils"%>
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
		$('#systemId').combobox({
			url : "${ctx}/system/system.do?method=getCombSystems",
			valueField : "id",
			textField : "text"
		});
		$.search();

	});
	
	function dateFormat(value){
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' +m + '-' + d;
    }
	
	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}
	var flag;
	$.openWin = function(obj) {
		$("#refundMoney").val('');
		$("#n_endDate").datebox('setValue','');	
		$("#monthVehicleBrandId").val('');
		$("#carNumber").val('');
		$("#vehicleBrandType").combobox('select', '');
		$("#vehiclePlace").val('');
		$("#carOwnerName").val('');
		$("#carOwnerAddres").val('');
		$("#carOwnerPhone").val('');
		$("#carOwnerEmail").val('');
		$("#remark").val('');
		$("#vehicleBrand").val('');
		$("#startDate").datebox('setValue','');	
		$("#endDate").datebox('setValue','');	
		$("#monthPayAmount").val('');
		var rows = $('#search').datagrid('getSelections');
		var len = rows.length;
		$('#markSave').window({
			title : '月卡缴费'
		});
		flag = obj;
		if (flag != "-1") {
			$('#markSave').window({title: '月卡编辑'});
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
			$("#monthVehicleBrandId").val(row.monthVehicleBrandId);
			if(flag=="2"){
				$("#r_carNumber").val(row.carNumber);
				$("#r_vehicleBrandType").combobox('select', row.vehicleBrandType);
				$("#r_vehiclePlace").val(row.vehiclePlace);
				$("#r_endDate").datebox('setValue',dateFormat(row.endDate));
				$("#r_carOwnerName").val(row.carOwnerName);
				$("#r_monthPayAmount").val(row.monthPayAmount);
			}else{
				$("#carNumber").val(row.carNumber);
				$("#vehicleBrandType").combobox('select', row.vehicleBrandType);
				$("#vehiclePlace").val(row.vehiclePlace);
				$("#carOwnerName").val(row.carOwnerName);
				$("#carOwnerAddres").val(row.carOwnerAddres);
				$("#carOwnerPhone").val(row.carOwnerPhone);
				$("#carOwnerEmail").val(row.carOwnerEmail);
				$("#remark").val(row.remark);
				$("#vehicleBrand").val(row.vehicleBrand);
				$("#startDate").datebox('setValue',dateFormat(row.startDate));
				$("#endDate").datebox('setValue',dateFormat(row.endDate));
				$("#monthPayAmount").val(row.monthPayAmount);
			}
		}
		if(flag=="2"){
			$("#editSave").window('open').window('refresh');
		}else{
			$("#markSave").window('open').window('refresh');
		}
	};

	$.save = function() {
		var monthVehicleBrandId =$("#monthVehicleBrandId").val();
		var carNumber = $("#carNumber").val();
		var vehicleBrandType = $("#vehicleBrandType").combobox('getValue');
		var vehiclePlace = $("#vehiclePlace").val();
		var carOwnerName = $("#carOwnerName").val();
		var carOwnerAddres = $("#carOwnerAddres").val();
		var carOwnerPhone =$("#carOwnerPhone").val();
		var carOwnerEmail =$("#carOwnerEmail").val();
		var remark = $("#remark").val();
		var vehicleBrand = $("#vehicleBrand").val();
		var startDate = $("#startDate").datetimebox('getValue');
		var endDate = $("#endDate").datetimebox('getValue');	
		var monthPayAmount = $("#monthPayAmount").val();
		if (flag != "2"&&$.trim(carNumber) == "") {
			$.messager.alert("提示 ", "请输入车牌号");
			return false;
		}
		if (flag == "-1") {
			$('#save').linkbutton('disable');
			$.post("${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=addMonthVehicleBrand", {
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
				startDate : startDate,
				endDate : endDate,
				monthPayAmount : monthPayAmount
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}else if(flag == "2"){
			var refundMoney = $("#refundMoney").val();
			var endDate = $("#n_endDate").datetimebox('getValue');
			var vehicleBrandType = $("#r_vehicleBrandType").combobox('getValue');
			var vehiclePlace = $("#r_vehiclePlace").val();
			var carOwnerName = $("#r_carOwnerName").val();
			if ($.trim(refundMoney) == "") {
				$.messager.alert("提示 ", "请输入退款金额");
				return false;
			}
			if ($.trim(endDate) == "") {
				$.messager.alert("提示 ", "请设置新的到期日期");
				return false;
			}
			$('#r_save').linkbutton('disable');
			$.post("${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=returnMonthVehicleBrand", {
				vehicleBrandType : vehicleBrandType,
				vehiclePlace : vehiclePlace,
				carOwnerName : carOwnerName,
				endDate : endDate,
				refundMoney : refundMoney,
				monthVehicleBrandId:monthVehicleBrandId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			$.post("${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=editMonthVehicleBrand", {
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
				startDate : startDate,
				endDate : endDate,
				monthPayAmount : monthPayAmount,
				monthVehicleBrandId:monthVehicleBrandId
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}

	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$('#r_save').linkbutton('enable');
		$.close();
		$.search();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$('#r_save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#markSave").window('close');
		$("#editSave").window('close');
	};
	
	function monthVehicleBrandExport() {
		var carNumber = $('#s_carNumber').val();
		var carOwnerName = $('#s_carOwnerName').val();
		var inStatus =  $("#s_inStatus").combobox('getValue');
		var isExpire =  $("#s_isExpire").combobox('getValue');
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if (carOwnerName == null || $.trim(carOwnerName) == '-1') {
			carOwnerName = "";
		}
		if (inStatus == null || $.trim(inStatus) == '-1') {
			inStatus = "";
		}
		if (isExpire == null || $.trim(isExpire) == '-1') {
			isExpire = "";
		}
		$.getToPost('${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=monthVehicleBrandExport', {
			carNumber : carNumber,
			carOwnerName : carOwnerName,
			inStatus : inStatus,
			isExpire : isExpire
		});
	}
	
	$.search = function() {
		var carNumber = $('#s_carNumber').val();
		var carOwnerName = $('#s_carOwnerName').val();
		var inStatus =  $("#s_inStatus").combobox('getValue');
		var isExpire =  $("#s_isExpire").combobox('getValue');
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		if (carOwnerName == null || $.trim(carOwnerName) == '-1') {
			carOwnerName = "";
		}
		if (inStatus == null || $.trim(inStatus) == '-1') {
			inStatus = "";
		}
		if (isExpire == null || $.trim(isExpire) == '-1') {
			isExpire = "";
		}
		$('#search').datagrid({
			title : '月卡车牌管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=getFreeVehicleBrand",
			queryParams : {
				carNumber : carNumber,
				carOwnerName : carOwnerName,
				inStatus : inStatus,
				isExpire : isExpire
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
		          		return '小型车';
		          	}else if(value == 2){
		          		return '中型车';
		          	}else if(value == 3){
		          		return '大型车';
		          	}else if(value == 4){
		          		return '摩托车';
		          	}else if(value == 5){
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
				field : "startDate",
				title : "起始日期",
				width : 100,
				align : "center",
				sortable : true,
				formatter : function(value){
					if(value!=null){
						var date = new Date(value);
	                    var y = date.getFullYear();
	                    var m = date.getMonth() + 1;
	                    var d = date.getDate();
	                    return y + '-' +m + '-' + d;
					}
                }
			}, {
				field : "endDate",
				title : "终止日期",
				width : 100,
				align : "center",
				sortable : true,
				formatter : function(value){
                    if(value!=null){
                    	var date = new Date(value);
	                    var y = date.getFullYear();
	                    var m = date.getMonth() + 1;
	                    var d = date.getDate();
	                    return y + '-' +m + '-' + d;
                    }
                }
			}, {
				field : "monthPayAmount",
				title : "当期缴纳金额",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "isExpire",
				title : "是否逾期",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "inStatus",
				title : "车辆在场状态",
				width : 150,
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
				text : '月卡缴费',
				iconCls : 'icon-add',
				handler : function() {
					$.openWin(-1);
				}
			},
			'-', {
				id : 'btncut',
				text : '月卡退费',
				iconCls : 'icon-redo',
				handler : function() {
					$.openWin(2);
				}
			},
			'-', {
				id : 'btncut',
				text : '月卡编辑',
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
		$("#s_inStatus").combobox('select', '');
		$("#s_isExpire").combobox('select', '');
	}
	
	$.upload = function(){
		var name = $("#upload_file").val();
		if(name==""){
			 $.messager.alert("提示","请选择文件！");
	    	   return;
		}
		if(!(name.endWith(".xls"))&&!(name.endWith(".xlsx"))){
	    	$.messager.alert("提示","文件类型不正确，请重新选择！");
	    	return;
	    }
		$.ajaxFileUpload({
		       url:"${ctx}/monthVehicleBrand/monthVehicleBrand.do?method=monthVehicleBrandImport",
		       secureuri:false,
		       fileElementId:'upload_file',
		       dataType: 'json',
		       success:function(data,textStatus) {
		       	   var jsonData = jQuery.parseJSON(jQuery(data).text());
		    	   $.messager.alert("提示",jsonData.message);
		    	   $.search();
		       }
		    }
		);
	};
	
	//判断文件类型
	String.prototype.endWith=function(oString){
		var reg=new RegExp(oString+"$");  
		return reg.test(this);     
	};
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
			<td>是否在场：</td>
			<td>
				<select class="easyui-combobox" id="s_inStatus" name="s_inStatus"  style="width: 55px;" editable="false">
					<option value="" selected="selected"></option>
					<option value="1">在场</option>
					<option value="0">离场</option>
				</select>
			</td>
			<td>是否逾期：</td>
			<td>
				<select class="easyui-combobox" id="s_isExpire" name="s_isExpire"  style="width: 55px;" editable="false">
					<option value="" selected="selected"></option>
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.search()">查询</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="reset()">重置</a></td>
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="monthVehicleBrandExport()">导出</a></td>
			<td>
				<input type="hidden" name='textfield' id='textfield' class='txt' />
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="$.upload()">导入</a>
				<input type="file" name="upload_file" class="file" id="upload_file" size="13"/>
			</td>
		</tr>
	</table>
	<table id="search"></table>
	<div id="markSave" class="easyui-window" title="免费车录入" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true" closable="false"
		style="width: 730px; height: 555px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="monthVehicleBrandId" name="monthVehicleBrandId"/>
				<table style="width: 100%;">
					<tr>
						<td align="right"><span style="color: red">*</span>车牌号码：</td>
						<td align="left"><input type="text" name="carNumber" id="carNumber" /></td>
						
						<td align="right"><span style="color: red">*</span>车牌类型：</td>
						<td align="left">
							<select class="easyui-combobox" id="vehicleBrandType" name="vehicleBrandType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">小型车</option>
									<option value="2">中型车</option>
									<option value="3">大型车</option>
									<option value="4">摩托车</option>
									<option value="5">其他</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>缴费金额：</td>
						<td align="left">
							<input type="text" name="monthPayAmount" id="monthPayAmount" />
						</td>
						<td align="right"><span style="color: red">*</span>停车位置：</td>
						<td align="left"><input type="text" name="vehiclePlace" id="vehiclePlace" maxlength="300" /></td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>起始日期：</td>
						<td align="left">
							<input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;" />
						</td>
						<td align="right"><span style="color: red">*</span>到期日期：</td>
						<td align="left"><input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;" /></td>
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
						<td align="right">电子邮件：</td>
						<td align="left">
							<input type="text" name="carOwnerEmail" id="carOwnerEmail" />
						</td>
					</tr>
					
					<tr>
						<td align="right">车辆品牌：</td>
						<td align="left">
							<input type="text" name="vehicleBrand" id="vehicleBrand" />
						</td>
						<td align="right"></td>
						<td align="left">
						</td>
					</tr>
									
					<tr>
						<td align="right">备注：</td>
						<td align="left">
							<textarea rows="5" cols="20" name="remark" id="remark" ></textarea>
						</td>
						<td align="right"></td>
						<td align="left">
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
	
	<div id="editSave" class="easyui-window" title="月卡退费" closed=true cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true" closable="false"
		style="width: 730px; height: 555px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="r_monthPayAmount" name="r_monthPayAmount"/>
				<table style="width: 100%;">
					<tr>
						<td align="right"><span style="color: red">*</span>车牌号码：</td>
						<td align="left"><input type="text" name="r_carNumber" id="r_carNumber" disabled="disabled"/></td>
						<td align="right"><span style="color: red">*</span>车牌类型：</td>
						<td align="left">
							<select class="easyui-combobox" id="r_vehicleBrandType" name="r_vehicleBrandType" style="width: 155px;" editable="false">
									<option value="" selected="selected">--请选择--</option>
									<option value="1">小型车</option>
									<option value="2">中型车</option>
									<option value="3">大型车</option>
									<option value="4">摩托车</option>
									<option value="5">其他</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>退费金额：</td>
						<td align="left">
							<input type="text" name="refundMoney" id="refundMoney" />
						</td>
						<td align="right"><span style="color: red">*</span>停车位置：</td>
						<td align="left"><input type="text" name="r_vehiclePlace" id="r_vehiclePlace" maxlength="300"/></td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>原到期日：</td>
						<td align="left">
							<input class="easyui-datebox" name="r_endDate" id="r_endDate" style="width: 150px;" disabled="disabled"/>
						</td>
						<td align="right"><span style="color: red">*</span>车主姓名：</td>
						<td align="left"><input type="text" name="r_carOwnerName" id="r_carOwnerName" />
						</td>
					</tr>
					
					<tr>
						<td align="right"><span style="color: red">*</span>新设到期日期：</td>
						<td align="left">
							<input class="easyui-datebox" name="n_endDate" id="n_endDate" style="width: 150px;"/>
						</td>
						<td align="right"></td>
						<td align="left"></td>
					</tr>
					
					<tr height="54px">
						<td align="center" colspan="6"><a name="r_save" id="r_save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
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

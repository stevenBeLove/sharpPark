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
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString()
					.trim()%>';
var sysId='<%=session.getAttribute(ConstantUtils.SYSTEMID).toString()
					.trim()%>';
var parentagencyId='<%=session.getAttribute(ConstantUtils.PARENTAGENCYID)
					.toString().trim()%>';
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
		$.viewRuleSet();
	});

	function check() {
		if (roleid != "1") {
			return true;
		}
		return false;
	}
	
	var flag;
	$.openWin = function(obj) {
		$("#parkRuleSetId").val('');
		$("#ruleName").val('');
		$("#vehicleType").combobox('select', '0');
		$("#dayLimit").val('');
		$("#startHour").val('');
		$("#endHour").val('');
		$("input[name='dateSet']").prop("checked", "");
		$("#dateSetS").combobox('select', '11');
		$("input[name='chargeType']").prop("checked", "");
		$("#freeTime").val('');
		$("#subCharge").val('');
		$("#timeSlotLimit").val('');
		$("#startChargeTime").val('');
		$("#startChargePrice").val('');
		$("#chargeTime").val('');
		$("#chargePrice").val('');
		$("input[name='nChargeType']").prop("checked", "");
		$("#nFreeTime").val('');
		$("#nSubCharge").val('');
		$("#nTimeSlotLimit").val('');
		$("#nStartChargeTime").val('');
		$("#nStartChargePrice").val('');
		$("#nChargeTime").val('');
		$("#nChargePrice").val('');
		$("#nStartHour").val('');
		$("#nEndHour").val('');
		$("#type").val('');
		$("#subFreeTime").val('');
		$("#nSubFreeTime").val('');
		$("#status").combobox('select', '1');
		var rows = $('#viewRuleSet').datagrid('getSelections');
		var len = rows.length;
		$('#markRuleSet').window({
			title : '添加停车场基础信息'
		});
		flag = obj;
		if (flag != "-1") {
			$('#markRuleSet').window({title: '修改停车场基础信息'});
			var rows = $('#viewRuleSet').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条记录修改");
				return;
			}else if(rows.length ==0){
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的记录");
				return;
			}
			var row = $('#viewRuleSet').datagrid('getSelected');
			$("#parkRuleSetId").val(row.parkRuleSetId);
			$("#ruleName").val(row.ruleName);
			$("#vehicleType").combobox('select',row.vehicleType);
			$("#dayLimit").val(row.dayLimit);
			$("#startHour").val(row.startHour);
			$("#endHour").val(row.endHour);
			
			var dateSet = row.dateSet;
			if(dateSet>3){
				$("input[name='dateSet'][value=99]").prop("checked", "checked");
				$("#dateSetS").combobox('select',row.dateSet);
			}else{
				$("input[name='dateSet'][value='" + row.dateSet + "']").prop("checked", "checked");
			}
			//$("#chargeType").val(row.chargeType);
			//$("input[name='chargeType'][value=row.chargeType]").attr("checked",true);
			$("input[name='chargeType'][value='" + row.chargeType + "']").prop("checked",true);
			$("#freeTime").val(row.freeTime);
			$("#subCharge").val(row.subCharge);
			$("#timeSlotLimit").val(row.timeSlotLimit);
			$("#startChargeTime").val(row.startChargeTime);
			$("#startChargePrice").val(row.startChargePrice);
			$("#chargeTime").val(row.chargeTime);
			$("#chargePrice").val(row.chargePrice);
			//$("#nChargeType").val(row.nChargeType);
			$("input[name='nChargeType'][value='" + row.nChargeType + "']").prop("checked", "checked");
			$("#nFreeTime").val(row.nFreeTime);
			$("#nSubCharge").val(row.nSubCharge);
			$("#nTimeSlotLimit").val(row.nTimeSlotLimit);
			$("#nStartChargeTime").val(row.nStartChargeTime);
			$("#nStartChargePrice").val(row.nStartChargePrice);
			$("#nChargeTime").val(row.nChargeTime);
			$("#nChargePrice").val(row.nChargePrice);
			$("#nStartHour").val(row.nStartHour);
			$("#nEndHour").val(row.nEndHour);
			$("#type").val(row.type);
			$("#subFreeTime").val(row.subFreeTime);
			$("#nSubFreeTime").val(row.nSubFreeTime);
			$("#status").combobox('select', row.status);
		}
		$("#markRuleSet").window('open').window('refresh');
	};

	$.RuleSetSave = function() {
		var parkRuleSetId = $("#parkRuleSetId").val();
		var ruleName = $("#ruleName").val();
		var vehicleType = $("#vehicleType").combobox('getValue');
		var dayLimit = $("#dayLimit").val();
		var startHour = $("#startHour").val();
		var endHour = $("#endHour").val();
		var dateSetS = $("#dateSetS").combobox('getValue');
		var dateSet = $("input[name='dateSet']:checked").val();
		if(dateSet==99){
			dateSet = dateSetS;
		}
		var chargeType = $("input[name='chargeType']:checked").val();
		var freeTime = $("#freeTime").val();
		var subCharge = $("#subCharge").val();
		var timeSlotLimit = $("#timeSlotLimit").val();
		var startChargeTime = $("#startChargeTime").val();
		var startChargePrice = $("#startChargePrice").val();
		var chargeTime = $("#chargeTime").val();
		var chargePrice = $("#chargePrice").val();
		var nChargeType = $("input[name='nChargeType']:checked").val();
		var nFreeTime = $("#nFreeTime").val();
		var nSubCharge = $("#nSubCharge").val();
		var nTimeSlotLimit = $("#nTimeSlotLimit").val();
		var nStartChargeTime = $("#nStartChargeTime").val();
		var nStartChargePrice = $("#nStartChargePrice").val();
		var nChargeTime = $("#nChargeTime").val();
		var nChargePrice = $("#nChargePrice").val();
		var nStartHour = $("#nStartHour").val();
		var nEndHour = $("#nEndHour").val();
		var type = $("#type").val();
		var subFreeTime = $("#subFreeTime").val();
		var nSubFreeTime =$("#nSubFreeTime").val();
		var status = $("#status").combobox('getValue');
		if ($.trim(ruleName) == "") {
			$.messager.alert("提示 ", "请输入策略名称");
			return false;
		}
		if ($.trim(dateSet) == "") {
			$.messager.alert("提示 ", "请输入日期设定");
			return false;
		}
		if(dateSet==99){
			if ($.trim(dateSetS) == "") {
				$.messager.alert("提示 ", "请选择自定义星期");
				return false;
			}
		}
		if ($.trim(chargeType) == "") {
			$.messager.alert("提示 ", "请输入日间计费方式");
			return false;
		}
		if ($.trim(nChargeType) == "") {
			$.messager.alert("提示 ", "请输入夜间计费方式");
			return false;
		}
		if ($.trim(type) == "") {
			type = "2";
		}
		if (flag == "-1") {
			$('#save').linkbutton('disable');
			$.post("${ctx}/parkRuleSet/parkRuleSet.do?method=addParkRule", {
				ruleName:ruleName,
				vehicleType : vehicleType,
				dayLimit : dayLimit,
				startHour : startHour,
				endHour : endHour,
				dateSet : dateSet,
				chargeType : chargeType,
				freeTime : freeTime,
				subCharge : subCharge,
				timeSlotLimit : timeSlotLimit,
				startChargeTime : startChargeTime,
				startChargePrice : startChargePrice,
				chargeTime : chargeTime,
				chargePrice : chargePrice,
				nChargeType : nChargeType,
				nFreeTime : nFreeTime,
				nSubCharge : nSubCharge,
				nTimeSlotLimit : nTimeSlotLimit,
				nStartChargeTime : nStartChargeTime,
				nStartChargePrice : nStartChargePrice,
				nChargeTime : nChargeTime,
				nChargePrice : nChargePrice,
				nStartHour : nStartHour,
				nEndHour : nEndHour,
				type : type,
				subFreeTime : subFreeTime,
				nSubFreeTime : nSubFreeTime,
				status:status
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		} else {
			$.post("${ctx}/parkRuleSet/parkRuleSet.do?method=editParkRule", {
				parkRuleSetId:parkRuleSetId,
				ruleName:ruleName,
				vehicleType : vehicleType,
				dayLimit : dayLimit,
				startHour : startHour,
				endHour : endHour,
				dateSet : dateSet,
				chargeType : chargeType,
				freeTime : freeTime,
				subCharge : subCharge,
				timeSlotLimit : timeSlotLimit,
				startChargeTime : startChargeTime,
				startChargePrice : startChargePrice,
				chargeTime : chargeTime,
				chargePrice : chargePrice,
				nChargeType : nChargeType,
				nFreeTime : nFreeTime,
				nSubCharge : nSubCharge,
				nTimeSlotLimit : nTimeSlotLimit,
				nStartChargeTime : nStartChargeTime,
				nStartChargePrice : nStartChargePrice,
				nChargeTime : nChargeTime,
				nChargePrice : nChargePrice,
				nStartHour : nStartHour,
				nEndHour : nEndHour,
				type : type,
				subFreeTime : subFreeTime,
				nSubFreeTime : nSubFreeTime,
				status:status
			}, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
		}

	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
		$.viewRuleSet();
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');

	};
	$.close = function() {
		$.hideDivShade();
		$("#markRuleSet").window('close');
	};
	$.viewRuleSet = function() {
		var carNumber = $('#carNumber').val();
		if (carNumber == null || $.trim(carNumber) == '-1') {
			carNumber = "";
		}
		$('#viewRuleSet').datagrid({
			title : '收费策略管理',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/parkRuleSet/parkRuleSet.do?method=getParkRuleSet",
			queryParams : {
				carNumber : carNumber
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{field : "ck",checkbox : true
			}, {
				field : "ruleName",
				title : "策略名称",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "vehicleType",
				title : "车辆类型",
				width : 100,
				align : "center",
				sortable : true,
				formatter:function(value,row,index){
		          	if(value == 0){
		          		return '全部';
		          	}else if(value == 1){
		          		return '小型车';
		          	}else if(value == 2){
		          		return '中型车';
		          	}else if(value == 3){
		          		return '大型车';
		          	}else if(value == 4){
		          		return '摩托车';
		          	}else if(value == 5){
		          		return '其他';
		          	}else{
		          		return '全部';
		          	}
		        }
			}, {
				field : "dateSet",
				title : "日期设定",
				width : 100,
				align : "center",
				sortable : true,
				formatter:function(value,row,index){
		          	if(value == 1){
		          		return '每周七日';
		          	}else if(value == 2){
		          		return '周一~周五';
		          	}else if(value == 3){
		          		return '周六周日';
		          	}else if(value == 11){
		          		return '周一';
		          	}else if(value == 12){
		          		return '周二';
		          	}else if(value == 13){
		          		return '周三';
		          	}else if(value == 14){
		          		return '周四';
		          	}else if(value == 15){
		          		return '周五';
		          	}else if(value == 16){
		          		return '周六';
		          	}else if(value == 17){
		          		return '周日';
		          	}else{
		          		return '未知';
		          	}
		        }
			}, {
				field : "dayLimit",
				title : "每日限额",
				width : 150,
				align : "center",
				sortable : true,
				formatter:function(value,row,index){
		          	if(value == 0){
		          		return '不限制';
		          	}else{
		          		return value;
		          	}
		        }
			}, {
				field : "startHourAndEndHour",
				title : "时间设定",
				width : 150,
				align : "center",
				sortable : true
			}, {
				field : "chargeTypeDesc",
				title : "计费方式",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "ruleDesc",
				title : "策略说明",
				width : 450,
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
				field : "parkRuleSetId",
				field : "startHour",
				field : "endHour",
				field : "chargeType",
				field : "freeTime",
				field : "subCharge",
				field : "timeSlotLimit",
				field : "startChargeTime",
				field : "startChargePrice",
				field : "chargeTime",
				field : "chargePrice",
				field : "status",
				field : "nChargeType",
				field : "nFreeTime",
				field : "nSubCharge",
				field : "nTimeSlotLimit",
				field : "nStartChargeTime",
				field : "nStartChargePrice",
				field : "nChargeTime",
				field : "nChargePrice",
				field : "nStartHour",
				field : "nEndHour",
				field : "type"
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
		var p = $('#viewRuleSet').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	
	function resetOrder(){
		$('#carNumber').val('');
	}
</script>
<style type="text/css">
#Layer1 {
	position: absolute;
	width: 583px;
	height: 123px;
	z-index: 1;
	left: 11px;
	top: 16px;
}

#Layer2 {
	position: absolute;
	width: 309px;
	height: 77px;
	z-index: 2;
	left: 272px;
	top: 33px;
	border: 1px solid;
}

#Layer3 {
	position: absolute;
	width: 583px;
	height: 242px;
	z-index: 2;
	left: 11px;
	top: 135px;
	border: 1px solid;
}

#Layer4 {
	position: absolute;
	width: 272px;
	height: 165px;
	z-index: 3;
	left: 6px;
	top: 35px;
}

#Layer5 {
	position: absolute;
	width: 270px;
	height: 27px;
	z-index: 1;
	left: 2px;
	top: 2px;
}

#Layer6 {
	position: absolute;
	width: 240px;
	height: 167px;
	z-index: 2;
	left: 2px;
	top: 31px;
	border: 1px solid;
}

#Layer7 {
	position: absolute;
	width: 165px;
	height: 162px;
	z-index: 3;
	left: 281px;
	top: 34px;
}

#Layer8 {
	position: absolute;
	width: 167px;
	height: 27px;
	z-index: 1;
	left: -2px;
	top: 3px;
}

#Layer9 {
	position: absolute;
	width: 165px;
	height: 156px;
	z-index: 2;
	left: -1px;
	top: 30px;
	border: 1px solid;
}

#Layer10 {
	position: absolute;
	width: 133px;
	height: 162px;
	z-index: 3;
	left: 450px;
	top: 38px;
}

#Layer11 {
	position: absolute;
	width: 133px;
	height: 28px;
	z-index: 1;
	left: 1px;
}

#Layer12 {
	position: absolute;
	width: 585px;
	height: 246px;
	z-index: 3;
	top: 396px;
	border: 1px solid;
}

span.title {
	display: block;
	width: 90px;
	height: 16px;
	position: relative;
	top: -12px;
	text-align: center;
	background: white;
	font-size: 13px;
}

#Layer13 {
	position: absolute;
	width: 583px;
	height: 242px;
	z-index: 2;
	left: 11px;
	top: 7px;
}
#Layer14 {
	position: absolute;
	width: 583px;
	height: 242px;
	z-index: 2;
	left: 11px;
	top: 660px;
}
</style>
</head>
<body id="indexd">
	<table id="viewRuleSet"></table>

	<div id="markRuleSet" class="easyui-window" title="添加收费策略" closed=true
		cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true"
		closable="false"
		style="width: 730px; height: 750px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="parkRuleSetId" name="parkRuleSetId" />
				<div id="Layer1">
					策略名称： <label> <input id="ruleName" type="text" name="ruleName" />
					</label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					是否有效：
					<select class="easyui-combobox" id="status" name="status" style="width: 80px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
					</select>
					<p>
						车型选择： <label> <select class="easyui-combobox" id="vehicleType" name="vehicleType" style="width: 100px;">
								<option value="0" >全部</option>
								<option value="1">小型车</option>
								<option value="2">中型车</option>
								<option value="3">大型车</option>
								<option value="4">摩托车</option>
								<option value="5">其他</option>
						</select>
						</label>
					</p>
					<p>
						每日限额： <label> <input id="dayLimit" name="dayLimit" type="text"
							size="3" />
						</label> 元
					</p>
					<div id="Layer2">
						<span class="title">日期设定</span> <label> <input
							type="radio" name="dateSet" value="1" /> 每周七日
						</label> <label> <input type="radio" name="dateSet"
							value="3" /> 周六周日
						</label>
						<p>
							<label> <input type="radio" name="dateSet"
								value="2" /> 周一~周五
							</label> <label> <input type="radio" name="dateSet"
								value="99" /> 自定义
							</label> <label> <select class="easyui-combobox" id="dateSetS" name="dateSetS" style="width: 100px;">
									<option value="11">周一</option>
									<option value="12">周二</option>
									<option value="13">周三</option>
									<option value="14">周四</option>
									<option value="15">周五</option>
									<option value="16">周六</option>
									<option value="17">周日</option>
							</select>
							</label>
						</p>
					</div>
					<p>&nbsp;</p>
				</div>
				<div id="Layer3">
					<span class="title">日间收费方式</span>
					<div id="Layer13">
						<label>起始当日 <select id="startHour" name="startHour">
								<option value="8:00">8:00</option>
						</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</label> <label>截至当日 <select id="endHour" name="endHour">
							<option value="20:00">20:00</option>
						</select>
						</label>
					</div>
					<div id="Layer4">
						<div id="Layer5">
							<label> <input type="radio" name="chargeType"
								value="1" /> 按时长计算
							</label>
						</div>
						<div id="Layer6">
						<span class="title">计时收费策略</span>
							<p>
								免费停车时长 <input id="freeTime" name="freeTime" type="text" size="1" /> 分钟
							</p>
							<p>
								前 <input id="startChargeTime" name="startChargeTime" type="text" size="1" /> 分钟，收费 <input
									id="startChargePrice" name="startChargePrice" type="text" size="1" /> 元
							</p>
							<p>
								以后每 <input id="chargeTime" name="chargeTime" type="text" size="1" /> 分钟，收费 <input
									id="chargePrice" name="chargePrice" type="text" size="1" /> 元
							</p>
							<p>
								该时段收费限额为 <input id="timeSlotLimit" name="timeSlotLimit" type="text" size="1" /> 元
							</p>
						</div>
					</div>
					<p>&nbsp;</p>
					<div id="Layer10">
						<div id="Layer11">
							<label> <input type="radio" name="chargeType"
								value="3" /> 免费
							</label>
						</div>
					</div>
					<div id="Layer7">
						<div id="Layer8">
							<label> <input type="radio" name="chargeType"
								value="2" /> 按次计算
							</label>
						</div>
						<div id="Layer9">
							<span class="title">计次收费策略</span>
							<p>
								免费停车时长 <input id="subFreeTime" name="subFreeTime" type="text" size="1" /> 分钟
							</p>
							<p>
								每次停车收费 <input id="subCharge" name="subCharge" type="text" size="1" /> 元
							</p>
						</div>
					</div>
				</div>
				<div id="Layer12">
					<span class="title">夜间收费方式</span>
					<div id="Layer13">
						<label>起始当日 <select id="nStartHour" name="nStartHour">
								<option value="20:00">20:00</option>
						</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</label> <label>截至次日 <select id="nEndHour" name="nEndHour">
							<option value="8:00">8:00</option>
						</select>
						</label>
					</div>
					<div id="Layer4">
						<div id="Layer5">
							<label> <input type="radio" name="nChargeType"
								value="1" /> 按时长计算
							</label>
						</div>
						<div id="Layer6">
						<span class="title">计时收费策略</span>
							<p>
								免费停车时长 <input id="nFreeTime" name="nSubFreeTime" type="text" size="1" /> 分钟
							</p>
							<p>
								前 <input id="nStartChargeTime" name="nStartChargeTime" type="text" size="1" /> 分钟，收费 <input
									id="nStartChargePrice" name="nStartChargePrice" type="text" size="1" /> 元
							</p>
							<p>
								以后每 <input id="nChargeTime" name="nChargeTime" type="text" size="1" /> 分钟，收费 <input
									id="nChargePrice" name="nChargePrice" type="text" size="1" /> 元
							</p>
							<p>
								该时段收费限额为 <input id="nTimeSlotLimit" name="nTimeSlotLimit" type="text" size="1" /> 元
							</p>
						</div>
					</div>
					<p>&nbsp;</p>
					<div id="Layer10">
						<div id="Layer11">
							<label> <input type="radio" name="nChargeType"
								value="3" /> 免费
							</label>
						</div>
					</div>
					<div id="Layer7">
						<div id="Layer8">
							<label> <input type="radio" name="nChargeType"
								value="2" /> 按次计算
							</label>
						</div>
						<div id="Layer9">
						<span class="title">计次收费策略</span>
							<p>
								免费停车时长 <input id="nSubFreeTime" name="nSubFreeTime" type="text" size="1" /> 分钟
							</p>
							<p>
								每次停车收费 <input id="nSubCharge" name="nSubCharge" type="text" size="1" /> 元
							</p>
						</div>
					</div>
				</div>
				<div id="Layer14" align="center"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
							onclick="$.RuleSetSave()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close"
							id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></div>
			</div>
		</div>
	</div>
</body>
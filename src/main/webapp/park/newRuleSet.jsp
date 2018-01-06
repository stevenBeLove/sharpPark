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
		divByType(0);
		cleanFrom(1);
		cleanFrom(2);
		cleanFrom(3);
		cleanFrom(4);
		cleanFrom(5);
		cleanFrom(6);
		var rows = $('#viewRuleSet').datagrid('getSelections');
		var len = rows.length;
		$('#markRuleSet').window({
			title : '添加收费策略'
		});
		flag = obj;
		if (flag != "-1") {
			$('#markRuleSet').window({title: '修改收费策略'});
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
			var type = row.type;
			$("#parkRuleSetId").val(row.parkRuleSetId);
			$("#ruleName").val(row.ruleName);
			$("#vehicleType").combobox('select',row.vehicleType);
			$("input[name='type'][value='" + row.type + "']").prop("checked", "checked");
			$("#status").combobox('select', row.status);
			
			if(type!='1'&&type!='0'&&type!='2'&&type!='3'&&type!='6'){
				$("#startHour"+type+"").combobox('select',row.startHour);
				$("#endHour"+type+"").combobox('select',row.endHour);
				if(type!='5'){
					$("#nStartHour"+type+"").combobox('select',row.nStartHour);
					$("#nEndHour"+type+"").combobox('select',row.nEndHour);
				}
			}
			
			$("#dayLimit"+type+"").val(row.dayLimit);
			//$("#chargeType").val(row.chargeType);
			//$("input[name='chargeType'][value=row.chargeType]").attr("checked",true);
			$("input[name='chargeType"+type+"'][value='" + row.chargeType + "']").prop("checked",true);
			$("#freeTime"+type+"").val(row.freeTime);
			$("#subCharge"+type+"").val(row.subCharge);
			$("#timeSlotLimit"+type+"").val(row.timeSlotLimit);
			$("#startChargeTime"+type+"").val(row.startChargeTime);
			$("#startChargePrice"+type+"").val(row.startChargePrice);
			$("#chargeTime"+type+"").val(row.chargeTime);
			$("#chargePrice"+type+"").val(row.chargePrice);
			//$("#nChargeType").val(row.nChargeType);
			$("input[name='nChargeType"+type+"'][value='" + row.nChargeType + "']").prop("checked", "checked");
			$("#nFreeTime"+type+"").val(row.nFreeTime);
			$("#nSubCharge"+type+"").val(row.nSubCharge);
			$("#nTimeSlotLimit"+type+"").val(row.nTimeSlotLimit);
			$("#nStartChargeTime"+type+"").val(row.nStartChargeTime);
			$("#nStartChargePrice"+type+"").val(row.nStartChargePrice);
			$("#nChargeTime"+type+"").val(row.nChargeTime);
			$("#nChargePrice"+type+"").val(row.nChargePrice);
			$("#subFreeTime"+type+"").val(row.subFreeTime);
			$("#nSubFreeTime"+type+"").val(row.nSubFreeTime);
			divByType(row.type);
		}
		$("#markRuleSet").window('open').window('refresh');
	};

	$.RuleSetSave = function() {
		var type = $("input[name='type']:checked").val();
		var ruleName = $("#ruleName").val();
		var vehicleType = $("#vehicleType").combobox('getValue');
		if (type == "") {
			$.messager.alert("提示 ", "请选择计费方式");
			return false;
		}
		var parkRuleSetId = $("#parkRuleSetId").val();
		var dayLimit = $("#dayLimit"+type+"").val();
		var startHour="";
		var endHour="";
		var nStartHour="";
		var nEndHour="";
		if(type!='1'&&type!='0'&&type!='2'&&type!='3'&&type!='6'){
			startHour = $("#startHour"+type+"").combobox('getValue');
			endHour = $("#endHour"+type+"").combobox('getValue');
			if(type!='5'){
				nStartHour = $("#nStartHour"+type+"").combobox('getValue');
				nEndHour = $("#nEndHour"+type+"").combobox('getValue');
			}
		}
		var chargeType = $("input[name='chargeType"+type+"']:checked").val();
		var freeTime = $("#freeTime"+type+"").val();
		var subCharge = $("#subCharge"+type+"").val();
		var timeSlotLimit = $("#timeSlotLimit"+type+"").val();
		var startChargeTime = $("#startChargeTime"+type+"").val();
		var startChargePrice = $("#startChargePrice"+type+"").val();
		var chargeTime = $("#chargeTime"+type+"").val();
		var chargePrice = $("#chargePrice"+type+"").val();
		var nChargeType = $("input[name='nChargeType"+type+"']:checked").val();
		var nFreeTime = $("#nFreeTime"+type+"").val();
		var nSubCharge = $("#nSubCharge"+type+"").val();
		var nTimeSlotLimit = $("#nTimeSlotLimit"+type+"").val();
		var nStartChargeTime = $("#nStartChargeTime"+type+"").val();
		var nStartChargePrice = $("#nStartChargePrice"+type+"").val();
		var nChargeTime = $("#nChargeTime"+type+"").val();
		var nChargePrice = $("#nChargePrice"+type+"").val();
		var subFreeTime = $("#subFreeTime"+type+"").val();
		var nSubFreeTime =$("#nSubFreeTime"+type+"").val();
		var status = $("#status").combobox('getValue');
		if ($.trim(ruleName) == "") {
			$.messager.alert("提示 ", "请输入策略名称");
			return false;
		}
		if ($.trim(vehicleType) == "") {
			$.messager.alert("提示 ", "请选择车辆类型");
			return false;
		}
		if(type==5||type==4||type==6){
			if ($.trim(chargeType) == ""||$.trim(nChargeType) == "") {
				$.messager.alert("提示 ", "请选择策略类型");
				return false;
			}
		}
		if (flag == "-1") {
			$('#save').linkbutton('disable');
			$.post("${ctx}/parkRuleSet/parkRuleSet.do?method=addParkRule", {
				ruleName:ruleName,
				vehicleType : vehicleType,
				dayLimit : dayLimit,
				startHour : startHour,
				endHour : endHour,
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
	
	$(document).ready(function(){
		$("input[name='type']").click(function(){
			var type = $("input[name='type']:checked").val();
			divByType(type);
		});
		$("#startHour4").combobox({
			onChange: function (n,o) {
				var startHour = $("#startHour4").combobox('getValue');
				upnendHour4(startHour);
			}
		});
		
		$("#endHour4").combobox({
			onChange: function (n,o) {
				var endHour = $("#endHour4").combobox('getValue');
				upnstartHour4(endHour);
			}
		});
		
		$("#startHour5").combobox({
			onChange: function (n,o) {
				var startHour = $("#startHour5").combobox('getValue');
				$("#night1").html(startHour); 
			}
		});
		
		$("#endHour5").combobox({
			onChange: function (n,o) {
				var endHour = $("#endHour5").combobox('getValue');
				$("#night2").html(endHour); 
			}
		});
	});
	
	function divByType(obj){
		var type = obj;
		var remark = "";
		if(type==0){
			$("#workTypeDiv").hide();
			$("#durationDiv").hide();
			$("#timeCountDiv").hide();
			$("#pollingDiv").hide();
			$("#intervalDayDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#freeTypeDiv").show();
			remark = "在免费策略生效期间，所有出场的车辆均免费";
		}else if(type==1){
			$("#workTypeDiv").hide();
			$("#freeTypeDiv").hide();
			$("#timeCountDiv").hide();
			$("#pollingDiv").hide();
			$("#intervalDayDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#durationDiv").show();
			remark = "在每日(0:00~24:00)内按照停车时长计费"
		}else if(type==2){
			$("#workTypeDiv").hide();
			$("#freeTypeDiv").hide();
			$("#durationDiv").hide();
			$("#pollingDiv").hide();
			$("#intervalDayDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#timeCountDiv").show();
			remark = "在每日(0:00~24:00)内按照停车次数计费";
		}else if(type==3){
			$("#workTypeDiv").hide();
			$("#freeTypeDiv").hide();
			$("#durationDiv").hide();
			$("#timeCountDiv").hide();
			$("#intervalDayDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#pollingDiv").show();
			remark = "从车辆入场时刻起算，每24小时作为一个计费周期";
		}else if(type==4){
			$("#freeTypeDiv").hide();
			$("#durationDiv").hide();
			$("#timeCountDiv").hide();
			$("#pollingDiv").hide();
			$("#workTypeDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#intervalDayDiv").show();
			remark = "按照日间和夜间分别设置收费策略：在跨日时段内，按照\"日间起始时刻~次日的起始时刻\",作为一个收费周期";
		}else if(type==5){
			$("#freeTypeDiv").hide();
			$("#durationDiv").hide();
			$("#timeCountDiv").hide();
			$("#pollingDiv").hide();
			$("#intervalDayDiv").hide();
			$("#workTypeDiv").hide();
			
			$("#nowDayDiv").show();
			remark = "按照日间和夜间分别设置收费策略：在当日时段内，按照0:00~24:00，作为一个计费周期";
		}else if(type==6){
			$("#freeTypeDiv").hide();
			$("#durationDiv").hide();
			$("#timeCountDiv").hide();
			$("#pollingDiv").hide();
			$("#intervalDayDiv").hide();
			$("#nowDayDiv").hide();
			
			$("#workTypeDiv").show();
			remark = "按照周一~周五和周六~周日分别设置收费策略：在每日内，可以按照时长/次数/免费。";
		}
		$("#remark").html(remark); 
	}
	
	function upnendHour4(obj){
		if(obj=="00:00"){
			$("#nEndHour4").combobox('select',"24:00");
		}else{
			$("#nEndHour4").combobox('select',obj);
		}
	}
	
	function upnstartHour4(obj){
		$("#nStartHour4").combobox('select',obj);
	}
	
	function cleanFrom(obj){
		var type = obj;
		$("input[name='type'][value='0']").prop("checked", "checked");
		$("#parkRuleSetId").val('');
		$("#ruleName").val('');
		$("#vehicleType").combobox('select', '0');
		$("#dayLimit"+type+"").val('');
		$("#startHour"+type+"").val('');
		$("#endHour"+type+"").val('');
		$("input[name='chargeType"+type+"']").prop("checked", "");
		$("#freeTime"+type+"").val('');
		$("#subCharge"+type+"").val('');
		$("#timeSlotLimit"+type+"").val('');
		$("#startChargeTime"+type+"").val('');
		$("#startChargePrice"+type+"").val('');
		$("#chargeTime"+type+"").val('');
		$("#chargePrice"+type+"").val('');
		$("input[name='nChargeType"+type+"']").prop("checked", "");
		$("#nFreeTime"+type+"").val('');
		$("#nSubCharge"+type+"").val('');
		$("#nTimeSlotLimit"+type+"").val('');
		$("#nStartChargeTime"+type+"").val('');
		$("#nStartChargePrice"+type+"").val('');
		$("#nChargeTime"+type+"").val('');
		$("#nChargePrice"+type+"").val('');
		$("#nStartHour"+type+"").val('');
		$("#nEndHour"+type+"").val('');
		$("#subFreeTime"+type+"").val('');
		$("#nSubFreeTime"+type+"").val('');
		$("#status").combobox('select', '1');
	}
</script>
<style type="text/css">
#Layer1 {
	position:absolute;
	width:318px;
	height:72px;
	z-index:1;
	left: 12px;
	top: 17px;
}
#Layer2 {
	position:absolute;
	width:247px;
	height:163px;
	z-index:2;
	top: 127px;
}
#Layer3 {
	position:absolute;
	width:241px;
	height:160px;
	z-index:3;
	left: 0px;
	top: 38px;
	border: 1px solid;
}
#Layer4 {
	position:absolute;
	width:256px;
	height:115px;
	z-index:3;
	left: 9px;
	top: 329px;
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

#save1 {
	position: absolute;
	width: 583px;
	height: 242px;
	z-index: 2;
	left: 11px;
	top: 660px;
}

#Layer5 {
	position:absolute;
	width:577px;
	height:552px;
	z-index:4;
	left: 269px;
	top: 49px;
	border: 1px solid;
}
#Layer6 {
	position:absolute;
	width:554px;
	height:263px;
	z-index:1;
	border: 1px solid;
	margin-left:10px;
}
#Layer7 {
	position:absolute;
	width:734px;
	height:29px;
	z-index:1;
	margin-left:10px;
}
#Layer8 {
	position:absolute;
	width:735px;
	height:182px;
	z-index:2;
	top: 33px;
}
#Layer9 {
	position:absolute;
	width:255px;
	height:26px;
	z-index:1;
	margin-top:7px;
}
#Layer10 {
	position:absolute;
	width:200px;
	height:27px;
	z-index:2;
	left: 256px;
	top: -3px;
	margin-top:7px;
}
#Layer11 {
	position:absolute;
	width:200px;
	height:25px;
	z-index:3;
	left: 457px;
	top: -1px;
	margin-top:7px;
}
#Layer12 {
	position:absolute;
	width:229px;
	height:175px;
	z-index:4;
	left: 5px;
	top: 45px;
	border: 1px solid;
}
#Layer13 {
	position:absolute;
	width:200px;
	height:175px;
	z-index:5;
	left: 258px;
	top: 45px;
	border: 1px solid;
}
#Layer14 {
	position:absolute;
	width:554px;
	height:243px;
	z-index:2;
	top: 302px;
	border: 1px solid;
	margin-left:10px;
}
#Layer15 {
	position:absolute;
	width:258px;
	height:29px;
	z-index:1;
}
#Layer16 {
	position:absolute;
	width:200px;
	height:29px;
	z-index:2;
	left: 261px;
}
#Layer17 {
	position:absolute;
	width:200px;
	height:30px;
	z-index:3;
	left: 463px;
}
#Layer18 {
	position:absolute;
	width:233px;
	height:178px;
	z-index:4;
	left: 4px;
	top: 54px;
	border: 1px solid;
}
#Layer19 {
	position:absolute;
	width:200px;
	height:175px;
	z-index:5;
	left: 263px;
	top: 55px;
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
#Layer20 {
	position:absolute;
	width:368px;
	height:33px;
	z-index:5;
	left: 357px;
	top: 14px;
}
#New1 {
	position:absolute;
	width:577px;
	height:600px;
	z-index:1;
	left:269px;
	top:49px;
	border: 1px solid;
}
#New2 {
	position:absolute;
	width:588px;
	height:25px;
	z-index:1;
	margin-left:8px;
}
#New3 {
	position:absolute;
	width:558px;
	height:256px;
	z-index:2;
	left: 9px;
	top: 56px;
	border: 1px solid;
}
#New4 {
	position:absolute;
	width:200px;
	height:28px;
	z-index:3;
	left: 4px;
	top: 28px;
}
#New5 {
	position:absolute;
	width:200px;
	height:29px;
	z-index:1;
	left: 233px;
	top: 10px;
}
#New6 {
	position:absolute;
	width:200px;
	height:31px;
	z-index:2;
	left: 1px;
	top: 32px;
}
#New7 {
	position:absolute;
	width:200px;
	height:32px;
	z-index:3;
	left: 249px;
	top: 33px;
}
#New8 {
	position:absolute;
	width:181px;
	height:32px;
	z-index:4;
	left: 447px;
	top: 32px;
}
#New9 {
	position:absolute;
	width:231px;
	height:177px;
	z-index:2;
	left: 1px;
	top: 65px;
	border: 1px solid;
}
#New10 {
	position:absolute;
	width:200px;
	height:177px;
	z-index:5;
	left: 247px;
	top: 68px;
	border: 1px solid;
}
#New11 {
	position:absolute;
	width:558px;
	height:258px;
	z-index:4;
	top: 333px;
	left:9px;
	border: 1px solid;
}
#New12 {
	position:absolute;
	width:200px;
	height:29px;
	z-index:1;
	top:10px;
	margin-left:16px;
}
#New13 {
	position:absolute;
	width:200px;
	height:28px;
	z-index:2;
	left: 233px;
	top: 10px;
}
#New14 {
	position:absolute;
	width:200px;
	height:30px;
	z-index:3;
	left: 1px;
	top: 33px;
}
#New15 {
	position:absolute;
	width:200px;
	height:32px;
	z-index:4;
	left: 249px;
	top: 31px;
}
#New16 {
	position:absolute;
	width:180px;
	height:35px;
	z-index:5;
	left: 447px;
	top: 32px;
}
#New17 {
	position:absolute;
	width:231px;
	height:177px;
	z-index:6;
	left: 2px;
	top: 71px;
	border: 1px solid;
}
#New18 {
	position:absolute;
	width:200px;
	height:177px;
	z-index:7;
	left: 247px;
	top: 71px;
	border: 1px solid;
}
#New19 {
	position:absolute;
	width:200px;
	height:30px;
	z-index:6;
	top:10px;
	margin-left:16px;
}
#Now1 {
	position:absolute;
	width:566px;
	height:612px;
	z-index:1;
	border: 1px solid;
	left:269px;
	top:49px;
}
#Now2 {
	position:absolute;
	width:558px;
	height:27px;
	z-index:2;
	top:62px;
	margin-left:278px;
}
#Now3 {
	position:absolute;
	width:554px;
	height:272px;
	z-index:1;
	left: 3px;
	top: 51px;
	border: 1px solid;
}
#Now4 {
	position:absolute;
	width:200px;
	height:30px;
	z-index:2;
	left: -3px;
	top: 30px;
}
#Now5 {
	position:absolute;
	width:200px;
	height:30px;
	z-index:1;
	left: 196px;
	top: 16px;
}
#Now6 {
	position:absolute;
	width:171px;
	height:34px;
	z-index:2;
	left: 11px;
	top: 48px;
}
#Now7 {
	position:absolute;
	width:155px;
	height:35px;
	z-index:3;
	left: 253px;
	top: 48px;
}
#Now8 {
	position:absolute;
	width:200px;
	height:32px;
	z-index:4;
	left: 437px;
	top: 48px;
}
#Now9 {
	position:absolute;
	width:225px;
	height:177px;
	z-index:5;
	left: 8px;
	top: 86px;
	border: 1px solid;
}
#Now10 {
	position:absolute;
	width:184px;
	height:177px;
	z-index:6;
	left: 247px;
	top: 86px;
	border: 1px solid;
}
#Now11 {
	position:absolute;
	width:552px;
	height:218px;
	z-index:3;
	top: 289px;
	left: 2px;
}
#Now12 {
	position:absolute;
	width:553px;
	height:267px;
	z-index:-4;
	left: 4px;
	top: 338px;
	border: 1px solid;
}
#Now13 {
	position:absolute;
	width:171px;
	height:34px;
	z-index:1;
	left: 11px;
	top: 90px;
}
#Now14 {
	position:absolute;
	width:155px;
	height:36px;
	z-index:2;
	left: 253px;
	top: 90px;
}
#Now15 {
	position:absolute;
	width:200px;
	height:32px;
	z-index:3;
	left: 437px;
	top: 90px;
}
#Now16 {
	position:absolute;
	width:225px;
	height:177px;
	z-index:4;
	left: 8px;
	top: 129px;
	border:1px solid;
}
#Now17 {
	position:absolute;
	width:184px;
	height:177px;
	z-index:5;
	left: 247px;
	top: 129px;
	border:1px solid;
}
</style>
</head>
<body id="indexd">
	<table id="viewRuleSet"></table>

	<div id="markRuleSet" class="easyui-window" title="添加收费策略" closed=true
		cache="false" collapsible="false" zIndex="20px" minimizable="false"
		maximizable="false" resizable="false" draggable="true"
		closable="false"
		style="width: 930px; height: 750px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding: 10px; background: #fff; overflow: hidden;">
				<input type="hidden" id="parkRuleSetId" name="parkRuleSetId" />
				<div>
				  <div id="Layer1">
				    <p>策略名称：
				      <input id="ruleName" type="text" name="ruleName" />
				    </p>
				    <p>车辆选择：
				      <label>
				       <select class="easyui-combobox" id="vehicleType" name="vehicleType" style="width: 176px;">
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
				        是否有效：
					<select class="easyui-combobox" id="status" name="status" style="width: 80px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
					</select>
				        </p>
				  </div>
				</div>
				<div id="Layer2"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label style="font-size:15px;">计费方式</label>
				  <div id="Layer3">
				  <span class="title">收费方式</span>
				    <label>
				    <input type="radio" name="type" value="1"/>
				    按时长</label>
				    <label></label>
				    <input type="radio" name="type" value="2" />
				    按次数
				    <p>
				      <label>
				      <input type="radio" name="type" value="5" />
				按当日时段
				<input type="radio" name="type" value="4" />
				按跨日时段</label>
				    </p>
				    <p>
				      <label>
				      <input type="radio" name="type" value="3" /> 
				      按24小时轮询</label>
				      <label>
				      <input type="radio" name="type" value="6" /> 
				      按工作日/非工作日</label>
				    </p>
				    <p>
				      <label>
				      <input type="radio" name="type" value="0" />
				免费</label>
				    </p>
				  </div>
				</div>
				<div id="Layer4">
				  <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注说明</p>
				  <p><label id="remark"></label></p>
				</div>

				<!-- 工作日/非工作日 -->
				<div id="workTypeDiv" style="display: none;">
				<div id="Layer5">
					<span class="title">计费策略</span>
					  <div id="Layer6">
					  <span class="title">周一到周五</span>
					    <div id="Layer7">每日限额
					       <input id="dayLimit6" name="dayLimit6" type="text"
							size="3" />
					    元</div>
					    <div id="Layer8">
					      <div id="Layer9">
					        <label>
					        <input type="radio" name="chargeType6"
								value="1" />
					        按时长计算</label>
					      </div>
					      <div id="Layer10">
					        <label>
					        <input type="radio" name="chargeType6"
								value="2" />
					        按次计算</label>
					      </div>
					      <div id="Layer11">
					        <label>
					        <input type="radio" name="chargeType6"
								value="3" />
					        免费</label>
					      </div>
					      <div id="Layer12">
						  <span class="title">计时收费策略</span>
					        <p>免费停车时长
					           <input id="freeTime6" name="freeTime6" type="text" size="1" /> 
					          分钟</p>
					        <p>前
					          <input id="startChargeTime6" name="startChargeTime6" type="text" size="1" />
					          分钟，收费
					          <input id="startChargePrice6" name="startChargePrice6" type="text" size="1" />
					        元</p>
					        <p>以后每
					          <input id="chargeTime6" name="chargeTime6" type="text" size="1" />
					          分钟，收费
					          <input id="chargePrice6" name="chargePrice6" type="text" size="1" /> 
					        元</p>
					        <p>该时段收费限额为
					          <input id="timeSlotLimit6" name="timeSlotLimit6" type="text" size="1" />
					          元
					</p>
					      </div>
					      <div id="Layer13">
						  <span class="title">计次收费策略</span>
					        <p>免费停车时长
					          <input id="subFreeTime6" name="subFreeTime6" type="text" size="1" /> 
					          分钟</p>
					        <p>每次停车收费
					          <input id="subCharge6" name="subCharge6" type="text" size="1" />
					          元
					        </p>
					      </div>
					    </div>
					  </div>
					  <div id="Layer14">
					  <span class="title">周六到周日</span>
					    <div id="Layer15">
					      <label>
					      <input type="radio" name="nChargeType6"
								value="1" /> 
					      按时长计算</label>
					    </div>
					    <div id="Layer16">
					      <label>
					      <input type="radio" name="nChargeType6"
								value="2" />
					      按次数计算</label>
					    </div>
					    <div id="Layer17">
					      <label>
					      <input type="radio" name="nChargeType6"
								value="3" /> 
					      免费</label>
					    </div>
					    <div id="Layer18">
						<span class="title">计时收费策略</span>
					      <p>免费停车时长
					        <input id="nSubFreeTime6" name="nSubFreeTime6" type="text" size="1" />
					        分钟</p>
					      <p>前
					        <input id="nStartChargeTime6" name="nStartChargeTime6" type="text" size="1" />
					        分钟，收费
					        <input id="nStartChargePrice6" name="nStartChargePrice6" type="text" size="1" />
					      元</p>
					      <p>以后每
					        <input id="nChargeTime6" name="nChargeTime6" type="text" size="1" /> 
					        分钟，收费
					        <input id="nChargePrice6" name="nChargePrice6" type="text" size="1" />
					      元</p>
					      <p>该时段收费限额为
					        <input id="nTimeSlotLimit6" name="nTimeSlotLimit6" type="text" size="1" />
					        元
					</p>
					    </div>
					    <div id="Layer19">
						<span class="title">计次收费策略</span>
					      <p>免费停车时长
					        <input id="nSubFreeTime6" name="nSubFreeTime6" type="text" size="1" />
					        分钟</p>
					      <p>每次停车收费
					        <input id="nSubCharge6" name="nSubCharge6" type="text" size="1" />
					        元
					      </p>
					    </div>
					  </div>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 0.免费 -->
				<div id="freeTypeDiv">
					<div id="Layer5">
						<span class="title">计费策略</span>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 1.按时长计费 -->
				<div id="durationDiv" style="display: none;">
					<div id="Layer5">
						<span class="title">计费策略</span>
						当日限额<input id="dayLimit1" name="dayLimit1" type="text" size="1" />元
						<p>免费停车时长
					          <input id="freeTime1" name="freeTime1" type="text" size="1" />
					          分钟</p>
					        <p>前
					          <input id="startChargeTime1" name="startChargeTime1" type="text" size="1" />
					          分钟，收费
					          <input id="startChargePrice1" name="startChargePrice1" type="text" size="1" />
					        元</p>
					        <p>以后每
					          <input id="chargeTime1" name="chargeTime1" type="text" size="1" />
					          分钟，收费
					          <input id="chargePrice1" name="chargePrice1" type="text" size="1" />
					        元</p>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 2.计次收费 -->
				<div id="timeCountDiv" style="display: none;">
					<div id="Layer5">
						<span class="title">计费策略</span>
						当日限额<input id="dayLimit2" name="dayLimit2" type="text" size="1" />元
						<p>免费停车时长
					          <input id="freeTime2" name="freeTime2" type="text" size="1" />
					          分钟</p>
					        <p>每次停车收费
					          <input id="subCharge2" name="subCharge2" type="text" size="1" />
					          元
					</p>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 3.24小时轮询 -->
				<div id="pollingDiv" style="display: none;">
					<div id="Layer5">
						<span class="title">计费策略</span>
						停车24小时限额<input id="dayLimit3" name="dayLimit3" type="text" size="1" />元
						<p>免费停车时长
					          <input id="freeTime3" name="freeTime3" type="text" size="1" />
					          分钟</p>
					        <p>前
					          <input id="startChargeTime3" name="startChargeTime3" type="text" size="1" />
					          分钟，收费
					          <input id="startChargePrice3" name="startChargePrice3" type="text" size="1" />
					        元</p>
					        <p>以后每
					          <input id="chargeTime3" name="chargeTime3" type="text" size="1" />
					          分钟，收费
					          <input id="chargePrice3" name="chargePrice3" type="text" size="1" />
					        元</p>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 4.跨日计费策略 -->
				<div id="intervalDayDiv" style="display: none;">
				<div id="New1">
					  <span class="title">计费策略</span>
					  <div id="New2">跨日24小时限额
					    <input id="dayLimit4" name="dayLimit4" type="text" size="1" />
					  元</div>
					  <div id="New3">
					    <div id="New9">
						<span class="title">计时收费策略</span>
					      <p>免费停车时长
					        <input id="freeTime4" name="freeTime4" type="text" size="1" />
					        分钟</p>
					      <p>前
					        <input id="startChargeTime4" name="startChargeTime4" type="text" size="1" />
					        分钟，收费
					        <input
									id="startChargePrice4" name="startChargePrice4" type="text" size="1" />
					      元</p>
					      <p>以后每
					        <input id="chargeTime4" name="chargeTime4" type="text" size="1" /> 
					        分钟，收费
					        <input
									id="chargePrice4" name="chargePrice4" type="text" size="1" />
					      元</p>
					      <p>该时段收费限额为
					        <input id="timeSlotLimit4" name="timeSlotLimit4" type="text" size="1" />
					        元
					</p>
					    </div>
						<span class="title">日间收费方式</span>
					    <div id="New5">
						当日截至时刻：
					      <label>
					      <select class="easyui-combobox" id="endHour4" name="endHour4">
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					      </label>
					    </div>
					    <div id="New6">
					      <label>
					      <input type="radio" name="chargeType4"
								value="1" />
					      按时长计算</label>
					    </div>
					    <div id="New7">
					      <label>
					      <input type="radio" name="chargeType4"
								value="2" />
					      按次计算</label>
					    </div>
					    <div id="New8">
					      <label>
					      <input type="radio" name="chargeType4"
								value="3" />
					      免费</label>
					    </div>
					    <div id="New10">
						<span class="title">计次收费策略</span>
					      <p>免费停车时长
					         <input id="subFreeTime4" name="subFreeTime4" type="text" size="1" />
					        分钟</p>
					      <p>每次停车收费
					        <input id="subCharge4" name="subCharge4" type="text" size="1" /> 
					        元
					      </p>
					    </div>
					    <div id="New19">当日起始时刻：
					      <label>
					      <select class="easyui-combobox" id="startHour4" name="startHour4">
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					      </label>
					    </div>
					  </div>
					  <div id="New4"></div>
					  <div id="New11">
					  <span class="title">夜间收费方式</span>
					    <div id="New12">当日起始时刻：
					      <label>
					      <select class="easyui-combobox" id="nStartHour4" name="nStartHour4" disabled="disabled">
					      	<option value=""></option>
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					      </label>
					    </div>
					    <div id="New13">次日截至时刻：
					      <label>
					      <select class="easyui-combobox" id="nEndHour4" name="nEndHour4" disabled="disabled">
					        <option value=""></option>
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					      </label>
					    </div>
					    <div id="New14">
					      <label>
					      <input type="radio" name="nChargeType4"
								value="1" />
					      按时长计算</label>
					    </div>
					    <div id="New15">
					      <label>
					      <input type="radio" name="nChargeType4"
								value="2" />
					      按次计算</label>
					    </div>
					    <div id="New16">
					      <label>
					      <input type="radio" name="nChargeType4"
								value="3" />
					      免费</label>
					    </div>
					
					    <div id="New17">
						<span class="title">计时收费策略</span>
					      <p>免费停车时长
					         <input id="nSubFreeTime4" name="nSubFreeTime4" type="text" size="1" />
					        分钟</p>
					      <p>前
					        <input id="nStartChargeTime4" name="nStartChargeTime4" type="text" size="1" />
					        分钟，收费
					        <input
									id="nStartChargePrice4" name="nStartChargePrice4" type="text" size="1" />
					      元</p>
					      <p>以后每
					        <input id="nChargeTime4" name="nChargeTime4" type="text" size="1" />
					        分钟，收费
					        <input
									id="nChargePrice4" name="nChargePrice4" type="text" size="1" />
					      元</p>
					      <p>该时段收费限额为
					         <input id="nTimeSlotLimit4" name="nTimeSlotLimit4" type="text" size="1" />
					        元
					</p>
					    </div>
					    <div id="New18">
						<span class="title">计次收费策略</span>
					      <p>免费停车时长
					        <input id="nSubFreeTime4" name="nSubFreeTime4" type="text" size="1" />
					        分钟</p>
					      <p>每次停车收费
					         <input id="nSubCharge4" name="nSubCharge4" type="text" size="1" />
					        元
					      </p>
					    </div>
					  </div>
					</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<!-- 5.当日时段 -->
				<div id="nowDayDiv" style="display: none;">
				<div id="Now1">
					<span class="title">计费策略</span>
					  <div id="Now3">
					  <span class="title">日间收费策略</span>
					  &nbsp;&nbsp;&nbsp;&nbsp;当日起始时刻
					    <select class="easyui-combobox" id="startHour5" name="startHour5">
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					    <div id="Now9">
						<span class="title">计时收费策略</span>
					      <p>免费停车时长
					        <input id="freeTime5" name="freeTime5" type="text" size="1" />
					分钟</p>
					      <p>前
					        <input id="startChargeTime5" name="startChargeTime5" type="text" size="1" />
					分钟，收费
					<input id="startChargePrice5" name="startChargePrice5" type="text" size="1" />
					元</p>
					      <p>以后每
					        <input id="chargeTime5" name="chargeTime5" type="text" size="1" />
					分钟，收费
					<input id="chargePrice5" name="chargePrice5" type="text" size="1" />
					元</p>
					      <p>该时段收费限额为
					         <input id="timeSlotLimit5" name="timeSlotLimit5" type="text" size="1" />
					        元
					</p>
					    </div>
					    <div id="Now5">当日截至时刻
					      <select class="easyui-combobox" id="endHour5" name="endHour5">
					      	<option value="00:00">00:00</option>
					      	<option value="01:00">01:00</option>
					      	<option value="02:00">02:00</option>
					      	<option value="03:00">03:00</option>
					      	<option value="04:00">04:00</option>
					      	<option value="05:00">05:00</option>
					      	<option value="06:00">06:00</option>
					      	<option value="07:00">07:00</option>
					      	<option value="08:00">08:00</option>
					      	<option value="09:00">09:00</option>
					      	<option value="10:00">10:00</option>
					      	<option value="11:00">11:00</option>
					      	<option value="12:00">12:00</option>
					      	<option value="13:00">13:00</option>
					      	<option value="14:00">14:00</option>
					      	<option value="15:00">15:00</option>
					      	<option value="16:00">16:00</option>
					      	<option value="17:00">17:00</option>
					      	<option value="18:00">18:00</option>
					      	<option value="19:00">19:00</option>
					      	<option value="20:00">20:00</option>
					      	<option value="21:00">21:00</option>
					      	<option value="22:00">22:00</option>
					      	<option value="23:00">23:00</option>
					      	<option value="24:00">24:00</option>
					      </select>
					    </div>
					    <div id="Now6">
					      <label>
					      <input type="radio" name="chargeType5"
								value="1" />
					      按时长计算</label>
					    </div>
					    <div id="Now7">
					      <label>
					      <input type="radio" name="chargeType5"
								value="2" />
					      按次计算</label>
					    </div>
					    <div id="Now8">
					      <label>
					      <input type="radio" name="chargeType5"
								value="3" />
					      免费</label>
					    </div>
					    <div id="Now10">
						<span class="title">计次收费策略</span>
					      <p>免费停车时长
					        <input id="subFreeTime5" name="subFreeTime5" type="text" size="1" />
					分钟</p>
					      <p>每次停车收费
					        <input id="subCharge5" name="subCharge5" type="text" size="1" /> 
					      元 </p>
					    </div>
					  </div>
					  <div id="Now4"></div>
					  <div id="Now11">
					    <div id="Now13">
					      <label>
					      <input type="radio" name="nChargeType5"
								value="1" />
					      按时长计算</label>
					    </div>
					    <div id="Now14">
					      <label>
					      <input type="radio" name="nChargeType5"
								value="2" />
					      按次计算</label>
					    </div>
					    <div id="Now15">
					      <label>
					      <input type="radio" name="nChargeType5"
								value="3" />
					      免费</label>
					    </div>
					    <div id="Now16">
						<span class="title">计时收费策略</span>
					      <p>免费停车时长
					        <input id="nSubFreeTime5" name="nSubFreeTime5" type="text" size="1" />
					        分钟</p>
					      <p>前
					        <input id="nStartChargeTime5" name="nStartChargeTime5" type="text" size="1" />
					        分钟，收费
					        <input id="nStartChargePrice5" name="nStartChargePrice5" type="text" size="1" />
					      元</p>
					      <p>以后每
					        <input id="nChargeTime5" name="nChargeTime5" type="text" size="1" />
					        分钟，收费
					        <input id="nChargePrice5" name="nChargePrice5" type="text" size="1" /> 
					      元</p>
					      <p>该时段收费限额为
					        <input id="nTimeSlotLimit5" name="nTimeSlotLimit5" type="text" size="1" />
					        元
					</p>
					    </div>
					    <div id="Now17">
						<span class="title">计次收费策略</span>
					      <p>免费停车时长
					         <input id="nSubFreeTime5" name="nSubFreeTime5" type="text" size="1" />
					        分钟</p>
					      <p>每次停车收费
					        <input id="nSubCharge5" name="nSubCharge5" type="text" size="1" />
					        元
					      </p>
					    </div>
					  </div>
					  <div id="Now12">
					  <span class="title">夜间收费方式</span>
					  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					  0:00~<label id="night1" style="color: red;"></label>
					  &nbsp;和&nbsp;
					  <label id="night2" style="color: red;"></label>~24:00
					  </div>
					</div>
					<div id="Now2">当日限额
					  <input id="dayLimit5" name="dayLimit5" type="text"
							size="3" />
					元</div>
					<div id="Layer20" align="center">计算策略设置</div>
				</div>
				
				<div id="save1" align="center"><a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
							onclick="$.RuleSetSave()">保存</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a name="close"
							id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a></div>
			</div>
		</div>
	</div>
</body>
</html>
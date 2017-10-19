<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">
		
	$(window).resize(function(){
		 $('#viewSplitFee').datagrid('resize', {
		 	width:cs()
		 });
	}); 
	function cs(){	
		return $(window).width()-6;
	}
    $(function(){
    	var myDate = new Date();
		var months=myDate.getMonth()+1;
		if(months<10){
			months="0"+months;
		}
		var yearmonth=myDate.getFullYear()+''+months;
		$("#reportData").val(yearmonth);
		$.viewSplitFee();
	});

	$.viewSplitFee = function() {
		var reportData = $("#reportData").val();
		$('#viewSplitFee').datagrid(
						{
						title : '分润信息确认',
						width:$(window).width()-10,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/splitfee/splitfee.do?method=getSplitFeeByAgencyDealType",
						queryParams:{
							reportData :reportData
						},
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field : "agnecyName",title : "机构名称",width :'200',align : "center",sortable : true},
										{field : "yearmonth",title : "日期",width : 100,align : "center",sortable : true},
										{field : "dealtypeStr",title : "交易类型",width : 150,align : "center",sortable : true},
										{field : "transCount",title : "交易总金额",width : 100,align : "center",sortable : true},
										{field : "dealCount",title : "交易总笔数",width :80,align : "center",sortable : true},
										{field : "amount",title : "分润金额",width : 100,align : "center",sortable : true},
										{field : "isAffirmStr",title : "是否确认",width : 80,align : "center",sortable : true},
										{field : "isCalcStr",title : "是否计算",width : 80,align : "center",sortable : true},
										{field : "opt",title : "操作",width : "200",align : "left",
											formatter : function(value, data, index) {
												var dealTypeId = data.dealtype;
												var yearmonth = data.yearmonth;
												var disableAff = "";
												var disableCalc = "";
												if(data.parentagencyId==null||data.parentagencyId==''){
														disableAff = "disabled='disabled'";
														disableCalc= "disabled='disabled'";
												}else if (data.parentagencyId=='RTB00000000'||data.parentagencyId=='RTB00000000'){
													disableAff = "disabled='disabled'";
												}else{
													if(data.isAffirm=='0'){
														disableCalc= "disabled='disabled'";
													}
												}
												if(data.isAffirm!='0'){
													disableAff = "disabled='disabled'";
												}
												
												if((data.parentagencyId=='RTB00000000'||data.parentagencyId=='RTB00000000')||data.parentIsCalc=='0'){
													disableAff = "disabled='disabled'";
												}
												
												var c = "<input type='button' "+disableAff+"onclick='$.affirmSplitFee("+dealTypeId+","+yearmonth+")' value='确认'/>";
												
											  var d = "<input type='button' "+disableCalc+"onclick='$.calcSplitFee("+dealTypeId+","+yearmonth+","+yearmonth+")' value='计算' />";
												return c+d ;
										  	}
										  }
										 ] ], 
										 hideColumn : [ [ 
														 {field:"parentagencyId"},
														 {field : "parentIsCalc"},
														 {field : "dealtype"},
														 {field : "agencyId"},
														 {field : "isAffirm"},
														 {field : "isCalc"}
										              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:toolbarDiv
						
					});
	var p = $('#viewSplitFee').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

$.affirmSplitFee = function(dealTypeId,yearmonth){
	
	$.post("${ctx}/splitfee/splitfee.do?method=splitFeeAffirm",
			 {
				dealTypeId:dealTypeId,
				reportData:yearmonth
			 }
			, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
};

$.calcSplitFee = function (dealTypeId,yearmonth,type){
	$.showDivShade('${ctx}');
	$.post("${ctx}/splitfee/splitfee.do?method=splitFeeCalc",
			 {
				dealTypeId:dealTypeId,
				reportData:yearmonth,
				type:type
			 }
			, function(data) {
				$.hideDivShade();
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
};

$.splitFeeCalc = function(){
	var reportData = $("#reportData").val();
	if ($.trim(reportData) == ""){
		$.messager.alert("提示 ", "请选择日期!");
		return false;
	}
	$.calcSplitFee('0',reportData,'');
};
$.success = function(message, data) {
	$.messager.alert("提示 ", message);
	//$('#splitFee').linkbutton('enable');
	$.viewSplitFee();
};

$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	//$('#splitFee').linkbutton('enable');
	//$.viewSplitFee();
};

</script>
</head>
<body id="indexd" bgcolor="#ff0000">

 <div id="toolbarDiv" style="padding:5px;height:auto">
 		 <input id="reportData" name="reportData" class="Wdate"  type="text" style="width: 120px;" onfocus="WdatePicker({dateFmt:'yyyyMM'})" readonly/>
 		 <a href="#" class="easyui-linkbutton" id="selectData" name="selectData" class="btn" iconCls="icon-search" plain="true" onclick="$.viewSplitFee()">分润查询</a>
		 <a href="#" class="easyui-linkbutton" id="splitFee" name="splitFee" class="btn" iconCls="icon-add" plain="true" onclick="$.splitFeeCalc()">分润计算</a>
</div>

<table id="viewSplitFee"></table>

</body>
</html>
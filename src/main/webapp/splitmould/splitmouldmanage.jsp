<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
.button1
{
  color: #444;
  background-repeat: no-repeat;
  background: #f5f5f5;
  background-repeat: repeat-x;
  border: 1px solid #bbb;
  background: -webkit-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: -moz-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: -o-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
  background: linear-gradient(to bottom,#ffffff 0,#e6e6e6 100%);
  background-repeat: repeat-x;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff,endColorstr=#e6e6e6,GradientType=0);
  -moz-border-radius: 5px 5px 5px 5px;
  -webkit-border-radius: 5px 5px 5px 5px;
  border-radius: 5px 5px 5px 5px;
}
</style>
<script type="text/javascript">
$(function(){
	$.viewTopMould();
	//页面上的模版名称下拉框
	$('#mouldname').combobox({
		url:"${ctx}/splitmould/splitmould.do?method=topGetMouldList",
		valueField:"id",
		textField:"text"
		});
		
	$('#agencyId').combobox({
		url:"${ctx}/agency/agency.do?method=getSuperiorAgencyList",
		valueField:"id",
		textField:"text"
		});
});

$(window).resize(function(){
	 $('#viewTopMould').datagrid('resize', {
	 	width:cs()
	 });
	
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
	flag = obj;
	//如果选的是添加
	if(flag == "-1"){
		$.showDivShade('${ctx}');
		//打开'splitrule' 这个DIV
	 	$('#splittopmould').window({title: '添加顶级分润模板'});
		$('#mouldName').val('');
		$('#ruleRem').val('');
	 	$("#splittopmould").window('open').window('refresh');
	}
	//如果选的是设置
	if(flag == "-2"){
		$.showDivShade('${ctx}');
		//打开'splitrule' 这个DIV
	 	$('#splitrule').window({title: '设置分润模板信息'});
	 	
	 	//给下拉框赋初值
		$("#dealType").combobox('clear');
		$("#splitMode").combobox('setValue', 0);
		$("#splitRegionMode").combobox('setValue',0);
		
		$("#ruleBegin").val('');
		$("#ruleEnd").val('');
		$("#ruleRemRule").val(''); 
////////////////////////////////////////////////////////	
		//初始化业务名称下拉框
	/*	$('#dealType').combobox({
			url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypesAll",
			valueField:"id",
			textField:"text"
			}); */
		
		//初始化业务名称下拉框
	 /*    $('#dealTypeUS').combobox({
			url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypesAll",
			valueField:"id",
			textField:"text"
			});  */
						
////////////////控制了一下交易类型的菜单，以后还可能放开/////////
		//初始化业务名称下拉框
		$('#dealType').combobox({
			url:"${ctx}/splitmould/splitmould.do?method=getMouldsDealType&flag=1",
			valueField:"id",
			textField:"text"
			});
		
		//初始化业务名称下拉框
		$('#dealTypeUS').combobox({
			url:"${ctx}/splitmould/splitmould.do?method=getMouldsDealType&flag=1",
			valueField:"id",
			textField:"text"
			});

////////////////////////////////////////////////////////		
		
			
		var rows = $('#viewTopMould').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条模板设置");
			return;
			}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要设置的模板");
			return;
			}
		var row = $('#viewTopMould').datagrid('getSelected');
		//给模板名称赋值，且不可修改
		$("#mouldNameU").val(row.mouldName);
		$("#mouldNameU").attr("disabled",true);
		//给隐藏域赋值ruleId
		$("#ruleIdU").val(row.ruleId);
		var ruleId = row.ruleId;
		//给模板信息里的datagrid赋值( mouldViewSection)
		$.viewMouldViewSection(ruleId);
		$("#splitrule").window('open').window('refresh');
	}
	
	//如果选的是分配
	if(flag == "-3"){
		$.showDivShade('${ctx}');
		//打开分配对话框，'distrMouldView' 这个DIV
	 	$('#distrMouldView').window({title: '分配分润模板信息'});
	 	//给下拉框赋初值
		$("#childAgencyId").combobox('setValue', -1);
		//初始化子机构下拉框
		$('#childAgencyId').combobox({
			url:"${ctx}/agency/agency.do?method=getChildAgencyList",
			valueField:"id",
			textField:"text"
			});
		
		var rows = $('#viewTopMould').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条设置分配");
			return;
			}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要分配的记录");
			return;
			}
		var row = $('#viewTopMould').datagrid('getSelected');
		//给隐藏域赋值ruleId
		$("#ruleIdD").val(row.ruleId);
		var ruleId = row.ruleId;
		//给模板信息里的datagrid赋值( mouldViewSection)
		$.viewMouldViewSectionD(ruleId);
		$("#distrMouldView").window('open').window('refresh');
	}
	
	//如果选的是修改
	if(flag == "-4"){
		$.showDivShade('${ctx}');
		//打开'splitrule' 这个DIV
	 	$('#updatesplittopmould').window({title: '修改顶级分润模板'});
	 	var rows = $('#viewTopMould').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条模板设置");
			return;
			}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要设置的模板");
			return;
			}
		var row = $('#viewTopMould').datagrid('getSelected');
		//给顶级模板名称，备注赋值
		$("#mouldNameUP").val(row.mouldName);
		$("#ruleRemUP").val(row.ruleRem);
		//给隐藏域赋值ruleId,agencyId
		$("#ruleIdUP").val(row.ruleId);
		$("#agencyIdUP").val(row.agencyId);
		
		$("#updatesplittopmould").window('open').window('refresh');
	}
	
	//如果双击了添加模板信息里的某一行
	if(flag == "-5"){
		$.showDivShade('${ctx}');
		//打开'splitrule' 这个DIV
	 	$('#updatesplittopmould').window({title: '修改顶级分润模板'});
	 	var rows = $('#viewTopMould').datagrid('getSelections');
		if (rows.length > 1) {
			$.hideDivShade();
			$.messager.alert("提示 ", "只能对单条模板设置");
			return;
			}else if(rows.length ==0){
			$.hideDivShade();
			$.messager.alert("提示 ", "请选择要设置的模板");
			return;
			}
		var row = $('#viewTopMould').datagrid('getSelected');
		//给顶级模板名称，备注赋值
		$("#mouldNameUP").val(row.mouldName);
		$("#ruleRemUP").val(row.ruleRem);
		//给隐藏域赋值ruleId,agencyId
		$("#ruleIdUP").val(row.ruleId);
		$("#agencyIdUP").val(row.agencyId);
		
		$("#updatesplittopmould").window('open').window('refresh');
	}
	
};

//给添加模板信息里的datagrid赋值( mouldViewSection)
$.viewMouldViewSection = function(ruleId){

	$('#mouldViewSection').datagrid(
		{
			title : '分润模板详细(双机选中行可修改)',
			//width:750,
			width:750,
			height : $(window).height()*0.6,
			url : "${ctx}/splitmould/splitmould.do?method=getMoulds",
			singleSelect: true,
			rownumbers : true,
			//定义双击某一行，打开编辑模式
			onDblClickRow: function(rowIndex, rowData){
				$.showDivShade('${ctx}');
				//打开'updatesplitrule' 这个DIV
			 	$('#updatesplitrule').window({title: '修改分润模板详细'});
			 	//给各项赋值
				$("#splitRegionModeUS").combobox('setValue', rowData.splittingRegionMode);
				$("#splitModeUS").combobox('setValue', rowData.splittingMode);
				$("#dealTypeUS").combobox('setValue', rowData.dealType);
				$("#ruleBeginUS").val(rowData.ruleBegin);
				$("#ruleEndUS").val(rowData.ruleEnd);
				$("#ruleRemUS").val(rowData.ruleRem);
				//给隐藏域赋值
				$("#ruleIdUS").val(rowData.ruleId);
				$("#ruleNumUS").val(rowData.ruleNum);
				//打开updatesplittopmould
				$("#updatesplitrule").window('open').window('refresh');
			},
			
			
			
			queryParams:{
				ruleId:ruleId
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [
						{field:"dealTypeName",title:"业务名称",width :130,align : "center",sortable : true},
						{field:"splittingModeStr",title:"分润方式",width :130,align : "center",sortable : true},
						{field:"splittingRegionModeStr",title:"分润规则",width :130,align : "center",sortable : true},
						{field:"ruleBegin",title:"规则区间起始",width :70,align : "center",sortable : true},
						{field:"ruleEnd",title:"规则区间结束",width :70,align : "center",sortable : true},
					//	{field:"ruleRem",title:"备注",width :157,align : "center",sortable : true},				
						] ],
						hideColumn : [ [ 
						                 {field:"ruleId"},
						                 {field:"ruleNum"},
						                 {field:"dealType"},
						                 {field:"splittingMode"},
						                 {field:"splittingRegionMode"},
						           ] ],
						onAfterEdit: function(rowIndex, rowData, changes){
						if(rowIndex!=0){
						  $(this).datagrid('beginEdit', rowIndex-1);
						  $(this).datagrid('getRows')[rowIndex-1]['ruleEnd'] = rowData.ruleBegin;
						  $(this).datagrid('endEdit', rowIndex-1);
					}
				}
		});
};

//给分配模板信息里的datagrid赋值( mouldViewSection)
$.viewMouldViewSectionD = function(ruleId){

	$('#mouldViewSectionD').datagrid(
		{
			title : '分润模板详细',
			width:750,
			height : 400,
			url : "${ctx}/splitmould/splitmould.do?method=getMoulds",
			singleSelect: true,
			rownumbers : true,
			queryParams:{
				ruleId:ruleId
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [
						{field:"dealTypeName",title:"业务名称",width :130,align : "center",sortable : true},
						{field:"splittingModeStr",title:"分润方式",width :130,align : "center",sortable : true},
						{field:"splittingRegionModeStr",title:"分润规则",width :130,align : "center",sortable : true},
						{field:"ruleBegin",title:"规则区间起始",width :70,align : "center",sortable : true},
						{field:"ruleEnd",title:"规则区间结束",width :70,align : "center",sortable : true},
				//	{field:"ruleRem",title:"备注",width :157,align : "center",sortable : true},					
						] ],
						hideColumn : [ [ 
						                 {field:"ruleId"}
						          ] ],
						onAfterEdit: function(rowIndex, rowData, changes){
						if(rowIndex!=0){
						  $(this).datagrid('beginEdit', rowIndex-1);
						  $(this).datagrid('getRows')[rowIndex-1]['scaleEndValue'] = rowData.scaleStartValue;
						  $(this).datagrid('endEdit', rowIndex-1);
					}
				}
		});
};

//保存添加模板内容的方法
$.addMould = function() {
	var splitRegionMode =$("#splitRegionMode").combobox('getValue');
	var dealType =$("#dealType").combobox('getValue');
	var ruleBegin =$("#ruleBegin").val();
	var ruleEnd =$("#ruleEnd").val();
	var splitMode =$("#splitMode").combobox('getValue');
	var ruleRem =$("#ruleRemRule").val();
	//取得隐藏域的ruleId
	var ruleId =$("#ruleIdU").val();
	
	if(splitMode=="0"){
		$.messager.alert("提示 ", "请选择分润方式");
		return false;
	}
	
	if(splitRegionMode=="0"){
		$.messager.alert("提示 ", "请选择分润模板规则");
		return false;
	}
	
	if (dealType == "-1") {
		$.messager.alert("提示 ", "请选择业务名称");
		return false;
	}
	if (ruleBegin == "") {
		$.messager.alert("提示 ", "请输入规则区间起始");
		return false;
	}
	
	if (ruleEnd == "") {
		$.messager.alert("提示 ", "请输入规则区间结束");
		return false;
	}
	if(parseFloat(ruleBegin)> parseFloat(ruleEnd) ){
		$.messager.alert("提示 ", "请输入规则区间起始值不能大于结束值");
		return false;
	}
	
	$('#addMould').linkbutton('disable');
		
	     $.post('${ctx}/splitmould/splitmould.do?method=addMould', {
			splitRegionMode : splitRegionMode,
			dealType : dealType,
			splitMode : splitMode,
			ruleBegin : ruleBegin,
			ruleEnd : ruleEnd,
			ruleRem : ruleRem,
			ruleId : ruleId
		}, function(data) {
			$('#mouldViewSection').datagrid('reload');
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");

};

//保存顶级模板方法
$.savetopmould = function() {
	var mouldName =$("#mouldName").val();
	var ruleRem =$("#ruleRem").val();
	if (mouldName == "") {
		$.messager.alert("提示 ", "请输入模板名称");
		return false;
	}
	
	$('#savetopmould').linkbutton('disable');
	
	    $.post("${ctx}/splitmould/splitmould.do?method=addTopMould", {
			mouldName : mouldName,
			ruleRem : ruleRem
			
		}, function(data) {
			$('#viewTopMould').datagrid("reload");
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	$.close(-1);
};

//修改顶级模板方法
$.updatetopmould = function() {
	var mouldName = $("#mouldNameUP").val();
	var ruleRem = $("#ruleRemUP").val();
	var ruleId = $("#ruleIdUP").val();
	if (mouldName == "") {
		$.messager.alert("提示 ", "请输入模板名称");
		return false;
	}
	
	$('#updatetopmould').linkbutton('disable');
		$.post("${ctx}/splitmould/splitmould.do?method=updateTopMould", {
			mouldName : mouldName,
			ruleRem : ruleRem,
			ruleId : ruleId
		}, function(data) {
			$('#viewTopMould').datagrid("reload");
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	$.close(-4);
};

//更新模板信息方法
$.updatesplitrule = function() {
	var splitRegionModeUS =$("#splitRegionModeUS").combobox('getValue');
	var dealTypeUS =$("#dealTypeUS").combobox('getValue');
	var ruleBeginUS =$("#ruleBeginUS").val();
	var ruleEndUS =$("#ruleEndUS").val();
	var splitModeUS =$("#splitModeUS").combobox('getValue');
	var ruleRemUS =$("#ruleRemUS").val();
//	取得隐藏域的ruleId
	var ruleIdUS =$("#ruleIdUS").val();
	var ruleNumUS =$("#ruleNumUS").val();
	
	if(splitModeUS=="0"){
		$.messager.alert("提示 ", "请选择分润方式");
		return false;
	}
	
	if(splitRegionModeUS =="0"){
		$.messager.alert("提示 ", "请选择分润模板规则");
		return false;
	}
	
	if (dealTypeUS == "-1") {
		$.messager.alert("提示 ", "请选择业务名称");
		return false;
	}
	if (ruleBeginUS == "") {
		$.messager.alert("提示 ", "请输入规则区间起始");
		return false;
	}
	
	if (ruleEndUS == "") {
		$.messager.alert("提示 ", "请输入规则区间结束");
		return false;
	}
	
	if(parseFloat(ruleBeginUS) > parseFloat(ruleEndUS) ){
		$.messager.alert("提示 ", "请输入规则区间起始值不能大于结束值");
		return false;
	}
	
		$.post("${ctx}/splitmould/splitmould.do?method=updateMould", {
			splitRegionModeUS : splitRegionModeUS,
			dealTypeUS : dealTypeUS,
			splitModeUS : splitModeUS,
			ruleBeginUS : ruleBeginUS,
			ruleEndUS : ruleEndUS,
			ruleRemUS : ruleRemUS,
			ruleIdUS : ruleIdUS,
			ruleNumUS : ruleNumUS
		}, function(data) {
			$('#mouldViewSection').datagrid("reload");
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		
	$.close(-5);
};

//更新模板信息方法
$.deleteRule = function() {
	var splitRegionModeUS =$("#splitRegionModeUS").combobox('getValue');
	var dealTypeUS =$("#dealTypeUS").combobox('getValue');
	var ruleBeginUS =$("#ruleBeginUS").val();
	var ruleEndUS =$("#ruleEndUS").val();
	var splitModeUS =$("#splitModeUS").combobox('getValue');
	var ruleRemUS =$("#ruleRemUS").val();
//	取得隐藏域的ruleId
	var ruleIdUS =$("#ruleIdUS").val();
	var ruleNumUS =$("#ruleNumUS").val();
	

	
		$.post("${ctx}/splitmould/splitmould.do?method=deleteRule", {
			splitRegionModeUS : splitRegionModeUS,
			dealTypeUS : dealTypeUS,
			splitModeUS : splitModeUS,
			ruleBeginUS : ruleBeginUS,
			ruleEndUS : ruleEndUS,
			ruleRemUS : ruleRemUS,
			ruleIdUS : ruleIdUS,
			ruleNumUS : ruleNumUS
		}, function(data) {
			$('#mouldViewSection').datagrid("reload");
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		
	$.close(-5);
};

//分配模板到子机构方法
$.distrMould = function() {
	//获得子机构
	var childAgencyId =$("#childAgencyId").combobox('getValue');
	var childAgencyId2 =$("#childAgencyId").combobox('getText');
	if(childAgencyId==childAgencyId2)
	  return;
	//取得隐藏域的ruleId
	var ruleId =$("#ruleIdD").val();
	
	if(childAgencyId =="-1"){
		$.messager.alert("提示 ", "请选择子机构");
		return false;
	}
	// 检查所选子机构是否有效（防止因为输入错误值导致后台处理出错）
	var data = $('#childAgencyId').combobox('getData');
	var i;
	for (i=0; i<data.length; i++) {
		if (childAgencyId == data[i].id) {
			break;
		}
	}
	if (i == data.length) {
		$.messager.alert("提示", "所选子机构无效");
		return false;
	}
		
	$('#distrMould').linkbutton('disable');
		
	     $.post("${ctx}/splitmould/splitmould.do?method=distrMould", {
			childAgencyId : childAgencyId,
			ruleId : ruleId
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
	$.close(-3);
	$('#viewTopMould').datagrid("reload");
};

$.succ= function(message, data){
	$.messager.alert("提示 ", message);
};
$.fail= function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.scaleSucc = function(message, data) {
	
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};

$.close = function(obj) {
	$.hideDivShade();
	var flag = obj;
	//关闭修改模板详细窗口
	if(flag == "-5"){
	$("#updatesplitrule").window('close');
	}
	//关闭修改顶级模板窗口
	if(flag == "-4"){
	$("#updatesplittopmould").window('close');
	}
	//关闭分配模板窗口
	if(flag == "-3"){
	$("#distrMouldView").window('close');
	}
	//关闭设置模板窗口
	if(flag == "-2"){
	$("#splitrule").window('close');
	}
	//关闭添加顶级模板窗口
	if(flag == "-1"){
	$("#splittopmould").window('close');
	}
};

$.deleteRole = function() {
	var rows = $('#viewTopMould').datagrid('getSelected');
	if(rows==null){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var ruleId = rows.ruleId;
	
	$.messager.confirm("提示","确定删除？",function(r){
	var index = $("#viewTopMould").datagrid('getRowIndex',rows);
	$("#viewTopMould").datagrid('deleteRow',index);
		if(r){
	$.post("${ctx}/splitmould/splitmould.do?method=deleteMould", {
		ruleId:ruleId
	}, function(data) {
		$('#viewTopMould').datagrid("reload");
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
	
};
$.success= function(message, data){
	$.messager.alert("提示 ", message);
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
};
$.viewTopMould = function() {
	var mouldName = $("#mouldname").combobox('getValue');
	$('#viewTopMould').datagrid(
					{
						title : '分润模板',
						width:$(window).width()*0.98,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/splitmould/splitmould.do?method=getTopMoulds",
						pageSize : 40,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							mouldName : mouldName
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field:"mouldName",title:"模板名称",width :100,align : "center",sortable : true},
										{field:"ruleId",title:"模板编号",width :130,align : "center",sortable : true},
										{field:"agencyName",title:"机构名称",width :100,align : "center",sortable : true},
										{field:"ruleRem",title:"备注",width :300,align : "center",sortable : true},
										] ],
						hideColumn : [ [ 
										 {field:"agencyId"},
						                 {field:"ruleId"},
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true,
						toolbar:[
						         {id:'btnadd',text:'添加',iconCls:'icon-add',
						        	 handler:function(){
						        		 $.openWin(-1);

						        	 }
								 },'-',
								 {id:'btncut',text:'设置',iconCls:'icon-edit',
									handler:function(){
											$.openWin(-2);
									}
								 },'-',
								  {id:'btncut',text:'分配',iconCls:'icon-edit',
									handler:function(){
											$.openWin(-3);
									}
								 },'-',
								 {id:'btncut',text:'修改',iconCls:'icon-edit',
									handler:function(){
											$.openWin(-4);
									}
								 },'-',
								 {id:'btnsave',text:'删除',iconCls:'icon-cut',
									handler:function(){
										$.deleteRole();
									}
								}
						]
						
					});
	var p = $('#viewTopMould').datagrid('getPager');
	$(p).pagination({
		pageList : [40],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

</script>
</head>
<body id="indexd" bgcolor="#ff0000">
	<table>
		<tr>
			<td align="left">模板名称：</td>
			<td align="left">
			<select style="width: 200px;" name="mouldname" editable="true" id="mouldname" class="easyui-combobox"></select>
			</td> 
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTopMould()">查询</a>
			</td>			
		</tr>
		
	</table>
		<table id="viewTopMould"></table>
	<div id="splittopmould" class="easyui-window" title="添加顶级分润模板" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIds" name="ruleIds" />
					<table width="100%">
						<tr style="height: 35px">
							<td align="right">分润模板名称：</td>
							<td align="left">
	    					<input type="text" name="mouldName" id="mouldName"  maxlength="50"/>
	    					</td>
						</tr>
						<tr>
							<td colspan="4" height="20">备注：</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td align="left" colspan="4"><textarea rows="5" cols="47" style="width: 100%"
									id="ruleRem" name="ruleRem" ></textarea>
							</td>
						</tr>
						<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.savetopmould()">保存</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close(-1)">关闭</a>
							</td>
						</tr>
						
					</table>
			</div>
		</div>
	</div>
	
	<div id="updatesplittopmould" class="easyui-window" title="修改顶级分润模板" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdUP" name="ruleIdUP" />
				<input type="hidden" id="agencyIdUP" name="agencyIdUP" />
					<table width="100%">
						<tr style="height: 35px">
							<td align="right">分润模板名称：</td>
							<td align="left">
	    					<input type="text" name="mouldNameUP" id="mouldNameUP"  maxlength="50"/>
	    					</td>
						</tr>
					<tr>
							<td colspan="4" height="20">备注：</td>
						</tr>
						<tr>
							<td align="left" colspan="4"><textarea rows="5" cols="47" style="width: 100%"
									id="ruleRemUP" name="ruleRemUP" ></textarea>
							</td>
						</tr>
							<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.updatetopmould()">保存</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close(-4)">关闭</a>
							</td>
						</tr>
						
					</table>
			</div>
		</div>
	</div>
	
	<div id="distrMouldView" class="easyui-window" title="分配模板" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdD" name="ruleIdD" />
					<table width="100%">
						<tr style="height: 35px">
							<td align="right">子机构名称：</td>
							<td align="left">
								<!-- <select id="childAgencyId" class="easyui-combobox" name="childAgencyId" style="width: 120px;" editable="false"></select> -->
								<select id="childAgencyId" class="easyui-combobox" name="childAgencyId" style="width: 120px;" editable="true"></select>
							</td>
						</tr>
						<tr>
						<td colspan="8" align="center" valign="top" >
							<table id="mouldViewSectionD" style="width:600px;height:280px;"></table>
							</td>		
						</tr>
							<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.distrMould()">分配</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close(-3)">关闭</a>
							</td>
						</tr>
						
					</table>
			</div>
		</div>
	</div>
		
	<div id="splitrule" class="easyui-window" title="添加分润模板信息" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdU" name="ruleIdU" />
					<table width="100%">
						<tr style="height: 35px">
							<td align="right">模板名称：</td>
							<td align="left">
								<input type="text" name="mouldNameU" id="mouldNameU" maxlength="50" />
							</td>
							
							<td align="right">业务名称：</td>
							<td align="left">
								<select id="dealType" class="easyui-combobox" name="dealType" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
								</select>
							</td>
						</tr>
						
					
	    			<tr  style="height: 35px">
							<td align="right">分润方式：</td>
							<td>
								<select id="splitMode" class="easyui-combobox" name="splitMode" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
									<option value="0">请选择</option>
									<option value="1">按笔数分润</option>
									<option value="2">按金额分润</option>
								</select>
							</td>
							<td align="right">分润模板规则：</td>
							<td align="left">
								<select id="splitRegionMode" class="easyui-combobox" name="splitRegionMode" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
									<option value="0">请选择</option>
									<option value="1">按月交易笔数分润</option>
									<option value="2">按月交易金额分润</option>
									<option value="3">按单笔交易金额分润</option>
								</select>
							</td>
					</tr>
					<tr>
						<td  align="right">规则区间起始：</td>
	    				<td align="left">
	    				<!-- 
		    				<input type="text" name="ruleBegin" id="ruleBegin" maxlength="10"
							data-options="precision:2" class="easyui-numberbox"/>
						 -->
						 <input type="text" name="ruleBegin" id="ruleBegin" maxlength="10"/>
	    				</td>
	    				
	    				<td align="right">规则区间结束：</td>
	    				<td align="left">
							<input type="text" name="ruleEnd" id="ruleEnd"  maxlength="9" title="最大长度为九位"/>
	    				</td>			   
	    			</tr>
	    			<tr>
						<td  align="right">备注：</td>
	    				<td align="left" >
		    				<input type="text" name="ruleRemRule" id="ruleRemRule" maxlength="50"/>
	    				</td>	
	    				<td></td>
	    				<td align="left" >
							<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.addMould()">添加 </a>
							
							<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close(-2)">关闭</a>
						</td>		   
	    			</tr>
					<tr>
						<td colspan="8" align="center" valign="top" >
							<table id="mouldViewSection" style="width:600px;height:280px;"></table>
						</td>		
						</tr>
				
					</table>
			</div>
		</div>
	</div>
	<div id="updatesplitrule" class="easyui-window" title="修改分润模板信息" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdUS" name="ruleIdUS" />
				<input type="hidden" id="ruleNumUS" name="ruleNumUS" />
					<table width="100%">
						<tr style="height: 35px">
							
							<td align="right">业务名称：</td>
							<td align="left">
								<select id="dealTypeUS" class="easyui-combobox" name="dealTypeUS" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
								</select>
							</td>
						</tr>
						
					
	    			<tr  style="height: 35px">
							<td align="right">分润方式：</td>
							<td>
								<select id="splitModeUS" class="easyui-combobox" name="splitModeUS" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
									<option value="0">请选择</option>
									<option value="1">按笔数分润</option>
									<option value="2">按金额分润</option>
								</select>
							</td>
							<td align="right">分润模板规则：</td>
							<td align="left">
								<select id="splitRegionModeUS" class="easyui-combobox" name="splitRegionModeUS" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
									<option value="0">请选择</option>
									<option value="1">按月交易笔数分润</option>
									<option value="2">按月交易金额分润</option>
									<option value="3">按单笔交易金额分润</option>
								</select>
							</td>
					</tr>
					<tr>
						<td  align="right">规则区间起始：</td>
	    				<td align="left">
		    				<input type="text" name="ruleBeginUS" id="ruleBeginUS" maxlength="50"/>
	    				</td>
	    				
	    				<td align="right">规则区间结束：</td>
	    				<td align="left">
	    					<input type="text" name="ruleEndUS" id="ruleEndUS"  maxlength="50"/>
	    				</td>			   
	    			</tr>
	    			<tr>
						<td  align="right">备注：</td>
					</tr>	
					<tr>
	    				<td align="left" colspan="4"><textarea rows="5" cols="47" style="width: 100%"
									id="ruleRemUS" name="ruleRemUS" ></textarea>
						</td>
	    			</tr>
	    				<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.updatesplitrule()">保存</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.deleteRule()">删除此条分润</a>
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close(-5)">关闭</a>
							</td>
					</tr>
					</table>
			</div>
		</div>
	</div>

</body>
</html>

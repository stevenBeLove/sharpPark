<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">	
$(window).resize(function(){
	 $('#viewTerminalManage').datagrid('resize', {
	 	width:cs()
	 });
}); 
function cs(){	
	return $(window).width()-8;
}
$(function(){
	$.viewTerminalManage();
		$('#agencytreeId').combotree({
				  url: '${ctx}/agency/agency.do?method=getAgencyTree&flag=profit',    
		   		 method:'get',
		  		  readonly:false,
		       onLoadSuccess:function(a,b){
		      },
		      panelWidth:   350
		});
	
	$(".combo-text").bind("input onchange",function(a,b){
			$('#agencytreeId').combotree('tree').tree("search",$(this).val());
			if($(this).val()==""||null==$(this).val()){
				$('#agencytreeId').combotree('tree').tree("collapseAll");
			} 
	});
		
	$('#terminalType').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});
	$('#terminalTypeF').combobox({
		url:"${ctx}/terminaltype/terminaltype.do?method=getTerminalTypeName&flag=1",
		valueField:"id",
		textField:"text"
		});
	});
	
	

	


	$.success = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#terminalBack').linkbutton('enable');
		 $.close(); 
		 $.viewTerminalManage();
	};
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#terminalBack').linkbutton('enable');
		 $.close(); 
	};
	$.close = function(){
		$.hideDivShade();
	};
	
	//加载数据表格
	$.viewTerminalManage = function() {
		var status = "0";
		var terminalCode = $("#terminalCode").val();
		var terminalTypeId =  $('#terminalTypeF').combobox('getValue');
		var terminalCodeEnd=$("#terminalCodeEnd").val();
		var agencytreeId= $('#agencytreeId').combobox('getValue');
		var  datestart = $('#yearmonthdatestart').val();
		var  dateend = $('#yearmonthdateend').val();
		if(status=='-1'){
			status='';
		}
		if(terminalTypeId=='-1'){
			terminalTypeId='';
		}
		if(agencytreeId=='-1'){
			agencytreeId='';
		}
		
		$('#viewTerminalManage').datagrid({
			title : '终端回拨',
			width : $(window).width()-8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			fitColumns:true,
			url : "${ctx}/terminalmanage/terminalmanage.do?method=getTerminalManageback",
			queryParams:{
				terminalCode:terminalCode,
				terminalTypeId:terminalTypeId,
				status:status,
				terminalCodeEnd:terminalCodeEnd,
				agencytreeId:agencytreeId,
				datestart:datestart,
			    dateend:dateend
			},		
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ 
			           /*   {field : "ck",checkbox : true,width : "50"}, */
			             {field : "terminalCode",title : "终端识别码",width : $(window).width()*0.25,align : "center",sortable : true},
			             {field : "terminaltypeName",title : "终端类型",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "agencyName",title : "所属机构",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "terminalStatusStr",title : "终端状态",width : $(window).width()*0.15,align : "center",sortable : true},
			           //  {field : "openDate",title : "开通时间",width : $(window).width()*0.2,align : "center",sortable : true}, 
			           //  {field : "clientCode",title : "所属客户编码",width : $(window).width()*0.2,align : "center",sortable : true},
			           //  {field : "scrapDate",title : "报废时间",width : $(window).width()*0.2,align : "center",sortable : true}, 
			             {field : "createId",title : "下发人",width : $(window).width()*0.2,align : "center",sortable : true},
			             {field : "createDt",title : "下发时间",width : $(window).width()*0.2,align : "center",sortable : true}
			           ] ],
		         hideColumn : 
		        	   [ [ 
						 {field : "onlyCode"},
						 {field : "agencyId"},
						 {field : "terminaltypeId"},
						 {field:"terminalStatus"}
		              	] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
		});
		//分页
		var p = $('#viewTerminalManage').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};
	//回拨
	$.terminalBack = function(){
		var rows = $('#viewTerminalManage').datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示 ", "请选择要回拨的记录!");
			return;
		}
		var terminalCodes = "";
		var agencyId='';
		for ( var i = 0; i < rows.length; i++) {
			if(rows[i].terminalStatus=='3'){
				$.messager.alert("提示 ", "已回拨的终端不能再次回拨");
				return;
				
			}
			if(rows[i].terminalStatus=='1'){
				$.messager.alert("提示 ", "已激活的终端不能回拨");
				return;
				
			}
			if(rows[i].terminalStatus=='4'){
				$.messager.alert("提示 ", "该终端已作废,不能回拨");
				return;
				
			}
			if(rows[i].agencyId=='1'){
				$.messager.alert("提示 ", "该机构属于中心，不能回拨");
				return;
			}	
			//if (i == rows.length - 1) {
			//	terminalCodes += "'" + rows[i].onlyCode + "'";
			//} else {
			//	terminalCodes += "'" + rows[i].onlyCode + "'" + ",";
			//}
		//14.10.30 需求变更，去除‘回拨-审核’环节，点回拨之后直接 调用回拨审核通过方法。
			if(rows[i].terminalStatus=='0')
				terminalCodes+="'"+rows[i].onlyCode+"',";
				agencyId=rows[i].agencyId;
			
			}
			
	
		$('#terminalBack').linkbutton('disable');
		
		
		$.post('${ctx}/terminalmanage/terminalmanage.do?method=terminalDeal', 
				{
					Ids  : terminalCodes ,
					flag :"up"
				}, function(data) {
					var flagTT=data.datas.flag;
					if("2"== flagTT){
						$.messager.confirm("提示", "终端已存在交易，您是否将交易同时进行分配?",function(r){
							$.post('${ctx}/terminalmanage/terminalmanage.do?method=checkTerminal', 
									{
										terminalCodes : terminalCodes,
										agencyId:agencyId,
										flag: r
									}, function(data) {
										$.parseAjaxReturnInfo(data, $.success, $.failed);
									}, "json");
					       });
						
					}else{
						$.post('${ctx}/terminalmanage/terminalmanage.do?method=checkTerminal', 
								{
									terminalCodes : terminalCodes,
									agencyId:agencyId,
									flag: false
								}, function(data) {
									$.parseAjaxReturnInfo(data, $.success, $.failed);
								}, "json");
						
					}
					 
				}, "json");
		
	}
</script>
</head>
<body id="indexd">
	<table>
		<tr>
			<td align="left">终端编号开始：</td>
			<td align="left"><input type="text" name="terminalCode" style="width: 150px;"
			 onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"   id="terminalCode" maxlength="100"/>
			</td>
			<td align="left">终端编号结束：</td>
			<td align="left"><input type="text" name="terminalCodeEnd" style="width: 150px;"
			onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"	  id="terminalCodeEnd" maxlength="100"/>
			</td>
			<td align="left">机构名称：</td>
			<td align="left"><select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true"></select></td>
			<td>
				<a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-search'" onclick="$.viewTerminalManage()">查询</a>
			</td>				
		</tr>
		<tr>
			<td align="left">终端类型：</td>
			<td align="left"><select id="terminalTypeF" editable="false" class="easyui-combobox" name="terminalTypeF" style="width: 150px;height: 30px"></select>
			</td>
			<td align="left">下发日期：</td>
			<td align="left" >
			 <input id="yearmonthdatestart" name="yearmonthdatestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'yearmonthdateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			 -
			 <input id="yearmonthdateend" name="yearmonthdateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 110px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'yearmonthdatestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left"><!--终端状态：</td>-->
			<td align="left"><!--
				<select id="status" editable="false" class="easyui-combobox" name="status" style="width: 150px;">
					<option selected="selected" value="-1">请选择终端状态</option>
					<option value="0">未激活</option>
					<option value="1">已激活</option>
					<option value="2">冻结</option>
					<option value="3">回拨</option>
					<option value="4">作废</option>
				</select>
			--></td>
			
			<td>
				<a id="terminalBack" name="terminalBack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.terminalBack()">申请回拨批次</a>
			</td>
			
		</tr>
	</table>
	<table id="viewTerminalManage"></table>

	
</body>
</html>
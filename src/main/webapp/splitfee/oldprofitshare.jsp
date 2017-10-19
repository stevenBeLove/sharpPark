<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">
//var yearmonthtitle;
//var agencyIdtitle;
		/*
		$(function(){
			var myDate = new Date();
			var months=myDate.getMonth()+1;
			if(months<10){
				months="0"+months;
			}
			var yearmonth=myDate.getFullYear()+''+months;
			$("#yearmonth").val(yearmonth);
			
			$('#agencyId').combobox({
				url:"${ctx}/agency/agency.do?method=getChildAgencyList",
				valueField:"id",
				textField:"text"
				});
			
			$('#dealType').combobox({
				url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypes",
				valueField:"id",
				textField:"text"
			});
			//进页面不面不加载
			$.feeReport();
		});
		*/
		
		$(function(){
			// 设置起止日期为当日
			var myDate = new Date();
			var month = myDate.getMonth()+1;
			var day = myDate.getDate();
			
			if (month < 10){
				month = "0"+month;
			}
			if (day < 10) {
				day ="0"+day;
			}
			var datestart=myDate.getFullYear()+''+month+day;
			$("#datestart").val(datestart);
			var dateend=myDate.getFullYear()+''+month+day;
			$("#dateend").val(dateend);
			
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
			$.splitFeeView();
		});

		$.exportChildSplitFee=function(yearmonth,agencyIdf,dealType){
			$.getToPost('${ctx}/splitfee/splitfee.do?method=exportChildSplitFee',{
				yearmonth:yearmonth,
				 agencyIdf:agencyIdf,
				 dealType:dealType
		   });
			/*  $.post("${ctx}/splitfee/splitfee.do?method=exportChildSplitFee",
					   {
						 yearmonth:yearmonth,
						 agencyIdf:agencyIdf,
						 dealType:dealType
					   },
					   function(data){
							location.href="${ctx}/export/export.jsp?file="+data.url;
					   },
					   "json"
					   );  */
		};
		
		$.exportSplitFeeDetail= function (agencyId,dealTypeId,yearmonth){
			$.getToPost('${ctx}/splitfee/splitfee.do?method=exportSplitFeeDetail',{
				yearmonth:yearmonth,
				agencyId:agencyId,
				dealTypeId:dealTypeId
		   });
			/* $.post("${ctx}/splitfee/splitfee.do?method=exportSplitFeeDetail",
					   {
						yearmonth:yearmonth,
						agencyId:agencyId,
						dealTypeId:dealTypeId
					   },
					   function(data){
							location.href=data.url;
					   },
					   "json"
					   ); */
		};
		/*
		$.feeReport= function(){
			var yearmonth = $("#yearmonth").val();
			var agencyId=$('#agencyId').combobox("getValue");
			var dealType =$('#dealType').combobox("getValue");
			$.splitFeeView(yearmonth,agencyId,dealType);
		};
		*/
		
		$.success = function(message, data) {
			$.messager.alert("提示", message);
		};
		$.failed = function(message, data) {
			$.messager.alert("提示", message);
		};
		
		// 导出平台结算款确认函文件 20141107
		$.exportConfirmationFile=function(){
			var agencyIdf=$('#agencytreeId').combobox("getValue");
			var datestart = $("#datestart").val();
			var dateend = $("#dateend").val();
			
			if (datestart.substr(0, 6) != dateend.substr(0, 6)) {
				$.messager.alert("提示", "输入的起止日期不在同一个月内");
				return;
			}
			/*
			$.post('${ctx}/splitfee/splitfee.do?method=exportConfirmationFile',{
				agencyIdf : agencyIdf,
				datestart : datestart,
				dateend : dateend
		   }, function(data) {
				$.parseAjaxReturnInfo(data, $.success, $.failed);
			}, "json");
			*/
			$.getToPost('${ctx}/splitfee/splitfee.do?method=exportHisConfirmationFile',{
				agencyIdf : agencyIdf,
				datestart : datestart,
				dateend : dateend
		   });
		};
		
		//$.splitFeeView=function(yearmonth,agencyId,dealType){
		$.splitFeeView=function(){
			var agencyIdf=$('#agencytreeId').combobox("getValue");
			var datestart = $("#datestart").val();
			var dateend = $("#dateend").val();
			//yearmonthtitle=yearmonth;
			
			if (datestart.substr(0, 6) != dateend.substr(0, 6)) {
				$.messager.alert("提示", "输入的起止日期不在同一个月内");
				return;
			}
			
			$('#splitFee').datagrid({
						url : '${ctx}/splitfee/splitfee.do?method=getChildHisSplitFee',
						queryParams : {								
							/*
							yearmonth:yearmonth,
							agencyIdf:agencyId,
							dealType:dealType
							*/
							agencyIdf : agencyIdf,
							datestart : datestart,
							dateend : dateend
						},
						nowrap : false,
						singleSelect : true,
						pageSize : 20,
						width : '100%',
						height :  $(window).height()*0.9,
						fitColumns:true,
						toolbar:[{id:'btnadd',text:'导出确认函',iconCls:'icon-undo',
							handler:function(){
								//$.exportChildSplitFee(yearmonth,agencyId,dealType);
								$.exportConfirmationFile();
							}
						}],
						columns : [ [
						             // 去掉来源系统，机构名称，日期和操作列 20141105
									//{field : "systemName",title : "来源系统",width :'100',align : "center",sortable : true},
									//{field : "agnecyName",title : "机构名称",width :'200',align : "center",sortable : true},
									{field : "dealtypeStr",title : "交易类型",width : 150,align : "center",sortable : true},
									{field : "dealCount",title : "交易总笔数",width :80,align : "center",sortable : true},
									{field : "transCount",title : "交易总金额",width : 100,align : "center",sortable : true},
									//{field : "yearmonth",title : "日期",width : 100,align : "center",sortable : true},
									{field : "amount",title : "分润金额",width : 100,align : "center",sortable : true},
									/*
									{field : "opt",title : "操作",width : "200",align : "left",
										formatter : function(value, data, index) {
										  //var a = "<input type='button' onclick='$.splitFeeDetail("+agencyId+","+dealTypeId+","+yearmonth+")' value='查看交易明细' />";
										  var a = "<input type='button' onclick='$.splitFeeDetail("+index+")' value='查看交易明细' />";
											return a ;
									  	}
									  }
									*/
						] ],
						hideColumn : [ [ 
											 {field : "dealtype"},
											 {field : "agencyId"}
							              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
					});
			var p = $('#splitFee').datagrid('getPager');
			$(p).pagination({
				pageList : [ 20 ],
				beforePageText : '第',
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
		};

		$.splitFeeDetail = function(index){
			$('#splitFee').datagrid('selectRow',index);
			var rows=$('#splitFee').datagrid('getSelected');
			var agencyId = rows.agencyId;
			var dealTypeId = rows.dealtype;
			var yearmonth = rows.yearmonth;
			
			$("#dealdetail").window('open').window('refresh');
			$('#splitFeeDetail').datagrid(
					{
						url : '${ctx}/splitfee/splitfee.do?method=oldSplitFeeDetail',
						queryParams : {								
							yearmonth:yearmonth,
							agencyId:agencyId,
							dealTypeId:dealTypeId
						},
						nowrap : false,
						pageSize : 20,
						width : '100%',
						height : 400,
						fitColumns:false,
						toolbar:[{id:'btnadd',text:'导出',iconCls:'icon-undo',
							handler:function(){
								$.exportSplitFeeDetail(agencyId,dealTypeId,yearmonth);
							}
						}],
						columns : [ [
								
								{field : "agencyName",width :200,title : "机构名称",align : "center"},
								{field : "serialNumber",width :100,title :"流水号",align : "center"},
								{field : "onlyCode",width :200,title : "交易唯一号",align : "center"},
								{field : "dealTypeStr",width :100,title : "交易类型",align : "center"},
								{field : "dealData",width :80,title : "交易日期",align : "center"},
								{field : "transAcount",width :80,title : "交易金额",align : "center"},
								{field : "feeAmt",width :80,title : "手续费金额",align : "center"},
								//{field : "feeAcount",width :80,title : "可分润金额",align : "center"},
								{field : "splitFeeAcount",width : 80,title : "分润金额 ",align : "center"}
								
						] ],
						hideColumn : [ [ 
										 {field : "agencyId"},
										 {field : "dealTypeId"}
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
					});
			var p = $('#splitFeeDetail').datagrid('getPager');
			$(p).pagination({
				pageList : [ 20 ],
				beforePageText : '第',
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
		};

	</script>
</head>
<body>
	<div style="margin:10px 0;"></div>
	<!-- 
	<input id="yearmonth" name="yearmonth" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;" onfocus="WdatePicker({dateFmt:'yyyyMM'})"/>
	<select style="width: 200px;" name="agencyId" editable="false" id="agencyId" class="easyui-combobox"></select>
	<select id="dealType" class="easyui-combobox"name="dealType" data-options="panelHeight:'auto'" style="width: 120px;" editable="false"></select>	
	 -->
	 <table>
	 	<tr>
	 		<td align="left">机构名称：</td>
			<td align="left"><select id="agencytreeId" editable="true" class="easyui-combotree" name="agencytreeId" style="width: 150px;" selected="true"></select></td>
			<td align="left">开始日期：</td>
			<td align="left">
				 <input id="datestart" name="datestart" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateend\')||\'%y-%M-%d\'}',dateFmt:'yyyyMMdd'})"/>
			</td>
			<td align="left">结束日期：</td>
			<td align="left">
				 <input id="dateend" name="dateend" class="Wdate"   type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;height: 20px" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datestart\')}',maxDate:'%y-%M-%d',dateFmt:'yyyyMMdd'})"/>
			</td>

			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.splitFeeView()">查询</a></td>
	 	</tr>
	 </table>
	
	<table id="splitFee"></table>
	
	<div id="dealdetail" class="easyui-window" title="交易明细" closable="true" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 1000px; height: 450px; top: 10px;background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="background: #fff; overflow: hidden;">
				<table id="splitFeeDetail"></table>
			</div>
		</div>
	</div>	

</body>

</html>
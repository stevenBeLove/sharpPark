<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<script type="text/javascript">
var yearmonthtitle;
var agencyIdtitle;
		
		$(function(){
			var myDate = new Date();
			var months=myDate.getMonth()+1;
			if(months<10){
				months="0"+months;
			}
			var yearmonth=myDate.getFullYear()+''+months;
			$("#yearmonth").val(yearmonth);
			$.feeReport();
			//profitView();
		});
		$.exportDealtypeprofit=function(yearmonth,agencyId){
			 $.post("${ctx}/deal/deal.do?method=exportDealtypeprofit",
					   {
						 yearmonth:yearmonth,
						 agencyId:agencyId
					   },
					   function(data){
							location.href="${ctx}/export/export.jsp?file="+data.url;
					   },
					   "json"
					   ); 
		};
		$.exportDealType=function(yearmonth,agencyId,dealtype){
			 $.post("${ctx}/deal/deal.do?method=exportDealType",
					   {
						 yearmonth:yearmonth,
						 agencyId:agencyId,
						 dealtype:dealtype
					   },
					   function(data){
							location.href="${ctx}/export/export.jsp?file="+data.url;
					   },
					   "json"
					   ); 
		};
		$.exportDealDetail=function(yearmonth,agencyId){
			 $.post("${ctx}/deal/deal.do?method=exportDealDetail",
					   {
						 yearmonth:yearmonth,
						 agencyId:agencyId
					   },
					   function(data){
							location.href="${ctx}/export/export.jsp?file="+data.url;
					   },
					   "json"
					   ); 
		};
		//导出交易按机构汇总数据
		$.exportAgencydeal=function(){
			var yearmonth = $("#yearmonth").val();
			 $.post("${ctx}/deal/deal.do?method=agencydealExport",
			   {
				 yearmonth:yearmonth					 
			   },
			   function(data){
					location.href="${ctx}/export/export.jsp?file="+data.url;
			   },
			   "json"
			   ); 
		};
		$.exportDeal=function(){
			var yearmonth = $("#yearmonth").val();
			 $.post("${ctx}/deal/deal.do?method=agencydealExport",
			   {
				 yearmonth:yearmonth					 
			   },
			   function(data){
					location.href="${ctx}/export/export.jsp?file="+data.url;
			   },
			   "json"
			   ); 
		};
		$.feeReport = function(){
			var yearmonth = $("#yearmonth").val();
			$('#feeReport').treegrid({      
				rownumbers: true,
				title:'分润报表',
				width:$(window).width()-6,
				height : $(window).height()*0.9,
				animate: true,
				collapsible: true,
				fitColumns: true,
				url: '${ctx}/deal/deal.do?method=getProfit',
				method: 'get',
				idField: 'agencyId',
				treeField: 'agencyName',
				loadFilter: $.pagerFilter,
				pagination: true,
				pageSize: 2,
				queryParams : {								
									yearmonth : yearmonth
								},
				pageList: [2,5,10],
				columns:[[    
				           {field:'agencyName',title:'机构名称',width:180},
				           {field:'yearmonth',title:'交易年月',width:60,align:'right'},  
				           {field:'dealCount',title:'总交易笔数',width:80} ,
				           {field:'dealCountagency',title:'机构总交易笔数',width:80} ,
				           {field:'transCount',title:'交易总金额',width:80,
				        	   formatter:function(value,row,index){
				        		   return "<a title='交易明细' onclick=$.dealView('"+row.yearmonth+"','"+row.agencyId+"')>"+value+"</a>"; 
				           }}, 
				           {field:'transCountagency',title:'机构交易总金额',width:80,
				        	   formatter:function(value,row,index){
				        		   return "<a title='交易明细' onclick=$.dealView('"+row.yearmonth+"','"+row.agencyId+"')>"+value+"</a>"; 
				           }}, 
				           {field:'amount',title:'可分润金额',width:80,hidden:true} ,
				           {field:'amountagency',title:'机构可分润金额',width:80,hidden:true} ,
				           {field:'amounted',title:'分润到金额',width:80,
				        	   formatter:function(value,row,index){
				        		   return "<a title='交易类型汇总' onclick=$.profitView('"+row.yearmonth+"','"+row.agencyId+"')>"+value+"</a>";   
				          }}    
				]]    

			  
			});  
		};
		
		
		$.pagerFilter = function(data){
            if ($.isArray(data)){    // is array  
                data = {  
                    total: data.length,  
                    rows: data  
                };  
            }
            var dg = $(this);  
			var state = dg.data('treegrid');
            var opts = dg.treegrid('options');  
            var pager = dg.treegrid('getPager');  
            pager.pagination({  
                onSelectPage:function(pageNum, pageSize){  
                    opts.pageNumber = pageNum;  
                    opts.pageSize = pageSize;  
                    pager.pagination('refresh',{  
                        pageNumber:pageNum,  
                        pageSize:pageSize  
                    });  
                    dg.treegrid('loadData',data);  
                }  
            });  
            if (!data.topRows){  
            	data.topRows = [];
            	data.childRows = [];
            	for(var i=0; i<data.rows.length; i++){
            		var row = data.rows[i];
            		row._parentId ? data.childRows.push(row) : data.topRows.push(row);
            	}
				data.total = (data.topRows.length);
            }  
            var start = (opts.pageNumber-1)*parseInt(opts.pageSize);  
            var end = start + parseInt(opts.pageSize);  
			data.rows = $.extend(true,[],data.topRows.slice(start, end).concat(data.childRows));
			return data;
		};
		$.dealtypeView=function(yearmonth,agencyId,dealtype){
			$("#dealtypedetail").window('open').window('refresh');
			$('#dealtypeList').datagrid(
					{
						url : '${ctx}/deal/deal.do?method=getDealtypedetail',
						queryParams : {								
							yearmonth:yearmonth,
							agencyId:agencyId,
							dealtype:dealtype
						},
						nowrap : false,
						pageSize : 20,
						width : '100%',
						height : $("#win").height() * 0.97,
						fitColumns:true,
						toolbar:[{id:'btnadd',text:'导出',iconCls:'icon-undo',
							handler:function(){
								$.exportDealType(yearmonth,agencyId,dealtype);
							}
						}],
						columns : [ [
								
								{field : "agencyName",width : $(window).width()*0.12,title : "机构名称",align : "center"},
								{field : "dealtype_id",width : $(window).width()*0.12,title : "交易类型",align : "center"},
								{field : "serialNumber",width : $(window).width()*0.14,title : "交易流水号",align : "center"},
								{field : "deal_data",width : $(window).width()*0.3,title : "交易日期",align : "center"},
								{field : "transacount",width : $(window).width()*0.12,title : "交易金额",align : "center"},
								{field : "cost",width : $(window).width()*0.14,title : "可分润金额",align : "center"},
								{field : "dealrebackcode",width : $(window).width()*0.14,title : "交易返回码",align : "center"},
								{field : "charge",width : $(window).width()*0.1,title : "手续费 ",align : "center"},
								{field : "dealdesc",width : $(window).width()*0.13,title : "交易描述 ",align : "center"},
								{field : "deal_status",width : $(window).width()*0.12,title : "交易状态 ",align : "center"},
								{field : "onlyCode",width : $(window).width()*0.36,title : "唯一编码",align : "center"},
								{field : "sysSource",width : $(window).width()*0.12,title : "来源系统",align : "center"},
								
						] ],
						onLoadSuccess:function(data){
							
						},
						pagination : true,
						rownumbers : true,
						showFooter : true
					});
			var p = $('#profitList').datagrid('getPager');
			$(p).pagination({
				pageList : [ 20 ],
				beforePageText : '第',
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
			
		};
		$.dealView=function(yearmonth,agencyId){
			yearmonthtitle=yearmonth;
			agencyIdtitle=agencyId;
			$("#dealdetail").window('open').window('refresh');
			$('#dealList').datagrid(
					{
						url : '${ctx}/deal/deal.do?method=getDealdetail',
						queryParams : {								
							yearmonth:yearmonth,
							agencyId:agencyId
						},
						nowrap : false,
						pageSize : 20,
						width : '100%',
						height : $("#win").height() * 0.97,
						fitColumns:true,
						toolbar:[{id:'btnadd',text:'导出',iconCls:'icon-undo',
							handler:function(){
								$.exportDealDetail(yearmonth,agencyId);
							}
						}],
						columns : [ [
								
								{field : "agencyName",width : $(window).width()*0.1,title : "机构名称",align : "center"},
								{field : "dealtype_id",width : $(window).width()*0.1,title : "交易类型",align : "center"},
								{field : "serialNumber",width : $(window).width()*0.12,title : "交易流水号",align : "center"},
								{field : "deal_data",width : $(window).width()*0.3,title : "交易日期",align : "center"},
								{field : "transacount",width : $(window).width()*0.1,title : "交易金额",align : "center"},
								{field : "cost",width : $(window).width()*0.12,title : "可分润金额",align : "center"},
								{field : "dealrebackcode",width : $(window).width()*0.12,title : "交易返回码",align : "center"},
								{field : "charge",width : $(window).width()*0.1,title : "手续费 ",align : "center"},
								{field : "dealdesc",width : $(window).width()*0.1,title : "交易描述 ",align : "center"},
								{field : "deal_status",width : $(window).width()*0.1,title : "交易状态 ",align : "center"},
								{field : "onlyCode",width : $(window).width()*0.32,title : "唯一编码",align : "center"},
								{field : "sysSource",width : $(window).width()*0.1,title : "来源系统",align : "center"},
								
						] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
					});
			var p = $('#profitList').datagrid('getPager');
			$(p).pagination({
				pageList : [ 20 ],
				beforePageText : '第',
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
			
		};
		$.profitView = function (yearmonth,agencyId) {
			$("#win").window('open').window('refresh');
			$('#profitList').datagrid(
							{
								url : '${ctx}/deal/deal.do?method=getDealByType',
								queryParams : {								
									yearmonth:yearmonth,
									agencyId:agencyId
								},
								nowrap : false,
								pageSize : 20,
								width : '100%',
								height : $("#win").height() * 0.97,
								fitColumns:true,
								toolbar:[{id:'btnadd',text:'导出',iconCls:'icon-undo',
									handler:function(){
										$.exportDealtypeprofit(yearmonth,agencyId);
									}
								}],
								columns : [ [
										{field : "agencyId",width : $(window).width()*0.1,title : "机构编号",align : "center",hidden : true},
										{field : "parentagencyId",width : $(window).width()*0.1,title : "父机构编号",align : "center",hidden : true},
										{field : "agnecyName",width : $(window).width()*0.1,title : "机构名称",align : "center"},
										{field : "dealtype",width : $(window).width()*0.1,title : "交易类型",align : "center"},
										{field : "yearmonth",width : $(window).width()*0.1,title : "交易年月",align : "center"},
										{field : "dealCount",width : $(window).width()*0.15,title : "总笔数",align : "center",
											 formatter:function(value,row,index){
												  return "<a title='交易类型明细' onclick=$.dealtypeView('"+row.yearmonth+"','"+row.agencyId+"','"+row.dealtype+"')>"+value+"</a>"; 
											 }
											},
										{field : "transCount",width : $(window).width()*0.1,title : "交易金额",align : "center"},
										{field : "amount",width : $(window).width()*0.15,title : "可分润金额",align : "center"},
										{field : "amounted",width : $(window).width()*0.15,title : "分润到的金额",align : "center"},
										{field : "fee",width : $(window).width()*0.1,title : "手续费 ",align : "center",hidden : false},
										
								] ],
								onLoadSuccess:function(data){
									
								},
								pagination : true,
								rownumbers : true,
								showFooter : true
							});
			var p = $('#profitList').datagrid('getPager');
			$(p).pagination({
				pageList : [ 20 ],
				beforePageText : '第',
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
		}
	</script>
</head>
<body>
	<div style="margin:10px 0;"></div>
	<input id="yearmonth" name="yearmonth" class="Wdate"  type="text" SelectedDate="{x:Static sys:DateTime.Now}" style="width: 120px;" onfocus="WdatePicker({dateFmt:'yyyyMM'})"/>   
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.feeReport()">查询</a>
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="$.exportAgencydeal()">导出</a>
	<table id="feeReport"></table> 
	  
	<div id="win" class="easyui-window" title="交易类型汇总" closable="true" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 900px; height: 450px; top: 10px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding:0px;background: #fff; overflow: hidden;">
				<table id="profitList"></table>
			</div>
		</div>
	</div>
	<div id="dealdetail" class="easyui-window" title="交易明细" closable="true" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="width: 1100px; height: 450px; top: 10px;background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="background: #fff; overflow: hidden;">
				<table id="dealList"></table>
			</div>
		</div>
	</div>
	<div id="dealtypedetail" title="交易类型查看" class="easyui-window"  closable="true" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="true" draggable="true"
		style="left: 170px;width: 980px; height: 450px; top: 30px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding:0px;background: #fff; overflow: hidden;">
				<table id="dealtypeList"></table>
			</div>
		</div>
	</div>
</body>

</html>
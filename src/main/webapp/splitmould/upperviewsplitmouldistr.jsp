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
		$.extend($.fn.datagrid.methods, {
	    addEditor : function(jq, param) {
	        if (param instanceof Array) {
	            $.each(param, function(index, item) {
	                var e = $(jq).datagrid('getColumnOption', item.field);
	                e.editor = item.editor;
	            });
	        } else {
	            var e = $(jq).datagrid('getColumnOption', param.field);
	            e.editor = param.editor;
	        }
	    },
	    removeEditor : function(jq, param) {
	        if (param instanceof Array) {
	            $.each(param, function(index, item) {
	                var e = $(jq).datagrid('getColumnOption', item);
	                e.editor = {};
	            });
	        } else {
	            var e = $(jq).datagrid('getColumnOption', param);
	            e.editor = {};
	        }
	    }
	});
	$.viewDistrMoulds();	
	//页面上的模版名称下拉框,可以看到自己做的模板
	$('#mouldname').combobox({
		url:"${ctx}/splitmould/splitmould.do?method=getTopMouldList",
		valueField:"id",
		textField:"text"
		});
});

$(window).resize(function(){
	 $('#viewDistrMoulds').datagrid('resize', {
	 	width:cs()
	 });
	
}); 
function cs(){	
	return $(window).width()-6;
}



$.succ= function(message, data){
	$.messager.alert("提示 ", message);
	var ruleId = $("#ruleId").val();
	$.viewSingleDealSection(ruleId);
};
$.fail= function(message, data) {
	
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};

$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
};


$.success= function(message, data){
	$.messager.alert("提示 ", message);
	$.viewSplitRule();
};
$.failed = function(){
	$.messager.alert("提示 ", message);
};
$.viewDistrMoulds = function() {
	var mouldName = $("#mouldname").combobox('getValue');
	if (mouldName == -1){
		mouldName="";
	}
	$('#viewDistrMoulds').datagrid(
					{
						title : '分润模板分配详情',
						//width:750,
						width : $(window).width()-6,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/splitmould/splitmould.do?method=getDistrMoulds",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							mouldName : mouldName,
							status: ''
						},
						//定义双击某一行，打开查看模板详情
						onDblClickRow: function(rowIndex, rowData){
						$.showDivShade('${ctx}');
						//打开'updatesplitrule' 这个DIV
					 	$('#updatesplitrule').window({title: '模板详细信息'});
					 	var ruleId = rowData.ruleId;
					 	var agencyId = rowData.agencyId;
					 	//给窗口里的dataguid赋值
						$.viewDistrMouldsViewD(ruleId,agencyId);
						//打开updatesplittopmould
						//给页面里的隐藏域赋值
						$("#ruleIdU").val(ruleId);
						$("#agencyIdU").val(agencyId);
						$("#distrMouldsView").window('open').window('refresh');
					},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field:"mouldName",title:"模板名称",width :200,align : "center",sortable : true},
										{field:"ruleId",title:"模板编码",width :130,align : "center",sortable : true},
										{field:"agencyName",title:"机构名称",width :200,align : "center",sortable : true},
										{field:"agencyNameD",title:"被分配至",width :200,align : "center",sortable : true},
									//	{field:"ruleRem",title:"备注",width :300,align : "center",sortable : true},
										] ],
						hideColumn : [ [ 
										 {field:"agencyId"},
						                 {field:"ruleId"},
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
						
					});
	var p = $('#viewDistrMoulds').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
};

//模板分配 详细情况
$.viewDistrMouldsViewD = function(ruleId,agencyId){
	var editing;
	$('#distrMouldsViewD').datagrid(
					{
						title : '分润模板分配详情（双击可修改）',
						width:840,
						height : $(window).height()*0.7,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/splitmould/splitmould.do?method=getDistrMouldsD",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						//定义双击事件
						onDblClickCell: function(index,field,value){
							editing = index;
							$(this).datagrid('beginEdit', index);
							var ed = $(this).datagrid('getEditor', {index:index,field:field});
							$(ed.target).focus();
							},
						//定义单击事件
						onSelect : function(rowIndex, rowData){
							if(rowIndex != editing && editing != undefined){
									$("#distrMouldsViewD").datagrid("endEdit",editing);
									editing = undefined;
								}
							},
						queryParams:{
							ruleId : ruleId,
							agencyId : agencyId
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field:"dealTypeName",title:"业务名称",width :100,align : "center",sortable : false},
										{field:"splittingModeStr",title:"分润方式",width :130,align : "center",sortable : false},
										{field:"splittingRegionModeStr",title:"分润规则",width :100,align : "center",sortable : false},
										{field:"ruleBegin",title:"规则起点",width :130,align : "center",sortable : false},
										{field:"ruleEnd",title:"规则终点",width :130,align : "center",sortable : false},
										{field:"ruleValue",title:"规则值" ,width :70,align : "center",
											formatter: function(value,row,index){
														if(row.splittingMode == "1"){
															$("#distrMouldsViewD").datagrid('addEditor', {
										                        field : 'ruleValue',
										                        editor : {
										                            type : 'numberbox',
										                            options : {
										                               precision:3
										                            }
										                        }
										                    });
															
															return row.ruleValue + "元/笔";
															}
														if(row.splittingMode == "2"){
															$("#distrMouldsViewD").datagrid('addEditor', {
											                        field : 'ruleValue',
											                        editor : {
											                            type : 'numberbox',
											                            options : {
											                               precision:3
											                            }
											                        }
											                    });
															return row.ruleValue + "%";
															}	
														}
											},
										{field:"validityDate",title:"生效期",width :70,align : "center",sortable : false},
										] ],
						hideColumn : [ [ 
										{field:"ruleId"},
										{field:"agencyId"},
						                {field:"ruleNum"},
						                {field:"dealType"},
						                {field:"splittingMode"},
						                {field:"splittingRegionMode"},
						                {field:"createrId"},
						                {field:"ruleRem"},
						              ] ],
						pagination : true,
						rownumbers : true,
						showFooter : true
						
					});
};

$.close = function(obj) {
	$.hideDivShade();
	var flag = obj;
	
	//关闭添加顶级模板窗口
	if(flag == "-1"){
	$("#distrMouldsView").window('close');
	}
	$.viewDistrMoulds();
};

//更新机构分润模板内容的方法
$.saveupdate = function() {
	var data = $('#distrMouldsViewD').datagrid('getData');
	var rows = data.rows;
	var ruleId = $("#ruleIdU").val();
	var agencyId = $("#agencyIdU").val();
	
		$.ajax("${ctx}/splitmould/splitmould.do?method=updateOrgMouldD&ruleId="+ruleId+"&agencyId="+agencyId,
				 {type:'POST'
	              ,contentType:'application/json'
	              ,data:$.toJSON(rows)
	              ,dataType:'json'
	              ,success:function (data) {
	                $.parseAjaxReturnInfo(data, $.success, $.failed);
	              }
	            }
	    );
	    $.close(-1);
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
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewDistrMoulds()">查询</a>
			</td>			
		</tr>
	</table>
	<table id="viewDistrMoulds"></table>
	
	
	<div id="distrMouldsView" class="easyui-window" title="分配模板" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 1000px; height: 700px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdU" name="ruleIdU" />
				<input type="hidden" id="agencyIdU" name="agencyIdU" />
					<table width="100%">
						<tr>
						<td colspan="8" align="center" valign="top" >
							<table id="distrMouldsViewD" style="width:600px;height:280px;"></table>
							</td>		
						</tr>
						<tr style="height: 50px">
							<td align="center" colspan="4">
								<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.saveupdate()">保存修改</a>
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
</body>
</html>
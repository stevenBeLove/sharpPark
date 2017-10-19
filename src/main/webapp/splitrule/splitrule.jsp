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
	$.viewSplitRule();
	$('#agencyId').combobox({
		url:"${ctx}/agency/agency.do?method=getChildAgencyList",
		valueField:"id",
		textField:"text"
		});
});

$(window).resize(function(){
	 $('#viewSplitRule').datagrid('resize', {
	 	width:cs()
	 });
	
}); 
function cs(){	
	return $(window).width()-6;
}
var flag;
$.openWin = function(obj) {
	$.showDivShade('${ctx}');
	$('#splitrule').window({title: '添加分润规则信息'});
	$("#childAgencyId").combobox('setValue', -1);
	$("#dealType").combobox('clear');
	$("#splitMode").combobox('setValue', 0);
	$("#scale").combobox('setValue',0);
	$("#validityData").val('');
	$('#scale').combobox({
		 onSelect: function(rec){
			 var childAgencyId =$("#childAgencyId").combobox('getValue');
			var dealType =$("#dealType").combobox('getValue');
			if(flag != "-1"){
				//修改
				$.viewScaleSection(childAgencyId,dealType);
			}else{
				//添加
				$.viewScaleSection('','');
			}
			
			 $.viewSingleDealSection('');
		 }
	});
	$('#splitMode').combobox({
		 onSelect: function(rec){
			 $.viewSingleDealSection('');
		 }
	});
	$('#childAgencyId').combobox({
		url:"${ctx}/agency/agency.do?method=getChildAgencyList",
		valueField:"id",
		textField:"text",
		onSelect:function(res){
			$('#dealType').combobox({
				url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypesByagencyId&agencyId="+res.id,
				valueField:"id",
				textField:"text",
				onLoadSuccess:function(){
					if(flag != "-1"){
						 $('#splitrule').window({title: '修改分润规则信息'});
						 $("#splitrule").window('open').window('refresh');
						 var row = $('#viewSplitRule').datagrid('getSelected');
						 $("#splitMode").combobox('setValue',row.splittingMode);
							$("#dealType").combobox('setValue', row.dealType);
							$("#scale").combobox('setValue',row.scale);
							$("#validityData").val(row.validityData);
							$("#ruleId").val(row.ruleId);
							//ruleIds = row.ruleId;
							var childAgencyId =$("#childAgencyId").combobox('getValue');
							var dealType =$("#dealType").combobox('getValue');
							$.viewScaleSection(childAgencyId,dealType);
							$.viewSingleDealSection('');
					}
				}
				});
		},
		onLoadSuccess:function(){
		
			 var childAgencyId =$("#childAgencyId").combobox('getValue');
				var dealType =$("#dealType").combobox('getValue');
				$.viewScaleSection(childAgencyId,dealType);
				 $.viewSingleDealSection('');
				 flag = obj;
				 if(flag != "-1"){
						var rows = $('#viewSplitRule').datagrid('getSelections');
						if (rows.length > 1) {
							$.hideDivShade();
							$.messager.alert("提示 ", "只能对单条记录修改");
							return;
						}else if(rows.length ==0){
							$.hideDivShade();
							$.messager.alert("提示 ", "请选择要修改的记录");
							return;
						}
						var row = $('#viewSplitRule').datagrid('getSelected');
						$("#childAgencyId").combobox('select', row.childAgencyId);
				 }else{
						$("#splitrule").window('open').window('refresh');
				 }
		    }
		});

	
};

$.viewScaleSection = function(childAgencyId,dealType){
	editIndex = undefined;
	var splitMode =$("#splitMode").combobox('getValue');
	var scale =$("#scale").combobox('getValue');
	var temp = "(元)";
	var singleTemp ="(元)";
	if(splitMode=='1'&&scale=="1"){
		singleTemp = "(笔)";
	}


	
	if(scale =='1'){
		temp ="(笔)";
	}
	
	
	
	$('#scaleSection').datagrid(
			{
				title : '交易规模区间',
				width:300,
				height : 200,
				url : "${ctx}/splitrule/splitrule.do?method=getRulesByIds",
				singleSelect: true,
				rownumbers : true,
				queryParams:{
					childAgencyId:childAgencyId,
					dealType:dealType
				},
				loadMsg : '数据载入中,请稍等！',
				remoteSort : false,
				columns : [ [
								
								{field:"scaleStartValue",title:"起始值"+temp,width:'100',align:'center',editor:{type:'numberbox',options:{precision:2}}},
								{field:"scaleEndValue",title:"结束值"+temp,width:'100',align:'center',
									formatter: function(value,row,index){
										if (value =="999999999999999999999"){
											value= "";
										} 
											return value;
										
									}
								}
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
$.save = function() {
	
	//var ruleIds =$("#ruleIds").val();
	var childAgencyId =$("#childAgencyId").combobox('getValue');
	var dealType =$("#dealType").combobox('getValue');
	var splitMode =$("#splitMode").combobox('getValue');
	var scale =$("#scale").combobox('getValue');
	var validityData =$("#validityData").val();
	if(splitMode=="0"){
		$.messager.alert("提示 ", "请选择分润依据");
		return false;
	}
	if(scale=="0"){
		$.messager.alert("提示 ", "请选择交易规模");
		return false;
	}
	if (childAgencyId == "-1") {
		$.messager.alert("提示 ", "请选择子机构名称");
		return false;
	}
	
	if (dealType == "-1") {
		$.messager.alert("提示 ", "请选择交易类型");
		return false;
	}
	if (validityData == "") {
		$.messager.alert("提示 ", "请输入生效日期");
		return false;
	}
	if (endEditing()){
		$('#scaleSection').datagrid('acceptChanges');
	}
	var data = $('#scaleSection').datagrid('getData');
	var rows = data.rows;
	for(var i=0;i<rows.length;i++){
		if(parseFloat(rows[0].scaleStartValue)!='0.00'){
			$.messager.alert("提示","规则区间必须从0开始!");
			return;
		}
		if(rows[i].scaleStartValue!=""&&rows[i].scaleStartValue!=null){
			if(parseFloat(rows[i].scaleEndValue)!='0'){
				if(parseFloat(rows[i].scaleStartValue)>=parseFloat(rows[i].scaleEndValue)){
					$.messager.alert("提示","第"+(i+1)+"条区间中起始值必须小于结束值!");
					return;
				}
			}
			
			
		}
		if(i!=rows.length-1){
			if(rows[i+1].scaleStartValue!=""&&rows[i+1].scaleStartValue!=null){
				if(parseFloat(rows[i].scaleEndValue)!=parseFloat(rows[i+1].scaleStartValue)){
					$.messager.alert("提示","第"+(i+1)+"与第"+(i+1+1)+"条区间存在断链或交叉,请修改!");
					return;
				}
			}
		}
		
	}
	
	$('#save').linkbutton('disable');
		$.ajax("${ctx}/splitrule/splitrule.do?method=addRule&childAgencyId="+childAgencyId+"&dealType="+dealType+"&splittingMode="+splitMode+"&scale="+scale+"&validityData="+validityData
	            , {type:'POST'
	              ,contentType:'application/json'
	              ,data:$.toJSON(rows)
	              ,dataType:'json'
	              ,success:function (data) {
	                $.parseAjaxReturnInfo(data, $.scaleSucc, $.fail);
	              }
	            }
	    );	
	

};

$.singleOk = function (){
	if (singleEndEditing()){
		$('#singleDealSection').datagrid('acceptChanges');
	}
	var ruleId = $("#ruleId").val();
	if(ruleId==null||ruleId==""){
		$.messager.alert("提示","请选择交易规模区间！");
		return ;
	}
	var splitMode =$("#splitMode").combobox('getValue');
	var scale =$("#scale").combobox('getValue');
	var data = $('#singleDealSection').datagrid('getData');
	
	
	var count=0;
	var rows = data.rows;
	for(var i=0;i<rows.length;i++){
		if(parseFloat(rows[0].singleDealStartValue)!='0.00'){
			$.messager.alert("提示","规则区间必须从0开始!");
			return;
		}
		if(rows[i].singleDealStartValue!=""&&rows[i].singleDealStartValue!=null){
			if(rows[i].singleDealEndValue!=''){
				if(parseFloat(rows[i].singleDealStartValue)>=parseFloat(rows[i].singleDealEndValue)){
					$.messager.alert("提示","第"+(i+1)+"条单笔区间中起始值必须小于结束值!");
					return;
				}
			}
			if(rows[i].rate==null||rows[i].rate==""){
				$.messager.alert("提示","请输入费率!");
				return;
			}
		}
		if(i!=rows.length-1){
			if(rows[i+1].singleDealStartValue!=""&&rows[i+1].singleDealStartValue!=null){
				if(rows[i].singleDealEndValue!=rows[i+1].singleDealStartValue){
					$.messager.alert("提示","第"+(i+1)+"与第"+(i+1+1)+"条单笔区间存在断链或交叉,请修改!");
					return;
				}
			}
		}
		if(rows[i].rate!=null && $.trim(rows[i].rate)!=""){
			count++;
		}
		
	}
	if(scale=='1' && splitMode=='1' && count>1){
		$.messager.alert("提示","只能设置一个费率");
		return;
	}
	
	if(splitMode=='3' && count>1){
		$.messager.alert("提示","只能设置一个费率");
		return;
	}
	
	
	
	$.ajax("${ctx}/splitrule/splitrule.do?method=addSingleRule&ruleId="+ruleId+"&splitMode="+splitMode+"&scale="+scale
            , {type:'POST'
              ,contentType:'application/json'
              ,data: $.toJSON(rows)
              ,dataType:'json'
              ,success:function (data) {
                $.parseAjaxReturnInfo(data, $.succ, $.fail);
              }
            }
    );	
	
};
$.succ= function(message, data){
	$.messager.alert("提示 ", message);
	var ruleId = $("#ruleId").val();
	$.viewSingleDealSection(ruleId);
};
$.fail= function(message, data) {
	
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
};
$.scaleSucc = function(message, data) {
	var childAgencyId =$("#childAgencyId").combobox('getValue');
	var dealType =$("#dealType").combobox('getValue');
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.viewScaleSection(childAgencyId,dealType);
};
$.failed = function(message, data) {
	$.messager.alert("提示 ", message);
	$('#save').linkbutton('enable');
	$.close();
};
$.close = function() {
	$.hideDivShade();
	$("#splitrule").window('close');
	$.viewSplitRule();
};

$.deleteRole = function() {
	var rows = $('#viewSplitRule').datagrid('getSelected');
	if(rows==null){
		$.messager.alert("提示 ", "请选择要删除的记录!");
		return;
	}
	var childAgencyId = rows.childAgencyId;
	var dealType = rows.dealType;
	var validityData = rows.validityData;

	$.messager.confirm("提示","确定删除？",function(r){
		if(r){
	$.post("${ctx}/splitrule/splitrule.do?method=deleteRules", {
		childAgencyId : childAgencyId,
		dealType:dealType,
		validityData:validityData
	}, function(data) {
		$.parseAjaxReturnInfo(data, $.success, $.failed);
	}, "json");}});
};
$.success= function(message, data){
	$.messager.alert("提示 ", message);
	$.viewSplitRule();
};
$.failed = function(){
	$.messager.alert("提示 ", message);
};
$.viewSplitRule = function() {
	var childagencyId = $("#agencyId").combobox('getValue');
	if(childagencyId == "-1"){
		childagencyId="";
	}
	$('#viewSplitRule').datagrid(
					{
						title : '分润规则',
						width:$(window).width()*0.98,
						height : $(window).height()*0.9,
						nowrap : true,
						fitColumns:false,
						url : "${ctx}/splitrule/splitrule.do?method=getRules",
						pageSize : 20,
						pageNumber : 1,
						singleSelect : true,
						queryParams:{
							childagencyId : childagencyId
						},
						loadMsg : '数据载入中,请稍等！',
						remoteSort : false,
						columns : [ [
										{field:"childAgencyName",title:"子机构名称",width :100,align : "center",sortable : true},
										{field:"splittingModeStr",title:"分润方式",width :130,align : "center",sortable : true},
										{field:"dealTypeName",title:"交易类型",width :100,align : "center",sortable : true},
										{field:"scaleStr",title:"交易规模",width :100,align : "center",sortable : true},
										{field:"validityData",title:"规则生效日期",width :130,align : "center",sortable : true},
 								  	    {field:"createrId",title:"创建人",width :100,align : "center",sortable : true},
										{field:"createDate",title:"创建时间",width :180,align : "center",sortable : true}
										 ] ],
						hideColumn : [ [ 
										 {field:"agencyId"},
						                 {field:"ruleId"},
						                 {field:"childAgencyId"},
						                 {field:"splittingMode"},
						                 {field:"dealType"},
						                 {field:"scale"}
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
								 {id:'btncut',text:'修改',iconCls:'icon-edit',
									handler:function(){
											$.openWin(-2);
									}
								 },'-',
								 {id:'btnsave',text:'删除',iconCls:'icon-cut',
									handler:function(){
										$.deleteRole();
									}
								}
						]
						
					});
	var p = $('#viewSplitRule').datagrid('getPager');
	$(p).pagination({
		pageList : [20],
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
			<td align="left">机构编号：</td>
			<td align="left"><select style="width: 200px;" name="agencyId" editable="false" id="agencyId" class="easyui-combobox"></select>
			</td> 
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewSplitRule()">查询</a>
			</td>			
		</tr>
		
	</table>
		<table id="viewSplitRule"></table>
		
	<div id="splitrule" class="easyui-window" title="添加分润规则信息" closable="false"
		closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="false"
		style="width: 750px; height: 470px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="true"
				style="padding:20px;background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIds" name="ruleIds" />
					<table width="100%">
						<tr style="height: 35px">
							<td align="right">子机构名称：</td>
							<td align="left">
								<select id="childAgencyId" class="easyui-combobox" name="childAgencyId" style="width: 120px;" editable="false"></select>
							</td>
							<td align="right">交易类型：</td>
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
									<option value="1">按总笔数分润</option>
									<option value="2">按总金额分润</option>
									<option value="3">按收益分润</option>
								</select>
							</td>
							<td align="right">生效日期：</td>
							<td>
								<input id="validityData" name="validityData" class="Wdate"  type="text" style="width: 120px;" onfocus="WdatePicker({dateFmt:'yyyy-MM'})" readonly/>
								
							</td>
						</tr>
						<tr  style="height: 35px">
							<td align="right">交易规模：</td>
							<td>
								<select id="scale" class="easyui-combobox" name="scale" data-options="panelHeight:'auto'" style="width: 120px;" editable="false">
									<option value="0">请选择</option>
									<option value="1">总笔数</option>
									<option value="2">总金额</option>
								</select>
							</td>
						</tr>
						
						<tr>
						
							<td colspan="2" align="left" valign="top">
								<!-- <input  class="easyui-numberbox" id="scaleStartValue" name="scaleStartValue" style="width: 80px;"/>~
								<input class="easyui-numberbox" id="scaleEndValue" name="scaleEndValue" style="width: 80px;"/> -->
									<table id="scaleSection" class="easyui-datagrid" title="交易规模区间" style="width:220px;height:280px;"
											data-options="
												iconCls: 'icon-edit',
												onClickCell: onClickCell,
												onClickRow:onClickRow
											">
										<thead>
											<tr>
												<th data-options="field:'code',width:20">NO</th>
												<th data-options="field:'scaleStartValue',width:100,align:'center',editor:{type:'numberbox',options:{precision:2}}">起始区间</th>
												<th data-options="field:'scaleEndValue',width:100,align:'center',editor:{type:'numberbox',options:{precision:2}}">结束区间</th>
											</tr>
										</thead>
									</table>
									<br/>
									<a name="save" id="save"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.save()">交易规模区间确认</a>
							</td>
							
							<td colspan="2" align="right" valign="top">
								<!-- <input  class="easyui-numberbox" id="scaleStartValue" name="scaleStartValue" style="width: 80px;"/>~
								<input class="easyui-numberbox" id="scaleEndValue" name="scaleEndValue" style="width: 80px;"/> -->
								<input type="hidden" id="ruleId" name="ruleId" />
									<table id="singleDealSection" class="easyui-datagrid" title="单笔交易区间" style="width:300px;height:280px;"
											data-options="
												iconCls: 'icon-edit',
												singleSelect: true,
												onClickRow: singleOnClickCell
											">
										<thead>
											<tr>
												<th data-options="field:'code',width:20">NO</th>
												<th data-options="field:'singleDealStartValue',width:100,align:'center',editor:{type:'numberbox',options:{precision:2}}">起始区间</th>
												<th data-options="field:'singleDealEndValue',width:100,align:'center',editor:{type:'numberbox',options:{precision:2}}">结束区间</th>
												<th data-options="field:'rate',width:80,align:'center',editor:{type:'numberbox',options:{precision:3}}">费率</th>
											</tr>
										</thead>
									</table>
									<br/>
									<a name="ok" id="ok"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-save'" onclick="$.singleOk()">单笔交易区间确认</a>
							</td>
						
							
						</tr>
						
						<tr>
							<td align="center" colspan="4">
								<a name="close" id="close"
								href="#" class="easyui-linkbutton"
								data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
							</td>
						</tr>
					</table>
			</div>
		</div>
	</div>
		<script type="text/javascript">
		var editIndex = undefined;
		var editSingleIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true;}
			if ($('#scaleSection').datagrid('validateRow', editIndex)){
				$('#scaleSection').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (editIndex != index){
				if (endEditing()){
					$('#scaleSection').datagrid('selectRow', index)
							.datagrid('beginEdit', index);
					editIndex = index;
				} else {
					$('#scaleSection').datagrid('selectRow', editIndex);
				}
			}
		}
		
		
	
		function singleEndEditing(){
			if (editSingleIndex == undefined){return true;}
			if ($('#singleDealSection').datagrid('validateRow', editSingleIndex)){
				$('#singleDealSection').datagrid('endEdit', editSingleIndex);
				editSingleIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function singleOnClickCell(index){
			if (editSingleIndex != index){
				if (singleEndEditing()){
					$('#singleDealSection').datagrid('selectRow', index)
							.datagrid('beginEdit', index);
					editSingleIndex = index;
				} else {
					$('#singleDealSection').datagrid('selectRow',editSingleIndex);
				}
			}
		}
		
		function onClickRow(rowIndex, rowData){
			$('#scaleSection').datagrid('selectRow', rowIndex);
			var ruleId = rowData.ruleId;
			$("#ruleId").val(ruleId);
			if(ruleId!=""&&ruleId!=null){
				$.viewSingleDealSection(ruleId);
			}
			
		}
		$.viewSingleDealSection= function(ruleId){
			editSingleIndex = undefined;
			var splitMode =$("#splitMode").combobox('getValue');
			var scale =$("#scale").combobox('getValue');
			var singleTemp ="(元)";
			var rateTemp ="(%)";
			if(splitMode=='1'&&scale=="1"){
				singleTemp = "(元)";
				rateTemp="(元)";
			}
			
			   if(splitMode=='1'&&scale=="2"){
				singleTemp = "(元)";
				rateTemp="(元)";
	}
 
			$('#singleDealSection').datagrid({
				title : '单笔交易区间',
				width:400,
				height : 200,
				nowrap : true,
				fitColumns:false,
				url : "${ctx}/splitrule/splitrule.do?method=getSingleDealSection",
				singleSelect : false,
				rownumbers : true,
				queryParams:{
					splitMode:splitMode,
					scale:scale,
					ruleId : ruleId
				},
				loadMsg : '数据载入中,请稍等！',
				remoteSort : false,
				columns : [ [
								{field:"singleRuleId",title:"NO",width :40,align : "center",sortable : true,hidden:true},
								{field:"singleDealStartValue",title:"起始值"+singleTemp,width:100,align:'center',editor:{type:'numberbox',options:{precision:2}}},
								{field:"singleDealEndValue",title:"结束值"+singleTemp,width:100,align:'center',
									formatter: function(value,row,index){
										if (value =="999999999999999999999"){
											value= "";
										} 
											return value;
										
									}},
								{field:"rate",title:"费率"+rateTemp,width:100,align:'center',editor:{type:'numberbox',options:{precision:3}}}
								 ] ],
				hideColumn : [ [ 
				                 {field:"ruleId"}
				              ] ],
              onAfterEdit: function(rowIndex, rowData, changes){
			    	 if(rowIndex!=0){
			    		 $(this).datagrid('beginEdit', rowIndex-1);
			    		 $(this).datagrid('getRows')[rowIndex-1]['singleDealEndValue'] = rowData.singleDealStartValue;
			    		 $(this).datagrid('endEdit', rowIndex-1);
			    	 }
				},
				showFooter : true
			});
		};
		
		
	</script>
</body>
</html>
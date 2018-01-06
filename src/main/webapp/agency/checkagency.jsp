<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%@ page import="com.compass.utils.ConstantUtils" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>云停风驰管理系统</title>
<script type="text/javascript">	
var onlineControl='<%=session.getAttribute(ConstantUtils.ONLINEFLAG)%>';
$(window).resize(function(){
	 /* $('#viewAgency').datagrid('resize', {
	 	width:cs()
	 }); */
}); 
function cs(){	
	return $(window).width()-8;
}
$(function(){
	$.viewAgency();
	$("#del").click(
			 function(){
				$.deleteMark();	
		 });	
	 });
	 
	 function onlineFlag(){
		if("0"==onlineControl){
			 return  true;
		}
		return false;
	}

$.checkAgency= function(){
		$.showDivShade('${ctx}');
	  	var rows=$('#viewAgency').datagrid('getSelections');
	    var len=rows.length;
	    if(len==0){
	    	$.hideDivShade();
	    	$.messager.alert("提示 ","请选择要审核的机构");
		   return ;
	    }
	    if(len>1){
	    	$.hideDivShade();
	    	$.messager.alert("提示 ","一次只能审核一个机构");
		   return ;
	    }
	   	var row= $('#viewAgency').datagrid('getSelected');
	   	// 只有待审核的机构可以审核 20141111
	    if(row.agency_status != 1){
	    	$.hideDivShade();
	    	$.messager.alert("提示", "机构已审核");
		   return ;
	    }
	   	$("#agencyId").val(row.agency_id);    //机构编码
		$("#agencyname").val(row.companyName);  //机构名称
		$("#agencybriefname").val(row.companyBriefName);  //机构名称
		$("#contactsName").val(row.contactsName);   //联系人
		$('#uppercompanyName').val(row.uppercompanyName);
		$('#vestagencyName').val(row.vestagencyName);
		$('#agencystatusSrc').val(row.agencystatusSrc);
		$("#companyPhone").val(row.companyPhone);  //企业电话
		$("#companyEmail").val(row.companyEmail);   //企业邮箱
		$("#companyAddr").val(row.companyAddr);   //企业地址
		$("#certificate").val(row.certificate);  //企业证书信息
		$("#legalinfo").val(row.legal_info);   //法人信息
		$("#organizationCode").val(row.organizationCode);   //组织机构代码
		$("#agreementcode").val(row.agreementcode);   //合同编号
		$("#provinceId").val(row.provinceName);   //省
		$("#cityId").val(row.cityName);           //市
		$("#accountBank").val(row.accountBank);   //开户银行
		$("#bankId").val(row.bankId);   //开户银行编码
		$("#accountName").val(row.accountName);    //户名
		$("#bankcode").val(row.bankcode);   //账号
		$("#agencydesc").val(row.agencydesc);
			   
	
		$("#markSave").window('open').window('refresh');  
	};
	$.checkagencySave = function(agencystatus){
		  $('#saveButton').linkbutton('disable');
		  var agencyid=$('#agencyId').val();
		  var agencydesc=$('#agencydesc').val();
		  $.post("${ctx}/agency/agency.do?method=checkAgency",
	              {
					  	agencyid:agencyid,
					  	agencystatus:agencystatus,
					  	agencydesc:agencydesc
	              },
	               function (data){
	                    $.parseAjaxReturnInfo(data, $.success, $.failed);
	              }, "json" );
	};
	$.success = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#saveButton').linkbutton('enable');
		 $.close(); 
		 $.viewAgency();
	};
	$.failed = function (message, data) {
		$.messager.alert("提示 ",message);
		 $('#saveButton').linkbutton('enable');
	};
	$.close = function(){
		$.hideDivShade();
		$("#markSave").window('close');  
	};	
	$.viewAgency = function() {
		/* var agencyId=$('#agencyheadId').combobox("getValue");
		if(agencyId="-1"){
			agencyId="";
		} */
		var agencyId ="";
		var companyName=$('#agencyheadName').val();
		var agencyStatus=$('#agencyheadstatus').combobox("getValue");
		if(agencyStatus==null || $.trim(agencyStatus)=='-1'){
			agencyStatus="";
		}
		
	 $('#viewAgency').datagrid({
			title : '机构审核',
			width:$(window).width()-8,
			height : $(window).height()*0.9,
			pageSize : 50,
			pageNumber : 1,
			fitColumns:false,
			url : "${ctx}/agency/agency.do?method=getAgencyCheck",
			queryParams : {
				agencyId : agencyId,
				companyName : companyName,
				agencyStatus:agencyStatus
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ 
			//{field : "systemName",title : "来源系统",width : 100,align : "center",sortable : true},
			//{field : "agency_id",title : "机构编码",width : 100,align : "center",sortable : true},
			{field : "companyName",title : "企业名称",width : 200,align : "center",sortable : true},
			{field : "vestagencyName",title : "归属机构",width : 100,align : "center", hidden: onlineFlag()},
			{field : "uppercompanyName",title : "上级机构",width : 100,align : "center",sortable : true},
			{field : "contactsName",title : "联系人",width : 100,align : "center",sortable : true}, 
			{field : "companyPhone",title : "企业电话",width : 100,align : "center",sortable : true}, 
			{field : "companyEmail",title : "企业邮箱",width : 150,align : "center",sortable : true}, 
			{field : "companyAddr",title : "企业地址",width : 150,align : "center",sortable : true}, 
			{field : "certificate",title : "企业证书信息",width : 200,align : "center",sortable : true},
			{field : "legal_info",title : "法人信息",width : 200,align : "center",sortable : true},
			{field : "organizationCode",title : "组织机构代码",width : 150,align : "center",sortable : true},
			{field : "agreementcode",title : "合同编号",width : 200,align : "center",sortable : true},
			{field : "provinceName",title : "省",width : 100,align : "center",sortable : true},
			{field : "cityName",title : "市",width : 100,align : "center",sortable : true},
			{field : "agencystatusSrc",title : "机构状态",width : 100,align : "center",sortable : true},
			{field : "accountBank",title : "开户银行",width : 100,align : "center",sortable : true},
			{field : "bankId",title : "开户银行编码",width : 150,align : "center",sortable : true},
			{field : "accountName",title : "户名",width : 100,align : "center",sortable : true},
			{field : "bankcode",title : "账号",width : 150,align : "center",sortable : true},
			{field : "agencydesc",title : "机构备注",width : 150,align : "center",sortable : true},
			{field : "createId",title : "创建人",width : 150,align : "center",sortable : true},
			{field : "createDt",title : "创建时间",width : 150,align : "center",sortable : true}

			] ],
			hideColumn : [ [ {
				field : "upperAgencyid",   //上级机构编码vestagencyName
				field : "vestagencyId",     //归属机构编码agency_status
				field : "provinceId",     //省编号
				field : "cityId",			//市编号
				field : "systemId",      //机构编号
				field :"companyBriefName" //公司简称
			}

			] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			singleSelect:true,
			toolbar : [ {
				id : 'btnadd',
				text : '机构审核',
				iconCls : 'icon-add',
				handler : function() {

					$.checkAgency();
				}
			} ]
		});
		var p = $('#viewAgency').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		}); 
	};
</script>
</head>
<body id="indexd">
<table width="100%">
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<%-- 机构编码：<input class="easyui-combotree" id="agencyheadId"  name="vestagencyId" 
							data-options="url:'${ctx}/agency/agency.do?method=getAgencyTree',method:'get',readonly:false" style="width:150px;"/>&nbsp;&nbsp;&nbsp; --%>
			<!-- 机构编码：<select style="width: 200px;" name="vestagencyId" id="agencyheadId" class="easyui-combobox"></select> 				 -->
			企业名称：<input type="text" name="agencyheadName" id="agencyheadName" style="width: 150px;"/>&nbsp;&nbsp;&nbsp;
			状态：<select class="easyui-combobox" id="agencyheadstatus"  name="agencyheadstatus" style="width:155px;" editable="false">
								<option value="-1" selected="selected">--请选择--</option>
								<option value="3">审核未通过</option>
								<option value="2">审核通过</option>   
							    <option value="1">待审核</option>   
						</select> 
						&nbsp;&nbsp;&nbsp;
						<a href="#" class="easyui-linkbutton" 
				data-options="iconCls:'icon-search'" onclick="$.viewAgency()">查询</a> 
			</td>
			
			
		</tr>
	</table>
	<table id="viewAgency"></table>
	 <div id="markSave" class="easyui-window" title="机构审核" closed=true cache ="false" collapsible="false" zIndex="20px"  
       minimizable="false" maximizable="false" resizable="false" draggable="true"  closable="false" 
       style="width:850px;height:320px;top:100px;padding:0px;background: #fafafa; overflow: hidden;">
	    <div class="easyui-layout" fit="true">	    
		<div region="center" border="true" style="padding:10px;background:#fff;overflow: hidden;">
			<input type="hidden" id="optFlag" name="optFlag" />		
	    		<table style="width:800px;">
	    			<tr height="30px" >
	    			   <td  align="left">机构编码：</td>
	    				<td align="left">
		    				<input type="text" name="agencyId" id="agencyId" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				
	    				<td align="left">企业名称：</td>
	    				<td align="left">
	    					<input type="text" name="agencyname" id="agencyname" maxlength="50" readonly="readonly"/>
	    				</td>	
	    				<td align="left">企业地址：</td>
	    				<td align="left"> 
							<input type="text" name="companyAddr" id="companyAddr" maxlength="50" readonly="readonly"/>
	    				</td>			   
	    					 			
	    			</tr>
	    			<tr height="30px" >
	    				<td align="left">上级机构：</td>
	    				<td align="left">
		    				<input type="text" name="uppercompanyName" id="uppercompanyName" readonly="readonly"/>
	    				</td>
	    				<td align="left">归属机构</td>
	    				<td align="left">
	    					<input type="text" name="vestagencyName" id="vestagencyName" readonly="readonly" />
	    				</td>	
	    				<td align="left">机构状态：</td>
	    				<td align="left">
	    				<input type="text" name="agencystatusSrc" id="agencystatusSrc" readonly="readonly" />
	    				</td>			   
   					 			
   					</tr>
	    			<tr height="30px" >
	    		
	    				<td  align="left">联系人：</td>
	    				<td align="left">
		    				<input type="text" name="contactsName" id="contactsName" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">企业电话：</td>
	    				<td align="left">
	    					<input type="text" name="companyPhone" id="companyPhone" maxlength="50" readonly="readonly"/>
	    				</td>	
	    				<td align="left">企业邮箱：</td>
	    				<td align="left">
	    					<input type="text" name="companyEmail" id="companyEmail" maxlength="50" readonly="readonly"/>
	    				</td>			   
	    					 			
	    			</tr><tr height="30px" >
	    		
	    				<td align="left">企业证书信息：</td>
	    				<td align="left">
		    				<input type="text" name="certificate" id="certificate" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">法人信息：</td>
	    				<td align="left">
	    					<input type="text" name="legalinfo" id="legalinfo" maxlength="50" readonly="readonly"/>
	    				</td>	
	    				<td align="left">组织机构代码：</td>
	    				<td align="left">
	    					<input type="text" name="organizationCode" id="organizationCode" maxlength="50" readonly="readonly"/>
	    				</td>			   
	    					 			
	    			</tr><tr height="30px" >
	    		
	    				<td  align="left">合同编号：</td>
	    				<td align="left">
		    				<input type="text" name="agreementcode" id="agreementcode" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">省：</td>
	    				<td align="left">
	    					<input type="text" name="provinceId" id="provinceId" maxlength="50" readonly="readonly"/>
	    				</td>	
	    				<td align="left">市：</td>
	    				<td align="left">
	    					<input type="text" name="cityId" id="cityId" maxlength="50" readonly="readonly"/>
	    				</td>			   
	    					 			
	    			</tr><tr height="30px" >
	    		
	    				<td  align="left">户名：</td>
	    				<td align="left">
		    				<input type="text" name="accountName" id="accountName" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">开户银行：</td>
	    				<td align="left">
	    					<input type="text" name="accountBank" id="accountBank" maxlength="50" readonly="readonly"/>
	    				</td>	
	    				<td align="left">开户银行编码：</td>
	    				<td align="left">
	    					<input type="text" name="bankId" id="bankId" maxlength="50" readonly="readonly"/>
	    				</td>			   
	    					 			
	    			</tr><tr height="30px" >
	    		
	    				<td  align="left">账号：</td>
	    				<td align="left">
		    				<input type="text" name="bankcode" id="bankcode" maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">企业简称</td>
	    				<td align="left">
	    						<input type="text" name="agencybriefname" id="agencybriefname"  maxlength="50" readonly="readonly"/>
	    				</td>
	    				
	    				<td align="left">备注</td>
	    				<td align="left">
	    						<input type="text" name="agencydesc" id="agencydesc" maxlength="50"/>
	    				</td>	
	    				
	    				</td>			   
	    					 			
	    			</tr>
   					
	    			<tr  height="30px">
	    			
	    				<td height="30px" align="center" colspan="6">
	    				
	    		
	    				 <a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.checkagencySave('2')">审核通过</a>
	    				 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    				  <a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.checkagencySave('3')">审核未通过</a>
	    				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    				 <a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a> 
	    				</td>
	    			
	    				
	    				
	    			</tr>
	    		</table>
	     	</div>
	    	</div>
	    </div>
</body>
</html>
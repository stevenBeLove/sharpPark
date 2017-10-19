<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.compass.utils.ConstantUtils;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
		table.formdata{          
		    border:1px solid #5F6F7E; 
		    border-collapse:collapse; 
		    font-family:Arial; 
		} 
		table.formdata caption{      
		    text-align:left; 
		    padding-bottom:6px; 
		} 
		table.formdata th{          
		    border:1px solid #5F6F7E; 
		    background-color:#E2E2E2; 
		    color:#000000; 
		    text-align:left; 
		    font-weight:normal; 
		    padding:2px 8px 2px 6px; 
		    margin:0px; 
		} 
		table.formdata td{        
		    margin:0px; 
		    padding:0px; 
		    border:1px solid #ABABAB;  
		} 
		table.formdata input{         
		    width:100px;  www.2cto.com
		    padding:1px 3px 1px 3px; 
		    margin:5px 5px 5px 25px;
		    font-family:Arial; 
		} 
		.boxUl {text-align:center; text-align:left }
</style>

</head>
<body id="indexd">
	<table>
		<tr>
			<td>代理商：</td>
			<td> <input id="cg" style="width:150px"></input></td>
			
			
			<td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchCost();">查询</a></td>
		</tr>
	</table>
	<table id="viewAgencyGroup"></table>
	
	
	
	<div id="viewDetailWin" class="easyui-window" title="查看详情" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:800px;height:400px;padding:5px;">
        <table id="viewAgencyCost"></table>
    </div>
	
	<!-- 设置分润比 -->
	<div id="setCostWin" class="easyui-window" title="设置下级机构成本" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:550px;height:450px;padding:5px;">
     	<div>       
                分配至子机构：<input id="distribution" style="width:120px"></input>
	        <ul class = "boxUl">
	        	<li>提示（本级生效中的费率）：</li>
	          <c:forEach items="${costList }" var="agencyCost">
	            <li>${agencyCost.businessTypeName }      百分比底价为：<font color="red">${agencyCost.costRate }</font>       单笔底价：<font color="red">${agencyCost.costFix }</font>  </li>
	           </c:forEach>
	          <li>“收款-云闪付到账”设置的分润成本只针对云闪付单笔交易≤1000元时有效。”</li>
	        </ul>
	     </div>      
	  <form id="costForm">
	  	<input type="hidden" id="agencyid" name="agencyid"/>
	  
	     <div>
	        <table class="formdata"> 
				<caption>设置下级分润成本</caption> 
				<tr> 
				<th>交易类型</th>
				<th scope="col">百分比（%）最小单位0.001</th> 
				<th scope="col">单笔（元）最小单位0.01</th> 
				</tr> 
				
				<c:forEach var="rtbType" items="${rtbTypeList }">
					<tr> 
					<th scope="row">${rtbType.productdesc }</th> 
					    <td><input  type="text" name="${rtbType.rtbType }costRate" id="${rtbType.rtbType }costRate" maxlength="5"/></td> 
					    <td><input  type="text" name="${rtbType.rtbType }costFix" id="${rtbType.rtbType }costFix" maxlength="4"/></td> 
					</tr> 
				</c:forEach>
				</table>
		</div>   		
	    <div style="text-align:center;padding:15px 0">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="costSubmitForm()" style="width:80px">确认并分配</a>
        </div>
      </form>	
         <div style="text-align:center;padding:0px 0">
         	<p>修改后的分润成本将实时生效，您每月可给每个下级机构修改<font color='red'>两次</font>分润成本</p>
        </div>
    </div>
	
	
	
 <script type="text/javascript">	
	$(function() {
		$('#viewAgencyGroup').datagrid({
			title : '机构分润成本查询',
			width : $(window).width() - 8,
			height : $(window).height() * 0.9,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/cost/agencyCost.do?method=getCostByGroup",
			queryParams : {
				agency_id : '',
				status : ''
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{
				field : "agencyId",
				title : "下级机构代码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "agencyName",
				title : "下级机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "status",
				title : "状态",
				width : 200,
				align : "center",
				formatter:function(val){
					if("0" == val){
						return "待生效";
					}else if("1" == val){
						return "生效中";
					}else{
						return "状态错误";
					}
					
				}
			},{
				field : "operName",
				title : "操作人",
				width : 200,
				align : "center",
				sortable : true
			},{
				field : "operdt",
				title : "操作时间",
				width : 200,
				align : "center",
				sortable : true
			},{
				field : "id",
				title : "操作",
				width : 200,
				align : "center",
				formatter:function(val,rec){
					return "<a href='javascript:void(0);' onclick=\"showDetailCost('"+rec.agencyId+"','"+rec.status+"')\">查看详情</a>";
				}
			}			
			] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar : [ 
			'-', {
				id : 'btnsave',
				text : '设置下级分润成本',
				iconCls : 'icon-cut',
				handler : function() {
					showSetWin();
				}
			} ]
		});

		var p_group = $('#viewAgencyGroup').datagrid('getPager');
		$(p_group).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
		
	});
	
	function showDetailCost(val,status){
		$("#viewDetailWin").window('open').window('refresh');
		$('#viewAgencyCost').datagrid({
			title : '费率详情查看',
			width : $(window).width() *0.9,
			height : $(window).height() * 0.5,
			pageSize : 20,
			pageNumber : 1,
			url : "${ctx}/cost/agencyCost.do?method=getCostDetail",
			queryParams : {
				agency_id : val,
				status : status
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			pagination : true,
			columns : [ [ 
			{
				field : "agencyName",
				title : "子机构名称",
				width : 150,
				align : "center",
				sortable : true
			},{
				field : "businessTypeName",
				title : "交易类型",
				width : 150,
				align : "center",
				sortable : true
			},{
				field : "costRate",
				title : "百分比（%）",
				width : 150,
				align : "center",
				sortable : true
			},{
				field : "costFix",
				title : "单笔（元）",
				width : 150,
				align : "center",
				sortable : true
			},{
				field : "status",
				title : "状态",
				width : 150,
				align : "center",
				sortable : true
			},{
				field : "operName",
				title : "操作人",
				width : 150,
				align : "center",
				sortable : true
			}, {
				field : "operdt",
				title : "操作时间",
				width : 150,
				align : "center",
				sortable : true
			}			
			] ],
			pagination : false,
			rownumbers : false,
			showFooter : true
		});
	}
	
	function showSetWin(){
		$("#setCostWin").window('open').window('refresh');
		$('#costForm')[0].reset();
		 $('#distribution').combogrid({
              panelWidth:380,
              url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
  			queryParams : {
  				rows : 40
  			},
              idField:'agency_id',
              textField:'companyName',
              mode:'remote',
              fitColumns:true,
              columns:[[
          {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			}
			]],
			onChange:function(){
				var agencyId = $('#distribution').combogrid('getValue');
				$("#agencyid").val(agencyId);
			}
          });
	}
	
	function costSubmitForm(){
		if($('#distribution').combogrid('getValue') == ''){
			 $.messager.alert('提示','请选择子机构','error');
			 return;
		}
		//查询是否有待生效的重复记录
		var agencyId = $('#distribution').combogrid('getValue');
		$.messager.confirm('修改', '<font color=\'red\'>该机构分润成本设置成功后将实时生效，是否确认提交？</font>', function(r){
            if (r){
           	 saveForm();
            }
        });
	}
	
	function saveForm(){
		var agencyidVal = $("#agencyid").val();
		if(checkChinese(agencyidVal)){
			$.messager.alert("提示 ", "机构编号不能为中文，请重新输入");
			return false;
		}
		$.ajax({
	         type: "POST",
	     	 url : "${ctx}/cost/agencyCost.do?method=saveAgencyCost",
	         data:$('#costForm').serialize(),
	         success: function(msg) {
	        	if(msg.success =='true'){
	        		 $.messager.alert('提示',msg.message);
	        		 $("#setCostWin").window('close');
	        		 $("#viewAgencyGroup").datagrid("load");
	        		 
	        	}else{
	        		 $.messager.alert('提示',msg.message,'error');
	        	}
	         }
	     });		
	}
	
	
	//判断是否为中文
	function checkChinese(str) {
		var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
		return reg.test(str);
	}

</script>
	
 	<script type="text/javascript">
        $(function(){
            $('#cg').combogrid({
                panelWidth:380,
                url : "${ctx}/agency/agency.do?method=getAgencyCombChild",
    			queryParams : {
    				rows : 40
    			},
                idField:'agency_id',
                textField:'companyName',
                mode:'remote',
                fitColumns:true,
                columns:[[
            {
				field : "agency_id",
				title : "机构编码",
				width : 100,
				align : "center",
				sortable : true
			}, {
				field : "companyName",
				title : "企业名称",
				width : 200,
				align : "center",
				sortable : true
			}
			]]
            });
        });
        
        
        
        function searchCost(){
        	var agencyId = $('#cg').combogrid('getValue');
        	var effect = "";
    	    
        	var opts = $("#viewAgencyGroup").datagrid("options");
    	    opts.queryParams = {
    	    		agency_id : agencyId,
    	    		status : effect
    	    };
    	    $("#viewAgencyGroup").datagrid("load");
        }
        
    </script>	
    
</body>
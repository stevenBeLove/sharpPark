<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>验证码查询</title>
<script type="text/javascript">
$(function(){
	//初始化页面
	viewApplyList();
});

function resetQuery()
{
	$(&quot;#userName&quot;).val(&apos;&apos;);
	$(&quot;#telephone&quot;).val(&apos;&apos;);
}

function viewApplyList()
{
	var userName = $(&quot;#userName&quot;).val();
	var telephone = $(&quot;#telephone&quot;).val();
	var startDate = $(&quot;#startDate&quot;).datebox(&apos;getValue&apos;);
	var endDate = $(&quot;#endDate&quot;).datebox(&apos;getValue&apos;);
	$(&quot;#messageList&quot;).datagrid({
		title : &apos;验证码查询&apos;,
		height : $(window).height() * 0.9,
		loadMsg : &apos;数据正在加载,请耐心等待...&apos; ,
		rownumbers:true ,
		remoteSort : false,
		url : &quot;${ctx}/message/message.do?method=getCheckCode&quot;,
		queryParams : {
			userName: userName,
			telephone: telephone,
			startDate: startDate,
			endDate:endDate
		},
		columns : [[    
		          	{field:&apos;userName&apos;,title:&apos;用户名称&apos;,width:100,align:&apos;left&apos;,sortable:true,halign:&apos;center&apos;},    
		          	{field:&apos;mobileNo&apos;,title:&apos;手机号码&apos;,width:100,align:&apos;center&apos;,sortable:true},    
		          	{field:&apos;checkCode&apos;,title:&apos;短信验证码&apos;,width:100,align:&apos;center&apos;,sortable:true},
		          	{field:&apos;effTime&apos;,title:&apos;有效时间&apos;,width:150,align:&apos;center&apos;,sortable:true,
		          		formatter:function(value, row, index){
		          			return value.substring(0,8) + &quot; &quot; + value.substring(8);
		          		}	
		          	},
		          	{field:&apos;checkFlag&apos;,title:&apos;验证标识&apos;,width:100,align:&apos;center&apos;,sortable:true,
		          		formatter:function(value,row,index){
		          			if(value == 0){
		          				return &quot;未验证&quot;;
		          			}else{
		          				return &quot;已验证&quot;;
		          			}
		          		}
		          	}
		]],
		pagination : true,
		pageSize : 20 ,
		pageList : [10, 20, 50, 100],
		showFooter :  true
	});
}
</script>
</head><body id="indexd">
	<table>
		<tr>
			<td>用户名称:</td>
			<td><input class="easyui-validatebox" id="userName" data-options="required:false"/></td>
			<td>手机号码:</td>
			<td><input class="easyui-validatebox" id="telephone" data-options="required:false"/></td>
			<td>开始日期:</td>
			<td><input class="easyui-datebox" name="startDate" id="startDate" style="width: 150px;"/></td>
			<td>结束日期:</td>
			<td><input class="easyui-datebox" name="endDate" id="endDate" style="width: 150px;"/></td>
			<td><a id="queryBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="viewApplyList()">查询</a>                                                                                                                      
			</td>
		</tr>
	</table>
	<table id="messageList"></table>
</body></html>
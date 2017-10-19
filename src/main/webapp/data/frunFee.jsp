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
$(window).resize(function(){
     $('#viewSystem').datagrid('resize', {
        width:cs()
     });
     
    
}); 
function cs(){  
    return $(window).width()-6;
}
var flag;

$(function(){
	$('#systemId').combobox({
	    url:"${ctx}/system/system.do?method=getCombSystemsSigle",
	    valueField:"id",
	    textField:"text" ,
        onLoadSuccess:function(){
            $.viewSystem();
        },
        onChange: function (n,o) {
        	$('#branchid').combobox({
                url:"${ctx}/data/data.do?method=getbranch&systemid="+n,
                valueField:"id",
                textField:"text"
            });      
        	 

        	}
	    });
 
	   $('#dealtype').combobox({
           url:"${ctx}/dealtype/dealtype.do?method=getCombDealTypes",
           valueField:"id",
           textField:"text"
       });
	
});


$('#agencyIds').combobox({
    url:"${ctx}/agency/agency.do?method=getChildAgencyListSin&systemId=TKMY",
    valueField:"id",
    textField:"text",
    onSelect: function(opt){
            agencyIds_value=opt.id;
        }
    });

    
$.openWin = function(obj) {
    $('#systemSave').window({title: '添加系统信息'});
    $.showDivShade('${ctx}');
    
 
    flag = obj;
    if (flag != "-1") {
        $('#systemSave').window({title: '修改费率信息'});
        var rows = $('#viewSystem').datagrid('getSelections');
        if (rows.length > 1) {
            $.hideDivShade();
            $.messager.alert("提示 ", "只能对单条记录修改");
            return;
        }else if(rows.length ==0){
            $.hideDivShade();
            $.messager.alert("提示 ", "请选择要修改的记录");
            return;
        }
        //$('#viewSystem').datagrid('selectRow', flag);
        var row = $('#viewSystem').datagrid('getSelected');
        $("#systemname").val(row.systemname);
        $("#dtname").val(row.dtname);
        $("#fee").val(row.fee);
        $("#feename").val(row.feename);
        $("#feeststusstr").val(row.feeststusstr);
        $("#statusupdate").combobox('select', row.status);
        $("#feestatus").val(row.feestatus);
    }
    $("#systemSave").window('open').window('refresh');
};
$.save = function() {
    var fee = $("#fee").val();
    var status = $("#statusupdate").combobox("getValue");
    var row = $('#viewSystem').datagrid('getSelected');
    var feestatus= $("#feestatus").val();
    var feeststusstr=$("#feeststusstr").val();
    var branchid=row.branchid;
    var prepbranch=row.prepbranch;
    var systemid=row.systemid;
    var feestatus=row.feestatus;
    var dealtype=row.dealtype;
    var shopno=row.shopno;
    if ($.trim(fee) == "") {
        $.messager.alert("提示 ", "请输入费率成本");
        return false;
    }
             
    var reg = /^([0-9]{1,2})+(.[0-9]{1,4})?/;
    if (!reg.test(fee)) {
         alert("正确的费率成本");
         return false;
    }  
    
    if ((1==feestatus||0==feestatus)&&0.0125<fee) {
        $.messager.alert("提示 ", "请输入正确"+feeststusstr+"的费率成本,应小于1.25%");
        return false;
    }
    if(2==feestatus&&40<fee){
    	  $.messager.alert("提示 ", "请输入正确封封的费率成本");
          return false;
    }
    
    $('#save').linkbutton('disable');
        $.post("${ctx}/data/data.do?method=updateFrunfee", {
        	systemid : systemid,
        	status:status,
        	prepbranch : prepbranch,
        	branchid :branchid,
            fee : fee,
            feestatus:feestatus,
            dealtype:dealtype,
            shopno:shopno
        }, function(data) {
            $.parseAjaxReturnInfo(data, $.success, $.failed);
        }, "json");
        
      

};
$.success = function(message, data) {
    $.messager.alert("提示 ", message);
    $('#save').linkbutton('enable');
    $.close();
    $.viewSystem();
};
$.failed = function(message, data) {
    $.messager.alert("提示 ", message);
    $('#save').linkbutton('enable');
};
$.close = function() {
    $.hideDivShade();
    $("#systemSave").window('close');
};

$.viewSystem = function() {
    var status = $("#status").combobox('getValue');
    var systemName = $("#systemId").combobox('getValue');
    var dealtype=$("#dealtype").combobox('getValue');
    var branchid=$("#branchid").combobox('getValue');
    if("-1"==systemName){
    	systemName="";
    }
    if("-1"==dealtype){
    	dealtype="";
    }
    if("-1"==branchid){
    	branchid="";
    }
    $('#viewSystem').datagrid(
                    {
                        title : '费率管理',
                        width:$(window).width()-6,
                        height : $(window).height()*0.9,
                        nowrap : true,
                        fitColumns:true,
                        url : "${ctx}/data/data.do?method=getfrunfee",
                        pageSize : 20,
                        pageNumber : 1,
                        singleSelect : false,
                        queryParams:{
                            status:status,
                            systemid:systemName,
                            dealtype:dealtype,
                            branchid:branchid
                        },
                        loadMsg : '数据载入中,请稍等！',
                        remoteSort : false,
                        columns : [ [
                                        {field : "ck",checkbox : true},
                                        {field : "systemname",title : "系统名称",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "systemid",title : "系统编号",width : $(window).width()*0.1,align : "center",sortable : true},
                                        {field : "prepbranch",title : "顶级机构",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "branchname",title : "机构编号",width : $(window).width()*0.2,align : "center",sortable : true},
                                        {field : "dtname",title : "交易类型",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "feename",title : "交易类型名 ",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "shopno",title : "费率编号",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "fee",title : "费率成本",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "feeststusstr",title : "费率类型",width : $(window).width()*0.15,align : "center",sortable : true},
                                        {field : "statusstr",title : "状态",width : $(window).width()*0.15,align : "center",sortable : true}
                                         ] ],
                                         
                                         
                        hideColumn : [ [ 
                                         {field : "dealtype"},
                                         {field : "status"},
                                         {field : "feestatus"}
                                      ] ],
                        pagination : true,
                        rownumbers : true,
                        showFooter : true,
                        toolbar:[
                                 {
                                    id:'btncut',
                                    text:'修改',
                                    iconCls:'icon-edit',
                                    handler:function(){
                                            $.openWin(-2);
                                    }
                                 },'-',
                        ]
                        
                    });
    var p = $('#viewSystem').datagrid('getPager');
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
            <td style="height: 30px">
                系统名称：
            </td>
            <td style="padding-left: 5px">
                <select id="systemId" class="easyui-combobox" name="systemId" data-options="panelHeight:'auto'" style="width: 200px;" editable="true"></select>
            </td>
       <td style="padding-left: 30px">
        APP：
             </td>
            <td style="padding-left: 5px">
                <select style="width: 200px;" name="branchid"  id="branchid" class="easyui-combobox"  data-options="panelHeight:'auto'"   editable="true"></select>  
            </td>      
            
           </tr>
            <tr>
           <td style="height: 30px">
                           交易类型：
             </td>
            <td style="padding-left: 5px">
                <select style="width: 200px;" name="dealtype"  id="dealtype" class="easyui-combobox"  data-options="panelHeight:'auto'"   editable="true"></select>  
            </td>
            <td style="padding-left: 30px">
                系统状态：
            </td>
            <td style="padding-left: 5px">
                <select id="status"  editable="false" class="easyui-combobox" name="status" style="width: 150px;">
                    <option value="1">有效</option>
                    <option value="0">无效</option>
                </select>
            </td>
            <td>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewSystem()">查询</a>
            </td>           
        </tr>
    </table>
    <table id="viewSystem"></table>
    
    <div id="systemSave" class="easyui-window" title="系统" closable="false"
        closed=true cache="false" collapsible="false" zIndex="20px"
        minimizable="false" maximizable="false" resizable="false" draggable="false"
        style="width: 550px; height: 320px; top: 100px; padding: 0px; background: #fafafa; overflow: hidden;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="true"
                style="padding:10px;background: #fff; overflow: hidden;">
                <input type="hidden" id="systemId" name="systemId" />
                <input type="hidden" id="feestatus" name="feestatus" />
                    <table width="100%">
                        <tr>
                            <td align="left">系统名称：</td>
                            <td align="left"><input type="text" name="systemname"
                                id="systemname" maxlength="100" readonly />
                            </td>
                            <td align="left">交易类型：</td>
                            <td align="left"><input type="text" name="dtname"
                                id="dtname" maxlength="100" readonly />
                            </td>
                        </tr>
                        <tr style="height: 40px">   
                            <td align="left">交易类型名：</td>
                           <td align="left"><input type="text" name="feename"
                                id="feename" maxlength="100" readonly />
                            </td>
                             <td align="left">费率类型：</td>
                            <td align="left"><input type="text" name="feeststusstr"
                                id="feeststusstr" maxlength="100" readonly />
                            </td>
                        </tr>
                        <tr>
                              <td align="left">状态：</td>
                            <td align="left">
	                            <select id="statusupdate" class="easyui-combobox"
	                                name="statusupdate" data-options="panelHeight:'auto'"   editable="false" style="width: 156px;">
	                                    <option value="1">有效</option>
	                                    <option value="0">无效</option>
	                            </select>
                            </td>
                              <td align="left">成本：</td>
	                            <td align="left">
	                            <input type="text" name="fee"
	                                id="fee" maxlength="100" onkeyup="value=this.value.replace(/[^\d|.]/g,'')"  />
	                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td align="center" colspan="4">
                                <a name="save" id="save"
                                href="#" class="easyui-linkbutton"
                                data-options="iconCls:'icon-save'" onclick="$.save()">保存</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a name="close" id="close"
                                href="#" class="easyui-linkbutton"
                                data-options="iconCls:'icon-cancel'" onclick="$.close()">关闭</a>
                            </td>
                        </tr>
                    </table>
            </div>
        </div>
    </div>
</body>
</html>
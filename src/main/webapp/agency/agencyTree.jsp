<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%@ page import="com.compass.utils.ConstantUtils" %>
<%
	String agencyId=session.getAttribute(ConstantUtils.AGENCYID).toString();
%>
<html >
<link rel="stylesheet" href="${ctx}/commons/css/demo.css" type="text/css">

<!--
必须文件 zTreeStyle.css、jquery.ztree.core-x.js、jquery-x.min.js -->   

<link rel="stylesheet" href="${ctx}/commons/css/zTreeStyle/zTreeStyle.css" type="text/css">

	<script type="text/javascript" src="${ctx}/commons/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${ctx}/commons/js/zTree/jquery.ztree.core-3.5.js"></script>
<script language="JavaScript"><!--
var agid='<%=session.getAttribute(ConstantUtils.AGENCYID).toString().trim()%>';
    var setting= {   
    	view: {
    	     	selectedMulti: false,
				fontCss: getFontCss
		},
    	async : {   
    			    enable: true,
					url: getUrl, // Ajax获取数据的 URL 地址
					autoParam:["id", "name" ],
       				dataFilter: filter
        },   
        data:{
        		simpleData: {
		  		   enable: true,
		  		   idKey: "id",
			    	pIdKey: "pId",
				   rootPId: agid
				}
        },
        // 回调函数
        callback : {   
            			beforeClick: beforeClick,
            			onClick: zTreeOnClick,
						beforeAsync: beforeAsync,
						onAsyncError: onAsyncError,
						onAsyncSuccess: onAsyncSuccess,
						beforeCollapse: beforeCollapse,
						beforeExpand: beforeExpand,
						onCollapse: onCollapse,
						onExpand: onExpand
            }
    };   

function getUrl(treeId, treeNode) {
	var id;
	if(!treeNode) {
		id = null;
	} else {
		id = treeNode.id+"";
	}
	return "${ctx}/agency/agency.do?method=createAgencyTree&id=" + id +"";
}


    // 加载错误提示

    function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {   
        alert("加载错误：" + XMLHttpRequest);   
    };   
    
    function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}

 
    
    
function beforeClick(treeId, treeNode) {
 return true;
/**	if (treeNode.isParent) {
		return false;
	} else {
		return true;
	}
	*/
}
var ret;

function zTreeOnClick(event, treeId, treeNode) {
    alert(treeNode.id + ", " + treeNode.name);
    ret=treeNode.id + "|" + treeNode.name;
     window.returnValue(ret);
};

var toUrl, className = "dark";

function beforeAsync(treeId, treeNode) {
	//alert('beforeAsync: ' + treeNode);
	className = (className === "dark" ? "":"dark");
	return true;
}

function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
}



var firstAsyncSuccessFlag = 0;
function onAsyncSuccess(event, treeId, msg) {  
	if (firstAsyncSuccessFlag == 0) {  
  try {  
         //调用默认展开第一个结点  
        /**  var selectedNode = zTree.getSelectedNodes();  
         var nodes = zTree.getNodes();  
         zTree.expandNode(nodes[0], true);  
      
         var childNodes = zTree.transformToArray(nodes[0]);  
         zTree.expandNode(childNodes[1], true);  
         zTree.selectNode(childNodes[1]);  
         var childNodes1 = zTree.transformToArray(childNodes[1]);  
         zTree.checkNode(childNodes1[1], true, true);  
         firstAsyncSuccessFlag = 1;  
         */
   } catch (err) {  
      
   }  
	}  
}
//function onAsyncSuccess(event, treeId, treeNode, msg) {
//}

/* 按名字搜索 Begin */
var key, lastValue = "", nodeList = [], fontCss = {};

function searchNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var keyValue = $.trim(key.get(0).value);
	if (key.hasClass("empty")) {
		keyValue = "";
	}
	if (lastValue === keyValue) return;
	lastValue = keyValue;
	if (keyValue === "") return;
	updateNodes(false);
	nodeList = zTree.getNodesByParamFuzzy("name", keyValue);
	updateNodes(true);
}

function updateNodes(highlight) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
}

function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}

function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}
/* 按名字搜索 End */
 

/* 展开/折叠所有已异步载入父节点 Begin */
function expandNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	type = e.data.type,
	nodes = zTree.getSelectedNodes();
	alert(type);
	if (type.indexOf("All")<0 && nodes.length == 0) {
		alert("请先选择一个父节点");
	}
  alert(type == "expandAll");
	if (type == "expandAll") {
		zTree.expandAll(true);
	} else if (type == "collapseAll") {
		zTree.expandAll(false);
	} else {
		var callbackFlag = $("#callbackTrigger").attr("checked");
		for (var i=0, l=nodes.length; i<l; i++) {
			zTree.setting.view.fontCss = {};
			if (type == "expand") {
				zTree.expandNode(nodes[i], true, null, null, callbackFlag);
			} else if (type == "collapse") {
				zTree.expandNode(nodes[i], false, null, null, callbackFlag);
			} else if (type == "toggle") {
				zTree.expandNode(nodes[i], null, null, null, callbackFlag);
			} else if (type == "expandSon") {
				zTree.expandNode(nodes[i], true, true, null, callbackFlag);
			} else if (type == "collapseSon") {
				zTree.expandNode(nodes[i], false, true, null, callbackFlag);
			}
		}
	}
}
function beforeCollapse(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	return (treeNode.collapse !== false);
}
function onCollapse(event, treeId, treeNode) {
}		
function beforeExpand(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	return (treeNode.expand !== false);
}
function onExpand(event, treeId, treeNode) {
}
    
// 渲染
    $(document).ready(function() {   
        $.fn.zTree.init($("#treeDemo"), setting);   
        
	zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.expandAll(true);
	key = $("#key");
	 key.bind("focus", focusKey).bind("blur", blurKey).bind("propertychange", searchNode).bind("input", searchNode);
	//$("#m_refresh").bind("click", {type:"refresh", silent:false}, refreshNode);
	
	$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
	$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);
    });   
--></script>
</HEAD>


<div class="ztreeDiv">
  <div>按名称模糊查找：</div>
  <input type="text" id="key" class="empty" maxlength="10" size="11">
  <a id="expandAllBtn" href="#" title="展开所有节点" onclick="return false;" style="font-size:14px;text-decoration:none;">展开</a>|
  <a id="collapseAllBtn" href="#" title="折叠所有节点" onclick="return false;" style="font-size:14px;text-decoration:none;">折叠</a>
  <div class="zTreeDemoBackground left" style="height: 100%;">
	 <ul id="treeDemo" class="ztree" style="height: 100%;"></ul>
  </div>
</div>
</BODY>
<html>

  
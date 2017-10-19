<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="cache-control" content="no-cache" />
<title>瑞通宝综合管理系统</title>
<style type="text/css">
.button1 {
	color: #444;
	background-repeat: no-repeat;
	background: #f5f5f5;
	background-repeat: repeat-x;
	border: 1px solid #bbb;
	background: -webkit-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -moz-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: -o-linear-gradient(top, #ffffff 0, #e6e6e6 100%);
	background: linear-gradient(to bottom, #ffffff 0, #e6e6e6 100%);
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff, endColorstr=#e6e6e6, GradientType=0);
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
}
</style>
<script type="text/javascript">
	$(function() {
		var textChange;
		var minDate;
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
		//浏览机构添加的模板
		$.viewTopMould();
		//页面上的模版名称下拉框，查看自己可以改动的模板
		$('#mouldname').combobox({
			url : "${ctx}/splitmould/splitmould.do?method=getTopMouldList",
			valueField : "id",
			textField : "text"
		});

	});

	$(window).resize(function() {
		$('#viewTopMould').datagrid('resize', {
			width : cs()
		});

	});
	function cs() {
		return $(window).width() - 6;
	}
	var flag;
	$.openWin = function(obj) {
		flag = obj;

		//如果选的是添加
		if (flag == "-2") {
			$.showDivShade('${ctx}');
			//打开'selectsplitmould' 这个DIV
			$('#selectSplitMould').window({
				title : '请选择需要添加的模板'
			});
			//打开dataguid		
			$.viewSelectSplitMould();
			$("#selectSplitMould").window('open').window('refresh');
		}

		//如果选的是分配
		if (flag == "-3") {
			$.showDivShade('${ctx}');
			//打开分配对话框，'distrMouldView' 这个DIV
			$('#distrMouldView').window({
				title : '分配机构分润模板'
			});
			//给下拉框赋初值
			$("#childAgencyId").combobox('setValue', -1);
			//初始化子机构下拉框
			$('#childAgencyId').combobox({
				url : "${ctx}/agency/agency.do?method=getChildAgencyList",
				valueField : "id",
				textField : "text"
			});

			var rows = $('#viewTopMould').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条设置分配");
				return;
			} else if (rows.length == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要分配的记录");
				return;
			}
			var row = $('#viewTopMould').datagrid('getSelected');
			//放入隐藏域
			$("#ruleIdD").val(row.ruleId);
			$("#mouldNameD").val(row.mouldName);

			var ruleId = row.ruleId;
			var mouldName = row.mouldName;
			//给模板信息里的datagrid赋值( mouldViewSection)
			$.viewMouldViewSectionD(ruleId, mouldName);
			$("#distrMouldView").window('open').window('refresh');
		}

		//如果选的是修改
		if (flag == "-4") {
			$.showDivShade('${ctx}');
			//打开'splitrule' 这个DIV
			$('#updatesplitrule').window({
				title : '修改机构分润模板'
			});
			var rows = $('#viewTopMould').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条模板修改");
				return;
			} else if (rows.length == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要修改的模板");
				return;
			}

			var row = $('#viewTopMould').datagrid('getSelected');
			//给隐藏域赋值
			var ruleIdU = row.ruleId;
			var mouldNameU = row.mouldName;
			var agencyIdU = row.agencyId;
			var validityDateU = row.validityDate;
			$("#ruleIdU").val(ruleIdU);
			$("#mouldNameU").val(mouldNameU);
			$("#agencyIdU").val(agencyIdU);
			//给生效日期下拉框赋值，给模板名称赋值
			//$("#validityDateU").val(validityDateU);

			//给模板信息里的datagrid赋值( mouldViewSection)
			$.viewMouldViewSectionU(ruleIdU, mouldNameU);
			$("#updateOrgsplitrule").window('open').window('refresh');
		}

		//如果双击了添加模板信息里的某一行
		if (flag == "-5") {
			$.showDivShade('${ctx}');
			//打开'splitrule' 这个DIV
			$('#updatesplittopmould').window({
				title : '修改顶级分润模板'
			});
			var rows = $('#viewTopMould').datagrid('getSelections');
			if (rows.length > 1) {
				$.hideDivShade();
				$.messager.alert("提示 ", "只能对单条模板设置");
				return;
			} else if (rows.length == 0) {
				$.hideDivShade();
				$.messager.alert("提示 ", "请选择要设置的模板");
				return;
			}
			var row = $('#viewTopMould').datagrid('getSelected');
			//给顶级模板名称，备注赋值
			$("#mouldNameUP").val(row.mouldName);
			$("#ruleRemUP").val(row.ruleRem);
			//给隐藏域赋值ruleId,agencyId
			$("#ruleIdUP").val(row.ruleId);
			$("#agencyIdUP").val(row.agencyId);

			$("#updatesplittopmould").window('open').window('refresh');
		}

	};

	//给选择模板的datagrid里赋值( )
	$.viewSelectSplitMould = function() {
		$('#viewSelectSplitMould').datagrid({
			title : '双击选择上级所分配的模板',
			width : 750,
			height : $(window).height() * 0.6,
			url : "${ctx}/splitmould/splitmould.do?method=getSelectMoulds",
			singleSelect : true,
			rownumbers : true,
			//定义双击某一行，打开新建详情
			onDblClickRow : function(rowIndex, rowData) {
				//打开'splitrule' 这个DIV
				$('#splitrule').window({
					title : '添加机构分润模板信息'
				});
				$('#validityDate').val('');
				$('#mouldName').val('');
				var ruleId = rowData.ruleId;
				var mouldName = "UUUUU";
				$.viewMouldViewSection(ruleId, mouldName);
				$("#splitrule").window('open').window('refresh');
			},

			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "mouldName",
				title : "模板名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "ruleId",
				title : "模板编码",
				width : 130,
				align : "center",
				sortable : true
			}, {
				field : "agencyName",
				title : "机构名称",
				width : 200,
				align : "center",
				sortable : true
			}, {
				field : "agencyNameD",
				title : "被分配至",
				width : 200,
				align : "center",
				sortable : true
			},
			//{field:"ruleRem",title:"备注",width :300,align : "center",sortable : true},
			] ],
			hideColumn : [ [ {
				field : "agencyId"
			}, {
				field : "ruleId"
			}, ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true

		});
		var p = $('#viewSelectSplitMould').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
			beforePageText : '第',
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		});
	};

	//给机构模板信息设置页面里的datagrid赋值( mouldViewSection)
	$.viewMouldViewSection = function(ruleId, mouldName) {

		var editing;
		$('#mouldViewSection').datagrid({
			title : '分润模板值设定',
			width : 750,
			height : $(window).height() * 0.6,
			url : "${ctx}/splitmould/splitmould.do?method=getOrgMoulds",
			singleSelect : true,
			rownumbers : true,

			//定义双击事件
			onDblClickCell : function(index, field, value) {
				editing = index;
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {
					index : index,
					field : field
				});
				$(ed.target).focus();
			},
			//定义单击事件
			onSelect : function(rowIndex, rowData) {
				if (rowIndex != editing && editing != undefined) {
					$("#mouldViewSection").datagrid("endEdit", editing);
					editing = undefined;
				}
			},
			queryParams : {
				ruleId : ruleId,
				mouldName : mouldName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "dealTypeName",
				title : "业务名称",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingModeStr",
				title : "分润方式",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingRegionModeStr",
				title : "分润规则",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "ruleBegin",
				title : "规则区间起始",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleEnd",
				title : "规则区间结束",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleValue",
				title : "规则值",
				width : 70,
				align : "center",
				formatter : function(value, row, index) {
					if (row.splittingMode == "1") {
						//添加editor
						$("#mouldViewSection").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});
						//尾部添加单位
						return row.ruleValue + "元/笔";
					}
					if (row.splittingMode == "2") {
						//添加editor
						$("#mouldViewSection").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});
						//尾部添加单位
						return row.ruleValue + "%";
					}
				}
			},
			//{field:"validityDate",title:"生效期",width :70,align : "center",sortable : false},
			//{field:"ruleRem",title:"备注",width :70,align : "center",sortable : false},				
			] ],
			hideColumn : [ [ {
				field : "ruleId"
			}, {
				field : "ruleNum"
			}, {
				field : "dealType"
			}, {
				field : "splittingMode"
			}, {
				field : "splittingRegionMode"
			}, {
				field : "ruleRem"
			},

			] ],
			onAfterEdit : function(rowIndex, rowData, changes) {
				if (rowIndex != 0) {
					$(this).datagrid('beginEdit', rowIndex - 1);
					//$(this).datagrid('getRows')[rowIndex]['ruleValue'] = rowData.ruleValue;
					$(this).datagrid('endEdit', rowIndex - 1);
				}
			},
			// 加载成功后将规则值清零
			onLoadSuccess : function(data) {
				var rows = $(this).datagrid('getRows');
				for (var i = 0; i < rows.length; i++) {
					rows[i].ruleValue = 0;
					$(this).datagrid('refreshRow', i);
				}
			}

		});
	};

	//给机构模板信息修改页面里的datagrid赋值( mouldViewSection)
	$.viewMouldViewSectionU = function(ruleId, mouldName) {
		var editing;
		$('#mouldViewSectionU').datagrid({
			title : '分润模板值修改',
			width : 750,
			height : $(window).height() * 0.6,
			url : "${ctx}/splitmould/splitmould.do?method=getOrgMoulds",
			singleSelect : true,
			rownumbers : true,
			//定义双击事件
			onDblClickCell : function(index, field, value) {
				editing = index;
				$(this).datagrid('beginEdit', index);
				var ed = $(this).datagrid('getEditor', {
					index : index,
					field : field
				});
				$(ed.target).focus();
			},
			//定义单击事件
			onSelect : function(rowIndex, rowData) {
				if (rowIndex != editing && editing != undefined) {
					$("#mouldViewSectionU").datagrid("endEdit", editing);
					editing = undefined;
				}
			},
			queryParams : {
				ruleId : ruleId,
				mouldName : mouldName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "dealTypeName",
				title : "业务名称",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingModeStr",
				title : "分润方式",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingRegionModeStr",
				title : "分润规则",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "ruleBegin",
				title : "规则区间起始",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleEnd",
				title : "规则区间结束",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleValue",
				title : "规则值",
				width : 70,
				align : "center",
				formatter : function(value, row, index) {
					if (row.splittingMode == "1") {
						$("#mouldViewSectionU").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});

						return row.ruleValue + "元/笔";
					}
					if (row.splittingMode == "2") {
						$("#mouldViewSectionU").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});
						return row.ruleValue + "%";
					}
				}
			},
			//{field:"validityDate",title:"生效期",width :70,align : "center",sortable : false},
			//{field:"ruleRem",title:"备注",width :70,align : "center",sortable : false},				
			] ],
			hideColumn : [ [ {
				field : "ruleId"
			}, {
				field : "ruleNum"
			}, {
				field : "dealType"
			}, {
				field : "splittingMode"
			}, {
				field : "splittingRegionMode"
			}, {
				field : "validityDate"
			},

			] ],
			onAfterEdit : function(rowIndex, rowData, changes) {
				if (rowIndex != 0) {
					$(this).datagrid('beginEdit', rowIndex - 1);
					//$(this).datagrid('getRows')[rowIndex]['ruleValue'] = rowData.ruleValue;
					$(this).datagrid('endEdit', rowIndex - 1);
				}
			},
			onLoadSuccess : function(data) {
				var nowDate = data.nowDate;
				var txt = document.getElementById("validityDateU");
				txt.onclick = function() {
					WdatePicker({
						dateFmt : 'yyyyMMdd',
						minDate : nowDate
					});
				};
				minDate=nowDate.replace(/-/g,"");
				txt.value=data.rows[0].validityDate;
				textChange=data.rows[0].validityDate;
			}
		});
	};

	//给分配模板信息里的datagrid赋值( mouldViewSection)
	$.viewMouldViewSectionD = function(ruleId, mouldName) {

		$('#mouldViewSectionD').datagrid({
			title : '分润模板详细',
			width : 750,
			height : 400,
			url : "${ctx}/splitmould/splitmould.do?method=getMouldsD",
			singleSelect : true,
			rownumbers : true,
			queryParams : {
				ruleId : ruleId,
				mouldName : mouldName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "dealTypeName",
				title : "业务名称",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingModeStr",
				title : "分润方式",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "splittingRegionModeStr",
				title : "分润规则",
				width : 130,
				align : "center",
				sortable : false
			}, {
				field : "ruleBegin",
				title : "规则区间起始",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleEnd",
				title : "规则区间结束",
				width : 70,
				align : "center",
				sortable : false
			}, {
				field : "ruleValue",
				title : "规则值",
				width : 70,
				align : "center",
				formatter : function(value, row, index) {
					if (row.splittingMode == "1") {
						$("#mouldViewSectionD").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});

						return row.ruleValue + "元/笔";
					}
					if (row.splittingMode == "2") {
						$("#mouldViewSectionD").datagrid('addEditor', {
							field : 'ruleValue',
							editor : {
								type : 'numberbox',
								options : {
									precision : 3
								}
							}
						});
						return row.ruleValue + "%";
					}
				}
			}, {
				field : "validityDate",
				title : "生效期",
				width : 70,
				align : "center",
				sortable : false
			}, ] ],
			hideColumn : [ [ {
				field : "ruleId"
			} ] ],
			onAfterEdit : function(rowIndex, rowData, changes) {
				if (rowIndex != 0) {
					$(this).datagrid('beginEdit', rowIndex - 1);
					$(this).datagrid('getRows')[rowIndex - 1]['scaleEndValue'] = rowData.scaleStartValue;
					$(this).datagrid('endEdit', rowIndex - 1);
				}
			}
		});
	};

	//保存添加模板内容的方法
	$.addOrgMould = function() {
		var validityDate = $("#validityDate").val();
		var mouldName = $("#mouldName").val();
		if (validityDate == "") {
			$.messager.alert("提示 ", "请输入生效日期");
			return false;
		}

		if (mouldName == "") {
			$.messager.alert("提示 ", "请输入模板名称");
			return false;
		}

		if (mouldName == "UUUUU") {
			$.messager.alert("提示 ", "对不起，模板名称不能为系统保留字");
			return false;
		}

		var data = $('#mouldViewSection').datagrid('getData');
		var rows = data.rows;

		$('#addOrgMould').linkbutton('disable');
		$.post("${ctx}/splitmould/splitmould.do?method=addOrgMould", {
			validityDate : validityDate,
			mouldName : mouldName,
			jsonStr : $.toJSON(rows)
		}, function(data) {
			$('#viewTopMould').datagrid('reload');
			$.parseAjaxReturnInfo(data, $.scaleSucc, $.fail);
		}, "json");

		$.close(-2);
	};

	//更新机构分润模板内容的方法
	$.updateOrgMould = function() {
		var data = $('#mouldViewSectionU').datagrid('getData');
		var rows = data.rows;
		//获得隐藏域 ruleID，agencyId，mouldName
		var ruleId = $("#ruleIdU").val();
		var agencyId = $("#agencyIdU").val();

		//获得页面上 的，生效日期和模板名称
		var validityDate = $("#validityDateU").val();
		if(validityDate==textChange){
			validityDate=minDate;
		}
		var mouldName = $("#mouldNameU").val();
		if (validityDate == "") {
			$.messager.alert("提示 ", "请输入生效日期");
			return false;
		}

		$('#updateOrgMould').linkbutton('disable');
		$.post("${ctx}/splitmould/splitmould.do?method=updateOrgMould", {
			validityDate : validityDate,
			mouldName : mouldName,
			ruleId : ruleId,
			agencyId : agencyId,
			jsonStr : $.toJSON(rows)
		}, function(data) {
			$('#viewTopMould').datagrid('reload');
			$.close(-5);
			$.parseAjaxReturnInfo(data, $.scaleSucc, $.fail);
		}, "json");

		//	    $.close(-5);
	};

	//分配模板到子机构方法
	$.distrMould = function() {
		//获得子机构
		var childAgencyId = $("#childAgencyId").combobox('getValue');
		//取得隐藏域的ruleId
		var ruleId = $("#ruleIdD").val();
		var mouldName = $("#mouldNameD").val();
		if (childAgencyId == "-1") {
			$.messager.alert("提示 ", "请选择子机构");
			return false;
		}
		// 检查所选子机构是否有效（防止因为输入错误值导致后台处理出错）
		var data = $('#childAgencyId').combobox('getData');
		var i;
		for (i = 0; i < data.length; i++) {
			if (childAgencyId == data[i].id) {
				break;
			}
		}
		if (i == data.length) {
			$.messager.alert("提示", "所选子机构无效");
			return false;
		}

		$('#distrMould').linkbutton('disable');

		$.post("${ctx}/splitmould/splitmould.do?method=upperDistrMould", {
			childAgencyId : childAgencyId,
			ruleId : ruleId,
			mouldName : mouldName
		}, function(data) {
			$.parseAjaxReturnInfo(data, $.success, $.failed);
		}, "json");
		$.close(-3);
	};

	$.succ = function(message, data) {
		$.messager.alert("提示 ", message);
	};
	$.fail = function(message, data) {

		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
	$.scaleSucc = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
	};
	$.failed = function(message, data) {
		$.messager.alert("提示 ", message);
		$('#save').linkbutton('enable');
		$.close();
	};
	$.close = function(obj) {
		$.hideDivShade();
		var flag = obj;

		//关闭选择模板的窗口
		if (flag == "-6") {
			$("#selectSplitMould").window('close');
		}

		//关闭修改模板详细窗口
		if (flag == "-5") {
			$("#updateOrgsplitrule").window('close');
		}
		//关闭修改顶级模板窗口
		if (flag == "-4") {
			$("#updatesplittopmould").window('close');
		}
		//关闭分配模板窗口
		if (flag == "-3") {
			$("#distrMouldView").window('close');
		}
		//关闭设置模板窗口
		if (flag == "-2") {
			$("#splitrule").window('close');
		}

		$.viewTopMould();
	};

	$.deleteRole = function() {
		var rows = $('#viewTopMould').datagrid('getSelected');
		if (rows == null) {
			$.messager.alert("提示 ", "请选择要删除的记录!");
			return;
		}

		var ruleId = rows.ruleId;
		var agencyId = rows.agencyId;
		var mouldName = rows.mouldName;
		$.messager.confirm("提示", "确定删除？", function(r) {
			if (r) {
				$.post("${ctx}/splitmould/splitmould.do?method=deleteOrgMould", {
					ruleId : ruleId,
					agencyId : agencyId,
					mouldName : mouldName
				}, function(data) {
					$.viewTopMould();
					$.parseAjaxReturnInfo(data, $.success, $.failed);
				}, "json");
			}
		});

	};
	$.success = function(message, data) {
		$.messager.alert("提示 ", message);
		$.viewSplitRule();
	};
	$.failed = function() {
		$.messager.alert("提示 ", message);
	};

	//已经添加过的机构分润表
	$.viewTopMould = function() {
		var mouldName = $("#mouldname").combobox('getValue');
		if (mouldName == "-1") {
			mouldName = "";
		}
		$('#viewTopMould').datagrid({
			title : '分润模板',
			width : $(window).width() - 6,
			height : $(window).height() * 0.9,
			nowrap : true,
			fitColumns : false,
			url : "${ctx}/splitmould/splitmould.do?method=getAddedOrgMoulds",
			pageSize : 20,
			pageNumber : 1,
			singleSelect : true,
			queryParams : {
				mouldName : mouldName
			},
			loadMsg : '数据载入中,请稍等！',
			remoteSort : false,
			columns : [ [ {
				field : "mouldName",
				title : "模板名称",
				width : 200,
				align : "center",
				sortable : true
			},
			//{field:"ruleId",title:"模板编号",width :130,align : "center",sortable : true},
			{
				field : "agencyName",
				title : "机构名称",
				width : 250,
				align : "center",
				sortable : true
			},

			] ],
			hideColumn : [ [ {
				field : "agencyId"
			}, {
				field : "validityDate"
			}, ] ],
			pagination : true,
			rownumbers : true,
			showFooter : true,
			toolbar : [

			{
				id : 'btnadd',
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					$.openWin(-2);
				}
			}, '-', {
				id : 'btnset',
				text : '分配',
				iconCls : 'icon-edit',
				handler : function() {
					$.openWin(-3);
				}
			}, '-', {
				id : 'btncut',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					$.openWin(-4);
				}
			}, '-', {
				id : 'btndel',
				text : '删除',
				iconCls : 'icon-cut',
				handler : function() {
					$.deleteRole();
				}
			} ]

		});
		var p = $('#viewTopMould').datagrid('getPager');
		$(p).pagination({
			pageList : [ 20 ],
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
			<td align="left">模板名称：</td>
			<td align="left">
				<select style="width: 200px;" name="mouldname" editable="true" id="mouldname" class="easyui-combobox"></select>
			</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$.viewTopMould()">查询</a>
			</td>
		</tr>

	</table>
	<table id="viewTopMould">
	</table>


	<div id="distrMouldView" class="easyui-window" title="分配模板" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;"
	>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 20px; background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdD" name="ruleIdD" />
				<input type="hidden" id="mouldNameD" name="mouldNameD" />
				<table width="100%">
					<tr style="height: 35px">
						<td align="right">子机构名称：</td>
						<td align="left">
							<!-- <select id="childAgencyId" class="easyui-combobox" name="childAgencyId" style="width: 120px;" editable="false"></select> -->
							<select id="childAgencyId" class="easyui-combobox" name="childAgencyId" style="width: 120px;" editable="true"></select>
						</td>
					</tr>
					<tr>
						<td colspan="8" align="center" valign="top">
							<table id="mouldViewSectionD" style="width: 600px; height: 280px;"></table>
						</td>
					</tr>
					<tr style="height: 50px">
						<td align="center" colspan="4">
							<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.distrMould()">分配</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close(-3)">关闭</a>
						</td>
					</tr>

				</table>
			</div>
		</div>
	</div>

	<div id="splitrule" class="easyui-window" title="设置机构分润模板" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;"
	>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 20px; background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdU" name="ruleIdU" />
				<table width="100%">
					<tr style="height: 35px">
						<td align="right">生效日期：</td>
						<td align="left">
							<input id="validityDate" name="validityDate" class="Wdate" type="text" style="width: 120px;" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})"
								readonly
							/>
						</td>

						<td align="right">模板名称：</td>
						<td align="left">
							<input type="text" name="mouldName" id="mouldName" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<table id="mouldViewSection" width="100%"></table>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.addOrgMould()">保存新增 </a>

							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close(-2)">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="updateOrgsplitrule" class="easyui-window" title="修改机构分润模板" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;"
	>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 20px; background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdU" name="ruleIdU" />
				<input type="hidden" id="agencyIdU" name="agencyIdU" />
				<table width="100%">
					<tr style="height: 35px">
						<td align="right">生效日期：</td>
						<td align="left">
							<input id="validityDateU" name="validityDateU" class="Wdate" type="text" style="width: 120px;" readonly />
						</td>

						<td align="right">模板名称：</td>
						<td align="left">
							<input type="text" name="mouldNameU" id="mouldNameU" maxlength="50" readonly />
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<table id="mouldViewSectionU" width="100%"></table>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<a name="save" id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="$.updateOrgMould()">保存修改 </a>

							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close(-5)">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="selectSplitMould" class="easyui-window" title="选择分润模板" closable="false" closed=true cache="false" collapsible="false" zIndex="20px"
		minimizable="false" maximizable="false" resizable="false" draggable="true"
		style="width: 850px; height: 600px; top: 20px; padding: 0px; background: #fafafa; overflow: hidden;"
	>
		<div class="easyui-layout" fit="true">
			<div region="center" border="true" style="padding: 20px; background: #fff; overflow: hidden;">
				<input type="hidden" id="ruleIdU" name="ruleIdU" />
				<input type="hidden" id="agencyIdU" name="agencyIdU" />
				<table width="100%">

					<tr>
						<td colspan="4" align="center">
							<table id="viewSelectSplitMould" width="100%"></table>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<a name="close" id="close" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$.close(-6)">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

</body>
</html>

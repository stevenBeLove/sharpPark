/**
 * 创建一个TD对象
 * @param str  td属性串，例如<td width="100%"></td>
 * @param name 名称
 * @param value 值
 * @param txt 显示值
 */
$.createTd = function(str, name, value, txt) {
  var td = $(str);
  td.attr("name", name);
  td.attr("value", value);
  td.html(txt == null ? value : txt);
  return td;
};

/**
 * 对AjaxRetInfo的处理
 * @param retInfo 返回信息
 * @param success 成功的回调函数
 * @param fail 失败的回调函数
 */
$.parseAjaxReturnInfo = function(retInfo, success, fail) {
  if (retInfo.success == "true") {
    success(retInfo.message, retInfo.datas);
  } else {
    fail(retInfo.message, retInfo.datas);
  }
};

/**
 *   等待操作响应页面，利用DIV遮罩
 */
$.showDivShade = function(ctx) {
  $("body").append("<div id='popupCover'></div><div id='popupLayer'><img src='" + ctx + "/commons/images/loading.gif' width='85' height='81' /></div>");

  $("#popupLayer").css({
            "z-index":"100",
            "position":"absolute",
            "top":$(document).scrollTop() + "px",
            "left":"0",
            "width":"100%",
            "margin-top":($(window).height() - $("#popupLayer img").height()) / 2 + "px",
            "text-align":"center"
          });
  if ($("#popupLayer img").height() > $(window).height()) {
    $("#popupLayer").css({"margin-top":"0","display":"none"});
  } else {
    $("#popupLayer").css({"display":"none"});
  }
  $("#popupCover").css({
            "dispaly":"none",
            "z-index":"99",
            "position":"absolute",
            "top":"0",
            "left":"0",
            "width":$("body").width(),
            "height":$("body").height(),
            "background-color":"#E6E6E6",
            "filter":"Alpha(opacity=0)",
            "-moz-opacity":"0",
            "opacity":"0"
          });

  //FadeIn
    $("#popupCover").fadeTo("normal", 0.8);
    $("#popupLayer").fadeTo("normal", 1);
};

/**
 * 取消DIV遮罩
 */
$.hideDivShade = function () {
  $("#popupLayer").fadeTo("normal", 0, function() {
    $("#popupLayer").remove();
  });
  $("#popupCover").fadeTo("normal", 0, function() {
    $("#popupCover").remove();
  });
};

/**
 * 将GET请求转换为POST请求
 */
$.getToPost = function (URL, PARAMS) {        
    var temp = document.createElement("form");        
    temp.action = URL;        
    temp.method = "post";        
    temp.style.display = "none";        
    for (var x in PARAMS) {        
        var opt = document.createElement("textarea");        
        opt.name = x;        
        opt.value = PARAMS[x];            
        temp.appendChild(opt);        
    }        
    document.body.appendChild(temp);        
    temp.submit();        
    return temp;        
};     
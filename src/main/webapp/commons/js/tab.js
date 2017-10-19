/***********************************************************************

* 娴ｆ粏锟介敍姘辨焿閻欏倻顫块幍锟絈Q閿涳拷055818239
* 鐠併劏顔戠紘銈忕窗112044258閵嗭拷2994605閵嗭拷6534121閵嗭拷6271061
* 閸掓稑缂撻弮銉︽埂閿涳拷011/1/17

* 楠炲灝鎲￠敍姘拱娴滅儤澹欓幒銉ユ倗缁銇囨稉顓炵毈閸ㄥ顓搁悶鍡欓兇缂佺喓娈戞潪顖欐閻ㄥ嫯顔曠拋鈥茬瑢瀵拷褰傞敍灞炬箒闂囷拷顪呴惃鍕箙閸欏浠堢化缁樺灉閸燁渻~~~

**********************************************************/

window.onload = function(){
	$('#loading-mask').fadeOut();
};

var onlyOpenTitle="濞嗐垼绻嬫担璺ㄦ暏";//娑撳秴鍘戠拋绋垮彠闂傤厾娈戦弽鍥╊劮閻ㄥ嫭鐖ｆ０锟�
$(function(){
	//InitLeftMenu();
	tabClose();
	tabCloseEven();


});

//閸掓繂顬婇崠鏍т箯娓氾拷
function InitLeftMenu() {
	$("#nav").accordion({animate:false,fit:true,border:false});
	var selectedPanelname = '';
    $.each(_menus.menus, function(i, n) {
		var menulist ='';
		menulist +='<ul class="navlist">';
        $.each(n.menus, function(j, o) {
			menulist += '<li><div ><a ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div> ';

			if(o.child && o.child.length>0)
			{
				//li.find('div').addClass('icon-arrow');

				menulist += '<ul class="third_ul">';
				$.each(o.child,function(k,p){
					menulist += '<li><div><a ref="'+p.menuid+'" href="#" rel="' + p.url + '" ><span class="icon '+p.icon+'" >&nbsp;</span><span class="nav">' + p.menuname + '</span></a></div> </li>'
				});
				menulist += '</ul>';
			}

			menulist+='</li>';
        })
		menulist += '</ul>';

		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
				border:false,
            iconCls: 'icon ' + n.icon
        });

		if(i==0)
			selectedPanelname =n.menuname;

    });
	$('#nav').accordion('select',selectedPanelname);



	$('.navlist li a').click(function(){
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = $(this).find('.icon').attr('class');

		var third = find(menuid);
		if(third && third.child && third.child.length>0)
		{
			$('.third_ul').slideUp();

			var ul =$(this).parent().next();
			if(ul.is(":hidden"))
				ul.slideDown();
			else
				ul.slideUp();



		}
		else{
			addTab(tabTitle,url,icon);
			$('.navlist li div').removeClass("selected");
			$(this).parent().addClass("selected");
		}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});





	//闁鑵戠粭顑跨娑擄拷
	//var panels = $('#nav').accordion('panels');
	//var t = panels[0].panel('options').title;
    //$('#nav').accordion('select', t);
}
//閼惧嘲褰囧锔挎櫠鐎佃壈鍩呴惃鍕禈閺嶏拷
function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 })
	})

	return icon;
}

function find(menuid){
	var obj=null;
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				obj = o;
			}
		 });
	});

	return obj;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
	
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function addSelect(subtitle,url,icon){
	$('#tabs').tabs('add',{
		title:subtitle,
		content:createFrame(url),
		closable:true,
		icon:icon
	});
	//tabClose();
}


function createFrame(url)
{
	var s = '<iframe id="childf" name="childf" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose()
{
	/*閸欏苯鍤崗鎶芥４TAB闁銆嶉崡锟�
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*娑撴椽锟芥い鐟板幢缂佹垵鐣鹃崣鎶芥暛*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}


//缂佹垵鐣鹃崣鎶芥暛閼挎粌宕熸禍瀣╂
function tabCloseEven() {

    $('#mm').menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });

    return false;
}

function closeTab(action)
{
    var alltabs = $('#tabs').tabs('tabs');
    var currentTab =$('#tabs').tabs('getSelected');
	var allTabtitle = [];
	$.each(alltabs,function(i,n){
		allTabtitle.push($(n).panel('options').title);
	});


    switch (action) {
        case "refresh":
            var iframe = $(currentTab.panel('options').content);
            var src = iframe.attr('src');
            $('#tabs').tabs('update', {
                tab: currentTab,
                options: {
                    content: createFrame(src)
                }
            });
            break;
        case "close":
            var currtab_title = currentTab.panel('options').title;
            $('#tabs').tabs('close', currtab_title);
            break;
        case "closeall":
            $.each(allTabtitle, function (i, n) {
                if (n != onlyOpenTitle){
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeother":
            var currtab_title = currentTab.panel('options').title;
            $.each(allTabtitle, function (i, n) {
                if (n != currtab_title && n != onlyOpenTitle)
				{
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeright":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);

            if (tabIndex == alltabs.length - 1){
                alert('娴滆绱濋崥搴ょ珶濞屸剝婀侀崯锟絕@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i > tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "closeleft":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);
            if (tabIndex == 1) {
                alert('娴滆绱濋崜宥堢珶闁絼閲滄稉濠傘仈閺堝姹夐敍灞芥尪閹�绗夌挧宄版憹閵嗭拷^@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i < tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "exit":
            $('#closeMenu').menu('hide');
            break;
    }
}


//瀵懓鍤穱鈩冧紖缁愭褰�title:閺嶅洭顣�msgString:閹绘劗銇氭穱鈩冧紖 msgType:娣団剝浼呯猾璇茬� [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

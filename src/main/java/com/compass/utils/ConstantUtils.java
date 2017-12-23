package com.compass.utils;

public class ConstantUtils {
	public static final String MENUTREEBASE = "0"; // 菜单根节点编号
	public static final String CENTERCODE = "RTB00000000";// 中心机构编号
	public static final String CENTERSYSTEMID = "CTP";
	public static final String COMBOXONEID = "-1";
	public static final String AGENCYSTARTVALUE = "0000";
	public static final String HOME_ID = "ruiyuanbao_ruitongbao";
	/**
	 * RTB机构前缀
	 */
	public static final String RTBPREFIX = "RTB";
	/**
	 * 管理员
	 */
	public static final String ADMIN     = "admin";
	

	public static final String MENULIST = "urlList";
	public static final String MAXVALUE = "999999999999999999999";
	public static final String USERID = "userId"; // 用户编号
	public static final String ROLETYPEID = "roletypeId";// 角色类型编号
	public static final String USERNAME = "username"; // 用户名称
	public static final String AGENCYID = "agencyId";// 机构编号
	public static final String PARENTAGENCYID = "parentagencyId";// 上级机构编号
	public static final String SYSTEMID = "systemid";// 系统编号
	public static final String COMPANYBRIEFNAME = "companyBriefName";// 企业简称
	public static final String IPADDRESS = "ipAddress";// IP地址
	public static final String ONLINEFLAG = "onlineFlag";// 归属机构显示
	public static final String AGENCYFLAG = "agencyFlag";// 归属机构显示
	public static final String AGENCYQUERYFLAG = "agencyFlag";// 区分是否显示机构查询选项
	public static final String USERPHONE = "userPhone"; // 手机号码
	public static final String LOGINSHOWFLAG = "loginShowFlag"; // 登录提示框FLAG
	public static final String VESTAGENCYID = "vestagencyId"; // 归属机构Id
	public static final String TINYBUSSINESS = "tinyBussiness";//是否为小微商
	public static final String USERLOGINNAME = "userLoginName";//登录名

	public static final String AGENCY[] = { "机构编号", "企业名称", "归属机构", "上级机构名称", "法人姓名", "身份证号", "手机号码", "激活状态", "激活链接", "激活时间", "企业邮箱", "企业地址", "组织机构代码", "法人信息 ", "合同编号", "省", "市",
			"实名状态", "开户银行", "开户银行编码", "户名", "银行账号", "创建人", "创建时间", "联系人", "企业电话"};//顶级机构导出
	public static final String AGENT[] = { "机构编号", "企业名称", "联系人", "企业电话", "激活状态", "激活链接", "激活时间", "企业地址", "省", "市", "创建人", "创建时间" };//代理商导出

	public static final String AGENCY_PD = "1"; // 机构待审核
	public static final String AGENCY_PS = "2"; // 机构审核通过
	public static final String AGENCY_PF = "3"; // 机构审核不通过
	public static final String AGENCY_NV = "0"; // 机构无效

	public static final String AGENCYDEAL[] = { "机构编号", "企业名称", "交易总笔数", "机构交易总笔数", "交易总金额", "机构交易总金额", "可分润金额", "机构可分润金额", "分润到金额" };

	public static final String DEALDETAIL[] = { "机构名称", "终端编号", "交易类型", "交易流水号", "交易日期", "交易时间", "交易金额", "手续费金额", "银行卡号", "手机号码", "客户姓名" };

	public static final String DEALTYPE[] = { "交易名称", "交易类型", "交易年月", "总笔数", "交易金额", "可分润金额", "分润到金额", "交易成本" };

	public static final String CHILDSPLITFEE[] = { "机构名称", "交易类型", "交易年月", "总金额", "总笔数", "分润金额" };

	// public static final String SPLITFEEDEALDETAIL[] =
	// {"机构名称","交易唯一号","终端号","流水号","交易类型","交易日期","交易金额","手续费金额","一级分润金额","二级分润金额","三级分润金额","四级分润金额","五级分润金额"};
	public static final String SPLITFEEDEALDETAIL[] = { "机构名称", "交易唯一号", "终端号", "流水号", "交易类型", "交易日期", "交易金额", "手续费金额", "分润金额", "下级分润金额", "金额差" };

	public static final String SPLITFEEDEALDETAILALL[] = { "机构名称", "交易唯一号", "终端号", "流水号", "交易类型", "交易日期", "交易金额", "手续费金额", "一级分润金额", "二级分润金额",
			"三级分润金额", "四级分润金额", "五级分润金额" };

	public static final String SPLITFEEDEALBYAGENCY[] = { "机构名称", "流水号", "交易唯一号", "交易类型", "交易日期", "交易金额", "手续费金额", "分润金额" };

	public static final String TERMINAL[] = { "来源系统", "终端识别码", "终端类型", "所属机构", "终端状态" };

	public static final String TERMINALINFO[] = { "机构号", "机构名称", "当前机构终端量", "当前机构激活终端量", "归属机构终端量", "归属机构激活终端量" };

	public static final String DEALSTAT[] = { "交易类型", "总交易笔数", "总交易金额(元)", "开始日期", "结束日期" };

	public static final String DEALDEDUCT[] = { "机构名称", "终端编号", "交易类型", "交易流水号", "交易金额", "交易日期", "交易时间", "扣款类型", "扣款金额", "扣款日期", "处理标志", "备注" };

	public static final String OTHERDEDUCT[] = { "机构名称", "扣款类型", "金额", "扣款金额", "扣款日期", "处理标志", "备注" };
	
	public static final String TRADEPROFIT[] = {"机构名称","手机号","交易流水号","终端号","交易类型","交易日期","交易时间","交易金额(元)","手续费(元)","终端费率百分比(%)","终端消费单笔(元)","本级分润成本百分比(%)","本级分润成本单笔(元)","直属下级分润成本百分比(%)","直属下级分润成本单笔(元)","直属下级分润金额(元)","分润总金额(元)","分润差值(元)"};

	public static final String SYSTEN_NV = "0";// 无效
	public static final String SYSTEN_IV = "1";// 有效

	public static final String ROLETYPESUPERADMIN = "1";// 角色类型为超级管理员
	public static final String ROLETYPEADMIN = "2";// 角色类型为管理员
	public static final String ROLETYPEOPERATOR = "3";// 角色类型为操作员
	public static final String ROLETYPEOPERATOR_TLMF = "50";// 角色类型为通联民富操作员

	public static final int RULECOUNT = 20; // 规则最大总数
	public static final int SINGLERULECOUNT = 20; // 单笔区间最大总数

	public static final String EXPORTSPLITFEEFILENAME = "分润.xls";
	public static final String EXPORTAGENCY = "机构明细.xls";
	public static final String EXPORTSPLITFEEDEALFILENAME = "分润交易明细各级分润.xls";
	public static final String EXPORTHISSPLITFEEDEALFILENAME = "历史分润交易明细各级分润.xls";
	public static final String EXPORTDEAL = "交易明细表.xls";
	public static final String EXPORTOLDDEAL = "历史交易明细表.xls";
	public static final String EXPORTSPLITFEEDEALBYAGENCYFILENAME = "分润明细.xls";
	public static final String EXPORTTERMINAL = "终端下发明细.xls";
	public static final String EXPORTDEALSTAT = "交易统计报表.xls";
	public static final String EXPORTDOLDEALSTAT = "历史交易统计报表.xls";
	public static final String EXPORTCONFIRMATIONTEMPLATE = "平台结算款确认函模板.xls";
	public static final String EXPORTDEALDEDUCT = "交易扣款明细.xls";
	public static final String EXPORTOTHERDEDUCT = "其他扣款明细.xls";
	public static final String EXPORTTRADEPROFIT = "交易及分润明细.xls";


	// 终端状态 0.未激活 1.已激活 2.冻结 3.回拨 4.作废
	public static final Integer TERMINALNONACTIVATEDSTATUS = 0;
	public static final Integer TERMINALNACTIVATEDSTATUS = 1;
	public static final Integer TERMINALNBLOCKINGSTATUS = 2;
	public static final Integer TERMINALNROLLBACKSTATUS = 3;
	public static final Integer TERMINALNSCRAPSTATUS = 4;

	// 模板还未启用时，启用日期设为99999999
	public static final String NOVALIDITYDATE = "99999999";
	// 模板还未启用时模板名称设为
	public static final String NOMOULDNAME = "UUUUU";

	// 分润方式,按笔数
	public static final String SPLITBYNUM = "1";
	// 分润方式,按金额
	public static final String SPLITBYAMT = "2";

	// 顶级模板表状态 0--无效，1--有效
	public static final int TOPMOULD_V = 1; // 有效
	public static final int TOPMOULD_IV = 0; // 无效

	// 操作名称
	public static final String OPERNAMELOGON = "登录模块";
	public static final String OPERNAMESYSTEM = "数据来源系统";
	public static final String OPERNAMEUSERROLE = "用户管理";
	public static final String OPERNAMEROLEAUTH = "角色权限映射";
	public static final String OPERNAMEAUTHORITY = " 菜单管理";
	public static final String OPERNAMEROLECONTROL = " 角色管理";
	public static final String OPERNAMEAGENCY = "机构管理";
	public static final String OPERNAMEAGENCYCHECK = "机构审核";
	public static final String OPERNAMETERMINQU = "终端下发";
	public static final String OPERNAMETERMOPT = "终端回拨";
	public static final String OPERNAMETERMTYPE = "终端类型";
	public static final String OPERNAMETERMMANAGE = "终端管理";
	public static final String OPERNAMETERMSTATUS = "终端状态变更";
	public static final String OPERNAMEDEALDET = "交易明细查询";
	public static final String OPERNAMEOLDDEALDET = "历史交易明细查询";
	public static final String OPERNAMEDEALTYPE = "交易类型管理";
	public static final String OPERNAMETERMDEALSTAT = "交易统计报表";
	public static final String OPERNAMETERMOLDDEALSTAT = "历史交易统计报表";
	public static final String OPERNAMESPLITFEE = " 分润计算";
	public static final String OPERNAMESPLITFEEDETAIL = " 分润明细查询";
	public static final String OPERNAMEOLDSPLITFEEDETAIL = "历史分润明细查询";
	public static final String OPERNAMESPLITFEESER = " 分润查询";
	public static final String OPERNAMEOLDSPLITFEESER = "历史分润查询";
	public static final String OPERNAMESPLITMOULDMANAGE = "机构模板管理";
	public static final String OPERNAMEMOULDDISTVIEW = " 机构模板分配查看";
	public static final String OPERNAMEORGSPLITMOULD = " 机构模板管理";
	public static final String OPERNAMEORGMOULDDISTVIEW = " 机构模板分配查看";
	public static final String OPERNAMEREALDEDUCT = "实际扣款管理";
	public static final String OPERNAMEDEALDEDUCT = "交易扣款管理";
	public static final String OPERNAMEOTHERDEDUCT = "其他扣款管理";
	public static final String OPERNAMEDEDUCTTYPE = "扣款类型管理";
	public static final String OPERNAMESYSLOG = "系统日志";
	public static final String OPERNAMETERMINALDEAL = "终端交易查询";
	public static final String OPERNAMEPSMFEE = "终端费率";
	//新增
	public static final String OPERNAMEPARK = "停车场管理";
	public static final String OPERNAMEPARKRULESET = "收费规则配置";
	public static final String OPERNAMECARINPARK = "在场车辆";
	public static final String OPERNAMEORDER = "订单管理";
	public static final String OPERNAMEFREEVEHICLEBRAND = "免费车牌管理";
	public static final String OPERNAMEMONTHVEHICLEBRAND = "月卡车牌管理";
	public static final String OPERNAMEACCOUNT = "对账管理";
	public static final String OPERNAMETRANSACTIONFLOW = "流水管理";
	public static final String OPERNAMEMANAGEMENTANALYSIS = "经营分析";
	public static final String OPERNAMECARENTRY = "车辆出入";
	public static final String OPERNAMECAREXCEP = "异常车牌";

	// 操作类型
	public static final String OPERTYPEADD = "1";// 增
	public static final String OPERTYPEDEL = "2";// 删
	public static final String OPERTYPEUPD = "3";// 改
	public static final String OPERTYPESER = "4";// 查
	public static final String OPERTYPELOGON = "5";// 登录
	public static final String OPERTYPELOGOFF = "6";// 退出登录
	public static final String OPERTYPELOGONFAIL = "7";// 登录失败
	public static final String OPERTYPERESETPSW = "8";// 密码重置
	public static final String OPERTYPECHE = "9";// 审核通过
	public static final String OPERTYPEEXPO = "10";// 导出
	public static final String OPERTYPEUPLO = "11";// 上传
	public static final String OPERTYPEXF = "12";// 终端下发
	public static final String OPERTYPEHB = "13";// 终端回拨
	public static final String OPERTYPESPLITFEE = "14";// 分润计算;
	public static final String OPERTYPESPLITCONFIRM = "15";// 分润确认;
	public static final String OPERTYPEMOULDDIST = "16";// 分配模板;
	public static final String OPERTYPEDEDUCTSTAT = "17";// 扣款统计;
	public static final String OPERTYPEREALDEDUCTSER = "18";// 实际扣款查询;
	public static final String OPERTYPEDEALSER = "19";// 交易查询;
	public static final String OPERTYPEDEDUCTSER = "20";// 扣款查询;
	public static final String OPERTYPETASK = "21";// 定时任务;
	public static final String OPERTYPERET = "22";// 退款;
  public static final String MSG_CODE = "MSG_CODE";//返回码
  public static final String MSG_TEXT = "MSG_TEXT";//返回内容
  /**
   * 返回成功
   */
  public static final String MSG_SUCCESS = "0000";
  
  
  public static final String TRADEPROFIT_MOBILENO = "mobileNo"; //交易明细机构手机号
  public static final String AGENCYBEAN = "agencyBean"; //机构表实体类
  public static final String AGENCY_CERTIFICATION = "flag"; //实名认证判断
  public static final String AGENCY_OPERNAMETERMINQU = "1"; //终端下发
}
